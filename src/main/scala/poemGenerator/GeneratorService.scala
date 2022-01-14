package poemGenerator
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import poemGenerator.controllers.{InputController, PrimaryController}
import poemGenerator.tools.{CORSHandler, ExceptionHandling}
import spray.json.DefaultJsonProtocol

class GeneratorService extends SprayJsonSupport with DefaultJsonProtocol with  CORSHandler with ExceptionHandling {
  val inputController = new InputController();
  val primaryController = new PrimaryController();

  case class generatorParameters(startingPoem: Array[Int], numOfPoems: Int, poemOrder: String)
  implicit val parametersFormat = jsonFormat3(generatorParameters)

  val routes: Route =
    concat(
      corsHandler(
        get {
          pathSingleSlash {
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, inputController.serverStatusMessage))
          }
          path("listAllPoems") {
            exceptionHandler(() => {
              inputController.getSorted2dArray()
            })
          } ~
          path("createDocument") {
            parameters("numOfPoems".as[Int], "startingPoem".as[Array[Int]], "poemOrder") { (numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) =>
              exceptionHandler(() => {
                val docByteArray: ByteString = primaryController.primaryExecutor(numOfPoems, startingPoem, poemOrder)
                val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, docByteArray)
                entity
              })






//              complete({
//                val result: ToResponseMarshallable = try {
//                  val docByteArray: ByteString = primaryController.primaryExecutor(numOfPoems, startingPoem, poemOrder)
//                  val entity = HttpEntity.Strict(MediaTypes.`application/octet-stream`, docByteArray)
//                  entity
//                } catch {
//                  case e: Throwable => {
//                    errorMessage(e)
//                  }
//                }
//                result
//              })
          }
        }
      }
    )
  )
}
