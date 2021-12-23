import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

object PoemGeneratorMain {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
//    implicit val executionContext = system.dispatcher

    val route: Route =
      get {
        pathSingleSlash {
          complete("PONG!")
        } ~
          path("ping") {
            complete("PONG!")
          } ~
          path("crash") {
            sys.error("BOOM!")
          }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/")
  }
}
