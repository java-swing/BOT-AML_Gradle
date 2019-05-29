package com.tpbank.control;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tpbank.botaml.TestInput;

import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

@SuppressWarnings("unused")
// TODO: this class should named Task instead of StartTask
public class StartTask extends TimerTask {
    private String downloadFolder;
    private static WebDriver driver = null;


    public StartTask() {
        super();
    }

    public StartTask(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    // TODO: option download folder
//	private ChromeOptions configChrome() {
//		System.setProperty("webdriver.chrome.driver",
//				"drives/chromedriver72.0.3626.69.exe");
//		String downloadFilepath = downloadFolder;
//		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//		chromePrefs.put("profile.default_content_settings.popups", 0);
//		chromePrefs.put("download.default_directory", downloadFilepath);
//		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("prefs", chromePrefs);
//		return options;
//	}

    @Override
    public void run() {
        try {
            TestInput.Amain();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}