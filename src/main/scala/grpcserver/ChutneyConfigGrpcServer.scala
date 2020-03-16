package grpcserver

import java.util.UUID

import info.vikrant.chutneyconfigserver.proto.configserver._
import common.{ConfigJsonGenerator, ServiceConfig}

import scala.concurrent._
import io.grpc.{Server, ServerBuilder}
import play.api.libs.json.Json
import zookeeper.{ZkClient, ZkNodeCache}

import scala.concurrent.ExecutionContext.Implicits.global


object ChutneyConfigGrpcServer {
  val port = 5555 // Move to config
}
class ChutneyConfigGrpcServer { self =>
  private[this] var server: Server = null
  val zkClient = new ZkClient(ServiceConfig.ZkServer)
  val zkCache = new ZkNodeCache(zkClient, ZkNodeCache.zkConfigServerConsumerNode)


  def startServer(): Future[Unit] =  {
    server =
      ServerBuilder.forPort(ChutneyConfigGrpcServer.port).addService(ConfigServerGrpc.bindService(new ChutneyConfigGrpcServerImpl, ExecutionContext.global)).build.start
    println(s"Server Started on port ${ChutneyConfigGrpcServer.port}")
    sys.addShutdownHook {
      System.err.println("*** shutting down gRPC server since JVM is shutting down")
      self.stop()
      System.err.println("*** server shut down")
    }
    zkCache.init()
  }

  private def stop(): Unit = {
    if (server != null) {
      server.shutdown()
    }
  }

  private class  ChutneyConfigGrpcServerImpl extends ConfigServerGrpc.ConfigServer {
    import common.constants._
    override def sayHello(request: HelloRequest): Future[HelloReply] = ???

    override def getConfig(request: GetConfigRequest): Future[GetConfigResponse] = {

      val nodePath = ZkNodeCache.zkConfigServerConsumerNode + Slash + request.consumerId + Slash + request.configKey

      zkCache.getNodeData(nodePath).map {
        case Some(data) =>
          println(data)
          val jsData = Json.parse(data)
          ConfigJsonGenerator.stringToConfigType((jsData \ "configType").as[String]).map { configType =>
            val config = ConfigJsonGenerator.getJsonForConfigType(request.configKey, configType, data)
              GetConfigResponse(ResponseCode.RESPONSE_CODE_OK, config, configType)
          }.getOrElse(GetConfigResponse(ResponseCode.RESPONSE_CODE_INVALID, ""))
      }
    }

    override def getAllConfig(request: GetAllConfigRequest): Future[GetAllConfigResponse] = ???

    override def setConfig(request: SetConfigRequest): Future[SetConfigResponse] = {
      val nodePath = ZkNodeCache.zkConfigServerConsumerNode + Slash + request.consumerId + Slash + request.configKey

      val valueJson = ConfigJsonGenerator.getValueJson(request.configType, request.value)
      zkCache.setNodeData(nodePath, valueJson).map { _ =>
        SetConfigResponse(ResponseCode.RESPONSE_CODE_OK, Json.obj(request.configKey -> request.value).toString())
      }.recoverWith {
        case _: Exception => Future(SetConfigResponse(ResponseCode.RESPONSE_CODE_INVALID, ""))
      }
    }



    override def addConsumer(request: AddConsumerRequest): Future[AddConsumerResponse] = Future.successful(AddConsumerResponse(ResponseCode.RESPONSE_CODE_OK,UUID.randomUUID().toString))
  }
}
