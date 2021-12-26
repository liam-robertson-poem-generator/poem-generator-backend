package poemGenerator.controllers

import akka.stream.scaladsl.Source
import akka.http.scaladsl.model.HttpEntity.{Chunked, ChunkStreamPart}
import akka.http.scaladsl.model.{HttpResponse, ContentTypes}
class PrimaryController {

  val inputController = new InputController();
  val exportController = new ExportController();

  /**
   * Takes in poem content and glyphs from file system
   * poems have codes in form [x,y,z] e.g. Array([1,1,1], [1,9,1], [5,3,2]...)
   * Algorithm iterates through poem codes by coordinate e.g. Array([x,y,z], [x+1,y,z], [x+1,y+1,z], [x+1,y+1,z+1], [x+2,y+1,z+1]...)
   * The poems outputted by the algorithm are then appended to a word document
   * The word document is served to the client
   */
  def primaryExecutor() = {
    val poem2dArray: Array[Array[Int]] = inputController.getSorted2dArray()
    val outputPoemList = inputController.iterateSorted2dArray(3, Array(1, 1, 1), "start", poem2dArray)
    exportController.exportToWord(outputPoemList)
  }

}
