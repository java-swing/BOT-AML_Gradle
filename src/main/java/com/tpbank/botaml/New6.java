package com.tpbank.botaml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tpbank.GUI.AML_BOTview;
import com.tpbank.botaml.util.BotUtil;



public class New6 {

	public static final Logger logger = Logger.getLogger(New6.class);

	public static WebDriver driver = null;
	public static String downloadFilepath = AML_BOTview.downloadFolder;
	
	public static Date date = new Date();
	public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static String dateString = df.format(date);

	public static void doProcess(String flowId, String key) throws InterruptedException {
		PropertyConfigurator.configure("res/log4j.properties");
		// Process
		JSONObject flow = BotUtil.getFlowById(flowId);

		// Start check AML
		int noOfSteps = Integer.parseInt(BotUtil.getValue(flow, "noOfSteps"));
		JSONObject step = null;
		for (int i = 1; i <= noOfSteps; i++) {

			// Loop each step
			step = BotUtil.getStep(flow, String.valueOf(i));
			process4Step(step, key);
		}
		WebElement Screening = driver.findElement(By
				.linkText("< Back to Screening"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", Screening);
		
		
		saveFileDownload(flowId,key);
	}

	public static boolean checkAlreadyLogin() {
		return false;
	}

	public static void doLogin() {
		
		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "/drivers/chromedriver");
			driver = new ChromeDriver(configChrome());
			driver.manage().window().maximize();
		}

		PropertyConfigurator.configure("res/log4j.properties");
		displayAndWriteLog("Mở trình duyệt.");

		JSONObject flowLogin = BotUtil.getFlowById("LOGIN");
		String url = (String) flowLogin.get("mainUrl");
		JSONObject stepLogin = BotUtil.getStep(flowLogin, "1");
		JSONArray arr = BotUtil.getStepElements(stepLogin);

		driver.get(url);
		displayAndWriteLog("Mở trang đăng nhập.");

		String waitElement = (String) stepLogin.get("waitElement");
		if (waitElement != null && !"".equals(waitElement)) {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.id(waitElement)));
		}

		JSONObject jsonObject = null;
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;

		WebElement webElement = null;

		for (int i = 1; i <= arr.size(); i++) {
			jsonObject = BotUtil.getStepElementByOrder(stepLogin,
					String.valueOf(i));
			id = (String) jsonObject.get("id");
			value = (String) jsonObject.get("value");
			findBy = (String) jsonObject.get("findBy");
			action = (String) jsonObject.get("action");
			waitElement = (String) jsonObject.get("waitElement");

			if ("ID".equals(findBy)) {
				webElement = driver.findElement(By.id(id));
				if (i == 1)
					displayAndWriteLog("Tìm phần tử tên đăng nhập.");
				if (i == 2) {
					displayAndWriteLog("Tìm phần tử mật khẩu đăng nhập.");
				}
			}
			if ("SENDTEXT".equals(action)) {
				webElement.sendKeys(value);
				new WebDriverWait(driver, 50);
				if (i == 1)
					displayAndWriteLog("Điền tên đăng nhập.");
				
				if (i == 2) {
					displayAndWriteLog("Điền mật khẩu đăng nhập.");
				}

			}
			if ("CLICK".equals(action)) {
				webElement.click();
				if (i == 3) {
					displayAndWriteLog("Click nút đăng nhập .");
				}
				WebDriverWait wait1 = new WebDriverWait(driver, 30);
				wait1.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(waitElement)));
				driver.switchTo()
						.frame(driver.findElement(By
								.xpath("//*[@id=\"accelus_components_application_IframeView_0\"]/iframe")));

				WebDriverWait wait2 = new WebDriverWait(driver, 30);
				wait2.until(ExpectedConditions.visibilityOfElementLocated(By
						.id("uniqName_2_0")));
			}
		}
	}

	private static void displayAndWriteLog(String log) {
		logger.info(log);
		String DATE_FORMATTER= "dd-MM-yyyy HH:mm:ss .SSS";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
		AML_BOTview.printLogOut = (LocalDateTime.now().format(formatter) +": \t"+ log + "\n");
		AML_BOTview.loadMonitor(AML_BOTview.printLogOut);
	}

	public static void process4Step(JSONObject step, String key) {
		PropertyConfigurator.configure("res/log4j.properties");
		if (step == null)
			return;

		JSONArray arr = BotUtil.getStepElements(step);
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		WebElement webElement = null;
		WebDriverWait wait = null;

		for (int i = 1; i <= arr.size(); i++) {
			JSONObject jsonObject = BotUtil.getStepElementByOrder(step,
					String.valueOf(i));

			id = (String) jsonObject.get("id");

			value = (String) jsonObject.get("value");
			if ((String) jsonObject.get("value") == null) {
				value = key;
			} else {
				value = (String) jsonObject.get("value");
			}

			findBy = (String) jsonObject.get("findBy");
			action = (String) jsonObject.get("action");

			switch (findBy) {
			case "ID":
				System.out.println(id);
				wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.id(id)));
				webElement = driver.findElement(By.id(id));
				break;
			case "XPATH":
				System.out.println(id);
				wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(id)));
				webElement = driver.findElement(By.xpath(id));
				break;
			case "CSS_SELECTOR":
				System.out.println(id);
				wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.cssSelector(id)));
				webElement = driver.findElement(By.cssSelector(id));
				break;

			default:
				break;
			}

			switch (action) {
			case "CLICK":
				System.out.println(action);
				webElement.click();
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Click nút.");
				break;
			case "SENDTEXT":
				System.out.println("value:" + value);
				webElement.sendKeys(key);
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Điền dữ liệu tìm kiếm .");
				break;
			case "ENTER":
				System.out.println(action);
				webElement.sendKeys(Keys.ENTER);
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Điền dữ liệu tìm kiếm .");
				break;
			default:
				break;
			}
			System.out.println("---------------------------------------");
		}
	}
	private static ChromeOptions configChrome() {
		System.setProperty("webdriver.chrome.driver",
				"/drives/chromedriver");

		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", downloadFilepath);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		return options;
	}
	
	public static void saveFileDownload(String type, String name) throws InterruptedException{
		 Date date1 = new Date();
	        DateFormat df1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
	        String dateString1 = df1.format(date1);

	        Thread.sleep(2000);
     File srcFile = new File("/IdeaProjects/JavaSwing/PDFBox/CaseDossierReport.pdf");
     
     String filec= downloadFilepath+ "/"+dateString+"/"+dateString1+"_"+type+"_"+name+".pdf";

     //System.out.println(filec);
     // File đích (Destination file).
     File destFile = new File(filec);

     if (srcFile.exists()) {
     	// Tạo thư mục cha của file đích.
         destFile.getParentFile().mkdirs();
  
         boolean renamed = srcFile.renameTo(destFile);
  
         System.out.println("Renamed: " + renamed);
     }	
	}
}