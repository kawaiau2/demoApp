
// Update this API version please update the recorded API version no. as well

package com.api;

import com.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.Augmenter;
// import org.testng.annotations.*;
import java.text.SimpleDateFormat;
import org.testng.annotations.Parameters;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.Date;

import java.io.*;

import io.appium.java_client.*;
import static org.junit.Assert.*;

public class ReportResult {

	private AppiumDriver driver;
	private String modelName;
	private String testCase;
	private String testStep;
	private String osVer;
	private String deviceType;
	private String deviceID;
	private String swVersion;
	private PrintWriter writerTxt;
	private Thread testThread;
	private String testCaseResult = "N/A";
	private TestResult testResults;
	private HTMLTable resultTable;
	private String testDate;
	private String apiVer = "2.0";
	private String firstScreen = "";
	private String jobName;
	private String buildNo;

	public ReportResult (AppiumDriver dr, String mdlNme, String tstCse, String osVersion, String dID, String dType, String jbName, String bdNo, String swVer, Thread testCaseThread) throws Exception {
        modelName = mdlNme;
        testCase = tstCse;
        osVer = osVersion;
        deviceID = dID;
        deviceType = dType;
        swVersion = swVer;
        testThread = new Thread(testCaseThread);
        // FileReader readGlobal = new FileReader("./testng.xml");
        // BufferedReader allText = new BufferedReader(readGlobal);
        // String allGlobal = allText.readLine();
        // allGlobal = allText.readLine();
        // allText.close();
        // jobName = allGlobal.substring(5,allGlobal.length()-4).split("/")[0];
        // buildNo = allGlobal.substring(5,allGlobal.length()-4).split("/")[1];
        jobName = jbName;
        buildNo = bdNo;
        testResults = new TestResult (testCase, modelName + ":::" + osVer);
        String folderPath = "./testResult/" + modelName + "/";
		(new File (folderPath)).mkdirs();
		File file = new File(folderPath + testCase + ".txt");
		// file.delete();
		writerTxt = new PrintWriter(folderPath + testCase + ".txt", "UTF-8");
		writerTxt.println("**********************************");
		writerTxt.println("***SW Team of Gibson Innovation***");
		writerTxt.println("**********************************");
		Date currentDate = new Date( );
    	SimpleDateFormat dateFormat = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    	testDate = dateFormat.format(currentDate);
    	writerTxt.println("Testing Date and Time: " + testDate);
    	writerTxt.println("Test Case: " + testCase);
    	writerTxt.println("Device Name: " + modelName);
    	writerTxt.println("OS Version: " + osVersion);
    	writerTxt.println("Device Type: " + deviceType);
    	writerTxt.println("Device ID: " + deviceID);
    	// Update this API version and update the follow version no. as well
    	writerTxt.println("API of Automation Version: v" + apiVer);
    	writerTxt.println("Software Version: " + swVersion);
    	addMsg("Test Case Description:");
    	resultTable = new HTMLTable ("");
    	resultTable.openRow("");
    	resultTable.addHeadElement("", "Test Step");
    	resultTable.addHeadElement("", "Result");
    	resultTable.addHeadElement("", "Screen");
    	resultTable.closeRow();
    	if (dr == null) {
    		testCaseResult = "Error";
    		firstScreen = "./noPic.png";
    		endTestCase();
    	} else {
    		driver = dr;
    	}
    }

	public void testCaseStart () {
		addMsg("-----------" + testCase + " Starting-----------");
		testCaseResult = "N/A";
	}

	public void setTestStep (String tstStp) {
		testStep = tstStp;
		addMsg("-----------" + tstStp + "-----------");
		clearThread();
	}

	public File capScreen() {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		try{ 
			testThread.sleep(1000);
	    } catch (Exception e) {}
	    //make screenshot and save it to the local filesystem
        File file = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        System.out.println("Len: " + file.length());
        return file;
	}

	public void saveScreen(boolean result) {
		String folderPath = "./screen/" + modelName + "/passed/" + testCase + "/";
		(new File (folderPath)).mkdirs();
        try {
        	if (result) {
        		FileUtils.copyFile(capScreen(), new File(folderPath + testStep + ".png"));
        		if (testStep.equals("Step1")) {
					firstScreen = folderPath + testStep + ".png";
				} 
        	} else {
        		FileUtils.copyFile(capScreen(), new File(folderPath + testStep + "_fail.png"));
        		folderPath = "./screen/" + modelName + "/failed/" + testCase + "/";
				(new File (folderPath)).mkdirs();
				FileUtils.copyFile(capScreen(), new File(folderPath + testStep + "_fail.png"));
				if (testStep.equals("Step1")) {
					firstScreen = folderPath + testStep + "_fail.png";
				} 
				clearThread();
        	}
        } catch (Exception e) { }
	}

	public void addMsg (String logMsg) {		
		System.out.println("***" + modelName + "***" + testCase + "***" + testStep + "***" + logMsg);
		writerTxt.println(logMsg);
	}

