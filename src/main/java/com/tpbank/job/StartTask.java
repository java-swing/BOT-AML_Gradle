package com.tpbank.job;

import com.tpbank.util.FileUtil;

import java.util.TimerTask;



public class StartTask extends TimerTask {
	//private String downloadFolder;
//	private static WebDriver driver = null;

	
	public StartTask() {
		super();
	}
//
//	public StartTask(String downloadFolder) {
//		this.downloadFolder = downloadFolder;
//	}

	public void run() {
		FileUtil.runBot();
	}
}