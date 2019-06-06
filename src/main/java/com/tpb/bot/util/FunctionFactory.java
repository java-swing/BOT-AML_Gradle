package com.tpb.bot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.tpb.bot.constant.Constant;
import com.tpb.bot.job.ScreeningJob;
import com.tpb.bot.job.ScreeningJob.StartTask;
import com.tpb.bot.ui.BotUI;

public class FunctionFactory {
	public static String printLogOut = "";
	public static BufferedReader br;

	public static void noteStatusAction(JButton btStart, JButton btStop,
			JLabel noteStatus, LocalDate dateMaking) throws Exception {
		String formattedDate = BotUI.today.format(DateTimeFormatter
				.ofPattern("dd-MM-yyyy"));
		if (dateMaking.isBefore(LocalDate.now())) {
			noteStatus.setVisible(true);
			noteStatus
					.setText("*Chú ý: Ngày kết thúc được nạp đang trước ngày hôm nay: "
							+ "\" " + formattedDate + " \"");
			btStart.setEnabled(false);
			btStop.setEnabled(false);
		} 
		else {
			noteStatus.setVisible(false);
			btStart.setEnabled(true);
			btStop.setEnabled(true);
		}
	}

	public static void loadingRecentStatus(JCheckBox cbMonday,
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
		setStatusAndVisibleOfRadioButtonFrequency(r1, r1Status);

		setStatusAndVisibleOfRadioButtonFrequency(r2, r2Status);

		textIntervalPeriod.setValue(Integer.parseInt(textIntervalPeriodValue));

		if (r1.isSelected()) {
			BotUI.period = 1;
			textIntervalPeriod.setEnabled(false);
			System.out.println(BotUI.period);
		} else if (r2.isSelected()) {
			if (textIntervalPeriod.getValue().toString().equals("0")) {
				BotUI.period = 24 * 60;
			} else {
				BotUI.period = Integer.parseInt(textIntervalPeriodValue);
			}
		}

		dateTimeStart.datePicker.setDate(dateStart);
		dateTimeStart.timePicker.setText(timeStart);
		dateTimePickerStop.datePicker.setDate(dateStop);
		dateTimePickerStop.timePicker.setText(timeStop);

		textSaveFolder.setText(savePath);

		setStatusAndVisibleCBDayOfWeek(cbSunday, cbSundayStatus);
		setStatusAndVisibleCBDayOfWeek(cbMonday, cbMondayStatus);
		setStatusAndVisibleCBDayOfWeek(cbTuesday, cbTuesdayStatus);
		setStatusAndVisibleCBDayOfWeek(cbWednesday, cbWednesdayStatus);
		setStatusAndVisibleCBDayOfWeek(cbThursday, cbThursdayStatus);
		setStatusAndVisibleCBDayOfWeek(cbFriday, cbFridayStatus);
		setStatusAndVisibleCBDayOfWeek(cbSaturday, cbSaturdayStatus);
	}

	static void setStatusAndVisibleOfRadioButtonFrequency(JRadioButton r,
			Boolean rStatus) {
		r.setSelected(rStatus);
		r.setEnabled(rStatus);
	}

	static void setStatusAndVisibleCBDayOfWeek(JCheckBox cbDayOfWeek,
			Boolean cbDayOfWeekStatus) {
		cbDayOfWeek.setSelected(cbDayOfWeekStatus);
		cbDayOfWeek.setEnabled(cbDayOfWeekStatus);
	}

