package poemGenerator

import java.io.File

class InputController {

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:1.5em;justify-content:center;font-size:2em;'>" +
      "<h1 style=font-family:'Arial'>Server is up</h1>" +
    "</div>"

  def listAll(): Array[Array[Int]] = {
    val poemNameList: Array[String] = new File("src/main/resources/poems").list
    val poemCodeListUnsorted: Array[Array[Int]] = poemNameList.map(poemName => {
      poemName.slice(0, poemName.length - 4).split("-").map(poemCodeStr => poemCodeStr.toInt)
    })
    val poemCodeList = poemCodeListUnsorted.sortBy(poemMatrix => (poemMatrix(0), poemMatrix(1), poemMatrix(2)))
    poemCodeList
  }

}
