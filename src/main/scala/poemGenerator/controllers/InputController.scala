package poemGenerator.controllers

import java.io.File
import java.util.jar.JarFile
import scala.io.Source

class InputController {

  def iterateSorted2dArray(numOfPoems: Int, startingPoem: Array[Int], poemDirection: String): Array[Array[Int]] = {
    var poemList: Array[Array[Int]] = this.getSorted2dArray
    var (uniqueCoordList1, uniqueCoordList2, uniqueCoordList3) = this.updateUniqueLists(poemList, startingPoem)

    val nextAxisNumberDict: Map[Int, Int] = Map(0 -> 1, 1 -> 2, 2 -> 0)
    var outputList: Array[Array[Int]] = Array();
    var successCounter: Int = 0
    var (xLoopCounter, yLoopCounter, zLoopCounter): (Int, Int, Int) = (0, 0, 0)
    var currentAxisNumber: Int = 0;

    var currentXUniqueList: Array[Int] = Array()
    var currentYUniqueList: Array[Int] = Array()
    var currentZUniqueList: Array[Int] = Array()
    var currentXCoord: Int = 0
    var currentYCoord: Int = 1
    var currentZCoord: Int = 2
    var currentTargetPoem: Array[Int] = Array()

    while (successCounter != numOfPoems) {
      val axisDict: Map[Int, Array[Int]] = Map(0 -> uniqueCoordList1, 1 -> uniqueCoordList2, 2 -> uniqueCoordList3)
      currentXUniqueList = axisDict(currentAxisNumber)
      currentYUniqueList = axisDict(nextAxisNumberDict(currentAxisNumber))
      currentZUniqueList = axisDict(nextAxisNumberDict(nextAxisNumberDict(currentAxisNumber)))
      currentXCoord = currentXUniqueList(xLoopCounter)
      currentYCoord = currentYUniqueList(yLoopCounter)
      currentZCoord = currentZUniqueList(zLoopCounter)

      val targetPoemDict: Map[Int, Array[Int]] = Map(0 -> Array(currentXCoord, currentYCoord, currentZCoord), 1 -> Array(currentZCoord, currentXCoord, currentYCoord), 2 -> Array(currentYCoord, currentZCoord, currentXCoord));
      currentTargetPoem = targetPoemDict(currentAxisNumber)

      if (!this.checkArrIn2dMatrix(outputList, currentTargetPoem) && this.checkArrIn2dMatrix(poemList, currentTargetPoem)) {
        val uniqueListTuple = this.updateUniqueLists(poemList, currentTargetPoem)
        uniqueCoordList1 = uniqueListTuple._1
        uniqueCoordList2 = uniqueListTuple._2
        uniqueCoordList3 = uniqueListTuple._3

        currentAxisNumber = nextAxisNumberDict(currentAxisNumber)
        outputList = outputList :+ currentTargetPoem;
        poemList = poemList.filter(poemCode => !(poemCode sameElements currentTargetPoem))
        xLoopCounter = 0
        yLoopCounter = 0
        zLoopCounter = 0

        xLoopCounter = 0
        yLoopCounter = 0
        zLoopCounter = 0
        successCounter += 1

      } else if (currentXCoord == currentXUniqueList(currentXUniqueList.length - 1)) {
        if (currentYCoord == currentYUniqueList(currentYUniqueList.length - 1)) {
          if (currentZCoord == currentZUniqueList(currentZUniqueList.length - 1)) {
            zLoopCounter  = 0;
            yLoopCounter = 0;
            xLoopCounter = 0;
          }
          zLoopCounter += 1;
          yLoopCounter = 0;
          xLoopCounter = 0;
        }
        yLoopCounter += 1;
        xLoopCounter = 0;
      } else {
        xLoopCounter += 1;
      }
    }
    if (poemDirection == "end") {
      outputList = outputList.reverse
    }
    outputList
  }