	@SuppressWarnings("deprecation")
	public static Calendar calendarFromPickers(DatePicker datePicker,
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

	private static Time sqlTimeFrom(TimePicker timePicker) {
		LocalTime LcTimeStart = timePicker.getTime();
		return Time.valueOf(LcTimeStart);
	}

	private static Date sqlDateFrom(DatePicker datePicker) {
		LocalDate localDate = datePicker.getDate();
		return java.sql.Date.valueOf(localDate);
	}

	public static long getDiffWithTodayByDay(DateTimePicker dateTimeStart) {
		final LocalDate today = LocalDate.now();
		long getDiffWithTodayByDay = today.toEpochDay()
				- dateTimeStart.getDatePicker().getDate().toEpochDay();
		return getDiffWithTodayByDay;
	}

	public static long getDiffWithTodayByDay(DatePicker dateStart) {
		final LocalDate today = LocalDate.now();
		long getDiffWithTodayByDay = today.toEpochDay()
				- dateStart.getDate().toEpochDay();
		return getDiffWithTodayByDay;
	}

	public static void checkDateStop(JTextField textFieldRecur,
			JCheckBox cbMonday, JCheckBox cbSunday, JCheckBox cbTuesday,
			JCheckBox cbWednesday, JCheckBox cbThursday, JCheckBox cbFriday,
			JCheckBox cbSaturday, JRadioButton r2,
			DateTimePicker dateTimePickerStart,
			DateTimePicker dateTimePickerStop) {
		if (dateTimePickerStart.getDatePicker().getDate() != null) {
			if (dateTimePickerStop.getDatePicker().getDate() != null) {
				LocalDate dateStart = dateTimePickerStart.getDatePicker()
						.getDate();
				LocalDate dateStop = dateTimePickerStop.getDatePicker()
						.getDate();

				if (dateStop.compareTo(dateStart.plusWeeks(1)) < 0) {
					setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
							cbWednesday, cbThursday, cbFriday, cbSaturday,
							false);
					if (dateStop.compareTo(dateStart.plusDays(1)) < 0) {
						r2.setEnabled(true);
					}
					if (dateStop.compareTo(dateStart.plusDays(1)) > 0) {
						r2.setEnabled(true);
					}
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

					setCBDayOfWeekStatus(cbMonday, cbSunday, cbTuesday,
							cbWednesday, cbThursday, cbFriday, cbSaturday, true);
					textFieldRecur.setEnabled(true);
				}

			}
		}
	}

	public static void setRadioButtonStatus(JRadioButton r1, JRadioButton r2,
			boolean status) {
		r1.setEnabled(status);
		r2.setEnabled(status);
	}

	public static void setCBDayOfWeekStatus(JCheckBox cbMonday,
			JCheckBox cbSunday, JCheckBox cbTuesday, JCheckBox cbWednesday,
			JCheckBox cbThursday, JCheckBox cbFriday, JCheckBox cbSaturday,
			boolean status) {
		cbSunday.setEnabled(status);
		cbMonday.setEnabled(status);
		cbTuesday.setEnabled(status);
		cbWednesday.setEnabled(status);
		cbThursday.setEnabled(status);
		cbFriday.setEnabled(status);
		cbSaturday.setEnabled(status);
	}

	public static void actionBtStart(String textIntervalPeriod,
			DateTimePicker dateTimePickerStart,
			DateTimePicker dateTimePickerStop, JTextArea txtSaveFolder,
			ArrayList<LocalDate> dateStartArr) {
		Calendar calEnd = calendarFromPickers(
				dateTimePickerStop.getDatePicker(),
				dateTimePickerStop.getTimePicker());

		Calendar calStart = calendarFromPickers(
				dateTimePickerStart.getDatePicker(),
				dateTimePickerStart.getTimePicker());
		// RUN BY DAY OF WEEK
		if (dateStartArr.size() > 0) {
			for (int i = 0; i <= dateStartArr.size(); i++) {
				calStart.set(dateStartArr.get(i).getYear(), dateStartArr.get(i)
						.getMonthValue() - 1, dateStartArr.get(i)
						.getDayOfMonth());
				System.out.println(calStart.getTime().toString());

				if (!textIntervalPeriod.equals("0")) {
					if (!textIntervalPeriod.equals("-1")) {
						BotUI.period = Integer.parseInt(textIntervalPeriod);
					} else {
						BotUI.period = 1;
					}
				}
				Timer timerRunDayOfWeek = new Timer();
				timerRunDayOfWeek.schedule(new TimerTask() {

					@Override
					public void run() {
						downloadFile(txtSaveFolder);
						Date dateNow = new Date();
						if (dateNow.compareTo(calEnd.getTime()) > 0) {
							System.out.println("Stop!");
							timerRunDayOfWeek.cancel();
						}

					}
				}, calStart.getTime(), TimeUnit.MILLISECONDS.convert(
						BotUI.period, TimeUnit.MINUTES));

			}
		} else {
			// RUN DAILY 8:00 - 17:30
			if (!textIntervalPeriod.equals("0")) {
				if (!textIntervalPeriod.equals("-1")) {
					BotUI.period = Integer.parseInt(textIntervalPeriod);
				} else {
					BotUI.period = 1;
				}
			}
			Timer timerDailyJob = new Timer();
			timerDailyJob.schedule(new TimerTask() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					System.out.println("Running!");
					try {
						downloadFile(txtSaveFolder);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						ScreeningJob.displayAndWriteLogError(e1);
					}
					try {
						Date dateNow = new Date();
						dateNow.setSeconds(00);
						Date datePause = new Date();
						datePause.setHours(17);
						datePause.setMinutes(30);
						datePause.setSeconds(00);

						if (dateNow.compareTo(calEnd.getTime()) > 0) {
							System.out.println("Stop!");
							timerDailyJob.cancel();
						}

						if (dateNow.compareTo(datePause) >= 0) {
							System.out.println("Sleeping!");

							try {
								Date dateNow2 = new Date();
								dateNow2.setHours(8);
								dateNow2.setMinutes(00);
								StringBuilder stringBuilder = new StringBuilder();
								stringBuilder.append(dateNow2.getHours());
								stringBuilder.append(":");
								stringBuilder.append(dateNow2.getMinutes());
								String time1 = stringBuilder.toString();
								String time2 = Constant.TIME_PAUSE_DAILY;
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
								ScreeningJob.displayAndWriteLogError(e);
							}

							System.out.println("Wake up!");
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ScreeningJob.displayAndWriteLogError(e);
					}
				}
			}, calStart.getTime(), TimeUnit.MILLISECONDS.convert(BotUI.period,
					TimeUnit.MINUTES));
			System.out.println("BotUI.period: "
					+ TimeUnit.MILLISECONDS.convert(BotUI.period,
							TimeUnit.MINUTES));
		}

