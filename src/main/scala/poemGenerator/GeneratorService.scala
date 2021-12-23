package poemGenerator
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, get, path, pathSingleSlash}

class GeneratorService {

  val routes =
    get {
      pathSingleSlash {
        complete("Server is up")
      } ~
        path("ping") {
          complete("PONG!")
        } ~
        path("crash") {
          sys.error("BOOM!")
        }
    }

}
