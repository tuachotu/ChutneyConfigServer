import grpcserver.ChutneyConfigGrpcServer


import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success


object ChutneyConfigServer extends  App  {
  val grpcServer = new ChutneyConfigGrpcServer()

  grpcServer.startServer().onComplete {
    case Success(value) => println("KtConfigServer Ready")
    case _ => println("KtConfigServer failed to initialized") // TODO-GOOD-TO-HAVE: Add error handling
  }

  // Do some thing better, following make our server run for ever
  Thread.currentThread().join()
}
