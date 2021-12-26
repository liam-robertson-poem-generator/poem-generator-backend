package poemGenerator
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class GeneratorService {

  val inputController = new InputController();
  val exportController = new ExportController();

  val routes: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
      } ~
      path("listAll") {
        complete(HttpEntity(ContentTypes.`application/json`, inputController.getSorted2dArray().toString))
      } ~
        path("getIteratedList") {
          complete(HttpEntity(ContentTypes.`application/json`, inputController.iterateSorted2dArray(20, Array(1,1,9), "forwards").toString))
      } ~
      path("saveDoc") {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, exportController.exportToWord()))
      }
    }

}
