package com.tpbank.GUI;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;

import com.tpbank.control.StartTask;
import com.tpbank.control.SingleTaskTimer;
import com.tpbank.dbJob.WriteLogToPdf;
import com.tpbank.dbJob.QueryJobInOracle;
import com.tpbank.dbJob.QueryJobInMySql;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class AML_BOTview extends JFrame {
    private String downloadFolder = defaultPathString();
    private String StartDateStr = null;
    private String EndDateStr = null;
    // private String log = "";
    // private String nameFile = "";
    // private String log = "";

    boolean btViewData = false;

    QueryJobInOracle queryJobInOracle = new QueryJobInOracle();

    public AML_BOTview() throws ClassNotFoundException, SQLException {
        createGUI();
        setDisplay();
    }

    private void setDisplay() {
        setTitle("BOT - AML - BOT VIEW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
        setDefaultLookAndFeelDecorated(true);
    }

    private void createGUI() throws ClassNotFoundException, SQLException {
        getContentPane().add(createTabbedPane());
    }

    private JTabbedPane createTabbedPane() throws ClassNotFoundException,
            SQLException {
        JTabbedPane tablePane = new JTabbedPane();
        tablePane.setBackground(Color.lightGray);

        JPanel panelResult = createPanelResult();
        panelResult.setBackground(Color.lightGray);

        JPanel panelMonitor = createPanelMonitor();
        panelMonitor.setBackground(Color.lightGray);

        JPanel panelEstablish = createPanelEstablish();
        panelEstablish.setBackground(Color.lightGray);

        tablePane.addTab("Hiển thị", null, panelMonitor);
        tablePane.addTab("Báo cáo", null, panelResult);
        tablePane.addTab("Thiết lập", null, panelEstablish);
        tablePane.setSelectedIndex(tablePane.getTabCount() - 2);

        return tablePane;
    }

    private JPanel createPanelResult() throws ClassNotFoundException,
            SQLException {
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JTextArea showLog = createTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(showLog);

        JLabel fromDate = createLabel("Từ ngày: ");
        JLabel toDate = createLabel("Tới ngày: ");

        DatePicker jdStartDateRs = generateDatePicker();
        DatePicker jdEndDateRs = generateDatePicker();

        JButton btView = createButton("Hiển thị ghi chú");
        btView.addActionListener(e -> {
            // btViewData = true;
            QueryJobInMySql queryJobInMySql = new QueryJobInMySql(jdStartDateRs, jdEndDateRs);
            String log;
            try {
                log = converteLinkedListToString(queryJobInMySql.querryJobInMySQL());
                showLog.append(log);
            } catch (Exception e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

        });
        JButton btExport = createButton("Xuất ra file *.pdf");
        btExport.addActionListener(e -> {
//            QueryJobInOracle queryJobInOracle = new QueryJobInOracle(jdStartDateRs, jdEndDateRs);
            QueryJobInMySql queryJobInMySql = new QueryJobInMySql(jdStartDateRs, jdEndDateRs);
            LinkedList<String> log;
            String nameFile = ("Result from " + jdStartDateRs + " to " + jdEndDateRs);
            try {
                log = queryJobInMySql.querryJobInMySQL();

                Iterator<String> it = log.iterator();
                System.out.println("Print Log in Export file");
                System.out.println("===========================================");
                while(it.hasNext()) {
                    String value = it.next();
                    System.out.println(value);
                }

                WriteLogToPdf createPdf = new WriteLogToPdf(log, nameFile);
                createPdf.createTextToAPdf(log, nameFile);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        c.gridx = 0;
        c.gridy = 0;
        panel.add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(fromDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        panel.add(jdStartDateRs, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(toDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(jdEndDateRs, c);

        c.fill = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(btExport, c);

        c.fill = GridBagConstraints.WEST;
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(btView, c);
        return panel;
    }

    private JPanel createPanelMonitor() {
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();

        JScrollPane scrollPane = new JScrollPane(createTextArea(10, 40));

        c.fill = GridBagConstraints.CENTER;
        c.ipady = 40;
        c.ipadx = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        c.gridx = 0;
        c.gridy = 0;
        panel.add(scrollPane, c);

        return panel;
    }

    private JPanel createPanelEstablish() {
        SingleTaskTimer timer = new SingleTaskTimer();

        JPanel pl = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        pl.setLayout(layout);
        GridBagConstraints c = new GridBagConstraints();

        JLabel startDate = createLabel("Ngày bắt đầu: ");
        JLabel endDate = createLabel("Ngày kết thúc: ");
        JLabel startTime = createLabel("Giờ bắt đầu: ");
        JLabel endTime = createLabel("Giờ kết thúc: ");
        JLabel intervalPeriod = createLabel("Interval (Period): ");
        JLabel intervalUnitTime = createLabel("h");
        JLabel saveFolder = createLabel("Folder lưu trữ: ");

        JTextArea textIntervalPeriod = createTextArea(1, 1);

        JButton btStart = createButton("START");
        JButton btStop = createButton("STOP");
        JButton btSaveFolder = createButton("SAVE FOLDER");

        DatePicker jdStartDate = generateDatePicker();
        DatePicker jdEndDate = generateDatePicker();
        TimePicker jdTimeStart = generateTimePicker();
        TimePicker jdTimeEnd = generateTimePicker();

        btSaveFolder.addActionListener(e -> setDownloadFolder());

        btStart.addActionListener(e -> {
            Calendar calEnd = calendarFromPickers(jdEndDate, jdTimeEnd);
            Calendar calStart = calendarFromPickers(jdStartDate, jdTimeStart);
            int period = Integer.parseInt(textIntervalPeriod.getText());
            long timePeriod = TimeUnit.MILLISECONDS.convert(period,
                    TimeUnit.SECONDS);

            timer.setTask(new StartTask(downloadFolder));
            timer.schedule(calStart.getTime(), calEnd.getTime(), timePeriod);
        });

        btStop.addActionListener(e -> timer.cancel());

        // Text start Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 50;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(startDate, c);

        // Picker start Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdStartDate, c);

        // Text start Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 0;
        c.gridwidth = 0;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        pl.add(startTime, c);

        // Picker start Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 0;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdTimeStart, c);

        // Text End Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 50;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(endDate, c);

        // Picker End Date
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdEndDate, c);

        // Picker End Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(jdTimeEnd, c);

        // Text End Time
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        pl.add(endTime, c);

        // Text intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(intervalPeriod, c);

        // Label intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(textIntervalPeriod, c);

        // intervalUnitTime
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 10, 0, 0);
        pl.add(intervalUnitTime, c);

        // Textarea intervalPeriod
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(saveFolder, c);

        // Button savefolder
        c.fill = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btSaveFolder, c);

        // Button start
        c.fill = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btStart, c);

        // Button stop
        c.fill = GridBagConstraints.EAST;
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 3;
        c.ipady = 0;
        c.ipadx = 0;
        c.weightx = 0.0;
        c.insets = new Insets(10, 0, 0, 0);
        pl.add(btStop, c);

        return pl;
    }

    private JLabel createLabel(String s) {
        JLabel jl = new JLabel(s);
        return jl;
    }

    private JTextArea createTextArea(int row, int col) {
        JTextArea ta = new JTextArea(row, col);
        return ta;
    }

    private JButton createButton(String s) {
        JButton jb = new JButton(s);
        return jb;
    }

    private DatePicker generateDatePicker() {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy");
        final DatePickerSettings settings = new DatePickerSettings(Locale.US);
        settings.setFormatForDatesCommonEra(formatter);
        settings.setFormatForDatesBeforeCommonEra(DateTimeFormatter
                .ofPattern("d.M.yy.G"));
        ArrayList parsingList = settings.getFormatsForParsing();
        parsingList.add(DateTimeFormatter.ofPattern("d M yyyy"));
        parsingList.add(DateTimeFormatter.ofPattern("d-M-yyyy"));
        parsingList.add(DateTimeFormatter.ofPattern("ddMMyyyy"));

        DatePicker datePicker = new DatePicker(settings.copySettings());
        return datePicker;
    }

    private TimePicker generateTimePicker() {
        // Set up the form which holds the date picker components.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setColor(TimeArea.TimePickerTextValidTime, Color.black);
        timeSettings.initialTime = LocalTime.now();
        TimePicker timePicker = new TimePicker(timeSettings);

        return timePicker;
    }

    private void setDownloadFolder() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new File(defaultPathString()));

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            chooser.setCurrentDirectory(f);

            // define the download folder
            downloadFolder = f.getPath();
        }
    }

    private String defaultPathString() {
        Preferences pref = Preferences.userRoot();
        return pref.get("DEFAULT_PATH", "");
    }

    private Calendar calendarFromPickers(DatePicker datePicker,
                                         TimePicker timePicker) {
        Date date = sqlDateFrom(datePicker);
        Time time = sqlTimeFrom(timePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
        calendar.set(Calendar.MINUTE, time.getMinutes());
        calendar.set(Calendar.SECOND, time.getSeconds());
        return calendar;
    }

    private Time sqlTimeFrom(TimePicker timePicker) {
        LocalTime LcTimeStart = timePicker.getTime();
        return java.sql.Time.valueOf(LcTimeStart);
    }

    private Date sqlDateFrom(DatePicker datePicker) {
        LocalDate localDate = datePicker.getDate();
        return java.sql.Date.valueOf(localDate);
    }

    private String converteLinkedListToString(LinkedList<String> list) {

        String listString = "";

        StringBuilder string = new StringBuilder();
        Iterator<?> it = list.descendingIterator();

        while (it.hasNext()) {
            string.append(it.next());
            string.append("\n");
        }
        listString = string.toString();
        return listString;
    }

}