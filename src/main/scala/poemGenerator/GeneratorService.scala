package poemGenerator
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.ByteString
import poemGenerator.controllers.{InputController, PrimaryController}
import poemGenerator.tools.CORSHandler
import spray.json.DefaultJsonProtocol

class GeneratorService extends SprayJsonSupport with DefaultJsonProtocol with  CORSHandler {
  val inputController = new InputController();
  val primaryController = new PrimaryController();

  case class generatorParameters(startingPoem: Array[Int], numOfPoems: Int, poemOrder: String)
  implicit val parametersFormat = jsonFormat3(generatorParameters)

  def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case cause: Exception =>
        complete(HttpResponse(500, entity = cause.getMessage)
        )
    }

        val routes: Route =
      concat(
        corsHandler(
          handleExceptions(myExceptionHandler) {
            get {
              pathSingleSlash {
                complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
              }
              path("listAllPoems") {
                complete(inputController.getSorted2dArray())
              } ~
              path("getIteratedList") {
                parameters("numOfPoems".as[Int], "startingPoem".as[Array[Int]], "poemOrder") { (numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) =>
                  complete(inputController.iterateSorted2dArray(numOfPoems, startingPoem, poemOrder))
                }
              } ~
              path("createDocument") {
                parameters("numOfPoems".as[Int], "startingPoem".as[Array[Int]], "poemOrder") { (numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) =>
                  val docByteArray: ByteString = primaryController.primaryExecutor(numOfPoems, startingPoem, poemOrder)
                  val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, docByteArray)
                  complete(entity)
                }
              }
            }
          }
        )
      )
}
