package poemGenerator.controllers

import java.io.File
import java.util.jar.JarFile
import java.util.zip.ZipInputStream
import scala.io.Source


class InputController {

  def iterateSorted2dArray(numOfPoems: Int, startingPoem: Array[Int], poemDirection: String) = {
    var coordList: Array[Int] = Array(1, 2, 0)
    var currentPoem: Array[Int] = startingPoem
    var nextPoem: Array[Int] = startingPoem

    currentPoem = nextPoem
    var final2dArray: Array[Array[Int]] = Array() :+ currentPoem
    coordList = coordList.slice(1,3) ++ coordList.slice(0,1)
    var poem2dArray: Array[Array[Int]] = this.getSorted2dArray().sortBy(poemMatrix => (poemMatrix(coordList(0)), poemMatrix(coordList(1)), poemMatrix(coordList(2))))
    nextPoem = poem2dArray(poem2dArray.map(poemName => poemName.mkString(",")).indexOf(currentPoem.mkString(",")) + 1)

    while (final2dArray.length < numOfPoems) {
      currentPoem = nextPoem
      final2dArray = final2dArray :+ currentPoem
      poem2dArray = poem2dArray.filter(poemName => poemName != currentPoem)
      coordList = coordList.slice(1,3) ++ coordList.slice(0,1)
      poem2dArray = poem2dArray.sortBy(poemMatrix => (poemMatrix(coordList(0)), poemMatrix(coordList(1)), poemMatrix(coordList(2))))
      nextPoem = poem2dArray(poem2dArray.map(poemName => poemName.mkString(",")).indexOf(currentPoem.mkString(",")) + 1)
    }
    if (poemDirection == "end") {
      final2dArray = final2dArray.reverse
    }
    final2dArray
  }

  def getSorted2dArray(): Array[Array[Int]] = {
    var poemNameList: Array[String] = Array()
    val jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    if(jarFile.isFile()) {
      val jar = new JarFile(jarFile);
      val entries = jar.entries();
      while (entries.hasMoreElements()) {
        val filePath = entries.nextElement().getName();
        if (filePath.startsWith("poems/") && filePath != "poems/") {
          val filename = filePath.split("/").last
          poemNameList = poemNameList :+ filename
        }
      }
      jar.close();
    } else {
      val poemStream =  getClass().getResourceAsStream("/poems")
      if (poemStream != null) {
        poemNameList = Source.fromInputStream(poemStream).getLines.toArray
      }
    }

    val poemCodeListUnsorted: Array[Array[Int]] = poemNameList.map(poemName => {
      poemName.slice(0, poemName.length - 4).split("-").map(poemCodeStr => poemCodeStr.toInt)
    })
    val poemCodeList = poemCodeListUnsorted.sortBy(poemMatrix => (poemMatrix(0), poemMatrix(1), poemMatrix(2)))
    poemCodeList
  }

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
      "<h2 style=font-family:'Arial'>Server is up</h2>" +
    "</div>"

}
