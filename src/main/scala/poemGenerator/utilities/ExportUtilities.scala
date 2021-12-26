package poemGenerator.utilities

import com.spire.doc.Document
import com.spire.doc.documents.{HorizontalAlignment, Paragraph, ParagraphStyle}

import java.io.File
import java.net.URL
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

class ExportUtilities {

  def parsingXml(poemPath: URL): (String, String)  = {
    val dbf = DocumentBuilderFactory.newInstance
    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    val db = dbf.newDocumentBuilder();
    val doc = db.parse(new File(poemPath.getFile));
    doc.getDocumentElement().normalize();
    val currentPoemText: String = doc.getElementsByTagName("text").item(0).getTextContent
    val currentPoemTitle: String = doc.getElementsByTagName("title").item(0).getTextContent
    (currentPoemTitle, currentPoemText)
  }

  def creatingDocContent(currentPoemCode: String, currentPoemTitle: String, glyphPath: URL, currentPoemText: String, para1: Paragraph, para2: Paragraph, para3: Paragraph) = {
    val glyph = para3.appendPicture(glyphPath.getPath);
    para1.appendText(currentPoemCode);
    para2.appendText(currentPoemText);
    glyph.setWidth(300f);
    glyph.setHeight(200f);

    para1.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    para1.getFormat().setAfterSpacing(15f);
    para2.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    para2.getFormat().setAfterSpacing(30f);
    para3.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);

    para1.applyStyle("titleStyle");
    para2.applyStyle("textStyle");
  }

  def settingDocStyles(document: Document) = {
    val style1 = new ParagraphStyle(document);
    style1.setName("titleStyle");
    style1.getCharacterFormat().setBold(true);
    style1.getCharacterFormat().setFontName("Arial");
    style1.getCharacterFormat().setFontSize(12f);
    document.getStyles().add(style1);

    val style2 = new ParagraphStyle(document);
    style2.setName("textStyle");
    style2.getCharacterFormat().setFontName("Arial");
    style2.getCharacterFormat().setFontSize(12f);
    document.getStyles().add(style2);
  }

}
