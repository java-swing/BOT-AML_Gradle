package com.tpb.bot.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.github.lgooddatepicker.components.DatePicker;
import com.tpb.bot.constant.Constant;
import com.tpb.bot.job.ScreeningJob;

public class ReadAndToExportLog {
	private DatePicker jdStartDateRs2;
	private DatePicker jdEndDateRs2;

	// private StringBuffer log;

	public ReadAndToExportLog(String jdStartDateRs, String jdEndDateRs) {
	}

	public ReadAndToExportLog() {
	}

	public ReadAndToExportLog(DatePicker jdStartDateRs2, DatePicker jdEndDateRs2) {
		super();
		this.jdStartDateRs2 = jdStartDateRs2;
		this.jdEndDateRs2 = jdEndDateRs2;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector getAllData() throws Exception {
		Vector list = new Vector();
		try {
			ArrayList<String> logs = new ArrayList<String>(
					FunctionFactory.loadFileLog());
			SimpleDateFormat format = new SimpleDateFormat(
					Constant.DATE_FORMAT_DDMMYYYY);

			// String sql = queryStringFromDatePicker(jdStartDateRs2,
			// jdEndDateRs2);
			int j = 1;
			for (int i = 0; i < logs.size(); i++) {
				String line = logs.get(i);

				String[] words = line.split("@");
				LocalDate dateRun = convertToLocalDateViaInstant(format
						.parse(words[0]));
				if (dateRun.isAfter(jdStartDateRs2.getDate().minusDays(1))
						&& dateRun.isBefore(jdEndDateRs2.getDate().plusDays(1))) {
					Vector data = new Vector();
					data.addElement(j);
					data.addElement(words[1]);
					data.addElement(words[0]);
					data.addElement(words[2]);
					data.addElement(words[3]);
					data.addElement(words[4]);
					data.addElement(words[5]);
					list.add(data);
					j++;

				}

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}

		return list;
	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDate();
	}

	public List<LogFileDownload> getAllDataToPrintPDF() throws Exception {
		List<LogFileDownload> list = new ArrayList<>();
		// Lấy ra đối tượng Connection kết nối vào DB.
		try {
			ArrayList<String> logs = new ArrayList<String>(
					FunctionFactory.loadFileLog());
			SimpleDateFormat format = new SimpleDateFormat(
					Constant.DATE_FORMAT_DDMMYYYY);

			// String sql = queryStringFromDatePicker(jdStartDateRs2,
			// jdEndDateRs2);
			System.out.println(" logs.size() Read " + logs.size());
			for (int i = 0; i < logs.size(); i++) {
				String line = logs.get(i);
				String[] words = line.split("@");

				LocalDate dateRun = convertToLocalDateViaInstant(format
						.parse(words[0]));
				if (dateRun.isAfter(jdStartDateRs2.getDate().minusDays(1))
						&& dateRun.isBefore(jdEndDateRs2.getDate().plusDays(1))) {
					LogFileDownload data = new LogFileDownload();
					// data.add(j);
					data.setType(words[1]);
					data.setDatefiledownload(words[0]);
					data.setName(words[2]);
					data.setStatus(words[3]);
					if (words[4] == null) {
						data.setFilepathfolder(" ");
					} else {
						data.setFilepathfolder(words[4]);
					}
					if (words[5] == null) {
						data.setNote(" ");
					} else {
						data.setNote(words[5]);
					}

					list.add(data);

				}

			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}

		return list;
	}
}
