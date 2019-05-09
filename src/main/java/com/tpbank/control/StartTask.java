package com.tpbank.control;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.TimerTask;

@SuppressWarnings("unused")
//TODO: this class should named Task instead of StartTask
public class StartTask extends TimerTask {
    private String downloadFolder;

    public StartTask(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    //TODO: option download folder
    private ChromeOptions configChrome() {
        System.setProperty("webdriver.chrome.driver", "drives/chromedriver");
        String downloadFilepath = downloadFolder;
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadFilepath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        return options;
    }

    @Override
    public void run() {
        String url = "http://10.1.14.221:7777/Report/login.jsp";
        //https://data.chiasenhac.com/dataxx/24/downloads/1261/4/1260557-ae7920d6/128/Beta%20-%20Hardwell_%20Nicky%20Romero.mp3
        String user = "nor\\thht-dev02";
        String password = "654321aa@";
        String maotp = "123456";
        String submit = "//input[@type='submit']";
        WebDriver driver = null;

        System.setProperty("webdriver.chrome.driver", "drives/chromedriver");

        try {
            driver = new ChromeDriver(configChrome());
            driver.manage().window().maximize();


            driver.get(url);
            WebDriverWait wait = new WebDriverWait(driver, 30);


            WebElement uname = driver.findElement(By.id("j_username"));
            uname.sendKeys(user);
            new WebDriverWait(driver, 30);

            WebElement pass = driver.findElement(By.id("j_password"));
            pass.sendKeys(password);
            new WebDriverWait(driver, 30);

            WebElement otp = driver.findElement(By.id("j_otp"));
            otp.sendKeys(maotp);
            new WebDriverWait(driver, 30);
            //login
            //driver.findElement(By.xpath("//input[@type='submit']")).click();
            driver.findElement(By.xpath(submit)).click();


//				//xem bao cao";
            submit = "Xem báo cáo";
            //driver.findElement(By.linkText("Xem báo cáo")).click();
            driver.findElement(By.linkText(submit)).click();
            new WebDriverWait(driver, 30);

//				//xem bao cao
            submit = "//a[@onclick=\"javascript:window.location='searchReportInUseShowInit.action?keySession='\"]";
            //driver.findElement(By.xpath("//a[@onclick=\"javascript:window.location='searchReportInUseShowInit.action?keySession='\"]")).click();
            driver.findElement(By.xpath(submit)).click();
            new WebDriverWait(driver, 30);
//
//				//chon ATM009
            //WebElement ATM009=driver.findElement(By.xpath("//a[@onclick=\"getReportId('ATM009','/Report/popupListParamShowResult.action')\"]"));
            submit = "//a[@onclick=\"getReportId('ATM009','/Report/popupListParamShowResult.action')\"]";
            WebElement ATM009 = driver.findElement(By.xpath(submit));
            ATM009.click();
            new WebDriverWait(driver, 30);
//				//xuất file PDF
//
////				WebDriverWait wait1 = new WebDriverWait(driver, 30);
////				WebElement el = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"Xuất PDF\"]")));
////				el.click();
//
            submit = "//input[@value=\"Xuất PDF\"]";
            //driver.findElement(By.xpath("//input[@value=\"Xuất PDF\"]")).click();
            driver.findElement(By.xpath(submit)).click();
            new WebDriverWait(driver, 30);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}