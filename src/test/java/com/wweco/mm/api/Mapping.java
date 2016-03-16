package com.wweco.mm.api;

import com.api.*;
import com.wweco.mm.api.*;

public class Mapping {

	public String appID;
	public Mapping (String appId) {
		appID = appId;
	}

	public enum Content {
	    XPATH,
	    ID,
	    NAME,
	    VALUE,
	    LABEL,
	    XAXIS,
	    YAXIS,
	    WIDTH,
	    HEIGHT,
	    TYPE
  	}

  	//sync with MappingAndroid and MappingiOS
	public String accessWithoutLoginButton (String selectContent) {
		return "";
	}

	//item attribute

	//item attribute

}