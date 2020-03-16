package common

import com.typesafe.config.ConfigFactory

object ServiceConfig {
  val config = ConfigFactory.load()

  // Value derived from config
  lazy val Organization = config.getString("cluster.organization")
  lazy val App = config.getString("cluster.app")
  lazy val Env = config.getString("cluster.env")

  lazy val ConsumerNode  = config.getString("cluster.zookeeper.consumer-node")
  lazy val ZkServer = config.getString("cluster.zookeeper.server")


}
