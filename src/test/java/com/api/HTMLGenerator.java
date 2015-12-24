package com.api;

import com.api.*;
import org.apache.commons.io.FileUtils;
import java.text.SimpleDateFormat;
import java.io.*;

public class HTMLGenerator {

	private String modelName;
	// private String testCase;
	// private String testStep;
	private String osVer;
	private PrintWriter writerTxt;
	private String testCaseResult = "N/A";
	private String writingState;
	public static String jobName;
	public static String buildNo;

	public HTMLGenerator (String htmlPath, String htmlFile) throws Exception {
		String folderPath = htmlPath;
		(new File (folderPath)).mkdirs();
		File file = new File(htmlPath + htmlFile);
		// file.delete();
		writerTxt = new PrintWriter(htmlPath + htmlFile, "UTF-8");
		writerTxt.println("<!DOCTYPE html>");
		writerTxt.println("<html>");
		writerTxt.println("<head>");
		writingState = "title";
	}

	public void closeHTML () throws Exception {
		if (writingState.equals("body")) {
			writerTxt.println("</body>");
			writerTxt.println("</html>");
			writerTxt.close();
		} else {
			throw new TestingException("It is not correct position to close HTML.");
		}
	}

	public void addTitle (String htmlTitle) throws Exception {
		if (writingState.equals("title")) {
			if (htmlTitle == "") {
				writerTxt.println("<title>" + jobName + " No." + buildNo + " Result</title>");
			} else {
				writerTxt.println("<title>" + htmlTitle + "</title>");
			}
			writingState = "closeHead";
		} else {
			throw new TestingException("It is not correct position to input title.");
		}
	}

	public void addLink (String htmlLink) throws Exception {
		if (writingState.equals("closeHead")) {
			writerTxt.println(htmlLink);
		} else {
			throw new TestingException("It is not correct position to input link.");
		}
	}

	public void addScript (String htmlScript) throws Exception {
		if (writingState.equals("closeHead")) {
			writerTxt.println(htmlScript);
		} else {
			throw new TestingException("It is not correct position to input Script.");
		}
	}

	public void closeHead () throws Exception {
		if (writingState.equals("closeHead")) {
			writerTxt.println("</head>");
			writerTxt.println("<style>");
			writingState = "style";
		} else {
			throw new TestingException("It is not correct position to close head.");
		}
	}

	public void addStyle (String htmlStyle) throws Exception {
		if (writingState.equals("style")) {
			writerTxt.println(htmlStyle);
		} else {
			throw new TestingException("It is not correct position to input Style.");
		}
	}

	public void closeStyle () throws Exception {
		if (writingState.equals("style")) {
			writerTxt.println("</style>");
			writerTxt.println("<body>");
			writingState = "body";
		} else {
			throw new TestingException("It is not correct position to close style.");
		}
	}

	public void addBody (String htmlStyle) throws Exception {
		if (writingState.equals("body")) {
			writerTxt.println(htmlStyle);
		} else {
			throw new TestingException("It is not correct position to input body.");
		}
	}

	public void addDefaultStyle () throws Exception {
		addStyle("#screenlen{max-width:1000px}");
		addStyle("table, th, td {border: 1px solid black;}");
		addStyle("table {border-spacing: 1px;margin: 0px 0px;}");
		addStyle("table#overall {border-spacing: 0px;margin: 0px 0px; border: none;}");
		addStyle("td#overall {border-spacing: 0px;margin: 0px 0px; border: none;vertical-align: top}");
		addStyle("th {text-align: left;vertical-align: center; min-width:60px}");
		addStyle("body {margin: 0px;}");
		addStyle("p {display: inline}");
		addStyle(".login {position: relative;");
		addStyle("top: 6px;margin-right: 10px;");
		addStyle("padding-top: 8px;float: right;");
		addStyle("display: inline-block; height: inherit;}");
		addStyle(".logo { display: inline-block;}");
		addStyle("div {font-family: Helvetica, Arial, sans-serif; font-size: 13px;}");
		addStyle("#Failed {color: red; max-width:450px; word-wrap: break-word;}");
		addStyle("button#Failed { background:none; border:none; min-width:95px}");
		addStyle("#Passed {color: green; max-width:450px; word-wrap: break-word;}");
		addStyle("button#Passed { background:none; border:none; min-width:95px}");
		addStyle("#Skipped {color: blue; max-width:450px; word-wrap: break-word;}");
		addStyle("button#Skipped { background:none; border:none; min-width:95px}");
		addStyle("#Error {font-weight: bold; max-width:450px; word-wrap: break-word;}");
		addStyle("#button#Error { background:none; border:none; min-width:95px}");
		addStyle("#resultBody { margin-left: 10px;}");
	}

