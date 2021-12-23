package poemGenerator
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, get, path, pathSingleSlash}
import akka.http.scaladsl.server.Route

class GeneratorService {

  val route: Route =
    get {
      pathSingleSlash {
        complete("server is up")
      } ~
        path("ping") {
          complete("PONG!")
        } ~
        path("crash") {
          sys.error("BOOM!")
        }
    }

}
