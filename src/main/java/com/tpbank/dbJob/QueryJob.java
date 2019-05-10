package com.tpbank.dbJob;

import com.github.lgooddatepicker.components.DatePicker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

public class QueryJob {
	private String jdStartDateRs;
	private String jdEndDateRs;
	private DatePicker jdStartDateRs2;
	private DatePicker jdEndDateRs2;
//	private StringBuffer log;

	public QueryJob(String jdStartDateRs, String jdEndDateRs) {
		this.jdStartDateRs = jdStartDateRs;
		this.jdEndDateRs = jdEndDateRs;
	}

	public QueryJob() {
	}

	public QueryJob(DatePicker jdStartDateRs2, DatePicker jdEndDateRs2) {
		super();
		this.jdStartDateRs2 = jdStartDateRs2;
		this.jdEndDateRs2 = jdEndDateRs2;
	}

	public LinkedList<String> querryJob() throws ClassNotFoundException, SQLException {
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
			
			int taskId = rs.getInt(1);
			String taskName = rs.getString(2);
			String fileName = rs.getString(3);
			String creatingTime = rs.getString(4);
			String status = rs.getString(5);

			log = log + ("-------------------- \n"
					+ "TaskId: "+taskId+"\n"
					+"Task name: "+taskName+"\n"
					+"File name: "+fileName+"\n"
					+"Time: "+creatingTime+"\n"
					+"Status: "+status+"\n");
			logLinkedList.add("---------------------------");
			logLinkedList.add("Task name: "+taskName);
			logLinkedList.add("File name: "+fileName);
			logLinkedList.add("File name: "+fileName);
			logLinkedList.add("Status: "+status);
			
		}

		System.out.println("finish " + log);
		// Đóng kết nối
		connection.close();
		return logLinkedList;
	}

	private String queryString(String jdStartDateRs2, String jdEndDateRs2) {
		String queryStr = "";
		queryStr = " select * from aml_bot_log a where a.doing_time between "
				+ "'" + jdStartDateRs2 + " ' " + "and " + "'" + jdEndDateRs2
				+ "'" + " order by a.doing_time";
		return queryStr;
	}

	private String queryStringFromDatePicker(DatePicker jdStartDateRs2,
                                             DatePicker jdEndDateRs2) {
		String queryStr = "";
		queryStr = " select * from aml_bot_log a where a.doing_time between "
				+ "'" + jdStartDateRs2 + " ' " + "and " + "'" + jdEndDateRs2
				+ "'" + " order by a.doing_time";
		return queryStr;
	}

}
