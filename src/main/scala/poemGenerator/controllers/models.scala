package poemGenerator.controllers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val parametersFormat: RootJsonFormat[GeneratePoemListReq] = jsonFormat3(GeneratePoemListReq)
}
case class GeneratePoemListReq(startingPoem: Array[Int], numOfPoems: Int, poemOrder: String)

