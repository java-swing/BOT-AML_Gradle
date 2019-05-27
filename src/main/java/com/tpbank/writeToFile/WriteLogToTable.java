package com.tpbank.writeToFile;

import java.util.ArrayList;
import java.util.LinkedList;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.pdf.BaseFont;

public class WriteLogToTable {

	private LinkedList<String> logLinkedList;
	private String nameFile;

	public WriteLogToTable(LinkedList<String> logLinkedList, String nameFile) {
		super();
		this.logLinkedList = logLinkedList;
		this.nameFile = nameFile;
	}

	public static final String FONT = "font/vuTimes.ttf";
	public static final int fontTitleSize = 13;
	public static final int fontTitleTableSize = 12;
	public static final int fontContentTableSize = 11;

	public void drawTablePDF(LinkedList<String> logLinkedList, String nameFile)
			throws Exception {

		String DEST = "/home/thanhdinh/IdeaProjects/JavaSwing/PDFBox" + nameFile + ".pdf";

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
		PdfFont font = PdfFontFactory.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		// Creating a Document object
		Document doc = new Document(pdfDoc);
		doc.setFont(font);

		// add paragraph title
		Paragraph title = new Paragraph(nameFile);
		title.setTextAlignment(TextAlignment.CENTER);
		title.setBold();
		title.setFontSize(fontTitleSize);
		// title.setFixedPosition(150, 800, 300);
		doc.add(title);
		// add area break for save title and table in a page

		// Creating a table
		float[] pointColumnWidths = { 30F, 100F, 70F, 145F, 50F, 70F, 60F };
		Table table = new Table(pointColumnWidths);

		// Adding row 1 to the table
		addCell(table, "STT", true);

		// Adding row 2 to the table
		addCell(table, "Thời gian thực hiện", true);

		// Adding row 3 to the table
		addCell(table, "Loại", true);

		// Adding row 4 to the table
		addCell(table, "Tên tệp", true);

		// Adding row 5 to the table
		addCell(table, "Trạng thái", true);

		// Adding row 6 to the table
		addCell(table, "Đường dẫn lưu tệp", true);

		// Adding row 7 to the table
		addCell(table, "Ghi chú", true);

		// ===========================================================Body================================
		ArrayList<String> list = new ArrayList<String>(logLinkedList);
		int i = 0;
		while (i <= list.size() - 6) {
			String valueId = list.get(i);
			String valueCreatingTime = list.get(i + 1);
			String valueKind = list.get(i + 2);
			String valueFileName = list.get(i + 3);
			String valueStatus = list.get(i + 4);
			String valueFilePath = list.get(i + 5);
			String valueNote = list.get(i + 6);
			i = i + 7;

			addCell(table, valueId, false);
			addCell(table, valueKind, false);
			addCell(table, valueCreatingTime, false);
			addCell(table, valueFileName, false);
			addCell(table, valueStatus.toString(), false);
			addCell(table, valueFilePath, false);
			addCell(table, valueNote, false);
		}

		doc.add(table);

		// Closing the document
		doc.close();
		System.out.println("Pdf export successfully..");
	}

	private void addCell(Table table, String header, boolean check) {
		Cell cell = new Cell();
//		Cell add = cell.add(header);
		if (check == true) {
			cell.setBold();
		}
		cell.setFontSize(fontTitleTableSize);
		cell.setTextAlignment(TextAlignment.CENTER);
		table.addCell(cell);
	}

}