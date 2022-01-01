package poemGenerator.controllers

import akka.util.ByteString

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
  def primaryExecutor(numOfPoems: Int, startingPoem: Array[Int], poemOrder: String) = {
    val outputPoemList = inputController.iterateSorted2dArray(numOfPoems, startingPoem, poemOrder)
    val docByteArray: ByteString = exportController.exportToWord(outputPoemList)
    docByteArray
  }

}
