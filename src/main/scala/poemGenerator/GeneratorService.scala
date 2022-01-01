package poemGenerator
import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes}
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
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  val routes: Route =
    concat(
      get {
        pathSingleSlash {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
        } ~
        pathPrefix("outputDocument") {
          getFromResource("output/output.docx")
        } ~
        path("listAllPoems") {
          complete(inputController.getSorted2dArray())
        } ~
        path("createDocument") {
          parameters("numOfPoems".as[Int], "startingPoem".as[Array[Int]], "poemOrder") { (numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) =>
            val body: ByteString = ByteString(primaryController.primaryExecutor(numOfPoems, startingPoem, poemOrder))
            val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, body)
            val httpResponse = HttpResponse(entity = entity)
            complete(httpResponse)
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
