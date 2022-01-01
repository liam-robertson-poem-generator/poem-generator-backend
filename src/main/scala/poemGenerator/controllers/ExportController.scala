package poemGenerator.controllers

import akka.util.ByteString
import com.spire.doc.documents.{HorizontalAlignment, Paragraph, ParagraphStyle}
import com.spire.doc.{Document, FileFormat}

import java.io.{ByteArrayOutputStream, InputStream}
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

class ExportController {

  val inputController = new InputController();

  def exportToWord(poemList: Array[Array[Int]]) = {
    val document = new Document();
    this.settingDocStyles(document);

    poemList.map(poem => {
      val currentPoemCode = poem.mkString("-")
      val imageStream: InputStream =  getClass().getResourceAsStream("/glyphs/" + currentPoemCode + ".jpg")
      val section = document.addSection();
      val para1 = section.addParagraph();
      val para2 = section.addParagraph();
      val para3 = section.addParagraph();

      val (currentPoemTitle, currentPoemText) = this.parsingXml(currentPoemCode);
      this.creatingDocContent(currentPoemCode, currentPoemTitle, imageStream, currentPoemText, document, para1, para2, para3)
    })
    val docByteOut = new ByteArrayOutputStream()
    document.saveToStream(docByteOut, FileFormat.Docx);
    val docByteArray = ByteString(docByteOut.toByteArray)
    docByteArray
  }

  def parsingXml(currentPoemCode: String): (String, String)  = {
    val poemStream: InputStream =  getClass().getResourceAsStream("/poems/" + currentPoemCode + ".xml")
    val dbf = DocumentBuilderFactory.newInstance
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    val db = dbf.newDocumentBuilder();
    val doc = db.parse(poemStream);
    doc.getDocumentElement().normalize();
    val currentPoemText: String = doc.getElementsByTagName("text").item(0).getTextContent
    val currentPoemTitle: String = doc.getElementsByTagName("title").item(0).getTextContent
    (currentPoemTitle, currentPoemText)
  }

  def creatingDocContent(currentPoemCode: String, currentPoemTitle: String, imageStream: InputStream, currentPoemText: String, document: Document, para1: Paragraph, para2: Paragraph, para3: Paragraph) = {
    val glyph = para3.appendPicture(imageStream);
    if (currentPoemTitle == "") {
      para1.appendText(currentPoemCode);
    } else {
      para1.appendText(currentPoemTitle);
    }
    para2.appendText(currentPoemText);
    glyph.setWidth(300f);
    glyph.setHeight(200f);

    para1.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    para1.getFormat().setBeforeSpacing(20f)
    para1.getFormat().setAfterSpacing(15f);
    para2.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    para2.getFormat().setLineSpacing(20f)
    para2.getFormat().setAfterAutoSpacing(false)
    para2.getFormat().setAfterSpacing(10f);
    para3.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);

    para1.applyStyle("titleStyle");
    para2.applyStyle("textStyle");
  }

  def settingDocStyles(document: Document) = {
    val style1 = new ParagraphStyle(document);
    style1.setName("titleStyle");
    style1.getCharacterFormat().setBold(true);
    style1.getCharacterFormat().setFontName("Garamond");
    style1.getCharacterFormat().setFontSize(14f);
    document.getStyles().add(style1);

    val style2 = new ParagraphStyle(document);
    style2.setName("textStyle");
    style2.getCharacterFormat().setFontName("Garamond");
    style2.getCharacterFormat().setFontSize(12f);
    document.getStyles().add(style2);
  }
}
