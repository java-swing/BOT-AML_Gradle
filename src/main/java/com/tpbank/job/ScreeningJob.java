package com.tpbank.job;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import com.tpbank.ui.BotUI;
import com.tpbank.util.FileUtil;
import com.tpbank.util.Util;
import com.tpbank.util.functionFactory;
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


import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;


public class ScreeningJob implements Job {

	private static final Logger logger = Logger.getLogger(ScreeningJob.class);

	public static WebDriver driver = null;
	private static Scheduler scheduler;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		displayAndWriteLog("execute fired");
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String partner = jobDataMap.getString("partner");
        String task = jobDataMap.getString("task");
        MessageObservable observable = (MessageObservable) jobDataMap.get("observable");
        displayAndWriteLog(partner + "-" + task + " execute fired");
        
		doTask(observable, partner, task);
	}

	public static void doTask(MessageObservable observable, String partner, String task){
		//Do something
		showMessage(observable, "hello");
	}
	
	public static void doProcess(String flowId, String key) throws InterruptedException, ClassNotFoundException, SQLException {
		PropertyConfigurator.configure("src/log4j.properties");
		// Process
		JSONObject flow = Util.getFlowById(flowId);

		// Start check AML
		int noOfSteps = Integer.parseInt(Util.getValue(flow, "noOfSteps"));
		JSONObject step = null;
		for (int i = 1; i <= noOfSteps; i++) {

			// Loop each step
			step = Util.getStep(flow, String.valueOf(i));
			process4Step(step, key);			
		}

		// Tro ve home
		goBack();
		// Save file download
		Thread.sleep(4000);
		FileUtil.saveFileDownload(flowId,key);
	}

	public static boolean checkAlreadyLogin() {
		return false;
	}

	public static void doLogin() {

		try {
			driver = new ChromeDriver(configChrome());
			driver.manage().window().maximize();

			PropertyConfigurator.configure("src/log4j.properties");
			displayAndWriteLog("Mở trình duyệt.");

			JSONObject flowLogin = Util.getFlowById("LOGIN");
			String url = (String) flowLogin.get("mainUrl");
			JSONObject stepLogin = Util.getStep(flowLogin, "1");
			JSONArray arr = Util.getStepElements(stepLogin);

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
				jsonObject = Util.getStepElementByOrder(stepLogin,
						String.valueOf(i));
				id = (String) jsonObject.get("id");
				value = (String) jsonObject.get("value");
				findBy = (String) jsonObject.get("findBy");
				action = (String) jsonObject.get("action");
				waitElement = (String) jsonObject.get("waitElement");

				if ("ID".equals(findBy)) {
					webElement = driver.findElement(By.id(id));
					if (i == 1) {
						displayAndWriteLog("Tìm phần tử tên đăng nhập .");
					}
					if (i == 2) {
						displayAndWriteLog("Tìm phần tử  mật khẩu đăng nhập .");
					}
				}
				if ("SENDTEXT".equals(action)) {
					webElement.sendKeys(value);
					new WebDriverWait(driver, 50);
					if (i == 1) {
						displayAndWriteLog("Điền tên đăng nhập .");
					}
					if (i == 2) {
						displayAndWriteLog("Điền mật khẩu đăng nhập .");
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}
	}

	public static void process4Step(JSONObject step, String key){
		PropertyConfigurator.configure("src/log4j.properties");
		if (step == null)
			return;

		JSONArray arr = Util.getStepElements(step);
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		WebElement webElement = null;
		WebDriverWait wait = null;

		for (int i = 1; i <= arr.size(); i++) {
			JSONObject jsonObject = Util.getStepElementByOrder(step,
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

				wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
				webElement = driver.findElement(By.id(id));
				break;
			case "XPATH":
				wait = new WebDriverWait(driver, 30);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By
						.xpath(id)));
				webElement = driver.findElement(By.xpath(id));
				break;
			case "CSS_SELECTOR":
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
				//System.out.println(action);
				webElement.click();
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Click nút.");
				break;
			case "SENDTEXT":
				//System.out.println("value:" + value);
				webElement.sendKeys(key);
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Điền dữ liệu tìm kiếm .");
				break;
			case "ENTER":
				//System.out.println(action);
				webElement.sendKeys(Keys.ENTER);
				new WebDriverWait(driver, 50);
				displayAndWriteLog("Nhấn Enter.");
				break;
			default:
				break;
			}		
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			ScreeningJob.displayAndWriteLogError(e);
		}
		
	}
	
	public static void goBack(){
		WebElement Screening = driver.findElement(By
				.linkText("< Back to Screening"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", Screening);
		
	}

	private static ChromeOptions configChrome() {
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", BotUI.downloadFolder);
		ChromeOptions options = new ChromeOptions();
		options.setExperimentalOption("prefs", chromePrefs);
		return options;
	}
	
	public static void createNewJob(BotUI botUI, String partner, String task, String cronExpression){
		//* * * * * * *
		//Second Min Hour Day(Month) Month Day(Week) Year(Optional)
		//"0/5 * * * * ?"
		displayAndWriteLog(partner + "- " + task + ": createNewJob");
		try {
			
			JobDataMap jobDataMap = new JobDataMap();
	        jobDataMap.put("partner", partner);
	        jobDataMap.put("task", task);
	        
	        displayAndWriteLog(partner + ": createNewJob 2");
	        
	        MessageObservable observable = new MessageObservable();
        	observable.addObserver(botUI);
	        jobDataMap.put("observable", observable);
			
	        displayAndWriteLog(partner + ": createNewJob 3");
	        
			JobDetail job = JobBuilder.newJob(ScreeningJob.class)
					.withIdentity(partner.toUpperCase() + "_" + task.toUpperCase(), "GROUP_01")
					.usingJobData(jobDataMap)
					.build();
			
			displayAndWriteLog(partner + ": createNewJob 4");
			
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(partner.toUpperCase() + "_" + task.toUpperCase(), "GROUP_01")
					.withSchedule(
						CronScheduleBuilder.cronSchedule(cronExpression))
					.build();
			
			displayAndWriteLog(partner + ": scheduler is null: " + (scheduler==null));
			
			if(scheduler == null){
				scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.start();
				displayAndWriteLog(partner + ": scheduler start");
			}
	    	scheduler.scheduleJob(job, trigger);
	    	
		} catch (SchedulerException e) {
			System.out.println("SchedulerException: " + e.toString());
			ScreeningJob.displayAndWriteLogError(e);
		}
	}
	
	public static void shutdown(){
		try {
			if(scheduler != null && scheduler.isStarted()){
				scheduler.shutdown();
				System.out.println("Shutdown scheduler!");
			}
		} catch (SchedulerException e) {
			System.out.println("SchedulerException: " + e.toString());
			ScreeningJob.displayAndWriteLogError(e);
		}
	}
	
	public static void showMessage(MessageObservable observable, String msg){
		observable.changeData(msg);
	}
		
	public static void displayAndWriteLog(String log) {
        logger.info(log);
        String DATE_FORMATTER= "dd-MM-yyyy HH:mm:ss .SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        BotUI.printLogOut = (LocalDateTime.now().format(formatter) +": \t"+ log + "\n");
        functionFactory.loadMonitor(BotUI.printLogOut);
 }
	
	
	public static void displayAndWriteLogError(Exception e) {
        logger.error(e.getMessage(),e);
        String DATE_FORMATTER= "dd-MM-yyyy HH:mm:ss .SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        BotUI.printLogOut = (LocalDateTime.now().format(formatter) +": \t"+ e.getMessage() + "\n");
        functionFactory.loadMonitor(BotUI.printLogOut);
 }

}