  def updateUniqueLists(poemList: Array[Array[Int]], currentPoem: Array[Int]): (Array[Int], Array[Int], Array[Int]) = {
    val uniqueCoordList1Raw: Array[Int] = this.sortedUniqueList(poemList, 0)
    val uniqueCoordList2Raw: Array[Int] = this.sortedUniqueList(poemList, 1)
    val uniqueCoordList3Raw: Array[Int] = this.sortedUniqueList(poemList, 2)

    val xIndex = uniqueCoordList1Raw.indexOf(currentPoem(0))
    val yIndex = uniqueCoordList2Raw.indexOf(currentPoem(1))
    val zIndex = uniqueCoordList3Raw.indexOf(currentPoem(2))

    val uniqueCoordList1: Array[Int] = uniqueCoordList1Raw.slice(xIndex, uniqueCoordList1Raw.length) ++ (uniqueCoordList1Raw.slice(0, xIndex));
    val uniqueCoordList2: Array[Int] = uniqueCoordList2Raw.slice(yIndex, uniqueCoordList2Raw.length) ++ (uniqueCoordList2Raw.slice(0, yIndex));
    val uniqueCoordList3: Array[Int] = uniqueCoordList3Raw.slice(zIndex, uniqueCoordList3Raw.length) ++ (uniqueCoordList3Raw.slice(0, zIndex));
    (uniqueCoordList1, uniqueCoordList2, uniqueCoordList3)
  }

  def sortedUniqueList(inputList: Array[Array[Int]], coordIndex: Int): Array[Int] = {
    val currentCoordSet: Set[Int] = inputList.map(poemCode => poemCode(coordIndex)).toSet
    val currentCoordUnique: Array[Int] = currentCoordSet.toArray
    val finalUniqueList: Array[Int] = currentCoordUnique.sorted
    finalUniqueList
  }

  def checkArrIn2dMatrix(matrix: Array[Array[Int]], testArr: Array[Int]): Boolean = {
    val matrixStr: Array[String] = matrix.map((poemCode: Array[Int]) => poemCode.mkString(","));
    matrixStr.contains(testArr.mkString(","))
  }

  def getSorted2dArray: Array[Array[Int]] = {
    val poemNameList = listDirectoryContent("poems")
    val poemCodeListUnsorted: Array[Array[Int]] = poemNameList.map(poemName => {
      poemName.slice(0, poemName.length - 4).split("-").map(poemCodeStr => poemCodeStr.toInt)
    })
    val poemCodeList = poemCodeListUnsorted.sortBy(poemMatrix => (poemMatrix(0), poemMatrix(1), poemMatrix(2)))
    poemCodeList
  }

  def getUniqueLists(poem2dArray: Array[Array[Int]]): (List[Int], List[Int], List[Int]) = {
    var uniqueXList: Set[Int] = Set()
    var uniqueYList: Set[Int] = Set()
    var uniqueZList: Set[Int] = Set()
    poem2dArray.foreach((poemCode: Array[Int]) => {
      uniqueXList += poemCode(0)
      uniqueYList += poemCode(1)
      uniqueZList += poemCode(2)
    })
    (uniqueXList.toList.sorted, uniqueYList.toList.sorted, uniqueZList.toList.sorted)
  }

  def listDirectoryContent(path: String) = {
    var poemNameList: Array[String] = Array()
    val jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
    if(jarFile.isFile()) {
      val jar = new JarFile(jarFile);
      val entries = jar.entries();
      while (entries.hasMoreElements()) {
        val filePath = entries.nextElement().getName();
        if (filePath.startsWith(path + "/") && filePath != path + "/") {
          val filename = filePath.split("/").last
          poemNameList = poemNameList :+ filename
        }
      }
      poemNameList = poemNameList.distinct
      jar.close();
    } else {
      val poemStream =  getClass().getResourceAsStream("/" + path)
      if (poemStream != null) {
        poemNameList = Source.fromInputStream(poemStream).getLines.toArray
      }
    }
    poemNameList
  }

  val serverStatusMessage: String =
    "<div style='display:flex;padding-top:3em;justify-content:center;font-size:2em;'>" +
      "<h2 style=font-family:'Arial'>Server is up</h2>" +
    "</div>"

}
