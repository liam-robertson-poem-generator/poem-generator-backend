package poemGenerator
import akka.actor.ActorSystem
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.ByteString
import poemGenerator.controllers.{InputController, PrimaryController}
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
        }
        path("listAllPoems") {
          complete(inputController.getSorted2dArray())
        } ~
        path("createDocument") {
          parameters("numOfPoems".as[Int], "startingPoem".as[Array[Int]], "poemOrder") { (numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) =>
            val docByteArray: ByteString = primaryController.primaryExecutor(numOfPoems, startingPoem, poemOrder)
            val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, docByteArray)
            complete(entity)
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
