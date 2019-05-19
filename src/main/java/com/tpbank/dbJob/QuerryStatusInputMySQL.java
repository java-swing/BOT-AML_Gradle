package com.tpbank.dbJob;

import afu.org.checkerframework.checker.units.UnitsTools;
import com.github.lgooddatepicker.components.DatePicker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class QuerryStatusInputMySQL {
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

    public QuerryStatusInputMySQL() {
    }

    public QuerryStatusInputMySQL(Boolean oneTime, Boolean daily, Boolean weekly, Boolean monthly, String startDate, String startTime, String endDate, String endTime, Integer intevalPeriod, Integer recur, Boolean sundayStatus, Boolean mondayStatus, Boolean tuesdayStatus, Boolean wednesdayStatus, Boolean thurdayStatus, Boolean fridayStatus, Boolean saturStatus) {
        this.oneTime = oneTime;
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
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

    //    @Override
    public LinkedList<String> querryJobInMySQL() throws ClassNotFoundException, SQLException {
        MySQLConnUtils conn = new MySQLConnUtils();

        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnectionToInputStatus();

        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();

        // Query Data from DB
        String sql = queryStringForInputRecentStatus(oneTime, daily,weekly, monthly,startDate,startTime,
                endDate,endTime, intevalPeriod,recur,sundayStatus,mondayStatus,tuesdayStatus,wednesdayStatus,
                thurdayStatus,fridayStatus,saturStatus);


        boolean resultSet = statement.execute(sql);

        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        ResultSet rs = statement.executeQuery(sql);
        String log = "";
        LinkedList<String> logLinkedList = new LinkedList<String>();
        // Duyệt trên kết quả trả về.
        while (rs.next()) {// Di chuyển con trỏ xuống bản ghi kế tiếp.

            int taskId = rs.getInt(1);
            String taskIdStr = Integer.toString(taskId);
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
            logLinkedList.add(taskIdStr);
            logLinkedList.add(taskName);
            logLinkedList.add(fileName);
            logLinkedList.add(creatingTime);
            logLinkedList.add(status);

        }

        System.out.println("finish at QueryJobInMySql" + log);
        // Đóng kết nối
        connection.close();
        return logLinkedList;
    }

    private String queryStringFromDatePicker(DatePicker jdStartDateRs2,
                                             DatePicker jdEndDateRs2) {
        String queryStr = "";
        queryStr = " select * from AML_BOT_LOG_MYSQL a where a.Create_Time between "
                + "'" + jdStartDateRs2 + " ' " + "and " + "'" + jdEndDateRs2
                + "'" + " order by a.Create_Time DESC";
        return queryStr;
    }
    private String queryStringForInputRecentStatus(oneTime, daily,weekly, monthly,startDate,startTime,
                                                   endDate,endTime, intevalPeriod,recur,sundayStatus,mondayStatus,tuesdayStatus,wednesdayStatus,
                                                   thurdayStatus,fridayStatus,saturStatus) {
        String queryStr = "";
        queryStr = " INSERT INTO AsaveLastStatusOperation(oneTime, daily,weekly, monthly,startDate,startTime," +
                "                                     endDate,endTime, IntervalPeriod,recur,sundayStatus,mondayStatus,tuesdayStatus,wednesdayStatus,\n" +
                "                                     thurdayStatus,fridayStatus,saturStatus)" +
                "VALUES (true, FALSE, FALSE, FALSE,'2019-5-20','05:00:00','2019-5-20','05:01:00',NULL,4," +
                "        false,true,false,false,false,false,false)";
        return queryStr;
    }
}
