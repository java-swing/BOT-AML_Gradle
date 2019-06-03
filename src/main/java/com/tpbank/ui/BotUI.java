package com.tpbank.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.tpbank.constant.Constant;
import com.tpbank.job.ScreeningJob;
import com.tpbank.util.*;
import org.apache.log4j.Logger;



import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;


public class BotUI extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	public static final Logger logger = Logger.getLogger(BotUI.class);

	private static final SimpleDateFormat dtf = new SimpleDateFormat(
			Constant.DATE_TIME_FORMAT);

	private static final SimpleDateFormat df = new SimpleDateFormat(
			Constant.DATE_FORMAT_DDMMYYYY);

	 public static String downloadFolder =	 functionFactory.defaultPathString();

	public static DateTimePicker dateTimePickerStart = new DateTimePicker();
	public static DateTimePicker dateTimePickerStop = new DateTimePicker();
	public static ArrayList<LocalDate> dateStartArr = new ArrayList<LocalDate>();
	public static DatePicker jdStartDate;
	public static DatePicker jdEndDate;
	public static int period = 0;
	public static final LocalDate today = LocalDate.now();

	public static boolean btViewData = false;
	public static boolean jrdOneTime;
	public static boolean jrdDaily;
	public static int intevalPeriod;
	public static String saveFile;
	public static boolean jcbSundayStatus;
	public static boolean jcbMondayStatus;
	public static boolean jcbTuesdayStatus;
	public static boolean jcbWednesdayStatus;
	public static boolean jcbThurdayStatus;
	public static boolean jcbFridayStatus;
	public static boolean jcbSaturStatus;
	public static String startDateStr;
	public static String startTimeStr;
	public static String endDateStr;
	public static String endTimeStr;
	public static String fontName = "TimesRoman";
	public static Integer fontSize = 13;
	public static JTextArea display;

	public static String printLogOut = "";
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// QueryJobToExportLog queryJob = new QueryJobToExportLog();

	private TableColumnModel columns;

	private TableModel dataFile;

	private JTable table;
	DefaultTableModel model;
	JScrollPane scrollPane;
	Timer timer2;

	@Override
	public void update(Observable o, Object data) {
		// StringBuffer sb = new StringBuffer(area.getText());
		// sb.append(sdf.format(new Date()) + ": " + (String) data + "\r\n");
		// area.setText(sb.toString());
	}

	public BotUI() throws Exception {
		createGUI();
		setDisplay();
	}

	private void setDisplay() {
		setTitle("BOT - AML");
		pack();
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createGUI() throws Exception {
		getContentPane().add(createTabbedPane());
	}

	private JTabbedPane createTabbedPane() throws Exception {
		JTabbedPane tablePane = new JTabbedPane();
		tablePane.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height / 2));
		tablePane.setBackground(Color.white);

		JPanel panelMonitor = createPanelMonitor();
		panelMonitor.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height / 2));
		panelMonitor.setBackground(Color.white);

		JPanel panelResult = createPanelReport();
		panelResult.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height / 2));
		panelResult.setBackground(Color.white);

		JPanel panelEstablish = createPanelConfig();
		panelEstablish.setPreferredSize(new Dimension(screenSize.width / 2,
				screenSize.height / 2));
		panelEstablish.setBackground(Color.white);

		tablePane.addTab("Monitor", null, panelMonitor);
		tablePane.addTab("Báo cáo", null, panelResult);
		tablePane.addTab("Thiết lập", null, panelEstablish);
		tablePane.setSelectedIndex(0);

		return tablePane;
	}

	private JPanel createPanelMonitor() {

		JPanel panelMonitor = new JPanel();
		GridBagLayout layout = new GridBagLayout();

		panelMonitor.setLayout(layout);
		display = new JTextArea(16, 58);
		display.setEditable(false);

		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelMonitor.add(scroll);

		return panelMonitor;
	}

	private JPanel createPanelReport() throws ClassNotFoundException,
			SQLException {
		JPanel panelResult = new JPanel();
		panelResult.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		Vector<String> cols = new Vector<String>();
		cols.addElement("TT");
		cols.addElement("Loại");
		cols.addElement("Thời gian thực hiện");
		cols.addElement("Tên khách hàng");
		cols.addElement("Trạng thái");
		cols.addElement("Đường dẫn");
		cols.addElement("Ghi chú");
		model = new DefaultTableModel(null, cols);
		model.fireTableDataChanged();

		table = new JTable(model);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);

		// STT
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		// Kind
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		// Time creating
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		// Custom name
		table.getColumnModel().getColumn(3).setPreferredWidth(70);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		// Status
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		// Save folder path
		table.getColumnModel().getColumn(5).setPreferredWidth(200);
		table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
		// Note
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		scrollPane = new JScrollPane(table);

		JLabel fromDate = new JLabel("Từ ngày: ");
		JLabel toDate = new JLabel("Đến ngày: ");

		DatePickerSettings dateSettingsEndDatePicker = new DatePickerSettings();
		DatePicker jdStartDateRs = new DatePicker();
		DatePicker jdEndDateRs = new DatePicker(dateSettingsEndDatePicker);
		jdEndDateRs.setEnabled(false);

		jdStartDateRs.addDateChangeListener(new DateChangeListener() {

			@Override
			public void dateChanged(DateChangeEvent arg0) {
				dateSettingsEndDatePicker.setDateRangeLimits(today
						.minusDays(functionFactory
								.getDiffWithTodayByDay(jdStartDateRs)), today
						.plusDays(3000));
				jdEndDateRs.setEnabled(true);

			}
		});

		JButton btView = new JButton("Hiển thị báo cáo");

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

		JButton btExport = new JButton("Xuất ra báo cáo");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		btExport.addActionListener(e -> {
			String nameFile = ("Báo cáo từ ngày "
					+ jdStartDateRs.getDate().format(dtf) + " đến ngày " + jdEndDateRs
					.getDate().format(dtf));
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

				WriteLogToTable createTableLog = new WriteLogToTable(
						log, nameFile, jdStartDateRs, jdEndDateRs);
				createTableLog.drawTablePDF(log, nameFile);

			} catch (Exception ex) {
				ex.printStackTrace();
				ScreeningJob.displayAndWriteLogError(ex);
			}

		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 200; // make this component tall
		c.ipadx = 100; // make this component tall
		c.weightx = 0.0;
		c.gridwidth = 6; // 5 columns wide
		CreatingGuiJob.positionInPanel(c, 0, 0);
		panelResult.add(scrollPane, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(c, 0, 1);
		c.gridwidth = 1;
		CreatingGuiJob.recurPanelPostion(c, 0, 0);
		c.insets = new Insets(10, 60, 0, 0);
		panelResult.add(fromDate, c);

		c.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(c, 1, 1);
		c.gridwidth = 1;
		c.insets = new Insets(10, -180, 0, 0);
		panelResult.add(jdStartDateRs, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(c, 2, 1);
		c.gridwidth = 1;
		c.insets = new Insets(10, 50, 0, 0);
		panelResult.add(toDate, c);

		c.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(c, 3, 1);
		c.gridwidth = 1;
		c.weightx = 30;
		c.insets = new Insets(10, -188, 0, 80);
		panelResult.add(jdEndDateRs, c);

		c.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(c, 0, 3);
		c.gridwidth = 1;
		c.insets = new Insets(10, 95, 0, 0);
		panelResult.add(btExport, c);

		c.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(c, 2, 3);
		c.gridwidth = 1;
		c.insets = new Insets(10, 90, 0, 0);
		panelResult.add(btView, c);
		return panelResult;
	}

	private JPanel createPanelConfig() throws Exception {
		// SingleTaskTimer timer = new SingleTaskTimer();

		JPanel panelConfig = new JPanel();
		GridBagConstraints configGrid = new GridBagConstraints();
		GridBagLayout layoutConfig = new GridBagLayout();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		panelConfig
				.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);

		panelConfig.setLayout(layoutConfig);
		Border blacklineEstablishPanel = BorderFactory.createTitledBorder("");
		panelConfig.setBorder(blacklineEstablishPanel);

		panelConfig.setBackground(Color.white);

		JPanel panelRecur = new JPanel();
		JTextField textFieldRecur = new JTextField();

		JCheckBox cbMonday = new JCheckBox(DayOfWeek.MONDAY.toString());

		JCheckBox cbSunday = new JCheckBox(DayOfWeek.SUNDAY.toString());

		JCheckBox cbTuesday = new JCheckBox(DayOfWeek.TUESDAY.toString());

		JCheckBox cbWednesday = new JCheckBox(DayOfWeek.WEDNESDAY.toString());

		JCheckBox cbThursday = new JCheckBox(DayOfWeek.THURSDAY.toString());

		JCheckBox cbFriday = new JCheckBox(DayOfWeek.FRIDAY.toString());

		JCheckBox cbSaturday = new JCheckBox(DayOfWeek.SATURDAY.toString());

		// JTextArea textIntervalPeriod = new JTextArea(1, 5);
		JSpinner textIntervalPeriod = new JSpinner(new SpinnerNumberModel(0, 0,
				100, 1));
		textIntervalPeriod.setBorder(null);
		textFieldRecur.setEnabled(false);
		textIntervalPeriod.setEnabled(false);
		functionFactory.setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
				cbWednesday, cbThursday, cbFriday, cbSaturday, false);

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

		ButtonGroup buttonGroupRadio = new ButtonGroup();
		buttonGroupRadio.add(r1);
		r1.setText("Một lần");
		r1.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
		r1.setEnabled(false);
		r1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				textFieldRecur.setEnabled(false);
				functionFactory.setCBDayOfWeekStatus(cbMonday, cbSunday,
						cbTuesday, cbWednesday, cbThursday, cbFriday,
						cbSaturday, false);
				textIntervalPeriod.setEnabled(false);
				dateTimePickerStop.setEnabled(false);
				period = 1;
				dateTimePickerStop.setEnabled(false);
				btStart.setEnabled(true);
				btStop.setEnabled(true);

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
				functionFactory.setCBDayOfWeekStatus(cbMonday, cbSunday,
						cbTuesday, cbWednesday, cbThursday, cbFriday,
						cbSaturday, true);
				dateTimePickerStop.setEnabled(true);
				textIntervalPeriod.setEnabled(true);
				period = 24 * 60;
			}
		});

		// ======== panelIntervalSimple=========================
		// RadioButton "One time"
		cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(cIntervalSimple, 0, 0);
		cIntervalSimple.anchor = GridBagConstraints.FIRST_LINE_START;
		cIntervalSimple.insets = new Insets(10, 5, 5, 0);
		panelIntervalSimple.add(r1, cIntervalSimple);

		// RadioButton "Daily"
		cIntervalSimple.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(cIntervalSimple, 0, 1);
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
		CreatingGuiJob.taskControlPanelPosition(cStartTime, 0, 0);
		CreatingGuiJob.positionInPanel(cStartTime, 0, 0);
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
									today.minusDays(functionFactory
											.getDiffWithTodayByDay(dateTimePickerStart)),
									today.plusDays(3000));
							textFieldRecur.setEnabled(false);
							functionFactory.setRadioButtonStatus(r1, r2, true);
							textIntervalPeriod.setEnabled(true);
						} else {
							textFieldRecur.setEnabled(false);
							functionFactory.setCBDayOfWeekStatus(cbMonday,
									cbSunday, cbTuesday, cbWednesday,
									cbThursday, cbFriday, cbSaturday, false);
							functionFactory.setRadioButtonStatus(r1, r2, true);
							textIntervalPeriod.setEnabled(false);
						}

					}
				});

		cStartTime.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.taskControlPanelPosition(cStartTime, 0, 0);
		CreatingGuiJob.startTimePanelPosition(cStartTime, 0, 1);
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
		CreatingGuiJob.taskControlPanelPosition(cStartTime, 0, 0);
		CreatingGuiJob.positionInPanel(cStartTime, 0, 1);
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
						functionFactory.checkDateStop(textFieldRecur, cbMonday,
								cbSunday, cbTuesday, cbWednesday, cbThursday,
								cbFriday, cbSaturday, r2, dateTimePickerStart,
								dateTimePickerStop);
						noteStatus.setVisible(false);
						try {
							functionFactory.noteStatusAction(btStart, btStop,
									noteStatus, dateTimePickerStop
											.getDatePicker().getDate());
						} catch (Exception e) {
							e.printStackTrace();
							ScreeningJob.displayAndWriteLogError(e);
						}
					}
				});
		cStartTime.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.taskControlPanelPosition(cStartTime, 0, 0);
		CreatingGuiJob.startTimePanelPosition(cStartTime, 1, 1);
		cStartTime.ipadx = 80;
		cStartTime.anchor = GridBagConstraints.LINE_START;
		cStartTime.insets = new Insets(5, 10, 10, 10);
		panelStartTime.add(dateTimePickerStop, cStartTime);

		// Panel Interval
		JPanel panelInterval = new JPanel();
		GridBagConstraints cInterval = new GridBagConstraints();
		GridBagLayout layoutInterval = new GridBagLayout();
		panelInterval.setLayout(layoutInterval);

		JLabel intervalPeriod = new JLabel("Chu kỳ lặp lại: ");
		intervalPeriod.setFont(new Font(fontName, Font.CENTER_BASELINE,
				fontSize));
		cInterval.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.taskControlPanelPosition(cInterval, 0, 0);
		CreatingGuiJob.startTimePanelPosition(cInterval, 0, 0);
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
					functionFactory.setRadioButtonStatus(r1, r2, true);
				} else {
					functionFactory.setRadioButtonStatus(r1, r2, true);
				}
			}
		});

		cInterval.fill = GridBagConstraints.CENTER;
		textIntervalPeriod.setFont(new Font("TimesRoman", Font.CENTER_BASELINE,
				16));
		CreatingGuiJob.taskControlPanelPosition(cInterval, 0, 0);
		CreatingGuiJob.startTimePanelPosition(cInterval, 0, 1);
		cInterval.anchor = GridBagConstraints.LINE_START;
		cInterval.insets = new Insets(5, 10, 10, 10);
		panelInterval.add(textIntervalPeriod, cInterval);

		JLabel intervalUnitTime = new JLabel("phút");
		intervalUnitTime.setFont(new Font("TimesRoman", Font.CENTER_BASELINE,
				13));
		cInterval.fill = GridBagConstraints.CENTER;
		CreatingGuiJob.taskControlPanelPosition(cInterval, 0, 0);
		CreatingGuiJob.startTimePanelPosition(cInterval, 0, 2);
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
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 0);
		cRecur.anchor = GridBagConstraints.LINE_START;
		cRecur.insets = new Insets(5, 0, 0, 10);
		panelRecur.add(cbMonday, cRecur);
		cbMonday.setText("Thứ hai");
		cbMonday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
		cbMonday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cbMonday.isSelected()) {
					CreatingGuiJob.cBDayOfWeekStatus(cbMonday,
							cbMonday.isSelected());
				}
			}
		});

		// Checkbox Tuesday
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 1);
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
					CreatingGuiJob.cBDayOfWeekStatus(cbTuesday,
							cbTuesday.isSelected());
				}
			}
		});

		// Checkbox Wednesday
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 2);
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
					CreatingGuiJob.cBDayOfWeekStatus(cbWednesday,
							cbWednesday.isSelected());
				}
			}
		});

		// Checkbox Thursday
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 3);
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
					CreatingGuiJob.cBDayOfWeekStatus(cbThursday,
							cbThursday.isSelected());
				}
			}
		});

		// Checkbox Friday
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 4);
		cRecur.gridwidth = 1;
		CreatingGuiJob.recurPanelPostion(cRecur, 1, 4);
		cRecur.anchor = GridBagConstraints.LINE_START;
		cRecur.insets = new Insets(5, 0, 0, 10);
		cbFriday.setText("Thứ sáu");
		cbFriday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
		panelRecur.add(cbFriday, cRecur);
		cbFriday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cbFriday.isSelected()) {
					CreatingGuiJob.cBDayOfWeekStatus(cbFriday,
							cbFriday.isSelected());
				}
			}
		});

		// Checkbox Saturday
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 5);
		cRecur.anchor = GridBagConstraints.LINE_START;
		cRecur.insets = new Insets(5, 0, 0, 10);
		cbSaturday.setText("Thứ bảy");
		cbSaturday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
		panelRecur.add(cbSaturday, cRecur);
		cbSaturday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cbSaturday.isSelected()) {
					CreatingGuiJob.cBDayOfWeekStatus(cbSaturday,
							cbSaturday.isSelected());
				}
			}
		});

		// Checkbox Sunday
		cbSunday.setText("Chủ nhật");
		cbSunday.setFont(new Font(fontName, Font.CENTER_BASELINE, fontSize));
		cRecur.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.startTimePanelPosition(cRecur, 1, 6);
		cRecur.anchor = GridBagConstraints.LINE_START;
		cRecur.insets = new Insets(5, 0, 0, 10);
		panelRecur.add(cbSunday, cRecur);
		cbSunday.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (cbSunday.isSelected()) {
					CreatingGuiJob.cBDayOfWeekStatus(cbSunday,
							cbSunday.isSelected());
				}
			}
		});

		// ============ panel StartTime to add Panel Setting===================
		// add Panel Setting to panel StartTime
		cSetting.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(cSetting, 0, 0);
		CreatingGuiJob.settingPanelWidthAndHeight(cSetting, 1, 2);
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
		CreatingGuiJob.settingPanelWidthAndHeight(cSetting, 4, 1);
		cSetting.anchor = GridBagConstraints.LINE_START;
		cSetting.ipadx = 2;
		cSetting.gridwidth = 4;
		cSetting.insets = new Insets(-10, 150, -20, 0);
		panelSetting.add(panelStartTime, cSetting);

		// add panel StartTime to panel Setting
		cSetting.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.positionInPanel(cSetting, 1, 1);
		CreatingGuiJob.settingPanelWidthAndHeight(cSetting, 4, 1);
		cSetting.anchor = GridBagConstraints.LINE_START;
		cSetting.insets = new Insets(15, 150, 0, 0);
		panelSetting.add(panelInterval, cSetting);

		// add panel Recur to panel Setting
		cSetting.fill = GridBagConstraints.HORIZONTAL;
		cSetting.weightx = 1.0; // request any extra HORIZONTAL space
		CreatingGuiJob.positionInPanel(cSetting, 0, 2);
		CreatingGuiJob.settingPanelWidthAndHeight(cSetting, 0, 0);
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
		CreatingGuiJob.startTimePanelPosition(cSaveSetting, 0, 0);
		cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
		// space
		cSaveSetting.insets = new Insets(5, 5, 10, 10);
		panelSaveSetting.add(lbSaveFolder, cSaveSetting);

		// Label Save textField
		JTextArea textSaveFolder = new JTextArea(1, 40);
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
						downloadFolder = textSaveFolder.getText();
					}

					@Override
					public void changedUpdate(DocumentEvent arg0) {
						btStart.setEnabled(true);
						btStop.setEnabled(true);
						btSaveSetting.setEnabled(true);
						downloadFolder = textSaveFolder.getText();
					}
				});
		Border blacklineTxtSaveFolder = BorderFactory
				.createLineBorder(Color.LIGHT_GRAY);
		textSaveFolder.setBorder(blacklineTxtSaveFolder);
		textSaveFolder.setFont(new Font(fontName, Font.CENTER_BASELINE,
				fontSize));
		cSaveSetting.fill = GridBagConstraints.FIRST_LINE_START;
		CreatingGuiJob.startTimePanelPosition(cSaveSetting, 0, 1);
		cSaveSetting.weightx = 2.0;
		cSaveSetting.anchor = GridBagConstraints.LINE_START; // bottom of
		// space
		cSaveSetting.insets = new Insets(5, 10, 10, 10);
		panelSaveSetting.add(textSaveFolder, cSaveSetting);

		// if(today.now().compareTo0(dateTimePickerStop.datePicker.setDateToToday(););
		System.out
				.println("==================================Loading last status=========================================");

		File checkLoading = new File(Constant.saveEstablishStatus);
		if (checkLoading.exists()) {
			if (functionFactory.loadFileAndReturnElement().size() != 0
					&& functionFactory.loadFileAndReturnElement().size() == 15) {
				functionFactory.loadingRecentStatus(cbMonday, Boolean
						.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(9)), cbSunday,
						Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(8)), cbTuesday,
						Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(10)),
						cbWednesday, Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(11)),
						cbThursday, Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(12)), cbFriday,
						Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(13)),
						cbSaturday, Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(14)),
						textIntervalPeriod, functionFactory
								.loadFileAndReturnElement().get(7), r1, Boolean
								.parseBoolean(functionFactory
										.loadFileAndReturnElement().get(0)),
						r2, Boolean.parseBoolean(functionFactory
								.loadFileAndReturnElement().get(1)),
						dateTimePickerStart, LocalDate.parse((functionFactory
								.loadFileAndReturnElement().get(2))),
						functionFactory.loadFileAndReturnElement().get(3),
						dateTimePickerStop, LocalDate.parse((functionFactory
								.loadFileAndReturnElement().get(4))),
						functionFactory.loadFileAndReturnElement().get(5),
						textSaveFolder, functionFactory
								.loadFileAndReturnElement().get(6));

				functionFactory.noteStatusAction(btStart, btStop, noteStatus,
						LocalDate.parse((functionFactory
								.loadFileAndReturnElement().get(4))));
			} else {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
				functionFactory.loadingRecentStatus(cbMonday, false, cbSunday,
						false, cbTuesday, false, cbWednesday, false,
						cbThursday, false, cbFriday, false, cbSaturday, false,
						textIntervalPeriod, "0", r1, false, r2, false,
						dateTimePickerStart, LocalDate.now(), LocalTime.now()
								.format(dtf).toString(), dateTimePickerStop,
						LocalDate.now(),
						LocalTime.now().format(dtf).toString(), textSaveFolder,
						"");
			}
		} else {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
			functionFactory.loadingRecentStatus(cbMonday, false, cbSunday,
					false, cbTuesday, false, cbWednesday, false, cbThursday,
					false, cbFriday, false, cbSaturday, false,
					textIntervalPeriod, "0", r1, false, r2, false,
					dateTimePickerStart, LocalDate.now(), LocalTime.now()
							.format(dtf).toString(), dateTimePickerStop,
					LocalDate.now(), LocalTime.now().format(dtf).toString(),
					textSaveFolder, "");
		}

		btSaveSetting.addActionListener(e -> {
			BotUI.jrdOneTime = r1.isSelected();
			BotUI.jrdDaily = r2.isSelected();

			if (!textIntervalPeriod.getValue().equals(0)) {
				BotUI.intevalPeriod = Integer.parseInt(textIntervalPeriod
						.getValue().toString());
			} else {
				BotUI.intevalPeriod = 0;
			}

			BotUI.jcbSundayStatus = cbSunday.isSelected();
			BotUI.jcbMondayStatus = cbMonday.isSelected();
			BotUI.jcbTuesdayStatus = cbTuesday.isSelected();
			BotUI.jcbWednesdayStatus = cbWednesday.isSelected();
			BotUI.jcbThurdayStatus = cbThursday.isSelected();
			BotUI.jcbFridayStatus = cbFriday.isSelected();
			BotUI.jcbSaturStatus = cbSaturday.isSelected();
			BotUI.saveFile = textSaveFolder.getText();
			BotUI.startDateStr = BotUI.dateTimePickerStart.getDatePicker()
					.getDate().toString();
			BotUI.startTimeStr = BotUI.dateTimePickerStart.getTimePicker()
					.getTime().toString();
			BotUI.endDateStr = BotUI.dateTimePickerStop.getDatePicker()
					.getDate().toString();
			BotUI.endTimeStr = BotUI.dateTimePickerStop.getTimePicker()
					.getTime().toString();

			String saveString = BotUI.jrdOneTime + "@" + BotUI.jrdDaily + "@"
					+ BotUI.startDateStr + "@" + BotUI.startTimeStr + "@"
					+ BotUI.endDateStr + "@" + BotUI.endTimeStr + "@"
					+ BotUI.saveFile + "@" + BotUI.intevalPeriod + "@"
					+ BotUI.jcbSundayStatus + "@" + BotUI.jcbMondayStatus + "@"
					+ BotUI.jcbTuesdayStatus + "@" + BotUI.jcbWednesdayStatus
					+ "@" + BotUI.jcbThurdayStatus + "@"
					+ BotUI.jcbFridayStatus + "@" + BotUI.jcbSaturStatus;
			LogFile.saveTextEstablish(saveString);
			try {
				functionFactory.loadFileAndReturnElement();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				ScreeningJob.displayAndWriteLogError(e1);
			}
		});
		cTaskControl.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.taskControlPanelPosition(cTaskControl, 0, 0);
		// startTimePanelPosition(cTaskControl, 0, 0);
		cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
		// space
		cTaskControl.insets = new Insets(5, 0, 0, 10);
		panelTaskControl.add(btSaveSetting, cTaskControl);

		btStart.addActionListener(e -> {

			functionFactory.actionBtStart(textIntervalPeriod.getValue()
					.toString(), dateTimePickerStart, dateTimePickerStop,
					textSaveFolder, dateStartArr);

		});
		cTaskControl.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.taskControlPanelPosition(cTaskControl, 0, 1);
		// startTimePanelPosition(cTaskControl, 0, 0);
		cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
		// space
		cTaskControl.insets = new Insets(5, 00, 0, 10);
		panelTaskControl.add(btStart, cTaskControl);

		// Start Button

		btStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// timer.cancel();
				timer2.cancel();

			}
		});
		;
		cTaskControl.fill = GridBagConstraints.LINE_START;
		CreatingGuiJob.taskControlPanelPosition(cTaskControl, 0, 3);
		cTaskControl.anchor = GridBagConstraints.LINE_START; // bottom of
		// space
		cTaskControl.insets = new Insets(5, 00, 0, 10);
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
		configGrid.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(configGrid, 0, 0);
		CreatingGuiJob.taskControlPanelPosition(configGrid, 0, 0);
		configGrid.ipadx = 75;
		configGrid.ipady = 50;
		configGrid.weightx = 40;
		configGrid.insets = new Insets(-50, -20, 0, 0);
		panelConfig.add(panelSetting, configGrid);

		// Establish=======================
		// add Panel Advance Setting to panel
		configGrid.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(configGrid, 0, 1);
		CreatingGuiJob.taskControlPanelPosition(configGrid, 0, 0);
		configGrid.insets = new Insets(10, 0, 0, 0);
		panelConfig.add(panelSaveSetting, configGrid);

		// Establish=======================
		// add Panel Advance Setting to panel
		configGrid.fill = GridBagConstraints.HORIZONTAL;
		CreatingGuiJob.positionInPanel(configGrid, 0, 2);
		CreatingGuiJob.taskControlPanelPosition(configGrid, 0, 0);
		configGrid.insets = new Insets(15, 0, 0, 0);
		panelConfig.add(panelTaskControl, configGrid);

		return panelConfig;
	}

}
