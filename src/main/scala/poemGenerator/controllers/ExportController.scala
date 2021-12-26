package poemGenerator.controllers

import com.spire.doc.{Document, FileFormat}
import poemGenerator.utilities.ExportUtilities

import java.io.File

class ExportController {

  val inputController = new InputController();
  val exportUtilities = new ExportUtilities();

  def exportToWord(poemList: Array[Array[Int]]) = {
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
    document.saveToFile(new File("src/main/resources/output/output.docx").getAbsolutePath(), FileFormat.Docx);
  }
}
