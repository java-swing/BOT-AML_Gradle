package com.tpbank.constant;

import java.util.prefs.Preferences;

public interface Constant {
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_DDMMYYYY = "dd-MM-yyyy";
	
	public static final String FONT = "font\\vuTimes.ttf";
	public static final int fontTitleSize = 14;
	public static final int fontTitleTableSize = 12;
	public static final int fontContentTableSize = 11;
	public static final String timePauseDaily = "17:30";
	public static Integer fontSizeUI = 13;
	public static final String saveEstablishStatus = "SaveEstablishLastStatus/saveEstablish.txt";
	public static final String saveReport = "D:/LoiPD/SaveReportBOT_AML/";
	

	public static String defaultPathString() {
		Preferences pref = Preferences.userRoot();
		return pref.get("DEFAULT_PATH", "");
	}
}
