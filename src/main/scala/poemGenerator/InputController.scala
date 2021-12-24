package poemGenerator

import java.io.File

class InputController {

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
      "<h2 style=font-family:'Arial'>Server is up</h2>" +
      "</div>"

  def iterateSorted2dArray(numOfPoems: Int, startingPoem: Array[Int], poemDirection: String) = {
    val final2dArray: Array[Array[Int]] = Array()
    val poem2dArray: Array[Array[Int]] = this.getSorted2dArray()
    var poemCoord: Int = 0
    var currentPoem: Array[Int] = startingPoem
    var currentPoemIndex: Int = poem2dArray.indexWhere(poem => poem == currentPoem)

    while (final2dArray.length < numOfPoems) {
      poemCoord += 1
      if (poemCoord == 3) {
        poemCoord = 0
      }
      poem2dArray.sortBy(poemMatrix => poemMatrix(poemCoord))
      currentPoemIndex = poem2dArray.indexWhere(poem => poem == currentPoem)
      final2dArray :+ poem2dArray(currentPoemIndex + 1)
    }
    final2dArray
  }

  def getSorted2dArray(): Array[Array[Int]] = {
    val poemNameList: Array[String] = new File("src/main/resources/poems").list
    val poemCodeListUnsorted: Array[Array[Int]] = poemNameList.map(poemName => {
      poemName.slice(0, poemName.length - 4).split("-").map(poemCodeStr => poemCodeStr.toInt)
    })
    val poemCodeList = poemCodeListUnsorted.sortBy(poemMatrix => (poemMatrix(0), poemMatrix(1), poemMatrix(2)))
    poemCodeList
  }

}
