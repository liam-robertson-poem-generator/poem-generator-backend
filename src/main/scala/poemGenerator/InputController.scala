package poemGenerator


import java.io.File


class InputController {

  def listAll() = {
    val numOfPoems = new File("src/main/resources/poems").list
  }

}
