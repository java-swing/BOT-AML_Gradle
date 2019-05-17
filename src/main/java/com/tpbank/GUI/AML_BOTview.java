package com.tpbank.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.tpbank.dbJob.QueryJobInMySql;
import net.bytebuddy.implementation.attribute.TypeAttributeAppender.ForInstrumentedType.Differentiating;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import com.github.lgooddatepicker.zinternaltools.TimeSpinnerTimer;
import com.tpbank.control.SingleTaskTimer;
import com.tpbank.control.StartTask;
import com.tpbank.dbJob.WriteLogToTable;

public class AML_BOTview extends JFrame {

    private String downloadFolder = defaultPathString();
    private String jtxSaveFolderStr = "";
    private String StartDateStr = null;
    private String EndDateStr = null;
    static DatePicker jdStartDate;
    DatePicker jdEndDate;
    int period = 0;

    boolean btViewData = false;

    QueryJobInMySql queryJobInMySql = new QueryJobInMySql();

    public AML_BOTview() throws ClassNotFoundException, SQLException {
        createGUI();
        setDisplay();
    }

    private void setDisplay() {
        setTitle("BOT - AML");
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
        tablePane.setBackground(Color.white);

        JPanel panelResult = createPanelResult();
        panelResult.setBackground(Color.white);

        JPanel panelMonitor = createPanelMonitor();
        panelMonitor.setBackground(Color.white);

        JPanel panelEstablish = createPanelEstablish();
        panelEstablish.setBackground(Color.white);

        tablePane.addTab("Monitor", null, panelMonitor);
        tablePane.addTab("Báo cáo", null, panelResult);
        tablePane.addTab("Thiết lập", null, panelEstablish);
        tablePane.setSelectedIndex(tablePane.getTabCount() - 1);

        return tablePane;
    }

    private JPanel createPanelResult() throws ClassNotFoundException,
            SQLException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JTextArea showLog = createTextArea(10, 40);
        showLog.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(showLog);

        JLabel fromDate = createLabel("Từ ngày: ");
        JLabel toDate = createLabel("Tới ngày: ");

        DatePicker jdStartDateRs = generateDatePickerStart();
        DatePicker jdEndDateRs = generateDatePickerStart();

