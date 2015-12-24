package com.api;

import com.api.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.lang.String;

public class HTMLTable{
	private String table ="";

	public HTMLTable (String attribute) {
		table = "<table " + attribute + ">\n";
	}

	public void openRow (String attribute) {
		table = table + "<tr " + attribute + ">\n";
	}

	public void closeRow () {
		table = table + "</tr>\n";
	}

	public void addHeadElement (String attribute, String content) {
		table = table + "<th " + attribute + ">" + content + "</th>\n";
	}

	public void addElement (String attribute, String content) {
		table = table + "<td " + attribute + ">" + content + "</td>\n";
	}

	public String outputTable () {
		return table + "</table>";
	}
}
