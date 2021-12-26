package poemGenerator
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import poemGenerator.controllers.{InputController, PrimaryController}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

class GeneratorService extends SprayJsonSupport with DefaultJsonProtocol {

  val inputController = new InputController();
  val primaryController = new PrimaryController();

  val routes: Route =
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
      } ~
        pathPrefix("outputDocument") {
          getFromDirectory("src/main/resources/output")
      } ~
      path("listAllPoems") {
        complete(inputController.getSorted2dArray())
      } ~
        path("getIteratedList") {
          complete(inputController.iterateSorted2dArray(20, Array(1,1,9), "start", inputController.getSorted2dArray()))
      } ~
      path("getDocument") {
        primaryController.primaryExecutor()
        complete(StatusCodes.OK)
      }
    }

}
