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

  case class generatorParameters(startingPoem: Array[Int], numOfPoems: Int, poemOrder: String)
  implicit val parametersFormat = jsonFormat3(generatorParameters)

  val routes: Route =
    concat(
      get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
      } ~
        pathPrefix("outputDocument") {
          getFromDirectory("build/resources/main/output")
        } ~
        path("listAllPoems") {
          complete(inputController.getSorted2dArray())
        }
    },
      post {
        path("createDocument") {
          entity(as[generatorParameters]) { parameters =>
            primaryController.primaryExecutor(parameters.numOfPoems, parameters.startingPoem, parameters.poemOrder)
            complete(StatusCodes.OK)
          }
        } ~
          path("getIteratedList") {
            entity(as[generatorParameters]) { parameters =>
              complete(inputController.iterateSorted2dArray(parameters.numOfPoems, parameters.startingPoem, parameters.poemOrder))
            }
          }
      }
    )

}
