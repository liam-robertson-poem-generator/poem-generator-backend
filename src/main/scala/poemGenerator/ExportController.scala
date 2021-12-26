package poemGenerator

import com.spire.doc.{Document, FileFormat, Section}
import poemGenerator.controllerUtilites.ExportUtilities

class ExportController {

  case class Poem(code: String, title: String, text: String, glyph: String)
  val inputController = new InputController();
  val exportUtilities = new ExportUtilities();

  def exportToWord() = {
    val poemList = inputController.iterateSorted2dArray(4, Array(1,1,9), "forwards");
    val document = new Document();

    exportUtilities.settingDocStyles(document);

    poemList.map(poem => {
      val currentPoemCode = poem.mkString("-")
      val poemPath = this.getClass.getResource("/poems/" + currentPoemCode + ".xml")
      val glyphPath = this.getClass.getResource("/glyphs/" + currentPoemCode + ".jpg")

      val section = document.addSection();
      val para1 = section.addParagraph();
      val para2 = section.addParagraph();
      val para3 = section.addParagraph();

      val (currentPoemTitle, currentPoemText) = exportUtilities.parsingXml(poemPath);
      exportUtilities.creatingDocContent(currentPoemCode, currentPoemTitle, glyphPath, currentPoemText, para1, para2, para3)

    })
    document.saveToFile("Output.docx", FileFormat.Docx);
    ""
  }
}
