package com.tpbank.util;

import com.tpbank.ui.BotUI;

import java.awt.GridBagConstraints;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;


import javax.swing.JCheckBox;

public class CreatingGuiJob {
	public static void cBDayOfWeekStatus(JCheckBox cbox, boolean cbStatus) {
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

		String dateStr = BotUI.dateTimePickerStart.getDatePicker()
				.getDate().getDayOfWeek().toString();
		if (cbStatus == true) {
			Calendar calEnd = functionFactory.calendarFromPickers(
					BotUI.dateTimePickerStop.getDatePicker(),
					BotUI.dateTimePickerStop.getTimePicker());
			// Calendar calStart = Calendar.getInstance();
			Calendar calStart = functionFactory.calendarFromPickers(
					BotUI.dateTimePickerStart.getDatePicker(),
					BotUI.dateTimePickerStart.getTimePicker());
			if (dayOfWeek.equals(dateStr)) {
				BotUI.dateStartArr.add(BotUI.dateTimePickerStart
						.getDatePicker().getDate());
			} else {
				List<LocalDate> lst = functionFactory.getDateTimeFromDayOfWeek(calStart,
						calEnd, dayOfWeek, BotUI.dateTimePickerStart
								.getTimePicker().getTime().getHour(),
						BotUI.dateTimePickerStart.getTimePicker()
								.getTime().getMinute());
				for (int i = 0; i < lst.size(); i++) {
					System.out.println(lst.get(i));
					BotUI.dateStartArr.add(lst.get(i));
				}
			}
		}
	}

	public static void recurPanelPostion(GridBagConstraints recur, int i, int i2) {
		recur.ipady = i;
		recur.ipadx = i2;
	}

	public static void settingPanelWidthAndHeight(GridBagConstraints setting,
			int i, int i2) {
		setting.gridwidth = i;
		setting.gridheight = i2;
	}

	public static void taskControlPanelPosition(
			GridBagConstraints cTaskControl, int i, int i2) {
		cTaskControl.ipadx = i; // reset to default
		cTaskControl.ipady = i2; // reset to default
	}

	public static void startTimePanelPosition(GridBagConstraints cStartTime,
			int i, int i2) {
		cStartTime.gridy = i;
		cStartTime.gridx = i2;
	}

	public static void positionInPanel(GridBagConstraints cIntervalSimple,
			int i, int i2) {
		cIntervalSimple.gridx = i;
		cIntervalSimple.gridy = i2;
	}


}
