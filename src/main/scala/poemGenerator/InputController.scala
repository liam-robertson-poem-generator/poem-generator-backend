package poemGenerator

import java.io.File
import scala.:+

class InputController {

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
      "<h2 style=font-family:'Arial'>Server is up</h2>" +
      "</div>"

  def iterateSorted2dArray(numOfPoems: Int, startingPoem: Array[Int], poemDirection: String) = {
    var currentPoemCoord: Int = 2
    var currentPoem: String = startingPoem.mkString(",")

    var poem2dArray: Array[String] = this.getSorted2dArray().sortBy(poemMatrix => (poemMatrix(currentPoemCoord)))
    var nextPoem: String = poem2dArray(poem2dArray.indexOf(currentPoem) + 1)
    var final2dArray = Array() :+ currentPoem
    currentPoem = nextPoem

    while (final2dArray.length < numOfPoems) {
      currentPoemCoord += 1
      if (currentPoemCoord == 3) {
        currentPoemCoord = 0
      }
      poem2dArray = poem2dArray.sortBy(poemMatrix => poemMatrix(currentPoemCoord)))
      nextPoem = poem2dArray(poem2dArray.map(poemName => poemName.toString).indexOf(currentPoem) + 1)
      final2dArray = final2dArray :+ currentPoem
      currentPoem = nextPoem
    }
    final2dArray.map(poemName => poemName.split(","))
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
