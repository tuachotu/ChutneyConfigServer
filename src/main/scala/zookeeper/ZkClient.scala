package zookeeper

import scala.collection.JavaConverters._
import common.ServiceConfig._
import common.constants._
import org.apache.curator.RetryPolicy
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.RetryForever
import concurrent._

object  ZkClient {
  lazy val zkConfigServerRootNode = makeZkPath(Seq(Organization,App,Env))
  lazy val zkConfigServerConsumerNode = zkConfigServerRootNode + Slash + ConsumerNode

  def retryEvery10ms(): RetryPolicy = new RetryForever(10)
  def makeZkPath(elements: Seq[String]): String = Slash + elements.mkString(Slash)
}

class ZkClient(server: String) {
  import ZkClient._

  private val client = CuratorFrameworkFactory.newClient(server, retryEvery10ms())

  def framework: CuratorFramework = client

  client.start()

  private def callback(p: Promise[Unit]): Unit = p.completeWith(Future.unit) // handle failure

  def setNodeData(path: String, data: Array[Byte]): Future[Unit] = {
    val p = Promise[Unit]
    framework.setData().inBackground(callback(p)).forPath(path, data)
    p.future
  }



  framework.getChildren.forPath(ZkNodeCache.zkConfigServerConsumerNode).asScala foreach println
}
