package com.tpbank.dbJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import com.github.lgooddatepicker.components.DatePicker;

public class QueryJobToExportLog {
	private String jdStartDateRs;
	private String jdEndDateRs;
	private DatePicker jdStartDateRs2;
	private DatePicker jdEndDateRs2;

	// private StringBuffer log;

	public QueryJobToExportLog(String jdStartDateRs, String jdEndDateRs) {
		this.jdStartDateRs = jdStartDateRs;
		this.jdEndDateRs = jdEndDateRs;
	}

	public QueryJobToExportLog() {
	}

	public QueryJobToExportLog(DatePicker jdStartDateRs2,
			DatePicker jdEndDateRs2) {
		super();
		this.jdStartDateRs2 = jdStartDateRs2;
		this.jdEndDateRs2 = jdEndDateRs2;
	}

	public LinkedList<String> getQueryJobToExportLog()
			throws ClassNotFoundException, SQLException {
		OracleConnUtils conn = new OracleConnUtils();

		// Lấy ra đối tượng Connection kết nối vào DB.
		Connection connection = OracleConnUtils.getOracleConnection();

		// Tạo đối tượng Statement.
		Statement statement = connection.createStatement();

		// Query Data from DB
		String sql = queryStringFromDatePicker(jdStartDateRs2, jdEndDateRs2);

		boolean resultSet = statement.execute(sql);

		// Thực thi câu lệnh SQL trả về đối tượng ResultSet.
		ResultSet rs = statement.executeQuery(sql);
		String log = "";
		LinkedList<String> logLinkedList = new LinkedList<String>();
		// Duyệt trên kết quả trả về.
		while (rs.next()) {// Di chuyển con trỏ xuống bản ghi kế tiếp.

			int TT = rs.getInt(1);
			String id = Integer.toString(TT);
			String kind = rs.getString(2);
			String creatingTime = rs.getString(3);
			String fileName = rs.getString(4);
			String status = rs.getString(5);
			String filePath = rs.getString(6);
			String note = rs.getString(7);

			log = log
					+ ("-------------------- \n" + "TT: " + id + "\n"
							+ "Loại: " + kind + "\n" + "Thời gian thực hiện: "
							+ creatingTime + "\n" + "File name: " + fileName
							+ "\n" + "Status: " + status + "\n"
							+ "Đường dẫn lưu file: " + filePath + "\n"
							+ "Ghi chú: " + note + "\n");
			logLinkedList.add(id);
			logLinkedList.add(kind);
			logLinkedList.add(creatingTime);
			logLinkedList.add(fileName);
			logLinkedList.add(note);
			logLinkedList.add(filePath);
			logLinkedList.add(note);

		}

		System.out.println("finish " + log);
		// Đóng kết nối
		connection.close();
		return logLinkedList;
	}

	public Vector getAllData() {
		Vector list = new Vector();
		// Lấy ra đối tượng Connection kết nối vào DB.
		try {
			OracleConnUtils conn = new OracleConnUtils();
			Connection connection = OracleConnUtils.getOracleConnection();
			// Tạo đối tượng Statement.
			Statement statement = connection.createStatement();
			// Query Data from DB
			String sql = queryStringFromDatePicker(jdStartDateRs2, jdEndDateRs2);
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) { 
				Vector data = new Vector();
				data.addElement(rs.getInt(1));
				data.addElement(rs.getString(2));
				data.addElement(rs.getString(3));
				data.addElement(rs.getString(4));
				data.addElement(rs.getString(5));
				data.addElement(rs.getString(6));
				data.addElement(rs.getString(7));
				list.add(data);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	private String queryString(String jdStartDateRs2, String jdEndDateRs2) {
		String queryStr = "";
		queryStr = " select * from aml_bot_log a where a.creating_time between "
				+ "'"
				+ jdStartDateRs2
				+ " ' "
				+ "and "
				+ "'"
				+ jdEndDateRs2
				+ "'" + " order by a.creating_time ASC";
		return queryStr;
	}

	private String queryStringFromDatePicker(DatePicker jdStartDateRs2,
			DatePicker jdEndDateRs2) {
		String queryStr = "";
		queryStr = " select * from aml_bot_log a where a.creating_time between "
				+ "'"
				+ jdStartDateRs2
				+ " ' "
				+ "and "
				+ "'"
				+ jdEndDateRs2
				+ "'" + " order by a.creating_time ASC";
		return queryStr;
	}

}
