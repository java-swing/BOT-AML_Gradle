package com.tpbank.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
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
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import com.tpbank.control.SingleTaskTimer;
import com.tpbank.control.StartTask;
import com.tpbank.dbJob.QueryJobToExportLog;
import com.tpbank.dbJob.QueryJobToImportRecentStatus;
import com.tpbank.writeToFile.ReadFromFile2;
import com.tpbank.writeToFile.SaveEstablishInTxtFile;
import com.tpbank.writeToFile.WriteLogToPdf;

public class AML_BOTview extends JFrame {

    private static final int ArrayList = 0;

    private static final DateTimePicker DateTimePicker = null;

    public static String printLogOut = "";

    public static final String saveEstablishStatus = "SaveEstablishLastStatus/saveEstablish.txt";

    public static String downloadFolder = defaultPathString();

    private DateTimePicker dateTimePickerStart = new DateTimePicker();
    private DateTimePicker dateTimePickerStop = new DateTimePicker();
    private ArrayList<LocalDate> dateStartArr = new ArrayList<LocalDate>();
    static DatePicker jdStartDate;
    DatePicker jdEndDate;
    int period = 0;
    final LocalDate today = LocalDate.now();

    boolean btViewData = false;
    boolean jrdOneTime;
    boolean jrdDaily;
    boolean jrdWeekly;
    boolean jrdMonthly;
    int intevalPeriod;
    int recur;
    String saveFile;
    boolean jcbSundayStatus;
    boolean jcbMondayStatus;
    boolean jcbTuesdayStatus;
    boolean jcbWednesdayStatus;
    boolean jcbThurdayStatus;
    boolean jcbFridayStatus;
    boolean jcbSaturStatus;
    String StartDateStr;
    String StartTimeStr;
    String EndDateStr;
    String EndTimeStr;
    String fontName = "TimesRoman";
    Integer fontSize = 13;
    static JTextArea display;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    QueryJobToExportLog queryJob = new QueryJobToExportLog();

    private TableColumnModel columns;

    private TableModel dataFile;

    private JTable table;
    DefaultTableModel model;
    JScrollPane scrollPane;
    Timer timer2;

    class DayInfo {
        public DayInfo(Boolean status, String dayOfWeek) {
            this.status = status;
            this.dayOfWeek = dayOfWeek;
        }

        private Boolean status;
        private String dayOfWeek;

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
    }

    public AML_BOTview() throws Exception {
        createGUI();
        setDisplay();
    }

    private void setDisplay() {
        setTitle("BOT - AML");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        invalidate();
        validate();
        repaint();
        // setDefaultLookAndFeelDecorated(true);

    }

    private void createGUI() throws Exception {
        getContentPane().add(createTabbedPane());
    }

    private JTabbedPane createTabbedPane() throws Exception {
        JTabbedPane tablePane = new JTabbedPane();
        tablePane.setPreferredSize(new Dimension(screenSize.width / 2,
                screenSize.height / 2));
        tablePane.setBackground(Color.white);

        JPanel panelResult = createPanelResult();
        panelResult.setPreferredSize(new Dimension(screenSize.width / 2,
                screenSize.height / 2));
        panelResult.setBackground(Color.white);

        JPanel panelMonitor = createPanelMonitor();
        panelMonitor.setPreferredSize(new Dimension(screenSize.width / 2,
                screenSize.height / 2));
        panelMonitor.setBackground(Color.white);

        JPanel panelEstablish = createPanelEstablish();
        panelEstablish.setPreferredSize(new Dimension(screenSize.width / 2,
                screenSize.height / 2));
        panelEstablish.setBackground(Color.white);

        tablePane.addTab("Monitor", null, panelMonitor);
        tablePane.addTab("Báo cáo", null, panelResult);
        tablePane.addTab("Thiết lập", null, panelEstablish);
        tablePane.setSelectedIndex(tablePane.getTabCount() - 3);

        return tablePane;
    }

