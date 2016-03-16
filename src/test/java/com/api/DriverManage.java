package com.api;

import com.api.*;
// import com.gibson.mojo.api.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.Rotatable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.testng.SkipException;

import java.io.File;
import java.lang.Integer;
import java.lang.System;
import java.net.URL;
import java.lang.*;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import static org.junit.Assert.*;

public class DriverManage {
	private AppiumDriver driver;
	private String deviceOS;
    private String deviceType;
    private int widthDemension;
    private int heightDemension;
    private String applicationID;

	public DriverManage (String delay, String port, String deviceModel, String appID, String deviceID, String firstPage, String systemOS, String sizeType, String testID, String osVersion) throws Exception{
		deviceOS = systemOS;
        deviceType = sizeType;
        applicationID = appID;

        if (port == null) {
            port = System.getProperty("port");
            if (port == null) {
                port = "4723";
            }
        
        }

        System.out.println("Test case: " + testID + ", Device: " + deviceModel + ", Port: " + port + ", OS: " + systemOS + ", Device Type: " + sizeType + ", Device ID: " + deviceID + ", OS Version:" + osVersion);

		final File classpathRoot = new File(System.getProperty("user.dir"));
        //final File appDir = new File(classpathRoot, "../app/build/outputs/apk");
        //final File app = new File(appDir, "app-debug.apk");
//  final File app = new File("./app-debug.apk");
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", deviceOS);
        capabilities.setCapability("rotatable", true);
        capabilities.setCapability("newCommandTimeout", "0");
        capabilities.setCapability("platformVersion", osVersion);
        try {
            if (deviceOS.equals("Android")) {
                capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
                capabilities.setCapability("deviceName", deviceModel);
                capabilities.setCapability("noSign", "true"); //XXX must have
        //        capabilities.setCapability("app", app.getAbsolutePath());
                capabilities.setCapability("appPackage", appID);
                //capabilities.setCapability("appActivity", firstPage);
                capabilities.setCapability("deviceType", sizeType);
                if (osVersion.equals("4.1")){
                    final File app = new File("app-production-debug.apk");
                    capabilities.setCapability("app", app.getAbsolutePath());
                    capabilities.setCapability("automationName", "Selendroid");
                    capabilities.setCapability("fullReset", "true");
                }
                driver = new AndroidDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
            } else {
                capabilities.setCapability("deviceName", deviceModel);
                capabilities.setCapability("platformVersion", osVersion);
                //capabilities.setCapability("app", "/Users/Bell/Desktop/MoJo/MoJoAppiumTest/./MoJo.ipa");
                capabilities.setCapability("bundleId", appID);
                capabilities.setCapability("udid", deviceID);
                // if (sizeType.equals("tablet")) {
                //     capabilities.setCapability("deviceName", "iPad");
                // } else {
                //     capabilities.setCapability("deviceName", "iPhone");
                // }
                
                //Accept permission
                // capabilities.setCapability("autoAcceptAlerts", true);
                driver = new IOSDriver(new URL("http://127.0.0.1:" + port + "/wd/hub"), capabilities);
            }
        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }

        widthDemension = driver.manage().window().getSize().getWidth();
        heightDemension = driver.manage().window().getSize().getHeight();
	}

	public AppiumDriver outDriver () {
		return driver;
	}

    public void quit () throws TestingException{
        try {
            driver.quit();
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }
    }

    public void launchApp () throws TestingException{
        try {
            driver.launchApp();
        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }
    }

    public void closeApp () throws TestingException{
        try {
            driver.closeApp();
        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }
    }

    public int getWidthDemension (){
        return widthDemension;
    }

    public int getHeightDemension () {
        return heightDemension;
    }

    public String getAppID() {
        return applicationID;
    }

    public WebElement findElementByXPath (String x_path) {
        return driver.findElementByXPath(x_path);
    }

    public void rotate (String rtt) throws TestingException {
        boolean success = true;
        String errors="error rotate";
        if (rtt.equals("PORTRAIT")) {
            try { 
                driver.rotate(ScreenOrientation.PORTRAIT);
            } catch (Exception e) {
                try {
                    Thread.sleep(5000);
                } catch (Exception ee) {}
                errors=e.getMessage();
            }
            
            if (!driver.getOrientation().value().equals("portrait")) {
                throw new TestingException(errors);
            }
        } else {
            try { 
                driver.rotate(ScreenOrientation.LANDSCAPE);
            } catch (Exception e) {
                try {
                    Thread.sleep(5000);
                } catch (Exception eec) {}
                errors=e.getMessage();
            }

            if (!driver.getOrientation().value().equals("landscape")) {
                throw new TestingException(errors);
            }
        }
    }
}

