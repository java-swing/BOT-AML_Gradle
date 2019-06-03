package com.tpbank.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Vector;

import com.github.lgooddatepicker.components.DatePicker;
import com.tpbank.job.ScreeningJob;

public class QueryJobToExportLog {
	private DatePicker jdStartDateRs2;
	private DatePicker jdEndDateRs2;

	// private StringBuffer log;

	public QueryJobToExportLog(String jdStartDateRs, String jdEndDateRs) {
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
		// Lấy ra đối tượng Connection kết nối vào DB.
		Connection connection = OracleConnUtils.getOracleConnection();

		// Tạo đối tượng Statement.
		Statement statement = connection.createStatement();

		// Query Data from DB
		String sql = queryStringFromDatePicker(jdStartDateRs2, jdEndDateRs2);

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
			String filePath = "";
			if (rs.getString(6) == null) {
				filePath = " ";
			} else {
				filePath = rs.getString(6);
			}
			String note = "";
			if (rs.getString(7) == null) {
				note = " ";
			} else {
				note = rs.getString(7);
			}

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
			logLinkedList.add(status);
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
			int j = 1;
			while (rs.next()) {
				Vector data = new Vector();
				data.addElement(j);
				data.addElement(rs.getString(2));
				data.addElement(rs.getString(3));
				data.addElement(rs.getString(4));
				data.addElement(rs.getString(5));
				data.addElement(rs.getString(6));
				data.addElement(rs.getString(7));
				list.add(data);
				j++;
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

	private String queryStringFromDatePicker(DatePicker jdStartDateRs2,
			DatePicker jdEndDateRs2) {
		String queryStr = "";
		queryStr = " select * from aml_bot_log a where a.creating_time between "
				+ "'"
				+ jdStartDateRs2
				+ " ' "
				+ "and "
				+ "'"
				+ jdEndDateRs2.getDate().plusDays(1)
				+ "'" + " order by a.creating_time ASC";
		return queryStr;
	}
	
	public static void queryStringInsertData(String type, String dateCreate,
			String name, String status, String foler, String note) {

		OracleConnUtils conn = new OracleConnUtils();

		// Lấy ra đối tượng Connection kết nối vào DB.
		Connection connection;
		try {
			connection = OracleConnUtils.getOracleConnection();
			Statement statement = connection.createStatement();

			String query = " INSERT INTO aml_bot_log(kind, creating_time, file_name, status,file_path,note)"
					+ " values (?, ?, ?, ?, ?,?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			preparedStmt.setString(1, type);
			preparedStmt.setString(2, dateCreate);
			preparedStmt.setString(3, name);
			preparedStmt.setString(4, status);
			preparedStmt.setString(5, foler);
			preparedStmt.setString(6, note);

			// execute the preparedstatement
			preparedStmt.execute();

			connection.close();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		} catch (SQLException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}

	}

}
