package com.tpbank.dbJob;

import com.github.lgooddatepicker.components.DatePicker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class QueryJobInMySql {
    private DatePicker jdStartDateRs2;
    private DatePicker jdEndDateRs2;

    public QueryJobInMySql() {
    }

    public QueryJobInMySql(DatePicker jdStartDateRs2, DatePicker jdEndDateRs2) {
        this.jdStartDateRs2 = jdStartDateRs2;
        this.jdEndDateRs2 = jdEndDateRs2;
    }

    //    @Override
    public LinkedList<String> querryJobInMySQL() throws ClassNotFoundException, SQLException {
        MySQLConnUtils conn = new MySQLConnUtils();

        // Lấy ra đối tượng Connection kết nối vào DB.
        Connection connection = MySQLConnUtils.getMySQLConnection();

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
            logLinkedList.add("Task ID: "+taskId);
            logLinkedList.add("Task name: "+taskName);
            logLinkedList.add("File name: "+fileName);
            logLinkedList.add("Time: "+creatingTime);
            logLinkedList.add("Status: "+status);

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
}
