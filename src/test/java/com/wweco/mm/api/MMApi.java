package com.wweco.mm.api;

import com.api.*;
import com.wweco.mm.api.*;
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

public class MMApi {
	private AppiumDriver driver;
	private String deviceOS;
    private String deviceType;
    private String osVer;
    private int widthDemension;
    private int heightDemension;
    private String applicationID;
    private ReportResult logReport;
    private Thread testThread;
    private Mapping mapping;


	public MMApi (String port, String deviceModel, String appID, String deviceID, String firstPage, String systemOS, String sizeType, String osVersion, AppiumDriver dr, ReportResult writeReport, Thread testCaseThread) throws TestingException{
		deviceOS = systemOS;
        deviceType = sizeType;
        applicationID = appID;
        osVer = osVersion;
        logReport = writeReport;
        driver = dr;
        testThread = new Thread(testCaseThread);

        if (port == null) {
            port = System.getProperty("port");
            if (port == null) {
                port = "4723";
            }
        
        }

        if (deviceOS.equals("Android")) {
            mapping = new MappingAndroid(applicationID);
        } else {
            mapping = new MappingiOS(applicationID);
        }
		
        widthDemension = driver.manage().window().getSize().getWidth();
        heightDemension = driver.manage().window().getSize().getHeight();
	}

    // public String \*methodName*/ () throws TestingException {
    //     try {
    //         //method content
    //         //return value
    //         }
    //     } catch (Exception e) {
    //         return e.getMessage();
    //     }
    // }

    public void clickUserName () throws TestingException {
        try {
            driver.findElementByXPath(mapping.username("XPATH")).click();

        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }
    }

    public void enterUserName (int keyAscii) throws TestingException {
        try {
            //WebElement usernameFill = driver.findElementByXPath(mapping.username("XPATH"));
            //Enter Ascii
            driver.sendKeyEvent(keyAscii);

        } catch (Exception e) {
            throw new TestingException(e.getMessage());
        }
    }

    //method

    //method

}

