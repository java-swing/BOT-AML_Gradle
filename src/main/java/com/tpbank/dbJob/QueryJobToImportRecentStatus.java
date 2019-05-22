package com.tpbank.dbJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

public class QueryJobToImportRecentStatus {
	private Boolean oneTime;
	private Boolean daily;
	private Boolean weekly;
	private Boolean monthly;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	private Integer intevalPeriod;
	private Integer recur;
	private Boolean sundayStatus;
	private Boolean mondayStatus;
	private Boolean tuesdayStatus;
	private Boolean wednesdayStatus;
	private Boolean thurdayStatus;
	private Boolean fridayStatus;
	private Boolean saturStatus;

	public Boolean getOneTime() {
		return oneTime;
	}

	public void setOneTime(Boolean oneTime) {
		this.oneTime = oneTime;
	}

	public Boolean getDaily() {
		return daily;
	}

	public void setDaily(Boolean daily) {
		this.daily = daily;
	}

	public Boolean getWeekly() {
		return weekly;
	}

	public void setWeekly(Boolean weekly) {
		this.weekly = weekly;
	}

	public Boolean getMonthly() {
		return monthly;
	}

	public void setMonthly(Boolean monthly) {
		this.monthly = monthly;
	}

	

	

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getIntevalPeriod() {
		return intevalPeriod;
	}

	public void setIntevalPeriod(Integer intevalPeriod) {
		this.intevalPeriod = intevalPeriod;
	}

	public Integer getRecur() {
		return recur;
	}

	public void setRecur(Integer recur) {
		this.recur = recur;
	}

	public Boolean getSundayStatus() {
		return sundayStatus;
	}

	public void setSundayStatus(Boolean sundayStatus) {
		this.sundayStatus = sundayStatus;
	}

	public Boolean getMondayStatus() {
		return mondayStatus;
	}

	public void setMondayStatus(Boolean mondayStatus) {
		this.mondayStatus = mondayStatus;
	}

	public Boolean getTuesdayStatus() {
		return tuesdayStatus;
	}

	public void setTuesdayStatus(Boolean tuesdayStatus) {
		this.tuesdayStatus = tuesdayStatus;
	}

	public Boolean getWednesdayStatus() {
		return wednesdayStatus;
	}

	public void setWednesdayStatus(Boolean wednesdayStatus) {
		this.wednesdayStatus = wednesdayStatus;
	}

	public Boolean getThurdayStatus() {
		return thurdayStatus;
	}

	public void setThurdayStatus(Boolean thurdayStatus) {
		this.thurdayStatus = thurdayStatus;
	}

	public Boolean getFridayStatus() {
		return fridayStatus;
	}

	public void setFridayStatus(Boolean fridayStatus) {
		this.fridayStatus = fridayStatus;
	}

	public Boolean getSaturStatus() {
		return saturStatus;
	}

	public void setSaturStatus(Boolean saturStatus) {
		this.saturStatus = saturStatus;
	}

	public QueryJobToImportRecentStatus(Boolean oneTime, Boolean daily,
			Boolean weekly, Boolean monthly, String startDateStr2,
			String startTimeStr2, String endDateStr2, String endTimeStr2,
			Integer intevalPeriod, Integer recur, Boolean sundayStatus,
			Boolean mondayStatus, Boolean tuesdayStatus,
			Boolean wednesdayStatus, Boolean thurdayStatus,
			Boolean fridayStatus, Boolean saturStatus) {
		super();
		this.oneTime = oneTime;
		this.daily = daily;
		this.weekly = weekly;
		this.monthly = monthly;
		this.startDate = startDateStr2;
		this.startTime = startTimeStr2;
		this.endDate = endDateStr2;
		this.endTime = endTimeStr2;
		this.intevalPeriod = intevalPeriod;
		this.recur = recur;
		this.sundayStatus = sundayStatus;
		this.mondayStatus = mondayStatus;
		this.tuesdayStatus = tuesdayStatus;
		this.wednesdayStatus = wednesdayStatus;
		this.thurdayStatus = thurdayStatus;
		this.fridayStatus = fridayStatus;
		this.saturStatus = saturStatus;
	}

	public QueryJobToImportRecentStatus() {
		super();
	}

	public String queryStringToImportRecentStatus(Boolean oneTime,
			Boolean daily, Boolean weekly, Boolean monthly, String startDate2,
			String startTime2, String endDate2, String endTime2,
			Integer intevalPeriod, Integer recur, Boolean sundayStatus,
			Boolean mondayStatus, Boolean tuesdayStatus,
			Boolean wednesdayStatus, Boolean thurdayStatus,
			Boolean fridayStatus, Boolean saturStatus) {
		String queryStr = "";
		queryStr = " INSERT INTO save_recent_status(oneTime, daily,weekly, monthly,startDate,startTime,"
				+ "endDate,endTime, intevalPeriod,recur,sundayStatus,mondayStatus,"
				+ "tuesdayStatus,wednesdayStatus,thursdayStatus,fridayStatus,saturdayStatus)"
				+ "VALUES ('"
				+ getOneTime()
				+ "', '"
				+ getDaily()
				+ "', '"
				+ getWeekly()
				+ "', '"
				+ getMonthly()
				+ "','"
				+ getStartDate()
				+ "','"
				+ getStartTime()
				+ "','"
				+ getEndDate()
				+ "','"
				+ getEndTime()
				+ "',"
				+ getIntevalPeriod()
				+ ","
				+ getRecur()
				+ ",'"
				+ getSundayStatus()
				+ "','"
				+ getMondayStatus()
				+ "',"
				+ "'"
				+ getTuesdayStatus()
				+ "','"
				+ getWednesdayStatus()
				+ "','"
				+ getThurdayStatus()
				+ ""
				+ "','"
				+ getFridayStatus()
				+ "','"
				+ getSaturStatus() + "');";
		return queryStr;
	}

	public void createQueryJobToImportStatus() {
		OracleConnUtils conn = new OracleConnUtils();

		// Lấy ra đối tượng Connection kết nối vào DB.
		Connection connection;
		try {
			connection = OracleConnUtils.getOracleConnection();

			// Tạo đối tượng Statement.
			Statement statement = connection.createStatement();

			// Query Data from DB
			String sql = queryStringToImportRecentStatus(oneTime, daily, weekly,
					monthly, startDate, startTime, endDate, endTime, intevalPeriod,
					recur, sundayStatus, mondayStatus, tuesdayStatus,
					wednesdayStatus, thurdayStatus, fridayStatus, saturStatus);

			// update log
			ResultSet rs  = statement.executeQuery(sql);
			// Đóng kết nối
			connection.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