        JButton btView = createButton("Hiển thị ghi chú");
        btView.addActionListener(e -> {
            // btViewData = true;
            QueryJobInMySql queryJobInMySql = new QueryJobInMySql(jdStartDateRs, jdEndDateRs);
            String nameFile = ("Result from " + jdStartDateRs + " to " + jdEndDateRs);
            String title = "Task ID\tTask name\tFile name\tStatus";
            String log;
            try {
                log = converteLinkedListToString(converseLogToWrite(queryJobInMySql
                        .querryJobInMySQL()));
                showLog.append("\t" + nameFile + " \n");
                showLog.append(title + "\n");
                showLog.append(log);
            } catch (Exception e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

        });
        JButton btExport = createButton("Xuất ra file *.pdf");
        btExport.addActionListener(e -> {
            QueryJobInMySql queryJobInMySql = new QueryJobInMySql(jdStartDateRs, jdEndDateRs);
            LinkedList<String> log;
            String nameFile = ("Result from " + jdStartDateRs + " to " + jdEndDateRs);
            try {
                log = queryJobInMySql.querryJobInMySQL();

                Iterator<String> it = log.iterator();
                System.out.println("Print Log in Export file");
                System.out
                        .println("===========================================");
                while (it.hasNext()) {
                    String value = it.next();
                    System.out.println(value);
                }
                //
                // WriteLogToPdf createPdf = new WriteLogToPdf(log, nameFile);
                // createPdf.createTextToAPdf(log, nameFile);

                WriteLogToTable createTableLog = new WriteLogToTable(log,
                        nameFile);
                createTableLog.drawTablePDF(log, nameFile);

                // WritePdfTest newPdf = new WritePdfTest(log, nameFile);
                // newPdf.createTextToAPdf(log, nameFile);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        intervalSimplePanelPosition(c, 0, 0);
        panel.add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(c, 0, 1);
        c.gridwidth = 1;
        recurPanelPostion(c, 0, 0);
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(fromDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(c, 1, 1);
        c.gridwidth = 2;
        panel.add(jdStartDateRs, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(c, 0, 2);
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(toDate, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(c, 1, 2);
        c.gridwidth = 2;
        panel.add(jdEndDateRs, c);

        c.fill = GridBagConstraints.EAST;
        intervalSimplePanelPosition(c, 1, 3);
        c.gridwidth = 1;
        c.insets = new Insets(10, 0, 0, 0);
        panel.add(btExport, c);

        c.fill = GridBagConstraints.WEST;
        intervalSimplePanelPosition(c, 2, 3);
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

        JTextArea showLog = createTextArea(10, 40);
        showLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(showLog);

        c.fill = GridBagConstraints.CENTER;
        recurPanelPostion(c, 40, 40);
        c.weightx = 0.0;
        c.gridwidth = 3; // 2 columns wide
        intervalSimplePanelPosition(c, 0, 0);
        panel.add(scrollPane, c);

        return panel;
    }

    private JPanel createPanelEstablish() {
        SingleTaskTimer timer = new SingleTaskTimer();
        final LocalDate today = LocalDate.now();

        JPanel panelEstablish = new JPanel();
        GridBagConstraints cEstablish = new GridBagConstraints();
        GridBagLayout layoutEstablish = new GridBagLayout();
        panelEstablish.setLayout(layoutEstablish);
        panelEstablish.setBackground(Color.white);

        JPanel panelRecur = new JPanel();
        JTextField textFieldRecur = new JTextField();

        JCheckBox cbMonday = new JCheckBox("Monday");

        JCheckBox cbSunday = new JCheckBox("Sunday");

        JCheckBox cbTuesday = new JCheckBox("Tuesday");

        JCheckBox cbWednesday = new JCheckBox("Wednesday");

        JCheckBox cbThursday = new JCheckBox("Thursday");

        JCheckBox cbFriday = new JCheckBox("Friday");

        JCheckBox cbSaturday = new JCheckBox("Saturday");

        JTextArea textIntervalPeriod = new JTextArea(1, 5);

        textFieldRecur.setEnabled(false);
        textIntervalPeriod.setEnabled(false);
        setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday, cbWednesday,
                cbThursday, cbFriday, cbSaturday, false);

        JPanel panelTaskControl = new JPanel();
        GridBagConstraints cTaskControl = new GridBagConstraints();
        GridBagLayout layoutTaskControl = new GridBagLayout();
        panelTaskControl.setLayout(layoutTaskControl);

        // Start Button
        JButton btStart = new JButton("START");
        JButton btStop = new JButton("STOP");
        btStart.setEnabled(false);
        btStop.setEnabled(false);
        // ======== panel Setting=========================
        JPanel panelSetting = new JPanel();
        GridBagConstraints cSetting = new GridBagConstraints();
        GridBagLayout layoutSetting = new GridBagLayout();
        panelSetting.setLayout(layoutSetting);
        Border blacklineSetting = BorderFactory.createTitledBorder("Setting");
        panelSetting.setBorder(blacklineSetting);
        panelSetting.setBackground(Color.white);

        // ======== panel panelIntervalSimple=========================
        String txtIntervalSimple = "";
        JPanel panelIntervalSimple = new JPanel();
        GridBagConstraints cIntervalSimple = new GridBagConstraints();
        GridBagLayout layoutIntervalSimple = new GridBagLayout();
        panelIntervalSimple.setLayout(layoutIntervalSimple);
        panelSetting.setLayout(layoutIntervalSimple);
        Border blacklineIntervalSimple = BorderFactory.createTitledBorder("");
        panelIntervalSimple.setBorder(blacklineIntervalSimple);
        panelIntervalSimple.setBackground(Color.white);

        JRadioButton r1 = new JRadioButton("One time");
        JRadioButton r2 = new JRadioButton("Daily");
        JRadioButton r3 = new JRadioButton("Weekly");
        JRadioButton r4 = new JRadioButton("Monthly");

        // r1.setMnemonic(KeyEvent.doOneTime);
        // r1.setSelected(true);

        ButtonGroup buttonGroupRadio = new ButtonGroup();
        buttonGroupRadio.add(r1);
        r1.setEnabled(false);
        r1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textFieldRecur.setEnabled(false);
                setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                        cbWednesday, cbThursday, cbFriday, cbSaturday, false);
                textIntervalPeriod.setEnabled(false);
                period = 1;

            }
        });
        buttonGroupRadio.add(r2);
        r2.setEnabled(false);
        r2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textFieldRecur.setEnabled(false);
                setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                        cbWednesday, cbThursday, cbFriday, cbSaturday, false);
                textIntervalPeriod.setEnabled(false);
                period = 24*60;
            }
        });

        buttonGroupRadio.add(r3);
        r3.setEnabled(false);
        r3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textFieldRecur.setEnabled(false);
                setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                        cbWednesday, cbThursday, cbFriday, cbSaturday, false);
                textIntervalPeriod.setEnabled(false);
                period = 7*24*60;
            }
        });

        buttonGroupRadio.add(r4);
        r4.setEnabled(false);
        r4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textFieldRecur.setEnabled(false);
                setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                        cbWednesday, cbThursday, cbFriday, cbSaturday, false);
                textIntervalPeriod.setEnabled(false);
                period = 30*24*60;
            }
        });

        // ======== panelIntervalSimple=========================
        // RadioButton "One time"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cIntervalSimple, 0, 0);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(10, 10, 0, 0);
        panelIntervalSimple.add(r1, cIntervalSimple);

        // RadioButton "Daily"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cIntervalSimple, 0, 1);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(10, 10, 0, 0);
        panelIntervalSimple.add(r2, cIntervalSimple);

        // RadioButton "Weekly"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cIntervalSimple, 0, 2);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(10, 10, 0, 0);
        panelIntervalSimple.add(r3, cIntervalSimple);

        // RadioButton "Monthly"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cIntervalSimple, 0, 3);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(10, 10, 0, 0);
        panelIntervalSimple.add(r4, cIntervalSimple);

        // =======Start Time=====================
        // Panel StartTime
        JPanel panelStartTime = new JPanel();
        GridBagConstraints cStartTime = new GridBagConstraints();
        GridBagLayout layoutStartTime = new GridBagLayout();
        panelStartTime.setLayout(layoutStartTime);
        panelStartTime.setBackground(Color.white);

        // add label Start to Panel Setting:
        JLabel startLb = new JLabel("Bắt đầu: 	");
        cStartTime.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cStartTime, 0, 0);
        intervalSimplePanelPosition(cStartTime, 0, 0);
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(0, 10, 0, 0);
        panelStartTime.add(startLb, cStartTime);

        // add label Start to Panel StartTime:
        TimePickerSettings timePickerSettingsStart = new TimePickerSettings();
        DatePickerSettings datePickerSettingsStart = new DatePickerSettings();
        DateTimePicker dateTimePickerStart = new DateTimePicker(
                datePickerSettingsStart, timePickerSettingsStart);
        datePickerSettingsStart.setDateRangeLimits(today.minusDays(0),
                today.plusDays(3000));
        DatePickerSettings dateSettingsEndDatePicker = new DatePickerSettings();
        dateTimePickerStart
                .addDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void dateOrTimeChanged(DateTimeChangeEvent arg0) {
                        if (dateTimePickerStart.getDatePicker().getDate() != null) {
                            dateSettingsEndDatePicker.setDateRangeLimits(
                                    today.minusDays(getDiffWithTodayByDay(dateTimePickerStart)),
                                    today.plusDays(3000));
                            textFieldRecur.setEnabled(false);
                            setRadioButtonStatus(r1, r2, r3, r4, true);
                            textIntervalPeriod.setEnabled(true);

                        } else {
                            textFieldRecur.setEnabled(false);
                            setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                                    cbWednesday, cbThursday, cbFriday,
                                    cbSaturday, false);
                            setRadioButtonStatus(r1, r2, r3, r4, false);
                            textIntervalPeriod.setEnabled(false);
                        }

                    }
                });
        // dateTimePickerStart.datePicker;

        cStartTime.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cStartTime, 0, 0);
        startTimePanelPosition(cStartTime, 0, 1);
        // cStartTime.weightx = 1;
        // cStartTime.gridwidth = 3;
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(5, 0, 0, 0);
        panelStartTime.add(dateTimePickerStart, cStartTime);

        // add label Start to Panel Setting:
        JLabel stopLb = new JLabel("Kết thúc: 	");
        cStartTime.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cStartTime, 0, 0);
        intervalSimplePanelPosition(cStartTime, 0, 1);
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(0, 10, 0, 0);
        panelStartTime.add(stopLb, cStartTime);

        // DateTime Strop
        TimePickerSettings timeSettings = new TimePickerSettings();

        DateTimePicker dateTimePickerStop = new DateTimePicker(
                dateSettingsEndDatePicker, timeSettings);
        dateTimePickerStop.setEnabled(false);
        dateTimePickerStart
                .addDateTimeChangeListener(new DateTimeChangeListener() {

                    @Override
                    public void dateOrTimeChanged(DateTimeChangeEvent arg0) {
                        if (dateTimePickerStart.getDatePicker().getDate() != null) {
                            dateTimePickerStop.setEnabled(true);
                        }

                    }
                });
        dateTimePickerStop
                .addDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void dateOrTimeChanged(DateTimeChangeEvent arg0) {
                        checkDateStop(textFieldRecur, cbMonday, cbSunday,
                                cbTuesday, cbWednesday, cbThursday, cbFriday,
                                cbSaturday, r2, r3, r4, dateTimePickerStart,
                                dateTimePickerStop);
                    }

                });
        cStartTime.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cStartTime, 0, 0);
        startTimePanelPosition(cStartTime, 1, 1);
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(5, 0, 0, 0);
        panelStartTime.add(dateTimePickerStop, cStartTime);

        // Panel Interval
        JPanel panelInterval = new JPanel();
        GridBagConstraints cInterval = new GridBagConstraints();
        GridBagLayout layoutInterval = new GridBagLayout();
        panelInterval.setLayout(layoutInterval);

        JLabel intervalPeriod = createLabel("Chu kỳ lặp lại: ");
        cInterval.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 0);
        cInterval.anchor = GridBagConstraints.LINE_START;
        cInterval.insets = new Insets(5, 10, 0, 0);
        panelInterval.add(intervalPeriod, cInterval);

        Border blacklineIntervalPeriod = BorderFactory
                .createLineBorder(Color.LIGHT_GRAY);
        textIntervalPeriod.setBorder(blacklineIntervalPeriod);
        textIntervalPeriod.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void removeUpdate(DocumentEvent arg0) {
                        setRadioButtonStatus(r1, r2, r3, r4, true);
                    }

                    @Override
                    public void insertUpdate(DocumentEvent arg0) {
                        setRadioButtonStatus(r1, r2, r3, r4, false);

                    }

                    @Override
                    public void changedUpdate(DocumentEvent arg0) {
                        setRadioButtonStatus(r1, r2, r3, r4, false);
                    }
                });

        cInterval.fill = GridBagConstraints.CENTER;
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 1);
        // cInterval.weightx = 0;
        cInterval.anchor = GridBagConstraints.LINE_START;
        cInterval.insets = new Insets(5, 15, 0, 0);
        panelInterval.add(textIntervalPeriod, cInterval);

        JLabel intervalUnitTime = createLabel("phút");
        cInterval.fill = GridBagConstraints.CENTER;
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 2);
        cInterval.weightx = 1.0;
        cInterval.anchor = GridBagConstraints.LINE_END;
        cInterval.insets = new Insets(5, 20, 0, 0);
        panelInterval.add(intervalUnitTime, cInterval);

        // =======Start Time=====================
        // Panel Recur

        GridBagConstraints cRecur = new GridBagConstraints();
        GridBagLayout layoutRecur = new GridBagLayout();
        panelRecur.setLayout(layoutRecur);
        Border blacklineRecur = BorderFactory.createTitledBorder("");
        panelRecur.setBorder(blacklineRecur);
        panelRecur.setBackground(Color.white);
        // Label Recur everyday
        JLabel labelRecur = new JLabel("Recur every: ");
        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cRecur, 0, 0);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(labelRecur, cRecur);

        // Label Recur everyday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cRecur, 1, 0);
        recurPanelPostion(cRecur, 0, 20);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(textFieldRecur, cRecur);

        // Label Recur everyday
        JLabel labelWeekon = new JLabel("week on");
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cRecur, 0, 0);
        intervalSimplePanelPosition(cRecur, 2, 0);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(labelWeekon, cRecur);

        // Checkbox Sunday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 0);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbSunday, cRecur);

        // Checkbox Monday
        // JCheckBox cbMonday = new JCheckBox("Monday");
        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.WEST;
        startTimePanelPosition(cRecur, 1, 1);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbMonday, cRecur);

        // Checkbox Tuesday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 2);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbTuesday, cRecur);

        // Checkbox Wednesday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 3);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbWednesday, cRecur);

        // Checkbox Thursday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 2, 0);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbThursday, cRecur);

        // Checkbox Friday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 2, 1);
        cRecur.gridwidth = 1;
        recurPanelPostion(cRecur, 1, 4);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbFriday, cRecur);

        // Checkbox Saturday

        taskControlPanelPosition(cRecur, 0, 0);
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 2, 2);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(0, 10, 0, 0);
        panelRecur.add(cbSaturday, cRecur);

        // ============ panel StartTime to add Panel Setting===================
        // add Panel Setting to panel StartTime
        cSetting.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cSetting, 0, 0);
        taskControlPanelPosition(cSetting, 1, 4);
        settingPanelWidthAndHeight(cSetting, 1, 4);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(10, 5, 10, 0);
        panelSetting.add(panelIntervalSimple, cSetting);
        panelSetting.setBackground(Color.white);

        // add panel StartTime to panel Setting
        cSetting.fill = GridBagConstraints.LINE_START;
        intervalSimplePanelPosition(cSetting, 1, 0);
        settingPanelWidthAndHeight(cSetting, 4, 1);
        taskControlPanelPosition(cSetting, 1, 1);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(5, 0, 0, 0);
        panelSetting.add(panelStartTime, cSetting);

        // add panel StartTime to panel Setting
        cSetting.fill = GridBagConstraints.LINE_START;
        intervalSimplePanelPosition(cSetting, 1, 1);
        settingPanelWidthAndHeight(cSetting, 4, 1);
        taskControlPanelPosition(cSetting, 1, 1);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(5, 0, 0, 0);
        panelSetting.add(panelInterval, cSetting);

        // add panel Recur to panel Setting
        cSetting.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cSetting, 1, 2);
        taskControlPanelPosition(cSetting, 0, 0);
        settingPanelWidthAndHeight(cSetting, 0, 0);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(5, 0, 0, 0);
        panelSetting.add(panelRecur, cSetting);

        // ======== panel Advance panelSaveSetting=========================
        JPanel panelSaveSetting = new JPanel();
        GridBagConstraints cSaveSetting = new GridBagConstraints();
        GridBagLayout layoutSaveSetting = new GridBagLayout();
        panelSaveSetting.setLayout(layoutSaveSetting);
        Border blacklineSaveSetting = BorderFactory
                .createTitledBorder("Save Setting");
        panelSaveSetting.setBorder(blacklineSaveSetting);
        panelSaveSetting.setBackground(Color.white);

        // Label Save textField
        JLabel lbSaveFolder = new JLabel("Save folder:");
        cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
        startTimePanelPosition(cSaveSetting, 0, 0);
        cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSaveSetting.insets = new Insets(0, 0, 0, 0);
        panelSaveSetting.add(lbSaveFolder, cSaveSetting);

        // Label Save textField
        JTextArea txtSaveFolder = new JTextArea(1, 20);
        // txtSaveFolder.addPropertyChangeListener(new PropertyChangeListener()
        // {
        //
        // @Override
        // public void propertyChange(PropertyChangeEvent arg0) {
        // btStart.setEnabled(false);
        // btStop.setEnabled(false);
        //
        // }
        // });
        Border blacklineTxtSaveFolder = BorderFactory
                .createLineBorder(Color.LIGHT_GRAY);
        txtSaveFolder.setBorder(blacklineTxtSaveFolder);
        cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
        startTimePanelPosition(cSaveSetting, 0, 1);
        cSaveSetting.weightx = 2.0;
        cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSaveSetting.insets = new Insets(5, 10, 10, 0);
        panelSaveSetting.add(txtSaveFolder, cSaveSetting);

        // Option Save Folder button=========================>>>>>>>>>>>
        JButton btsaveFolder = createButton("SAVE FOLDER");
        btsaveFolder.addActionListener(e -> setDownloadFolder());
        cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
        startTimePanelPosition(cSaveSetting, 0, 2);
        cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSaveSetting.insets = new Insets(5, 10, 10, 0);
        // panelSaveSetting.add(btsaveFolder, cSaveSetting);

        // ======== panel panelTaskControl=========================
        btStart.addActionListener(e -> {

            actionBtnStart(timer, textIntervalPeriod, dateTimePickerStart,
                    dateTimePickerStop, txtSaveFolder);
        });
        cTaskControl.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cTaskControl, 1, 0);
        startTimePanelPosition(cTaskControl, 0, 0);
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(5, 10, 0, 0);
        panelTaskControl.add(btStart, cTaskControl);

        // Start Button

        btStop.addActionListener(e -> timer.cancel());
        cTaskControl.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cTaskControl, 1, 0);
        startTimePanelPosition(cTaskControl, 0, 3);
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(5, 100, 0, 0);
        panelTaskControl.add(btStop, cTaskControl);

        // ========== add Panel Save folder to panel
        // Save Setting=======================
        // add Panel Setting to panel
        cSaveSetting.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cSaveSetting, 0, 4);
        taskControlPanelPosition(cSaveSetting, 0, 0);
        cSaveSetting.insets = new Insets(5, 0, 0, 0);
        panelSaveSetting.add(panelTaskControl, cSaveSetting);

        // ========== add Panel Setting to panel
        // Establish=======================
        // add Panel Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cEstablish, 0, 0);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.insets = new Insets(0, 0, 0, 0);
        panelEstablish.add(panelSetting, cEstablish);

        // Establish=======================
        // add Panel Advance Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cEstablish, 0, 1);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.insets = new Insets(0, 0, 0, 0);
        panelEstablish.add(panelSaveSetting, cEstablish);

        // Establish=======================
        // add Panel Advance Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        intervalSimplePanelPosition(cEstablish, 0, 2);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.insets = new Insets(0, 0, 0, 0);
        panelEstablish.add(panelTaskControl, cEstablish);

        return panelEstablish;
    }

    private void recurPanelPostion(GridBagConstraints cRecur, int i, int i2) {
        cRecur.ipady = i;
        cRecur.ipadx = i2;
    }

    private void settingPanelWidthAndHeight(GridBagConstraints cSetting, int i, int i2) {
        cSetting.gridwidth = i;
        cSetting.gridheight = i2;
    }

    private void taskControlPanelPosition(GridBagConstraints cTaskControl, int i, int i2) {
        cTaskControl.ipadx = i; // reset to default
        cTaskControl.ipady = i2; // reset to default
    }

    private void startTimePanelPosition(GridBagConstraints cStartTime, int i, int i2) {
        cStartTime.gridy = i;
        cStartTime.gridx = i2;
    }

    private void intervalSimplePanelPosition(GridBagConstraints cIntervalSimple, int i, int i2) {
        cIntervalSimple.gridx = i;
        cIntervalSimple.gridy = i2;
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

    private DatePicker generateDatePickerStart() {
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

    private DatePicker generateDatePickerEnd() {
        final DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy");
        final DatePickerSettings settings = new DatePickerSettings(Locale.US);
        settings.setFormatForDatesCommonEra(formatter);
        settings.setFormatForDatesBeforeCommonEra(DateTimeFormatter
                .ofPattern("d.M.yy.G"));
        // settings.getV
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

    private LinkedList<String> converseLogToWrite(
            LinkedList<String> logLinkedList) {
        LinkedList<String> linkedExp = new LinkedList<String>();
        ArrayList<String> listRaw = new ArrayList<String>(logLinkedList);
        ArrayList<String> listExp = new ArrayList<String>();

        int i = 0;
        while (i <= listRaw.size() - 4) {
            String valueId = listRaw.get(i);
            String valueTaskName = listRaw.get(i + 1);
            String valueFileName = listRaw.get(i + 2);
            String valueCreatingTime = listRaw.get(i + 3);
            String valueStatus = listRaw.get(i + 4);
            listExp.add(valueId + "\t" + valueTaskName + "\t" + valueFileName
                    + "\t" + valueCreatingTime + "\t" + valueStatus);
            i = i + 5;
        }
        linkedExp = new LinkedList<String>(listExp);
        return linkedExp;
    }

    private long getDiffWithTodayByDay(DateTimePicker dateTimeStart) {
        final LocalDate today = LocalDate.now();
        long getDiffWithTodayByDay = today.toEpochDay()
                - dateTimeStart.getDatePicker().getDate().toEpochDay();
        return getDiffWithTodayByDay;
    }

    private void checkDateStop(JTextField textFieldRecur, JCheckBox cbMonday,
                               JCheckBox cbSunday, JCheckBox cbTuesday, JCheckBox cbWednesday,
                               JCheckBox cbThursday, JCheckBox cbFriday, JCheckBox cbSaturday,
                               JRadioButton r2, JRadioButton r3, JRadioButton r4,
                               DateTimePicker dateTimePickerStart,
                               DateTimePicker dateTimePickerStop) {
        if (dateTimePickerStart.getDatePicker().getDate() != null) {
            if (dateTimePickerStop.getDatePicker().getDate() != null) {
                LocalDate dateStart = dateTimePickerStart.getDatePicker()
                        .getDate();
                LocalDate dateStop = dateTimePickerStop.getDatePicker()
                        .getDate();

                if (dateStop.compareTo(dateStart.plusMonths(1)) < 0) {
                    r4.setEnabled(false);
                }
                if (dateStop.compareTo(dateStart.plusMonths(1)) > 0) {
                    r4.setEnabled(true);
                }
                if (dateStop.compareTo(dateStart.plusWeeks(1)) < 0) {
                    setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                            cbWednesday, cbThursday, cbFriday, cbSaturday,
                            false);
                    if (dateStop.compareTo(dateStart.plusDays(1)) < 0) {
                        r2.setEnabled(false);
                    }
                    if (dateStop.compareTo(dateStart.plusDays(1)) > 0) {
                        r2.setEnabled(true);
                    }
                    r3.setEnabled(false);
                    Duration diff = Duration.between(dateStart.atStartOfDay(),
                            dateStop.atStartOfDay());
                    long diffDays = diff.toDays();

                    for (int i = 0; i <= diffDays; i++) {
                        String dayOfWeek = dateStart.plusDays(i).getDayOfWeek()
                                .toString();
                        switch (dayOfWeek) {
                            case "MONDAY":
                                cbMonday.setEnabled(true);
                                break;
                            case "TUESDAY":
                                cbTuesday.setEnabled(true);
                                break;
                            case "WEDNESDAY":
                                cbWednesday.setEnabled(true);
                                break;
                            case "THURSDAY":
                                cbThursday.setEnabled(true);
                                break;
                            case "FRIDAY":
                                cbFriday.setEnabled(true);
                                break;
                            case "SATURDAY":
                                cbSaturday.setEnabled(true);
                                break;
                            case "SUNDAY":
                                cbSunday.setEnabled(true);
                                break;
                        }
                    }
                }
                if (dateStop.compareTo(dateStart.plusWeeks(1)) > 0) {
                    r3.setEnabled(true);
                    setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                            cbWednesday, cbThursday, cbFriday, cbSaturday, true);
                    textFieldRecur.setEnabled(true);
                }

            }
        } else {
        }
    }

    private void setRadioButtonStatus(JRadioButton r1, JRadioButton r2,
                                      JRadioButton r3, JRadioButton r4, boolean status) {
        r1.setEnabled(status);
        r2.setEnabled(status);
        r3.setEnabled(status);
        r4.setEnabled(status);
    }

    private void setCBDayOfWeekStatus(JCheckBox cbMonday, JCheckBox cbSunday,
                                      JCheckBox cbTuesday, JCheckBox cbWednesday, JCheckBox cbThursday,
                                      JCheckBox cbFriday, JCheckBox cbSaturday, boolean status) {
        cbSunday.setEnabled(status);
        cbMonday.setEnabled(status);
        cbTuesday.setEnabled(status);
        cbWednesday.setEnabled(status);
        cbThursday.setEnabled(status);
        cbFriday.setEnabled(status);
        cbSaturday.setEnabled(status);
    }

    private void actionBtnStart(SingleTaskTimer timer,
                                JTextArea textIntervalPeriod, DateTimePicker dateTimePickerStart,
                                DateTimePicker dateTimePickerStop, JTextArea txtSaveFolder) {
        Calendar calEnd = calendarFromPickers(
                dateTimePickerStop.getDatePicker(),
                dateTimePickerStop.getTimePicker());
        Calendar calStart = calendarFromPickers(
                dateTimePickerStart.getDatePicker(),
                dateTimePickerStart.getTimePicker());
        period = Integer.parseInt(textIntervalPeriod.getText());
        long timePeriod = TimeUnit.MILLISECONDS.convert(period,
                TimeUnit.MINUTES);
        // add and validate Download folder
        downloadFolder = txtSaveFolder.getText();
        File dir = new File(downloadFolder);
        if (!dir.exists()) {
            downloadFolder = defaultPathString();
        }
        timer.setTask(new StartTask(downloadFolder));
        timer.schedule(calStart.getTime(), calEnd.getTime(), timePeriod);
    }

}