	public void addResult (boolean result, String resultMsg) {
		resultTable.openRow("");
		if (result) {
			resultTable.addElement("", "<a href=\"../../testResult/" + modelName + "/" + testCase + ".txt\">" + testStep + "</a>");
			resultTable.addElement("id=\"Passed\"", resultMsg);
			resultTable.addElement("", "<button onclick=\"changeScreen('../../screen/" + modelName + "/passed/" + testCase + "/" + testStep + ".png')\" id=\"Passed\">Passed Screen</button>");
		} else {
			resultTable.addElement("", "<a href=\"../../testResult/" + modelName + "/" + testCase + ".txt\">" + testStep + "</a>");
			resultTable.addElement("id=\"Failed\"", resultMsg);
			resultTable.addElement("", "<button onclick=\"changeScreen('../../screen/" + modelName + "/passed/" + testCase + "/" + testStep + "_fail.png')\" id=\"Failed\">Failed Screen</button>");
		}
		resultTable.closeRow();
	}

	public void saveResult(boolean result, String resultMsg) {
		saveScreen(result);
		addResult(result, resultMsg);
		if (result) {
			addMsg("Passed: " + resultMsg);
			resultTable.openRow("");

			if (testCaseResult.equals("N/A")) {
				testCaseResult = "Passed";
			}
		} else {
			for (String str : resultMsg.split("\\r?\\n")) {
				addMsg("Failed: " + str);
			}
			testCaseResult = "Failed";
		}
		
	}
	
	public void endTestCase () {
		setTestStep ("Test Case Result");
		addMsg(testCase + " is " + testCaseResult + " in " + modelName + "!");
		addMsg("-----------" + testCase + " Ended-----------");
		clearThread();
		writerTxt.close();
		testResults.putTestResult(testCase, modelName + ":::" + osVer + ":::" + testCaseResult);
		try {
			HTMLGenerator htmlResult = new HTMLGenerator("./testCaseResult/" + testCase + "/", modelName + ".html");
			htmlResult.addTitle(modelName + ": " + testCaseResult);
			htmlResult.addLink("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cm.woox.com/jenkins-theme/style.css\">");
			htmlResult.closeHead();
			htmlResult.addDefaultStyle();
			htmlResult.closeStyle();
			// Java Script
			htmlResult.addBody("<script>");
			htmlResult.addBody("function changeScreen(img) {");
			htmlResult.addBody("document.getElementById(\"screen\").setAttribute(\"src\", img);");
			htmlResult.addBody("}");
			htmlResult.addBody("</script>");
			// Header
			htmlResult.addDefaultHeader(jobName);
			// Content
			htmlResult.addBody("<div id=\"resultBody\">");
			htmlResult.addBody("<h1>Test Case: " + testCase + "</h1>");
			htmlResult.addBody("<h2>" + modelName + ": " + testCaseResult + "</h2>");
			htmlResult.addBody("<p1>Testing Date and Time: " + testDate + "</p1><br/>");
			htmlResult.addBody("<p1>Device Type: " + deviceType + "</p1><br/>");
			htmlResult.addBody("<p1>Device ID: " + deviceID + "</p1><br/>");
			htmlResult.addBody("<p1>OS Version: " + osVer + " </p1><br/>");
			htmlResult.addBody("<p1>API of Automation Version: v" + apiVer + "</p1><br/>");
			htmlResult.addBody("<p1>Software Version: " + swVersion + " </p1><br/>");
			htmlResult.addBody("<table id=\"overall\">");
			htmlResult.addBody("<td id=\"overall\">");
			if (testCaseResult.equals("Skipped")) {
				htmlResult.addBody("<h3>" + testCase + " is skipped in " + modelName + ".<br/>Please check the detail in <a href=\"../../testResult/" + modelName + "/" + testCase + ".txt\">here</a>!</h3>");
			} else if (testCaseResult.equals("Error")) {
				htmlResult.addBody("<h3>" + testCase + " is error in " + modelName + ".<br/>Please check the detail in <a href=\"https://unicorn.int.gibson.com/job/" + jobName + "/" + buildNo + "/console\">Overall log in console</a>!</h3>");
			} else {
				htmlResult.addBody(resultTable.outputTable());
			}
			htmlResult.addBody("<br/>");
			// Footer
			htmlResult.addBody("<a href=\"../../console.log\">Overall log in txt</a>");
			htmlResult.addDefaultFooter(buildNo);

			htmlResult.addBody("</td>");
			if (!(testCaseResult.equals("Skipped") || testCaseResult.equals("Error"))) {
				htmlResult.addBody("<td id=\"screenLen\">");
				htmlResult.addBody("<img id=\"screen\" src=\"../." + firstScreen + "\" style=\" max-width: 1000px; max-height: 1000px;\"/>");
				htmlResult.addBody("</td>");
			}
			htmlResult.addBody("</table>");
			htmlResult.addBody("</div>");
			htmlResult.addBody("</div>");
			htmlResult.closeHTML();
		
			testThread.sleep(5000);
		} catch (Exception e) {}
	}

	public void clearThread () {
        try {
            testThread.sleep(100);
        } catch (Exception te) {
            addMsg("Clear Sleep Interrupt: " + te);
        }
    }

    public void updateTestCaseResult (String result) {
        testCaseResult = result;
    }

    public void skipTestCase (String skipMsg) {
        testCaseResult = "Skipped";
        addMsg(skipMsg);
        endTestCase();
    }
}