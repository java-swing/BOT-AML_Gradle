package com.tpbank.botaml;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.tpbank.botaml.util.BotUtil;

public class ScreeningService {

	public static final Logger logger = Logger
			.getLogger(ScreeningService.class);

	private static WebDriver driver = null;

	public static void main(String[] args) {
		doProcess("INDIVIDUAL");

		WebElement Screening = driver.findElement(By
				.linkText("< Back to Screening"));
		JavascriptExecutor executor1 = (JavascriptExecutor) driver;
		executor1.executeScript("arguments[0].click();", Screening);

		doProcess("INDIVIDUAL");
	}

	public static void doProcess(String flowId) {

		if (driver == null) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}

		// Process
		JSONObject flow = BotUtil.getFlowById(flowId);

		// Check require login
		String requiredLogin = BotUtil.getValue(flow, "requiredLogin");

		if ("YES".equals(requiredLogin)) {
			if (!checkAlreadyLogin()) {
				doLogin();
			}
		}

		// Start check AML
		int noOfSteps = Integer.parseInt(BotUtil.getValue(flow, "noOfSteps"));
		JSONObject step = null;
		for (int i = 1; i <= noOfSteps; i++) {

			// Loop each step
			step = BotUtil.getStep(flow, String.valueOf(i));
			process4Step(step);
		}
	}

	public static boolean checkAlreadyLogin() {
		return false;
	}

	public static void doLogin() {

		JSONObject flowLogin = BotUtil.getFlowById("LOGIN");
		String url = (String) flowLogin.get("mainUrl");
		JSONObject stepLogin = BotUtil.getStep(flowLogin, "1");
		JSONArray arr = BotUtil.getStepElements(stepLogin);

		driver.get(url);

		String waitElement = (String) stepLogin.get("waitElement");
		if (waitElement != null && !"".equals(waitElement)) {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By
					.id(waitElement)));
		}

		Iterator<Object> iterator = arr.iterator();
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
			}
			if ("SENDTEXT".equals(action)) {
				webElement.sendKeys(value);
				new WebDriverWait(driver, 50);
			}
			if ("CLICK".equals(action)) {
				webElement.click();

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

	public static void process4Step(JSONObject step) {

		if (step == null)
			return;

		JSONArray arr = BotUtil.getStepElements(step);
		String id = null;
		String value = null;
		String findBy = null;
		String action = null;
		String waitElement = null;
		String waitElementFindBy = null;

		WebElement webElement = null;
		WebDriverWait wait = null;

		for (int i = 1; i <= arr.size(); i++) {
			JSONObject jsonObject = BotUtil.getStepElementByOrder(step,
					String.valueOf(i));

			id = (String) jsonObject.get("id");
			value = (String) jsonObject.get("value");
			findBy = (String) jsonObject.get("findBy");
			action = (String) jsonObject.get("action");
			waitElement = (String) jsonObject.get("waitElement");
			waitElementFindBy = (String) jsonObject.get("waitElementFindBy");

			if ("ID".equals(findBy)) {
				webElement = driver.findElement(By.id(id));
			} else if ("XPATH".equals(findBy)) {
				webElement = driver.findElement(By.xpath(id));
			} else if ("CSS_SELECTOR".equals(findBy)) {
				webElement = driver.findElement(By.cssSelector(id));
			}

			if ("SENDTEXT".equals(action)) {
				webElement.sendKeys(value);
				new WebDriverWait(driver, 50);
			} else if ("CLICK".equals(action)) {
				webElement.click();
				new WebDriverWait(driver, 50);
			} else if ("ENTER".equals(action)) {
				webElement.sendKeys(Keys.ENTER);
				new WebDriverWait(driver, 50);
			}

			if (waitElement != null && !"".equals(waitElement)) {
				wait = new WebDriverWait(driver, 30);
				if ("ID".equals(waitElementFindBy)) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.id(waitElement)));
				} else if ("XPATH".equals(waitElementFindBy)) {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath(waitElement)));
				}
			}
		}
	}
}