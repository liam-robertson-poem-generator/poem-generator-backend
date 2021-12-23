package poemGenerator
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, get, path, pathSingleSlash}
import akka.http.scaladsl.server.Route

class GeneratorService {

  val inputController = new InputController();

  val routes: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
      } ~
      pathPrefix("poems") {
        getFromDirectory("src/main/resources/poems")
      } ~
      pathPrefix("glyphs") {
        getFromDirectory("src/main/resources/glyphs")
      } ~
      path("listAll") {
        complete(HttpEntity(ContentTypes.`application/json`, inputController.listAll().toString))
      }
    }

}