	public void addDefaultHeader (String jbName) throws Exception {
		if (!jbName.equals("")) {
			jobName = jbName;
		}
		addBody("<div id=\"page-body\"><div id=\"header\">");
		addBody("<div class=\"logo\"><a id=\"jenkins-home-link\" href=\"/\">");
		addBody("<img id=\"jenkins-head-icon\" alt=\"title\" src=\"/static/64141ff4/images/headshot.png\">");
		addBody("<img id=\"jenkins-name-icon\" height=\"34\" alt=\"title\" width=\"139\" src=\"/static/64141ff4/images/title.png\">");
		addBody("</a></div>");
		addBody("<div class=\"login\"><a href=\"https://unicorn.int.gibson.com/job/" + jobName + "\">Return to " + jobName + "</a>");
		addBody("</div></div>");
	}

	public void addDefaultFooter (String bdNo) throws Exception {
		if (!bdNo.equals("")) {
			buildNo = bdNo;
		}
		addBody("<p> (Caution: without appium server log and jenkins reply)</p><br/>");
		addBody("<a href=\"https://unicorn.int.gibson.com/job/" + jobName + "/" + buildNo + "/console\">Overall log in console</a><br/>");
		addBody("<a href=\"https://unicorn.int.gibson.com/job/" + jobName + "/" + buildNo + "/artifact/*zip*/archive.zip\">Download all the Artifacts</a>");
	}

	public void addPipeChartScript () throws Exception {
		addScript("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
		addScript("<script type=\"text/javascript\">");
		addScript("google.load('visualization', '1.0', {'packages':['corechart']});");
		addScript("google.setOnLoadCallback(drawChart);");
	}

	public void addPlanTitle () throws Exception {
		writerTxt.println("<h1>" + jobName + " No." + buildNo + " Result</h1>");
	}

	public void addChart (String chartTitle, int passed, int failed, int skipped, int errors) throws Exception {
		addPipeChartScript();
        addScript("function drawChart() {");
        addScript("var data = new google.visualization.DataTable();");
        addScript("data.addColumn('string', 'Status');");
        addScript("data.addColumn('number', 'Cases');");
        addScript("data.addRows([ ['Passed', " + passed + "], ['Failed', " + failed + "], ['Skipped', " + skipped + "], ['Error', " + errors + "] ]);");
        addScript("var options = {'title':'" + chartTitle + "', 'is3D':true, 'width':400, 'height':300,");
    	addScript("colors: ['#00aa00', '#ff0000', '#0070ff', '#444444'],");
    	addScript("chartArea:{left:70,top:20, width:\"100%\",height:\"400\"} };");
        addScript("var chart = new google.visualization.PieChart(document.getElementById('chart_div'));");
        addScript("chart.draw(data, options);");
        addScript("}");
        addScript("</script>");
    }

    public void addnOtherChart (String chartTitle, String content, String chartID) throws Exception {
		addPipeChartScript();
        addScript("function drawChart() {");
        addScript("var data = new google.visualization.DataTable();");
        addScript("data.addColumn('string', 'Status');");
        addScript("data.addColumn('number', 'Cases');");
        addScript("data.addRows([" + content + "]);");
        addScript("var options = {'title':'" + chartTitle + "', 'is3D':true, 'width':400, 'height':300,");
    	addScript("chartArea:{left:70,top:20, width:\"100%\",height:\"400\"} };");
        addScript("var chart = new google.visualization.PieChart(document.getElementById('" + chartID + "'));");
        addScript("chart.draw(data, options);");
        addScript("}");
        addScript("</script>");
    }
      
}