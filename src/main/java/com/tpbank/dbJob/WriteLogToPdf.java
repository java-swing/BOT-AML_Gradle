package com.tpbank.dbJob;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class WriteLogToPdf {
	private LinkedList<String> logLinkedList; String nameFile;

	public WriteLogToPdf(LinkedList<String> logLinkedList, String nameFile) {
		this.logLinkedList = logLinkedList;
		this.nameFile = nameFile;
	}

	public void createTextToAPdf(LinkedList<String> log, String nameFile)
			throws IOException {

		PDDocument doc = new PDDocument();
		doc.addPage(new PDPage());

		PDPage page = doc.getPage(0);
		PDPageContentStream contentStream = new PDPageContentStream(doc, page);

		// Begin the Content stream
		contentStream.beginText();

		// Setting the font to the Content stream
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 16);

		// không sử dụng được for each nên phải sử dụng iterator cho linkedlist đẻ add các dòng vào contentStream
		Iterator<String> it = log.iterator();
		while(it.hasNext()) {
			String value = it.next();
			contentStream.showText(value);
			contentStream.newLine();

		}
		// Setting the position for the line
		contentStream.newLineAtOffset(25, 725);
//		String text = log;

		// Adding text in the form of string
//		contentStream.showText(text);

		// Ending the content stream
		contentStream.endText();
		System.out.println("Content added");

		// Closing the content stream
		contentStream.close();

		// Saving the document
		doc.save(new File("/home/thanhdinh/IdeaPro/" + nameFile + ".pdf"));
		System.out.println("PDF had been saved");

		// Closing the document
		doc.close();

	}

}
// Introduction to use pdfbox, that tool to create pdf in java
// https://www.tutorialspoint.com/pdfbox/pdfbox_adding_multiple_lines.htm