		loadMonitor(printLogOut);
	}

	public static void loadMonitor(String logs) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BotUI.display.append(logs);

			}
		});
	}

	public static String downloadFolder = Constant.defaultPathString();

	private static void downloadFile(JTextArea txtSaveFolder) {
		try {
			downloadFolder = txtSaveFolder.getText();
			File dir = new File(BotUI.downloadFolder);
			if (!dir.exists()) {
				BotUI.downloadFolder = Constant.defaultPathString();
			}
			// StartTask startTask = new StartTask(BotUI.downloadFolder);
			StartTask startTask = new StartTask();
			startTask.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}
	}

	public static ArrayList<String> loadFileAndReturnElement() throws Exception {
		ArrayList<String> loadingStatus = new ArrayList<String>();
		File file = new File(Constant.SAVE_ESTABLISH_PATH);

		br = new BufferedReader(new FileReader(file));
		String st = "";
		while ((st = br.readLine()) != null) {
			String s1 = st;
			String[] words = s1.split("@");
			for (String w : words) {
				loadingStatus.add(w);
			}
		}
		return loadingStatus;
	}
	
	public static ArrayList<String> loadFileLog() throws Exception {
		ArrayList<String> loadingStatus = new ArrayList<String>();
		File file = new File(Constant.PATH_LOADING_LOG);

		br = new BufferedReader(new FileReader(file));
		String st = "";
		while ((st = br.readLine()) != null) {
			String s1 = st;
			String[] paragraph = s1.split("\n");
			for (String line : paragraph) {
				loadingStatus.add(line);
			}
		}
		return loadingStatus;
	}
	

	public static List<LocalDate> getDateTimeFromDayOfWeek(Calendar cStart,
			Calendar cEnd, String dayOfWeekInput, int hourInput, int minuteInput) {
		List<LocalDate> lcal = new ArrayList<LocalDate>();

		int count = 0;
		int dayOfWeek = 0;

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
		return lcal;
	}

	public static void actionSaveConfigButton(JCheckBox cbMonday,
			JCheckBox cbSunday, JCheckBox cbTuesday, JCheckBox cbWednesday,
			JCheckBox cbThursday, JCheckBox cbFriday, JCheckBox cbSaturday,
			JSpinner textIntervalPeriod, JRadioButton r1, JRadioButton r2,
			JTextArea textSaveFolder, String startDateStr, String startTimeStr,
			String endDateStr, String endTimeStr) {
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
		BotUI.endDateStr = BotUI.dateTimePickerStop.getDatePicker().getDate()
				.toString();
		BotUI.endTimeStr = BotUI.dateTimePickerStop.getTimePicker().getTime()
				.toString();

		String saveString = BotUI.jrdOneTime + "@" + BotUI.jrdDaily + "@"
				+ BotUI.startDateStr + "@" + BotUI.startTimeStr + "@"
				+ BotUI.endDateStr + "@" + BotUI.endTimeStr + "@"
				+ BotUI.saveFile + "@" + BotUI.intevalPeriod + "@"
				+ BotUI.jcbSundayStatus + "@" + BotUI.jcbMondayStatus + "@"
				+ BotUI.jcbTuesdayStatus + "@" + BotUI.jcbWednesdayStatus + "@"
				+ BotUI.jcbThurdayStatus + "@" + BotUI.jcbFridayStatus + "@"
				+ BotUI.jcbSaturStatus;
		LogFile.saveTextEstablish(saveString);
		try {
			FunctionFactory.loadFileAndReturnElement();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}
	}

}
