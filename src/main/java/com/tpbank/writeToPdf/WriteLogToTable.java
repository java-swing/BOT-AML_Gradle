package com.tpbank.writeToPdf;

import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import be.quodlibet.boxable.*;

public class WriteLogToTable {
	private LinkedList<String> logLinkedList;
	private String nameFile;

	public WriteLogToTable(LinkedList<String> logLinkedList, String nameFile) {
		super();
		this.logLinkedList = logLinkedList;
		this.nameFile = nameFile;
	}

	public void drawTablePDF(LinkedList<String> logLinkedList, String nameFile)
			throws Exception {
		String outputFileName = "/home/thanhdinh/IdeaProjects/JavaSwing/PDFBox/" + nameFile + ".pdf";
		// if (args.length > 0)
		// outputFileName = args[0];

		// Create a new font object selecting one of the PDF base fonts
		PDFont fontPlain = PDType1Font.HELVETICA;
		PDFont fontBold = PDType1Font.HELVETICA_BOLD;
		PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;
		PDFont fontMono = PDType1Font.COURIER;

		// Create a document and add a page to it
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		// PDRectangle.LETTER and others are also possible
		PDRectangle rect = page.getMediaBox();
		// rect can be used to get the page width and height
		document.addPage(page);

		// Start a new content stream which will "hold" the to be created
		// content
		PDPageContentStream cos = new PDPageContentStream(document, page);
//==================================================================================================================
		// Begin the Content stream
		cos.beginText();

		// Setting the font to the Content stream
		cos.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 13);

		// Setting the position for the line
		cos.newLineAtOffset(185, 800);

		String text = nameFile;

		// Adding text in the form of string
		cos.showText(text);
		cos.newLine();

		// Ending the content stream
		cos.endText();
//==================================================================================================================
		// Dummy Table
		float margin = 50;
		// starting y position is whole page height subtracted by top and bottom
		// margin
		float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
		// we want table across whole page width (subtracted by left and right
		// margin ofcourse)
		float tableWidth = page.getMediaBox().getWidth() - (2 * margin);

		boolean drawContent = true;
		float yStart = yStartNewPage;
		float bottomMargin = 70;
		// y position is your coordinate of top left corner of the table
		float yPosition = 775;

		BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin,
				tableWidth, margin, document, page, true, drawContent);

		// ========================================================================================================

		// the parameter is the row height
		Row<PDPage> headerRow = table.createRow(20);
		// the first parameter is the cell width

		Cell<PDPage> cell = headerRow.createCell(5, "TT");
		cell.setFontSize(12);
		cell.setFont(fontBold);
		cell.setValign(VerticalAlignment.MIDDLE);
		cell.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell1 = headerRow.createCell(10, "Kind");
		cell1.setFontSize(12);
		cell1.setFont(fontBold);
		cell1.setValign(VerticalAlignment.MIDDLE);
		cell1.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell2 = headerRow.createCell(25, "Creating Time");
		cell2.setFontSize(12);
		cell2.setFont(fontBold);
		cell2.setValign(VerticalAlignment.MIDDLE);
		cell2.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell3 = headerRow.createCell(25, "File name");
		cell3.setFontSize(12);
		cell3.setFont(fontBold);
		cell3.setValign(VerticalAlignment.MIDDLE);
		cell3.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell4 = headerRow.createCell(10, "Status");
		cell4.setFontSize(12);
		cell4.setFont(fontBold);
		cell4.setValign(VerticalAlignment.MIDDLE);
		cell4.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell5 = headerRow.createCell(15, "Save Folder");
		cell4.setFontSize(12);
		cell4.setFont(fontBold);
		cell4.setValign(VerticalAlignment.MIDDLE);
		cell4.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		Cell<PDPage> cell6 = headerRow.createCell(10, "Note");
		cell4.setFontSize(12);
		cell4.setFont(fontBold);
		cell4.setValign(VerticalAlignment.MIDDLE);
		cell4.setAlign(HorizontalAlignment.CENTER);
		table.addHeaderRow(headerRow);

		// ===========================================================Body================================
		// Iterator<String> it = logLinkedList.iterator();
		ArrayList<String> list = new ArrayList<String>(logLinkedList);
		int i = 0;
		String valueFilePath = "";
		String valueNote = "";
		while (i <= list.size() - 6) {
			String valueId = list.get(i);
			String valueKind = list.get(i + 1);
			String valueCreatingTime = list.get(i + 2);
			String valueFileName = list.get(i + 3);
			String valueStatus = list.get(i + 4);
			String valueSaveFilePath = list.get(i + 5);
			String valueNoteError = list.get(i + 6);
			i = i + 7;

			Row<PDPage> row = table.createRow(10);
			cell = row.createCell(5, valueId);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(10, valueKind);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(25, valueCreatingTime);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(25, valueFileName);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(10, valueStatus);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(15, valueSaveFilePath);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

			cell = row.createCell(10, valueNoteError);
			cell.setFontSize(11);
			cell.setValign(VerticalAlignment.MIDDLE);
			cell.setAlign(HorizontalAlignment.CENTER);

		}

		table.draw();

		float tableHeight = table.getHeaderAndDataHeight();
		System.out.println("tableHeight = " + tableHeight);
		System.out.println("result save in: \"/home/thanhdinh/IdeaProjects/JavaSwing/PDFBox/\"");

		// close the content stream
		cos.close();

		// Save the results and ensure that the document is properly closed:
		document.save(outputFileName);
		document.close();
	}
}