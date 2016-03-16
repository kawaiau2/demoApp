package com.api;

import com.api.*;

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
import org.testng.ISuiteListener;
import org.testng.annotations.Parameters;
import org.testng.ISuite;
import java.text.DecimalFormat;

import java.io.File;
import java.lang.Integer;
import java.lang.System;
import java.net.URL;
import java.lang.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import static org.junit.Assert.*;

public class TestConfig implements ISuiteListener{

	private TestResult testResult;
    private HTMLTable testPlanTable;
    private HTMLTable devicesTable;
    private HTMLTable testCaseTable;
    private HTMLTable overAllTable;
    private HTMLTable osAllTable;
    private String testCaseResultDetail = "";

	public synchronized void onStart(ISuite suite) {
        System.out.println("Test Suite Start:");
        testResult = new TestResult ("", "");
        System.out.println("abc");
        // testResult.defaultTest();
    }

    public synchronized void onFinish(ISuite suite) {

        HashMap osVer = new HashMap();

        int caseNoError = 0;
        int caseNoFail = 0;
        int caseNoPass = 0;
        int caseNoSkip = 0;
        int caseNoNoRun = 0;

        int planNoError = 0;
        int planNoFail = 0;
        int planNoPass = 0;
        int planNoSkip = 0;

        String temp = "";

        try {
            HTMLGenerator testCaseHTML;

            String testCaseTitle;

            String osList = "";

            String osChartContent ="";

            System.out.println("Test Suite End: ");

            overAllTable = new HTMLTable("");
            overAllTable.openRow("");
            overAllTable.addHeadElement("id=\"Passed\"", "Passed");
            overAllTable.addHeadElement("id=\"Failed\"", "Failed");
            overAllTable.addHeadElement("id=\"Skipped\"", "Skipped");
            overAllTable.addHeadElement("id=\"Error\"", "Error");
            overAllTable.addHeadElement("", "Passing Rate");
            overAllTable.closeRow();
            overAllTable.openRow("");

            testPlanTable = new HTMLTable("");
            testPlanTable.openRow("");
            testPlanTable.addHeadElement("", "Test Case");
            testPlanTable.addHeadElement("", "Result");
            testPlanTable.addHeadElement("", "Passing Rate");
            testPlanTable.closeRow();

            for (String testCase : testResult.outTestIDs().split("///")) {

                testCaseHTML = new HTMLGenerator ("./testCaseResult/", testCase + ".html");

                testCaseTable = new HTMLTable("");
                testCaseTable.openRow("");
                testCaseTable.addHeadElement("id=\"Passed\"", "Passed");
                testCaseTable.addHeadElement("id=\"Failed\"", "Failed");
                testCaseTable.addHeadElement("id=\"Skipped\"", "Skipped");
                testCaseTable.addHeadElement("id=\"Error\"", "Error");
                testCaseTable.addHeadElement("", "Passing Rate");
                testCaseTable.closeRow();
                testCaseTable.openRow("");

                testPlanTable.openRow("");
                testPlanTable.addElement("", "<a href=\"testCaseResult/" + testCase + ".html\">" + testCase + "</a>");

                switch (compareStatus(testResult.getTestResult(testCase))) {
                    case 0:
                        testCaseTitle = testCase + ": Error";
                        testPlanTable.addElement("id=\"Error\"", "Error");
                        planNoError++;
                        break;
                    case 1:
                        testCaseTitle = testCase + ": Failed";
                        testPlanTable.addElement("id=\"Failed\"", "Failed");
                        planNoFail++;
                        break;
                    case 2:
                        testCaseTitle = testCase + ": Passed";
                        testPlanTable.addElement("id=\"Passed\"", "Passed");
                        planNoPass++;
                        break;
                    case 3:
                        testCaseTitle = testCase + ": Skipped";
                        testPlanTable.addElement("id=\"Skipped\"", "Skipped");
                        planNoSkip++;
                        break;
                    default:
                        testCaseTitle = testCase + ": Error (No Run)";
                        testPlanTable.addElement("id=\"Error\"", "Error (No Run)");
                        planNoError++;
                        break;
                }

                System.out.println(testCaseTitle);
                testCaseHTML.addTitle(testCaseTitle);

                caseNoError = 0;
                caseNoFail = 0;
                caseNoPass = 0;
                caseNoSkip = 0;
                caseNoNoRun = 0;

                devicesTable = new HTMLTable("");
                devicesTable.openRow("");
                devicesTable.addHeadElement("", "Device");
                devicesTable.addHeadElement("", "Result");
                devicesTable.closeRow();
                devicesTable.openRow("");

                for (String result : testResult.getTestResult(testCase).split("///")) {
                    System.out.println(result.split(":::")[0] + ": " + result.split(":::")[2]);
                    devicesTable.addElement("", "<a href=\"" + testCase + "/" + result.split(":::")[0] + ".html\">" + result.split(":::")[0] + "</a>");
                    devicesTable.addElement("id=\"" + result.split(":::")[2] + "\"", result.split(":::")[2]);
                    devicesTable.closeRow();
                    switch (compareStatus(result.split(":::")[2])) {
                        case 0:
                            caseNoError++;
                            break;
                        case 1:
                            caseNoFail++;
                            break;
                        case 2:
                            caseNoPass++;
                            break;
                        case 3:
                            caseNoSkip++;
                            break;
                        default:
                            caseNoError++;
                            System.out.println(result.split(":::")[0] + ": Error (No Run)");
                            break;
                    }

                    if (osVer.containsKey(result.split(":::")[1])) {
                        temp = (String) osVer.get(result.split(":::")[1]);
                        if (!temp.matches("(.*)" + result.split(":::")[0] + "(.*)")) {
                            osVer.put(result.split(":::")[1], result.split(":::")[0] + "///" + osVer.get(result.split(":::")[1]));
                        }
                    } else {
                        osVer.put(result.split(":::")[1], result.split(":::")[0]);
                        if (osList.equals("")){
                            osList = result.split(":::")[1];
                        } else {
                            osList = result.split(":::")[1] + "///" + osList;
                        }
                    }
                }

                testCaseTable.addElement("id=\"Passed\"", String.valueOf(caseNoPass));
                testCaseTable.addElement("id=\"Failed\"", String.valueOf(caseNoFail));
                testCaseTable.addElement("id=\"Skipped\"", String.valueOf(caseNoSkip));
                testCaseTable.addElement("id=\"Error\"", String.valueOf(caseNoError));
                if ((caseNoPass + caseNoFail + caseNoError) > 0) {
                    testCaseTable.addElement("", roundDecimal(caseNoPass * 100.0 / (caseNoPass + caseNoFail + caseNoError)) + "%");

                    testCaseTable.closeRow();

                    testPlanTable.addElement("", roundDecimal(caseNoPass * 100.0 / (caseNoPass + caseNoFail + caseNoError)) + "%");
                } else {
                    testCaseTable.addElement("", "0%");

                    testCaseTable.closeRow();

                    testPlanTable.addElement("", "0%");
                }

                testCaseHTML.addLink("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cm.woox.com/jenkins-theme/style.css\">");
                // Java Script
                testCaseHTML.addChart("The Status of Test Case", caseNoPass, caseNoFail, caseNoSkip, caseNoError);
                testCaseHTML.closeHead();
                // Style
                testCaseHTML.addDefaultStyle();
                testCaseHTML.closeStyle();
                // Header
                testCaseHTML.addDefaultHeader("");
                testCaseHTML.addBody("<div id=\"resultBody\">");
                testCaseHTML.addBody("<h1>" + testCaseTitle + "</h1>");
                testCaseHTML.addBody("<table id=\"overall\">");
                testCaseHTML.addBody("<tr>");
                testCaseHTML.addBody("<td align=\"center\" id=\"overall\">");
                testCaseHTML.addBody(testCaseTable.outputTable());
                testCaseHTML.addBody("</td>");
                testCaseHTML.addBody("</tr>");
                testCaseHTML.addBody("<tr>");
                testCaseHTML.addBody("<td align=\"center\" id=\"overall\">");
                testCaseHTML.addBody("<div id=\"chart_div\"></div>");
                testCaseHTML.addBody("</td>");
                testCaseHTML.addBody("</tr>");
                testCaseHTML.addBody("</table>");
                testCaseHTML.addBody(devicesTable.outputTable());
                testCaseHTML.addBody("<br/>");
                testCaseHTML.addBody("<a href=\"../console.log\">Overall log in txt</a>");
                testCaseHTML.addDefaultFooter("");
                testCaseHTML.addBody("</div>");
                testCaseHTML.addBody("</div>");
                testCaseHTML.closeHTML();

                testPlanTable.closeRow();            

            }
            osAllTable = new HTMLTable("");
            osAllTable.openRow("");
            for (String ver1 : osList.split("///")) {
                osAllTable.addHeadElement("", ver1);
            }
            osAllTable.closeRow();
            osAllTable.openRow("");
            String noVer;
            for (String ver2 : osList.split("///")) {
                noVer = (String) osVer.get(ver2);
                osAllTable.addElement("", String.valueOf(noVer.split("///").length));
                if (osChartContent.equals("")) {
                    osChartContent = "['" + ver2 + "', " + String.valueOf(noVer.split("///").length) + "]";
                } else {
                    osChartContent = "['" + ver2 + "', " + String.valueOf(noVer.split("///").length) + "], " + osChartContent;
                }
            } 

            osAllTable.closeRow();

            overAllTable.addElement("id=\"Passed\"", String.valueOf(planNoPass));
            overAllTable.addElement("id=\"Failed\"", String.valueOf(planNoFail));
            overAllTable.addElement("id=\"Skipped\"", String.valueOf(planNoSkip));
            overAllTable.addElement("id=\"Error\"", String.valueOf(planNoError));
            if ((planNoPass + planNoFail + planNoError) > 0) {
                overAllTable.addElement("", roundDecimal(planNoPass * 100.0 / (planNoPass + planNoFail + planNoError)) + "%");
            } else {
                overAllTable.addElement("", "0%");
            }
            overAllTable.closeRow();
            // OverAll result
            HTMLGenerator testPlanHTML = new HTMLGenerator ("./", "result.html");
            testPlanHTML.addTitle("");
            testPlanHTML.addLink("<link rel=\"stylesheet\" type=\"text/css\" href=\"https://cm.woox.com/jenkins-theme/style.css\">");
            testPlanHTML.addChart("The Status of Test Plan", planNoPass, planNoFail, planNoSkip, planNoError);
            testPlanHTML.addnOtherChart("The OS Distribution of Test Plan", osChartContent, "osChart");
            testPlanHTML.closeHead();
            testPlanHTML.addDefaultStyle();
            testPlanHTML.closeStyle();
            // content
            testPlanHTML.addDefaultHeader("");
            testPlanHTML.addBody("<div id=\"resultBody\">");
            testPlanHTML.addPlanTitle();
            testPlanHTML.addBody("<table id=\"overall\">");
            testPlanHTML.addBody("<tr>");
            testPlanHTML.addBody("<td align=\"center\" id=\"overall\">");
            testPlanHTML.addBody(overAllTable.outputTable());
            testPlanHTML.addBody("</td>");
            testPlanHTML.addBody("<td align=\"center\" id=\"overall\">");
            testPlanHTML.addBody(osAllTable.outputTable());
            testPlanHTML.addBody("</td>");
            testPlanHTML.addBody("</tr>");
            testPlanHTML.addBody("<tr>");
            testPlanHTML.addBody("<td align=\"center\" id=\"overall\">");
            testPlanHTML.addBody("<div id=\"chart_div\"></div>");
            testPlanHTML.addBody("</td>");
            testPlanHTML.addBody("<td align=\"center\" id=\"overall\">");
            testPlanHTML.addBody("<div id=\"osChart\"></div>");
            testPlanHTML.addBody("</td>");
            testPlanHTML.addBody("</tr>");
            testPlanHTML.addBody("</table>");
            testPlanHTML.addBody("<br/>");
            testPlanHTML.addBody(testPlanTable.outputTable());
            testPlanHTML.addBody("<br/>");
            testPlanHTML.addBody("<a href=\"./console.log\">Overall log in txt</a>");
            testPlanHTML.addDefaultFooter("");
            testPlanHTML.addBody("</div>");
            testPlanHTML.addBody("</div>");
            testPlanHTML.closeHTML();



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("overAllTable:");
        System.out.println(overAllTable.outputTable());
        System.out.println("testPlanTable:");
        System.out.println(testPlanTable.outputTable());
        System.out.println("testCaseTable:");
        System.out.println(testCaseTable.outputTable());
        System.out.println("devicesTable:");
        System.out.println(devicesTable.outputTable());
        System.out.println("osAllTable:");
        System.out.println(osAllTable.outputTable());
        
    }
    public int compareStatus (String status) {
        if (status.matches("(.*)Error(.*)")) {
                return 0;
            } else if (status.matches("(.*)Failed(.*)")) {
                    return 1;
                } else if (status.matches("(.*)Passed(.*)")) {
                        return 2;
                    } else if (status.matches("(.*)Skipped(.*)")) {
                            return 3;
                        } else {
                            return 4;
                        }
    }

    public double roundDecimal(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.0");
        return Double.valueOf(twoDForm.format(d));
    }
}