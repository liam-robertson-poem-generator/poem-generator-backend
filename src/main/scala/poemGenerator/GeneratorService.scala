package poemGenerator
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, get, path, pathSingleSlash}
import akka.http.scaladsl.server.Route

class GeneratorService {

  val inputController = new InputController();

  val routes: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Server is up</h1>"))
      } ~
      pathPrefix("poems") {
        getFromDirectory("src/main/resources/poems")
      } ~
      pathPrefix("glyphs") {
        getFromDirectory("src/main/resources/glyphs")
      } ~
      path("listAll") {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, ""))
      }
    }

}
