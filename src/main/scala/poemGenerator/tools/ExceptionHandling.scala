package poemGenerator.tools

import akka.http.javadsl.server.directives.LogEntry.error
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import org.apache.commons.lang3.exception.ExceptionUtils
import spray.json.{DefaultJsonProtocol, JsObject, JsString}

trait ExceptionHandling extends SprayJsonSupport with DefaultJsonProtocol{

  protected def exceptionHandler(foo: () => ToResponseMarshallable): StandardRoute = {
    complete({
      val result: ToResponseMarshallable = try {
        foo()
      } catch {
        case e: Throwable => {
          JsObject("error" -> JsString(e.getMessage))
//          errorMessage(e)
        }
      }
      result
    })
  }

  protected def errorMessage(e: Throwable): JsObject = {
    error(e.getMessage, e)
    val sw = ExceptionUtils.getStackTrace(e)
    error(sw)
    JsObject("error" -> JsString(e.getMessage), "details" -> JsString(sw))
  }

}
