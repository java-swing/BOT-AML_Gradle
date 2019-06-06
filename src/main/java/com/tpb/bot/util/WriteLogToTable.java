package com.tpb.bot.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.github.lgooddatepicker.components.DatePicker;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.pdf.BaseFont;

public class WriteLogToTable {
	private DatePicker jdStartDateRs;
	private DatePicker jdStopDateRs;

	public WriteLogToTable(List<LogFileDownload> logLinkedList,
			String nameFile, DatePicker jdStartDateRs, DatePicker jdStopDateRs) {
		super();
		this.jdStartDateRs = jdStartDateRs;
		this.jdStopDateRs = jdStopDateRs;
	}

	public static final String FONT = "font\\vuTimes.ttf";
	public static final int fontTitleSize = 14;
	public static final int fontTitleTableSize = 12;
	public static final int fontContentTableSize = 11;

	public void drawTablePDF(List<LogFileDownload> logList, String nameFile)
			throws Exception {

		String DEST = com.tpb.bot.constant.Constant.SAVE_REPORT + nameFile
				+ ".pdf";
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
		pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

		PdfFont font = PdfFontFactory.createFont(FONT, BaseFont.IDENTITY_H,
				BaseFont.EMBEDDED);
		Document doc = new Document(pdfDoc);
		doc.setFont(font);
		Paragraph title = new Paragraph("BÁO CÁO \n Từ ngày: "
				+ jdStartDateRs.getDate().format(dtf) + " đến ngày: "
				+ jdStopDateRs.getDate().format(dtf) + "\n");
		title.setTextAlignment(TextAlignment.CENTER);
		title.setBold();
		title.setFontSize(fontTitleSize);
		doc.add(title);
		float[] pointColumnWidths = { 30F, 120F, 70F, 100F, 100F, 250F, 100F };
		Table table = new Table(pointColumnWidths);
		addCell(table, "STT", true);
		addCell(table, "Thời gian thực hiện", true);
		addCell(table, "Loại", true);
		addCell(table, "Tên khách hàng", true);
		addCell(table, "Trạng thái", true);
		addCell(table, "Đường dẫn lưu tệp", true);
		addCell(table, "Ghi chú", true);

		List<LogFileDownload> list = logList;
		int i = 0;
		int j = 1;
		while (i < list.size()) {
			String valueId = "" + j;
			String valueCreatingTime = list.get(i).getDatefiledownload();
			String valueKind = list.get(i).getType();
			String valueFileName = list.get(i).getName();
			String valueStatus = list.get(i).getStatus();
			String valueFilePath = list.get(i).getFilepathfolder();
			String valueNote = list.get(i).getNote();
			i++;
			j++;

			addCell(table, valueId, false);
			addCell(table, valueKind, false);
			addCell(table, valueCreatingTime, false);
			addCell(table, valueFileName, false);
			addCell(table, valueStatus.toString(), false);
			addCell(table, valueFilePath, false);
			addCell(table, valueNote, false);
		}

		doc.add(table);
		doc.close();
		System.out.println("Data export to pdf file has been successfully!");
		System.out.println("Pdf file has save in: "
				+ com.tpb.bot.constant.Constant.SAVE_REPORT + ": " + nameFile
				+ ".pdf");
	}

	private void addCell(Table table, String header, boolean check) {
		Cell cell = new Cell();
		cell.add(header);
		if (check == true) {
			cell.setFontSize(fontTitleTableSize);
			cell.setBold();
			table.addHeaderCell(cell);
			cell.setTextAlignment(TextAlignment.CENTER);
		} else {
			table.addCell(cell);
			cell.setFontSize(fontContentTableSize);
			
		}
	}

}