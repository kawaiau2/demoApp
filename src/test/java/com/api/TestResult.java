package com.api;

import com.api.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TestResult {
	public static HashMap testCase = new HashMap();
	public static String testIDs = "";
	public static String modelNames = "";

	public TestResult (String testID, String modelName) {
		if (!testID.equals("")){
			if (!testIDs.matches("(.*)" + testID + "(.*)")){
				testIDs = testID + "///" + testIDs;
			}
			if (!modelNames.matches("(.*)" + modelName + "(.*)")) {
				modelNames = modelName + "///" + modelNames;
			}
		}
	}
	
	public synchronized void defaultTest() {
		testCase = new HashMap();
	}

	public synchronized void putTestResult (String testID, String testResult) {
		if (containTestID(testID)){
			testCase.put(testID, testResult + "///" + getTestResult(testID));
		} else {
			testCase.put(testID, testResult);
		}
	}

	public synchronized boolean containTestID (String testID) {
		return testCase.containsKey(testID);
	}

	public synchronized String getTestResult (String testID) {
		return (String) testCase.get(testID);
	}

	public synchronized void resetTest() {
		testCase.clear();
	}

	public synchronized HashMap outputAllResult() {
		return testCase;
	}

	public synchronized String outTestIDs() {
		return testIDs;
	}

	public synchronized String outModelNames() {
		return modelNames;
	}

}