package com.wweco.mm.demo;

import com.api.*;
import com.wweco.mm.api.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
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
import io.appium.java_client.TouchAction;

import static org.junit.Assert.*;

public class AccessWithoutLogin {
    
    private DriverManage driver;
    private String testCaseID = "AccessWithoutLogin";
    private ReportResult logReport;
    private String deviceOS;
    private String deviceType;
    private MMApi apiAction;

    @Test(timeOut = 120000)
    public void Step1() throws Exception {
        logReport.setTestStep("Step1");
        Thread.sleep(5000);
        logReport.addMsg("Click the icon of \"MM\" in testing device");
        logReport.saveResult(true, "The \"MM\" launches successfully");
    }

    @Test(timeOut = 120000)
    public void Step2() throws Exception {
        logReport.setTestStep("Step2");
        logReport.addMsg("AccessWithoutLogin");
        try {
            apiAction.accessWithoutLogin();
        } catch (Exception ex) {
            try {
                logReport.saveResult(false, ex.getMessage());
            } catch (Exception le) {}
            throw new TestingException(ex.getMessage());
        }

        try {
                logReport.saveResult(true, "Select Username success!");
            } catch (Exception logE) {}
    }

    // @Test(timeOut = 120000)
    // public void Step3() throws Exception {
    //     logReport.setTestStep("Step3");
    //     logReport.addMsg("Enter very long name");
    //     try {
    //         //Enter 100 char in "a"
    //         for (int i=0; i<100; i++)
    //             apiAction.enterUserName(29);
    //     } catch (Exception e) {
    //         logReport.saveResult(false, e.getMessage());
    //         throw new TestingException(e.getMessage());
    //     }
    //     logReport.saveResult(true, "100 char can be entered");
    // }
    
    // @Test(timeOut = 120000)
    // public void Step4() throws Exception {
    //     logReport.setTestStep("Step4");
    //     logReport.addMsg("Click the inactive area in the right");
        
    //     try {
    //         apiAction.disableSideMenu();
    //     } catch (Exception e) {
    //         logReport.saveResult(false, e.getMessage());
    //         throw new TestingException(e.getMessage());
    //     }

    //     try {
    //             logReport.saveResult(true, "The side menu is disabled");
    //         } catch (Exception e) {}
    // }

    // @Test(timeOut = 120000)
    // public void Step5() throws Exception {
    //     logReport.setTestStep("Step5");
    //     logReport.addMsg("CLick Add Speaker Button");
        
    //     try {
    //         apiAction.clickAddButton();
    //     } catch (Exception e) {
    //         logReport.saveResult(false, e.getMessage());
    //         throw new TestingException(e.getMessage());
    //     }

    //     try {
    //             logReport.saveResult(true, "Click Add Button success");
    //         } catch (Exception e) {}
    // }

    @Parameters({"delay","port","deviceModel", "appID", "deviceID", "firstPage", "OSType", "sizeType", "osVersion", "jobName", "buildNo"})
    @BeforeClass
    public void setUp(String delay, String port, String deviceModel, String appID, String deviceID, String firstPage, String systemOS, String sizeType, String osVersion, String jobName, String buildNo) throws Exception {
        deviceOS = systemOS;
        deviceType = sizeType;
        // modelName = deviceModel;
        
        if (deviceOS.equals("Android")) {
            testCaseID = "trial";
        } else {
            testCaseID = "trial";
            Thread.sleep(3000);
        }
        try {
            driver =  new DriverManage (delay, port, deviceModel, appID, deviceID, firstPage, deviceOS, deviceType, testCaseID, osVersion);
        } catch (Exception e) {
            logReport = new ReportResult(null, deviceModel, testCaseID, osVersion, deviceID, deviceType, jobName, buildNo, "", Thread.currentThread());
            throw new TestingException(e.getMessage());
        }
        logReport = new ReportResult(driver.outDriver(), deviceModel, testCaseID, osVersion, deviceID, deviceType, jobName, buildNo, "", Thread.currentThread());
        apiAction = new MMApi (port, deviceModel, appID, deviceID, firstPage, deviceOS, deviceType, osVersion, driver.outDriver(), logReport, Thread.currentThread());  
        //Add test case description
        logReport.addMsg("Access without Login");
        logReport.testCaseStart();
        
        if (sizeType.equals("tablet")) {
            logReport.skipTestCase("This device is not phone. The test case skips!");
            throw new SkipException("This device is not phone. The test case skips!");
        }

        Thread.sleep(1000);
    }
    
    
    @AfterClass
    public void tearDown() throws Exception {
        try { 
            Thread.sleep(1000);
        } catch (Exception e) {}
        logReport.endTestCase();
        driver.quit();
    }
    
}