    private JPanel createPanelResult() throws ClassNotFoundException,
            SQLException {
        JPanel panelResult = new JPanel();
        panelResult.setPreferredSize(new Dimension(screenSize.width / 2,
                screenSize.height / 2));
        panelResult.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        Vector<String> cols = new Vector<String>();
        cols.addElement("TT");
        cols.addElement("Loại");
        cols.addElement("Thời gian thực hiện");
        cols.addElement("Tên tệp");
        cols.addElement("Trạng thái");
        cols.addElement("Đường dẫn");
        cols.addElement("Ghi chú");
        model = new DefaultTableModel(null, cols);
        model.fireTableDataChanged();
        table = new JTable(model);
        scrollPane = new JScrollPane(table);

        JLabel fromDate = createLabel("Từ ngày: ");
        JLabel toDate = createLabel("Đến ngày: ");

        DatePickerSettings dateSettingsEndDatePicker = new DatePickerSettings();
        DatePicker jdStartDateRs = new DatePicker();
        DatePicker jdEndDateRs = new DatePicker(dateSettingsEndDatePicker);
        jdEndDateRs.setEnabled(false);

        jdStartDateRs.addDateChangeListener(new DateChangeListener() {

            @Override
            public void dateChanged(DateChangeEvent arg0) {
                dateSettingsEndDatePicker.setDateRangeLimits(
                        today.minusDays(getDiffWithTodayByDay(jdStartDateRs)),
                        today.plusDays(3000));
                jdEndDateRs.setEnabled(true);

            }
        });

        JButton btView = createButton("Hiển thị báo cáo");

        btView.addActionListener(e -> {
            // btViewData = true;
            QueryJobToExportLog queryJob = new QueryJobToExportLog(
                    jdStartDateRs, jdEndDateRs);
            Vector<Vector> listRowData = new Vector<>();
            listRowData.removeAllElements();
            listRowData = queryJob.getAllData();
            int rowCount = model.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            for (int i = 0; i < listRowData.size(); i++) {
                model.addRow(listRowData.get(i));
            }

            model.fireTableDataChanged();
            table = new JTable(model);
            table.setFillsViewportHeight(true);
            scrollPane = new JScrollPane(table);

        });

        JButton btExport = createButton("Xuất ra file báo cáo");
        btExport.addActionListener(e -> {
            String nameFile = ("Result from " + jdStartDateRs + " to " + jdEndDateRs);
            QueryJobToExportLog queryJob = new QueryJobToExportLog(
                    jdStartDateRs, jdEndDateRs);
            LinkedList<String> log;
            try {
                log = queryJob.getQueryJobToExportLog();

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

                com.tpbank.writeToFile.WriteLogToTable createTableLog = new com.tpbank.writeToFile.WriteLogToTable(
                        log, nameFile);
                createTableLog.drawTablePDF(log, nameFile);

                // WritePdfTest newPdf = new WritePdfTest(log, nameFile);
                // newPdf.createTextToAPdf(log, nameFile);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 200; // make this component tall
        c.ipadx = 100; // make this component tall
        c.weightx = 0.0;
        c.gridwidth = 6; // 5 columns wide
        positionInPanel(c, 0, 0);
        panelResult.add(scrollPane, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(c, 0, 1);
        c.gridwidth = 1;
        recurPanelPostion(c, 0, 0);
        c.insets = new Insets(10, 60, 0, 0);
        panelResult.add(fromDate, c);

        c.fill = GridBagConstraints.LINE_START;
        positionInPanel(c, 1, 1);
        c.gridwidth = 1;
        c.insets = new Insets(10, -180, 0, 0);
        panelResult.add(jdStartDateRs, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(c, 2, 1);
        c.gridwidth = 1;
        c.insets = new Insets(10, 50, 0, 0);
        panelResult.add(toDate, c);

        c.fill = GridBagConstraints.LINE_START;
        positionInPanel(c, 3, 1);
        c.gridwidth = 1;
        c.weightx = 30;
        c.insets = new Insets(10, -188, 0, 80);
        panelResult.add(jdEndDateRs, c);

        c.fill = GridBagConstraints.LINE_START;
        positionInPanel(c, 0, 3);
        c.gridwidth = 1;
        c.insets = new Insets(10, 95, 0, 0);
        panelResult.add(btExport, c);

        c.fill = GridBagConstraints.LINE_START;
        positionInPanel(c, 2, 3);
        c.gridwidth = 1;
        c.insets = new Insets(10, 90, 0, 0);
        panelResult.add(btView, c);
        return panelResult;
    }

    private JPanel createPanelMonitor() throws ClassNotFoundException,
            SQLException {
        JPanel panelMonitor = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        panelMonitor.setLayout(layout);

        GridBagConstraints cMonitorPanel = new GridBagConstraints();

        panelMonitor.setBorder(new TitledBorder(new EtchedBorder(),
                "Công việc đang thực hiện"));

        display = new JTextArea(16, 58);
        ReadFromFile2 rf = new ReadFromFile2();

        display.setEditable(false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelMonitor.add(scroll);

        return panelMonitor;
    }

    private JPanel createPanelEstablish() throws Exception {
        SingleTaskTimer timer = new SingleTaskTimer();

        JPanel panelEstablish = new JPanel();
        GridBagConstraints cEstablish = new GridBagConstraints();
        GridBagLayout layoutEstablish = new GridBagLayout();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        panelEstablish.setBounds(0, 0, screenSize.width / 2,
                screenSize.height / 2);

        panelEstablish.setLayout(layoutEstablish);
        Border blacklineEstablishPanel = BorderFactory.createTitledBorder("");
        panelEstablish.setBorder(blacklineEstablishPanel);

        panelEstablish.setBackground(Color.white);

        JPanel panelRecur = new JPanel();
        JTextField textFieldRecur = new JTextField();

        JCheckBox cbMonday = new JCheckBox("MONDAY");

        JCheckBox cbSunday = new JCheckBox("SUNDAY");

        JCheckBox cbTuesday = new JCheckBox("TUESDAY");

        JCheckBox cbWednesday = new JCheckBox("WEDNESDAY");

        JCheckBox cbThursday = new JCheckBox("THURDAY");

        JCheckBox cbFriday = new JCheckBox("FRIDAY");

        JCheckBox cbSaturday = new JCheckBox("SATURDAY");

        // JTextArea textIntervalPeriod = new JTextArea(1, 5);
        JSpinner textIntervalPeriod = new JSpinner(new SpinnerNumberModel(0, 0,
                100, 1));
        textIntervalPeriod.setBorder(null);
        textFieldRecur.setEnabled(false);
        textIntervalPeriod.setEnabled(false);
        setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday, cbWednesday,
                cbThursday, cbFriday, cbSaturday, false);

        JPanel panelTaskControl = new JPanel();
        GridBagConstraints cTaskControl = new GridBagConstraints();
        GridBagLayout layoutTaskControl = new GridBagLayout();
        panelTaskControl.setLayout(layoutTaskControl);

        // Start Button
        JButton btSaveSetting = new JButton("Lưu cấu hình");
        JButton btStart = new JButton("START");
        JButton btStop = new JButton("STOP");
        JLabel noteStatus = new JLabel();
        btSaveSetting.setEnabled(false);
        btStart.setEnabled(false);
        btStop.setEnabled(false);
        noteStatus.setVisible(false);
        // ======== panel Setting=========================
        JPanel panelSetting = new JPanel();
        GridBagConstraints cSetting = new GridBagConstraints();
        GridBagLayout layoutSetting = new GridBagLayout();
        panelSetting.setLayout(layoutSetting);
        Border blacklineSetting = BorderFactory.createTitledBorder("Đặt lịch");
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
        r1.setText("Một lần");
        r1.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
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
        r2.setText("Hàng ngày");
        r2.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        r2.setEnabled(false);
        r2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                textFieldRecur.setEnabled(false);
                setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
                        cbWednesday, cbThursday, cbFriday, cbSaturday, false);
                textIntervalPeriod.setEnabled(true);
                period = 24 * 60;
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
                period = 7 * 24 * 60;
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
                period = 30 * 24 * 60;
            }
        });

        // ======== panelIntervalSimple=========================
        // RadioButton "One time"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(cIntervalSimple, 0, 0);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(10, 5, 5, 0);
        panelIntervalSimple.add(r1, cIntervalSimple);

        // RadioButton "Daily"
        cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(cIntervalSimple, 0, 1);
        cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
        cIntervalSimple.insets = new Insets(-10, 5, 10, 10);
        panelIntervalSimple.add(r2, cIntervalSimple);

        // =======Start Time=====================
        // Panel StartTime
        JPanel panelStartTime = new JPanel();
        GridBagConstraints cStartTime = new GridBagConstraints();
        GridBagLayout layoutStartTime = new GridBagLayout();
        panelStartTime.setLayout(layoutStartTime);
        panelStartTime.setBackground(Color.white);

        // add label Start to Panel Setting:
        JLabel startLb = new JLabel("Bắt đầu: 	");
        startLb.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        cStartTime.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cStartTime, 0, 0);
        positionInPanel(cStartTime, 0, 0);
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(10, 10, 10, 10);
        panelStartTime.add(startLb, cStartTime);

        // add label Start to Panel StartTime:
        TimePickerSettings timePickerSettingsStart = new TimePickerSettings();
        // timePickerSettingsStart.setFontValidDate(new Font("Monospaced",
        // Font.ITALIC | Font.BOLD, 17));
        timePickerSettingsStart.setInitialTimeToNow();
        timePickerSettingsStart.setFormatForDisplayTime(PickerUtilities
                .createFormatterFromPatternString("HH:mm",
                        timePickerSettingsStart.getLocale()));

        DatePickerSettings datePickerSettingsStart = new DatePickerSettings();
        dateTimePickerStart = new DateTimePicker(datePickerSettingsStart,
                timePickerSettingsStart);

        datePickerSettingsStart.setFormatForDatesCommonEra("dd-MM-yyyy");
        /*
         * datePickerSettingsStart.setDateRangeLimits(today.minusDays(0),
         * today.plusDays(3000));
         */
        DatePickerSettings dateSettingsStopDatePicker = new DatePickerSettings();
        dateTimePickerStart
                .addDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void dateOrTimeChanged(DateTimeChangeEvent arg0) {
                        if (dateTimePickerStart.getDatePicker().getDate() != null) {
                            dateSettingsStopDatePicker.setDateRangeLimits(
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
        cStartTime.weightx = 1;
        cStartTime.gridwidth = 3;
        cStartTime.ipadx = 80;
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(5, 10, 10, 10);
        panelStartTime.add(dateTimePickerStart, cStartTime);

        // add label Start to Panel Setting:
        JLabel stopLb = new JLabel("Kết thúc: 	");
        stopLb.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        cStartTime.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cStartTime, 0, 0);
        positionInPanel(cStartTime, 0, 1);
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(5, 10, 10, 10);
        panelStartTime.add(stopLb, cStartTime);

        // DateTime Strop
        TimePickerSettings timeSettingsStop = new TimePickerSettings();
        timeSettingsStop.setInitialTimeToNow();
        timeSettingsStop.setFormatForDisplayTime(PickerUtilities
                .createFormatterFromPatternString("HH:mm",
                        timeSettingsStop.getLocale()));

        dateTimePickerStop = new DateTimePicker(dateSettingsStopDatePicker,
                timeSettingsStop);
        dateSettingsStopDatePicker.setFormatForDatesCommonEra("dd-MM-yyyy");
        dateTimePickerStop.setEnabled(false);
        dateTimePickerStart
                .addDateTimeChangeListener(new DateTimeChangeListener() {

                    @Override
                    public void dateOrTimeChanged(DateTimeChangeEvent arg0) {
                        if (dateTimePickerStart.getDatePicker().getDate() != null) {
                            dateTimePickerStop.setEnabled(true);
                            noteStatus.setVisible(false);

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
                        noteStatus.setVisible(false);

                        try {
                            noteStatusAction(btStart, btStop, noteStatus,
                                    dateTimePickerStop.getDatePicker()
                                            .getDate());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                });
        cStartTime.fill = GridBagConstraints.HORIZONTAL;
        taskControlPanelPosition(cStartTime, 0, 0);
        startTimePanelPosition(cStartTime, 1, 1);
        cStartTime.ipadx = 80;
        cStartTime.anchor = GridBagConstraints.LINE_START;
        cStartTime.insets = new Insets(5, 10, 10, 10);
        panelStartTime.add(dateTimePickerStop, cStartTime);

        // Panel Interval
        JPanel panelInterval = new JPanel();
        GridBagConstraints cInterval = new GridBagConstraints();
        GridBagLayout layoutInterval = new GridBagLayout();
        panelInterval.setLayout(layoutInterval);

        JLabel intervalPeriod = createLabel("Chu kỳ lặp lại: ");
        intervalPeriod.setFont(new Font(fontName, Font.CENTER_BASELINE,
                fontSize));
        cInterval.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 0);
        cInterval.anchor = GridBagConstraints.LINE_START;
        cInterval.insets = new Insets(5, 10, 10, 10);
        panelInterval.add(intervalPeriod, cInterval);

        Border blacklineIntervalPeriod = BorderFactory
                .createLineBorder(Color.LIGHT_GRAY);
        textIntervalPeriod.setBorder(blacklineIntervalPeriod);
        // textIntervalPeriod.getDocument().addDocumentListener(
        textIntervalPeriod.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent arg0) {
                if (!textIntervalPeriod.getValue().equals(0)) {
                    setRadioButtonStatus(r1, r2, r3, r4, false);
                } else {
                    setRadioButtonStatus(r1, r2, r3, r4, true);
                }
            }
        });

        cInterval.fill = GridBagConstraints.CENTER;
        textIntervalPeriod.setFont(new Font("TimesRoman", Font.CENTER_BASELINE,
                16));
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 1);
        cInterval.anchor = GridBagConstraints.LINE_START;
        cInterval.insets = new Insets(5, 10, 10, 10);
        panelInterval.add(textIntervalPeriod, cInterval);

        JLabel intervalUnitTime = createLabel("phút");
        intervalUnitTime.setFont(new Font("TimesRoman", Font.CENTER_BASELINE,
                13));
        cInterval.fill = GridBagConstraints.CENTER;
        taskControlPanelPosition(cInterval, 0, 0);
        startTimePanelPosition(cInterval, 0, 2);
        cInterval.weightx = 1.0;
        cInterval.anchor = GridBagConstraints.LINE_END;
        cInterval.insets = new Insets(5, 10, 10, 10);
        panelInterval.add(intervalUnitTime, cInterval);

        // =======Start Time=====================
        // Panel Recur

        GridBagConstraints cRecur = new GridBagConstraints();
        GridBagLayout layoutRecur = new GridBagLayout();
        panelRecur.setLayout(layoutRecur);
        Border blacklineRecur = BorderFactory.createTitledBorder("");
        panelRecur.setBorder(blacklineRecur);
        panelRecur.setBackground(Color.white);

        // Checkbox Monday
        // JCheckBox cbMonday = new JCheckBox("Monday");

        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 0);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        panelRecur.add(cbMonday, cRecur);
        cbMonday.setText("Thứ hai");
        cbMonday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        cbMonday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbMonday.isSelected()) {
                    cBDayOfWeekStatus(cbMonday, cbMonday.isSelected());
                }
            }
        });

        // Checkbox Tuesday
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 1);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        cbTuesday.setText("Thứ ba");
        ;
        cbTuesday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        panelRecur.add(cbTuesday, cRecur);
        cbTuesday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbTuesday.isSelected()) {
                    cBDayOfWeekStatus(cbTuesday, cbTuesday.isSelected());
                }
            }
        });

        // Checkbox Wednesday
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 2);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        cbWednesday.setText("Thứ tư");
        ;
        cbWednesday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        panelRecur.add(cbWednesday, cRecur);
        cbWednesday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbWednesday.isSelected()) {
                    cBDayOfWeekStatus(cbWednesday, cbWednesday.isSelected());
                }
            }
        });

        // Checkbox Thursday
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 3);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        cbThursday.setText("Thứ năm");
        ;
        cbThursday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        panelRecur.add(cbThursday, cRecur);
        cbThursday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbThursday.isSelected()) {
                    cBDayOfWeekStatus(cbThursday, cbThursday.isSelected());
                }
            }
        });

        // Checkbox Friday
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 4);
        cRecur.gridwidth = 1;
        recurPanelPostion(cRecur, 1, 4);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        cbFriday.setText("Thứ sáu");
        cbFriday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        panelRecur.add(cbFriday, cRecur);
        cbFriday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbFriday.isSelected()) {
                    cBDayOfWeekStatus(cbFriday, cbFriday.isSelected());
                }
            }
        });

        // Checkbox Saturday
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 5);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        cbSaturday.setText("Thứ bảy");
        cbSaturday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        panelRecur.add(cbSaturday, cRecur);
        cbSaturday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbSaturday.isSelected()) {
                    cBDayOfWeekStatus(cbSaturday, cbSaturday.isSelected());
                }
            }
        });

        // Checkbox Sunday
        cbSunday.setText("Chủ nhật");
        cbSunday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        cRecur.fill = GridBagConstraints.HORIZONTAL;
        startTimePanelPosition(cRecur, 1, 6);
        cRecur.anchor = GridBagConstraints.LINE_START;
        cRecur.insets = new Insets(5, 0, 0, 10);
        panelRecur.add(cbSunday, cRecur);
        cbSunday.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (cbSunday.isSelected()) {
                    cBDayOfWeekStatus(cbSunday, cbSunday.isSelected());
                }
            }
        });

        // ============ panel StartTime to add Panel Setting===================
        // add Panel Setting to panel StartTime
        cSetting.fill = GridBagConstraints.LINE_START;
        positionInPanel(cSetting, 0, 0);
        settingPanelWidthAndHeight(cSetting, 1, 2);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(-10, 5, 30, 0);
        cSetting.weightx = 0.5; // request any extra HORIZONTAL space
        cSetting.ipadx = 5;
        cSetting.gridwidth = 2;
        panelSetting.add(panelIntervalSimple, cSetting);
        panelSetting.setBackground(Color.white);

        // add panel StartTime to panel Setting
        cSetting.fill = GridBagConstraints.LINE_START;
        // intervalSimplePanelPosition(cSetting, 1, 0);
        settingPanelWidthAndHeight(cSetting, 4, 1);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.ipadx = 2;
        cSetting.gridwidth = 4;
        cSetting.insets = new Insets(-10, 150, -20, 0);
        panelSetting.add(panelStartTime, cSetting);

        // add panel StartTime to panel Setting
        cSetting.fill = GridBagConstraints.LINE_START;
        positionInPanel(cSetting, 1, 1);
        settingPanelWidthAndHeight(cSetting, 4, 1);
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(15, 150, 0, 0);
        panelSetting.add(panelInterval, cSetting);

        // add panel Recur to panel Setting
        cSetting.fill = GridBagConstraints.HORIZONTAL;
        cSetting.weightx = 1.0; // request any extra HORIZONTAL space
        positionInPanel(cSetting, 0, 2);
        settingPanelWidthAndHeight(cSetting, 0, 0);
        cSetting.gridwidth = 6;
        cSetting.anchor = GridBagConstraints.LINE_START;
        cSetting.insets = new Insets(5, 5, 10, 10);
        panelSetting.add(panelRecur, cSetting);

        // ======== panel Advance panelSaveSetting=========================
        JPanel panelSaveSetting = new JPanel();
        GridBagConstraints cSaveSetting = new GridBagConstraints();
        GridBagLayout layoutSaveSetting = new GridBagLayout();
        panelSaveSetting.setLayout(layoutSaveSetting);
        Border blacklineSaveSetting = BorderFactory
                .createTitledBorder("Thư mục lưu báo cáo");
        panelSaveSetting.setBorder(blacklineSaveSetting);
        panelSaveSetting.setBackground(Color.white);

        // Label Save textField
        JLabel lbSaveFolder = new JLabel("Đường dẫn:");
        lbSaveFolder
                .setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
        cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
        startTimePanelPosition(cSaveSetting, 0, 0);
        cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSaveSetting.insets = new Insets(10, 10, 10, 10);
        panelSaveSetting.add(lbSaveFolder, cSaveSetting);

        // Label Save textField
        JTextArea textSaveFolder = new JTextArea(1, 20);
        textSaveFolder.getDocument().addDocumentListener(
                new DocumentListener() {

                    @Override
                    public void removeUpdate(DocumentEvent arg0) {
                        btStart.setEnabled(false);
                        btStop.setEnabled(false);
                        btSaveSetting.setEnabled(false);
                    }

                    @Override
                    public void insertUpdate(DocumentEvent arg0) {
                        btStart.setEnabled(true);
                        btStop.setEnabled(true);
                        btSaveSetting.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent arg0) {
                        btStart.setEnabled(true);
                        btStop.setEnabled(true);
                        btSaveSetting.setEnabled(true);
                    }
                });
        Border blacklineTxtSaveFolder = BorderFactory
                .createLineBorder(Color.LIGHT_GRAY);
        textSaveFolder.setBorder(blacklineTxtSaveFolder);
        textSaveFolder.setFont(new Font(fontName, Font.CENTER_BASELINE,
                fontSize));
        cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
        startTimePanelPosition(cSaveSetting, 0, 1);
        cSaveSetting.weightx = 2.0;
        cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cSaveSetting.insets = new Insets(5, 10, 10, 10);
        panelSaveSetting.add(textSaveFolder, cSaveSetting);

        // if(today.now().compareTo0(dateTimePickerStop.datePicker.setDateToToday(););
        System.out
                .println("==================================Loading last status=========================================");

        File checkLoading = new File(saveEstablishStatus);
        if (checkLoading.exists()) {
            if (loadFileAndReturnElement().size() != 0
                    && loadFileAndReturnElement().size() == 15) {
                String test = loadFileAndReturnElement().get(7);
                loadingRecentStatus(
                        cbMonday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(9)),
                        cbSunday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(8)),
                        cbTuesday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(10)),
                        cbWednesday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(11)),
                        cbThursday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(12)),
                        cbFriday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(13)),
                        cbSaturday,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(14)),
                        textIntervalPeriod,
                        loadFileAndReturnElement().get(7),
                        r1,
                        Boolean.parseBoolean(loadFileAndReturnElement().get(0)),
                        r2, Boolean.parseBoolean(loadFileAndReturnElement()
                                .get(1)), dateTimePickerStart, LocalDate
                                .parse((loadFileAndReturnElement().get(2))),
                        loadFileAndReturnElement().get(3), dateTimePickerStop,
                        LocalDate.parse((loadFileAndReturnElement().get(4))),
                        loadFileAndReturnElement().get(5), textSaveFolder,
                        loadFileAndReturnElement().get(6));

                noteStatusAction(btStart, btStop, noteStatus,
                        LocalDate.parse((loadFileAndReturnElement().get(4))));
            } else {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                loadingRecentStatus(cbMonday, false, cbSunday, false,
                        cbTuesday, false, cbWednesday, false, cbThursday,
                        false, cbFriday, false, cbSaturday, false,
                        textIntervalPeriod, "-1", r1, false, r2, false,
                        dateTimePickerStart, LocalDate.now(), LocalTime.now()
                                .format(dtf).toString(), dateTimePickerStop,
                        LocalDate.now(),
                        LocalTime.now().format(dtf).toString(), textSaveFolder,
                        "");
            }
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            loadingRecentStatus(cbMonday, false, cbSunday, false, cbTuesday,
                    false, cbWednesday, false, cbThursday, false, cbFriday,
                    false, cbSaturday, false, textIntervalPeriod, "-1", r1,
                    false, r2, false, dateTimePickerStart, LocalDate.now(),
                    LocalTime.now().format(dtf).toString(), dateTimePickerStop,
                    LocalDate.now(), LocalTime.now().format(dtf).toString(),
                    textSaveFolder, "");
        }

        btSaveSetting
                .addActionListener(e -> {
                    jrdOneTime = r1.isSelected();
                    jrdDaily = r2.isSelected();

                    if (!textIntervalPeriod.getValue().equals(0)) {
                        intevalPeriod = Integer.parseInt(textIntervalPeriod
                                .getValue().toString());
                    } else {
                        intevalPeriod = -1;
                    }

                    jcbSundayStatus = cbSunday.isSelected();
                    jcbMondayStatus = cbMonday.isSelected();
                    jcbTuesdayStatus = cbTuesday.isSelected();
                    jcbWednesdayStatus = cbWednesday.isSelected();
                    jcbThurdayStatus = cbThursday.isSelected();
                    jcbFridayStatus = cbFriday.isSelected();
                    jcbSaturStatus = cbSaturday.isSelected();
                    saveFile = textSaveFolder.getText();
                    StartDateStr = dateTimePickerStart.getDatePicker()
                            .getDate().toString();
                    StartTimeStr = dateTimePickerStart.getTimePicker()
                            .getTime().toString();
                    EndDateStr = dateTimePickerStop.getDatePicker().getDate()
                            .toString();
                    EndTimeStr = dateTimePickerStop.getTimePicker().getTime()
                            .toString();

                    System.out.println("0. jrdOneTime " + jrdOneTime);
                    System.out.println("1. jrdDaily " + jrdDaily);

                    System.out.println("2. StartDateStr " + StartDateStr);
                    System.out.println("3. StartTimeStr " + StartTimeStr);
                    System.out.println("4. EndDateStr " + EndDateStr);
                    System.out.println("5. EndTimeStr " + EndTimeStr);
                    System.out.println("6. saveFile " + saveFile);
                    System.out.println("7. intevalPeriod " + intevalPeriod);
                    System.out.println("8. jcbSundayStatus " + jcbSundayStatus);
                    System.out.println("9. jcbMondayStatus " + jcbMondayStatus);
                    System.out.println("10. jcbTuesdayStatus "
                            + jcbTuesdayStatus);
                    System.out.println("11. jcbWednesdayStatus "
                            + jcbWednesdayStatus);
                    System.out.println("12. jcbThurdayStatus "
                            + jcbThurdayStatus);
                    System.out
                            .println("13. jcbFridayStatus " + jcbFridayStatus);
                    System.out.println("14. jcbSaturStatus " + jcbSaturStatus);
                    System.out
                            .println("===========================================================================================");
                    String saveString = jrdOneTime + "@" + jrdDaily + "@"
                            + StartDateStr + "@" + StartTimeStr + "@"
                            + EndDateStr + "@" + EndTimeStr + "@" + saveFile
                            + "@" + intevalPeriod + "@" + jcbSundayStatus + "@"
                            + jcbMondayStatus + "@" + jcbTuesdayStatus + "@"
                            + jcbWednesdayStatus + "@" + jcbThurdayStatus + "@"
                            + jcbFridayStatus + "@" + jcbSaturStatus;
                    SaveEstablishInTxtFile newSaveFile = new SaveEstablishInTxtFile(
                            saveString);
                    newSaveFile.saveTextEstablish(saveString);
                    try {
                        loadFileAndReturnElement();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                });
        cTaskControl.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cTaskControl, 0, 0);
        // startTimePanelPosition(cTaskControl, 0, 0);
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(5, 0, 0, 50);
        panelTaskControl.add(btSaveSetting, cTaskControl);

        btStart.addActionListener(e -> {

            actionBtStart(textIntervalPeriod.getValue().toString(),
                    dateTimePickerStart, dateTimePickerStop, textSaveFolder,
                    dateStartArr);

        });
        cTaskControl.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cTaskControl, 0, 1);
        // startTimePanelPosition(cTaskControl, 0, 0);
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(5, 00, 0, 30);
        panelTaskControl.add(btStart, cTaskControl);

        // Start Button

        btStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                timer.cancel();
                timer2.cancel();

            }
        });
        ;
        cTaskControl.fill = GridBagConstraints.LINE_START;
        taskControlPanelPosition(cTaskControl, 0, 3);
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(5, 20, 0, 50);
        panelTaskControl.add(btStop, cTaskControl);

        noteStatus.setForeground(Color.RED);
        cTaskControl.fill = GridBagConstraints.LINE_START;
        // taskControlPanelPosition(cTaskControl, 0, 0);
        cTaskControl.gridy = 1;
        cTaskControl.gridx = 0;
        cTaskControl.ipadx = 0;
        cTaskControl.ipady = 0;
        cTaskControl.gridwidth = 3;
        cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
        // space
        cTaskControl.insets = new Insets(20, -120, 10, 10);
        panelTaskControl.add(noteStatus, cTaskControl);

        // Establish=======================
        // add Panel Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(cEstablish, 0, 0);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.ipadx = 75;
        cEstablish.ipady = 50;
        cEstablish.weightx = 40;
        cEstablish.insets = new Insets(-50, -20, 0, 0);
        panelEstablish.add(panelSetting, cEstablish);

        // Establish=======================
        // add Panel Advance Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(cEstablish, 0, 1);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.insets = new Insets(10, 0, 0, 0);
        panelEstablish.add(panelSaveSetting, cEstablish);

        // Establish=======================
        // add Panel Advance Setting to panel
        cEstablish.fill = GridBagConstraints.HORIZONTAL;
        positionInPanel(cEstablish, 0, 2);
        taskControlPanelPosition(cEstablish, 0, 0);
        cEstablish.insets = new Insets(15, 0, 0, 0);
        panelEstablish.add(panelTaskControl, cEstablish);

        return panelEstablish;
    }

    private void noteStatusAction(JButton btStart, JButton btStop,
                                  JLabel noteStatus, LocalDate dateMaking) throws Exception {
        String formattedDate = today.format(DateTimeFormatter
                .ofPattern("dd-MM-yyyy"));
        if (dateMaking.isBefore(LocalDate.now())) {
            noteStatus.setVisible(true);
            noteStatus
                    .setText("*Chú ý: Ngày kết thúc được nạp đang trước ngày hôm nay: "
                            + "\" " + formattedDate + " \"");
            btStart.setEnabled(false);
            btStop.setEnabled(false);
        } else {
            noteStatus.setVisible(false);
            btStart.setEnabled(true);
            btStop.setEnabled(true);
        }
    }

    private void loadingRecentStatus(JCheckBox cbMonday,
                                     Boolean cbMondayStatus, JCheckBox cbSunday, Boolean cbSundayStatus,
                                     JCheckBox cbTuesday, Boolean cbTuesdayStatus,
                                     JCheckBox cbWednesday, Boolean cbWednesdayStatus,
                                     JCheckBox cbThursday, Boolean cbThursdayStatus, JCheckBox cbFriday,
                                     Boolean cbFridayStatus, JCheckBox cbSaturday,
                                     Boolean cbSaturdayStatus, JSpinner textIntervalPeriod,
                                     String textIntervalPeriodValue, JRadioButton r1, Boolean r1Status,
                                     JRadioButton r2, Boolean r2Status, DateTimePicker dateTimeStart,
                                     LocalDate dateStart, String timeStart,
                                     DateTimePicker dateTimePickerStop, LocalDate dateStop,
                                     String timeStop, JTextArea textSaveFolder, String savePath)
            throws Exception {
        r1.setSelected(r1Status);
        r2.setSelected(r2Status);

        r1.setEnabled(r1Status);
        r2.setEnabled(r2Status);

        textIntervalPeriod.setValue(Integer.parseInt(textIntervalPeriodValue));

        if (r1.isSelected()) {
            period = 1;
            textIntervalPeriod.setEnabled(false);
            System.out.println(period);
        } else if (r2.isSelected()) {
            if (textIntervalPeriod.getValue().toString().equals("0")) {
                period = 24 * 60;
            } else {
                period = Integer.parseInt(textIntervalPeriodValue);
            }
        }

        dateTimeStart.datePicker.setDate(dateStart);
        dateTimeStart.timePicker.setText(timeStart);
        dateTimePickerStop.datePicker.setDate(dateStop);
        dateTimePickerStop.timePicker.setText(timeStop);

        textSaveFolder.setText(savePath);

        cbSunday.setSelected(cbSundayStatus);
        cbMonday.setSelected(cbMondayStatus);
        cbTuesday.setSelected(cbTuesdayStatus);
        cbWednesday.setSelected(cbWednesdayStatus);
        cbThursday.setSelected(cbThursdayStatus);
        cbFriday.setSelected(cbFridayStatus);
        cbSaturday.setSelected(cbSaturdayStatus);

        cbSunday.setEnabled(cbSundayStatus);
        cbMonday.setEnabled(cbMondayStatus);
        cbTuesday.setEnabled(cbTuesdayStatus);
        cbWednesday.setEnabled(cbWednesdayStatus);
        cbThursday.setEnabled(cbThursdayStatus);
        cbFriday.setEnabled(cbFridayStatus);
        cbSaturday.setEnabled(cbSaturdayStatus);

        System.out
                .println("==================================Loading last status DONE=========================================");

        System.out.println("0. jrdOneTime " + r1.isSelected());
        System.out.println("1. jrdDaily " + r2.isSelected());

        System.out.println("2. StartDateStr "
                + dateTimePickerStart.datePicker.toString());
        System.out.println("3. StartTimeStr "
                + dateTimePickerStart.timePicker.toString());
        System.out.println("4. EndDateStr "
                + dateTimePickerStop.datePicker.toString());
        System.out.println("5. EndTimeStr "
                + dateTimePickerStop.timePicker.toString());
        System.out.println("6. saveFile " + textSaveFolder.getText());
        System.out.println("7. intevalPeriod " + textIntervalPeriod.getValue());
        System.out.println("8. jcbSundayStatus " + cbSunday.isSelected());
        System.out.println("9. jcbMondayStatus " + cbMonday.isSelected());
        System.out.println("10. jcbTuesdayStatus " + cbTuesday.isSelected());
        System.out
                .println("11. jcbWednesdayStatus " + cbWednesday.isSelected());
        System.out.println("12. jcbThurdayStatus " + cbThursday.isSelected());
        System.out.println("13. jcbFridayStatus " + cbFriday.isSelected());
        System.out.println("14. jcbSaturStatus " + cbSaturday.isSelected());
        System.out
                .println("==================================Done=========================================");
    }

    private void cBDayOfWeekStatus(JCheckBox cbox, boolean cbStatus) {
        // System.out.println(cbox.getText().toUpperCase() + ": " + cbStatus);
        // Create new dayInfo object and add to dayInfoArrayList
        String dayOfWeek = "";
        switch (cbox.getText()) {
            case "Thứ hai":
                dayOfWeek = "MONDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Thứ ba":
                dayOfWeek = "TUESDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Thứ tư":
                dayOfWeek = "WEDNESDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Thứ năm":
                dayOfWeek = "THURSDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Thứ sáu":
                dayOfWeek = "FRIDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Thứ bảy":
                dayOfWeek = "SATURDAY";
                break;
        }
        switch (cbox.getText()) {
            case "Chủ nhật":
                dayOfWeek = "SUNDAY";
                break;
        }

        String dateStr = dateTimePickerStart.getDatePicker().getDate()
                .getDayOfWeek().toString();
        if (cbStatus == true) {
            Calendar calEnd = calendarFromPickers(
                    dateTimePickerStop.getDatePicker(),
                    dateTimePickerStop.getTimePicker());
            // Calendar calStart = Calendar.getInstance();
            Calendar calStart = calendarFromPickers(
                    dateTimePickerStart.getDatePicker(),
                    dateTimePickerStart.getTimePicker());
            if (dayOfWeek.equals(dateStr)) {
                dateStartArr.add(dateTimePickerStart.getDatePicker().getDate());
            } else {
                List<LocalDate> lst = getDateTimeFromDayOfWeek(calStart,
                        calEnd, dayOfWeek, dateTimePickerStart.getTimePicker()
                                .getTime().getHour(), dateTimePickerStart
                                .getTimePicker().getTime().getMinute());
                for (int i = 0; i < lst.size(); i++) {
                    System.out.println(lst.get(i));
                    dateStartArr.add(lst.get(i));
                }
            }
        }
    }

    private void recurPanelPostion(GridBagConstraints cRecur, int i, int i2) {
        cRecur.ipady = i;
        cRecur.ipadx = i2;
    }

    private void settingPanelWidthAndHeight(GridBagConstraints cSetting, int i,
                                            int i2) {
        cSetting.gridwidth = i;
        cSetting.gridheight = i2;
    }

    private void taskControlPanelPosition(GridBagConstraints cTaskControl,
                                          int i, int i2) {
        cTaskControl.ipadx = i; // reset to default
        cTaskControl.ipady = i2; // reset to default
    }

    private void startTimePanelPosition(GridBagConstraints cStartTime, int i,
                                        int i2) {
        cStartTime.gridy = i;
        cStartTime.gridx = i2;
    }

    private void positionInPanel(GridBagConstraints cIntervalSimple, int i,
                                 int i2) {
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

    private static String defaultPathString() {
        Preferences pref = Preferences.userRoot();
        return pref.get("DEFAULT_PATH", "");
    }

    @SuppressWarnings("deprecation")
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

    private long getDiffWithTodayByDay(DateTimePicker dateTimeStart) {
        final LocalDate today = LocalDate.now();
        long getDiffWithTodayByDay = today.toEpochDay()
                - dateTimeStart.getDatePicker().getDate().toEpochDay();
        return getDiffWithTodayByDay;
    }

    private long getDiffWithTodayByDay(DatePicker dateStart) {
        final LocalDate today = LocalDate.now();
        long getDiffWithTodayByDay = today.toEpochDay()
                - dateStart.getDate().toEpochDay();
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

    private void actionBtStart(String textIntervalPeriod,
                               DateTimePicker dateTimePickerStart,
                               DateTimePicker dateTimePickerStop, JTextArea txtSaveFolder,
                               ArrayList<LocalDate> dateStartArr) {
        Calendar calEnd = calendarFromPickers(
                dateTimePickerStop.getDatePicker(),
                dateTimePickerStop.getTimePicker());
        // Calendar calStart = Calendar.getInstance();
        Calendar calStart = calendarFromPickers(
                dateTimePickerStart.getDatePicker(),
                dateTimePickerStart.getTimePicker());
        // RUN BY DAY OF WEEK
        if (dateStartArr.size() > 0) {
            for (LocalDate date : dateStartArr) {
                calStart.set(date.getYear(), date.getMonthValue() - 1,
                        date.getDayOfMonth());
                System.out.println(calStart.getTime().toString());
                /*
                 * if
                 * (date.getDayOfWeek().equals(calStart.get(Calendar.DAY_OF_WEEK
                 * ))) { timer2 = new Timer(); timer2.schedule(new TimerTask() {
                 *
                 * @Override public void run() { downloadFile(txtSaveFolder);
                 * Date dateNow = new Date(); if
                 * (dateNow.compareTo(calEnd.getTime()) > 0) {
                 * System.out.println("Stop!"); timer2.cancel(); }
                 *
                 * } }, calStart.getTime(), TimeUnit.MILLISECONDS.convert(
                 * period, TimeUnit.MINUTES)); timer2.cancel(); } else {
                 */
                if (!textIntervalPeriod.equals("0")) {
                    period = Integer.parseInt(textIntervalPeriod);
                }
                timer2 = new Timer();
                timer2.schedule(new TimerTask() {

                                    @Override
                                    public void run() {
                                        downloadFile(txtSaveFolder);
                                        Date dateNow = new Date();
                                        if (dateNow.compareTo(calEnd.getTime()) > 0) {
                                            System.out.println("Stop!");
                                            timer2.cancel();
                                        }

                                    }
                                }, calStart.getTime(),
                        TimeUnit.MILLISECONDS.convert(period, TimeUnit.MINUTES));
                if (LocalDateTime.now().compareTo(
                        LocalDateTime.now().withHour(16).withMinute(58)) > 0) {
                    System.out.println("Timer cancel");
                    timer2.cancel();
                }

            }
        } else {
            // RUN DAILY 8:00 - 17:30
            if (!textIntervalPeriod.equals("0")) {
                period = Integer.parseInt(textIntervalPeriod);
            }
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Running!");
                    downloadFile(txtSaveFolder);
                    try {
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date dateNow = new Date();
                        dateNow.setSeconds(00);
                        Date datePause = new Date();
                        datePause.setHours(17);
                        datePause.setMinutes(19);
                        datePause.setSeconds(00);

                        if (dateNow.compareTo(calEnd.getTime()) > 0) {
                            System.out.println("Stop!");
                            timer2.cancel();
                        }

                        if (dateNow.compareTo(datePause) >= 0) {
                            System.out.println("Sleeping!");

                            try {
                                Date dateNow2 = new Date();
                                String time1 = dateNow2.getHours() + ":"
                                        + dateNow2.getMinutes();
                                String time2 = "17:23";
                                SimpleDateFormat format = new SimpleDateFormat(
                                        "HH:mm");
                                Date date1;
                                date1 = format.parse(time1);
                                Date date2 = format.parse(time2);
                                long totalTimeSleep;
                                System.out.println(dateNow2);
                                if (date1.compareTo(date2) < 0) {
                                    totalTimeSleep = (date2.getTime() - date1
                                            .getTime());
                                } else {
                                    totalTimeSleep = (date2.getTime() - date1
                                            .getTime()) + (24 * 60 * 60 * 1000);
                                }
                                Thread.sleep(totalTimeSleep);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            System.out.println("Wake up!");
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }, calStart.getTime(), TimeUnit.MILLISECONDS.convert(period,
                    TimeUnit.MINUTES));
            System.out.println("period: "
                    + TimeUnit.MILLISECONDS.convert(period, TimeUnit.MINUTES));
        }
        loadMonitor(printLogOut);
    }

    public static void loadMonitor(String logs) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                display.append(logs);
            }
        });
    }

    private void downloadFile(JTextArea txtSaveFolder) {
        downloadFolder = txtSaveFolder.getText();
        File dir = new File(downloadFolder);
        if (!dir.exists()) {
            downloadFolder = defaultPathString();
        }
        StartTask startTask = new StartTask(downloadFolder);
        startTask.run();
    }

    /*
     * private void downloadFileByDayOfWeek(String textIntervalPeriod, JTextArea
     * txtSaveFolder, Calendar calEnd, Calendar calStart) {
     *
     * //long timePeriod = TimeUnit.MILLISECONDS.convert(period, //
     * TimeUnit.MINUTES); // add and validate Download folder downloadFolder =
     * txtSaveFolder.getText(); File dir = new File(downloadFolder); if
     * (!dir.exists()) { downloadFolder = defaultPathString(); }
     *
     * System.out.println(calStart.getTime());
     * System.out.println(calEnd.getTime());
     *
     * StartTask startTask = new StartTask(downloadFolder); Timer timer = new
     * Timer(); timer.schedule(new TimerTask() {
     *
     * @Override public void run() { startTask.run(); } }, calStart.getTime(),
     * timePeriod); //timer.schedule(calStart.getTime(), calEnd.getTime(),
     * timePeriod); }
     */

    private void saveRecentEstablishTabStatus(Boolean oneTime, Boolean daily,
                                              Boolean weekly, Boolean monthly, String startDateStr2,
                                              String startTimeStr2, String endDateStr2, String endTimeStr2,
                                              Integer intevalPeriod, Integer recur, Boolean sundayStatus,
                                              Boolean mondayStatus, Boolean tuesdayStatus,
                                              Boolean wednesdayStatus, Boolean thurdayStatus,
                                              Boolean fridayStatus, Boolean saturStatus)
            throws ClassNotFoundException, SQLException {
        QueryJobToImportRecentStatus queryImportStatus = new QueryJobToImportRecentStatus(
                oneTime, daily, weekly, monthly, startDateStr2, startTimeStr2,
                endDateStr2, endTimeStr2, intevalPeriod, recur, sundayStatus,
                mondayStatus, tuesdayStatus, wednesdayStatus, thurdayStatus,
                fridayStatus, saturStatus);
        queryImportStatus.createQueryJobToImportStatus();
    }

    private ArrayList<String> loadFileAndReturnElement() throws Exception {
        // We need to provide file path as the parameter:
        // double backquote is to avoid compiler interpret words
        // like \test as \t (ie. as a escape sequence)
        ArrayList<String> loadingStatus = new ArrayList<String>();
        File file = new File("SaveEstablishLastStatus/saveEstablish.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {
            String s1 = st;
            String[] words = s1.split("@");
            for (String w : words) {
                loadingStatus.add(w);
            }
        }
        return loadingStatus;
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

    private List<LocalDate> getDateTimeFromDayOfWeek(Calendar cStart,
                                                     Calendar cEnd, String dayOfWeekInput, int hourInput, int minuteInput) {
        List<LocalDate> lcal = new ArrayList<LocalDate>();

        int count = 0;
        int dayOfWeek = 0;
        // DayOfWeek.valueOf(dayOfWeekInput).getValue() + 1;
        // int dayOfWeek = Calendar.SUNDAY;
        switch (dayOfWeekInput) {
            case "SUNDAY":
                dayOfWeek = 1;
                break;
            case "MONDAY":
                dayOfWeek = 2;
                break;
            case "TUESDAY":
                dayOfWeek = 3;
                break;
            case "WEDNESDAY":
                dayOfWeek = 4;
                break;
            case "THURSDAY":
                dayOfWeek = 5;
                break;
            case "FRIDAY":
                dayOfWeek = 6;
                break;
            case "SATURDAY":
                dayOfWeek = 7;
                break;
        }
        while (cEnd.compareTo(cStart) > 0) {
            if (cStart.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
                count = count + 1;

                int hour = hourInput; // 10 AM
                int minute = minuteInput;

                Calendar cal = cStart;
                /*
                 * Calendar.getInstance(); // Today, now if
                 * (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
                 * cal.add(Calendar.DAY_OF_MONTH, (dayOfWeek + 7 -
                 * cal.get(Calendar.DAY_OF_WEEK)) % 7); } else { int minOfDay =
                 * cal.get(Calendar.HOUR_OF_DAY) * 60 +
                 * cal.get(Calendar.MINUTE); if (minOfDay >= hour * 60 + minute)
                 * cal.add(Calendar.DAY_OF_MONTH, 7); // Bump to next week }
                 */
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                LocalDate lcd = LocalDateTime.ofInstant(cal.toInstant(),
                        cal.getTimeZone().toZoneId()).toLocalDate();
                lcal.add(lcd);
                System.out.println(cal.getTime());
            }
            cStart.add(Calendar.DATE, 1);
        }

        /*
         * System.out.println(cal.getTime()); TimeZone tz = cal.getTimeZone();
         * ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
         *
         * LocalDateTime dateTimeOutPut =
         * LocalDateTime.ofInstant(cal.toInstant(), zid);
         */
        return lcal;
    }

}