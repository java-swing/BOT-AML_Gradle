package com.tpb.bot.constant;

import java.util.prefs.Preferences;

public interface Constant {
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_DDMMYYYY = "dd-MM-yyyy";

	public static final String FONT = "font\\vuTimes.ttf";
	public static final int FONT_TITLE_SIZE = 14;
	public static final int FONT_TITLE_TABLE_SIZE = 12;
	public static final int FONT_CONTENT_TABLE_SIZE = 11;
	public static final String TIME_PAUSE_DAILY = "17:30";
	public static Integer FONT_SIZE_UI = 13;
	public static final String SAVE_ESTABLISH_PATH = "SaveEstablishLastStatus/saveEstablish.txt";
	public static final String PATH_LOADING_LOG = "log/downloadfile.txt";
	public static final String SAVE_REPORT = "D:/PdfBox/SaveReportBOT_AML/";
	public static final String DRIVER_FILE_PATH = "driver/chromedriver";
	
	public static final int TIME_SLEEP_IN_JOB = 4000;

	public static String defaultPathString() {
		Preferences pref = Preferences.userRoot();
		return pref.get("DEFAULT_PATH", "");
	}
}
