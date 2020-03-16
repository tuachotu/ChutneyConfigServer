package zookeeper


import info.vikrant

import scala.concurrent._
import common.ServiceConfig._
import common.constants._
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.recipes.cache.{TreeCache, TreeCacheEvent, TreeCacheListener}
import zookeeper.ZkClient._
import info.vikrant.chutneyconfigserver.proto.configserver._

import scala.concurrent.ExecutionContext.Implicits.global


object ZkNodeCache {
  lazy val zkConfigServerRootNode = makeZkPath(Seq(Organization,App,Env))
  lazy val zkConfigServerConsumerNode = zkConfigServerRootNode + Slash + ConsumerNode
}

class ZkNodeCache (client: ZkClient, pathToNode: String) {
  private val cache =  TreeCache.newBuilder(client.framework, ZkNodeCache.zkConfigServerConsumerNode).setMaxDepth(10).build()
  private val initialized = Promise[Unit]()


  def getNodeData(path: String): Future[Option[String]] =
    Future {Option(cache.getCurrentData(path)) map (_.getData) map (new String(_))}

  def init(): Future[Unit] = {
    cache.getListenable().addListener(handler)
    cache.start()
    initialized.future
  }

   def setNodeData(path: String, data: String): Future[Unit] = {
    client.setNodeData(path, data.getBytes())
  }

  private val handler = new TreeCacheListener {
    override def childEvent(client: CuratorFramework, event: TreeCacheEvent): Unit = {
      event.getType match {
        case TreeCacheEvent.Type.INITIALIZED =>
          println(ConfigType.BOOLEAN)
          println('ZkCacheInitialized)
          initialized.completeWith(Future.unit)
        case TreeCacheEvent.Type.NODE_REMOVED =>
          println('NodeRemoved)
        case TreeCacheEvent.Type.NODE_ADDED =>
          println('NodeAdded)
        case TreeCacheEvent.Type.NODE_UPDATED =>
          println("NodeUpdated")
        case _ =>
          println("Unhandled ZL Event") // TODO-GOOD-TO-HAVE: Add connection failure handling

      }
    }
  }



}
