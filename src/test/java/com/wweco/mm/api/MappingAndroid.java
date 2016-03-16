package com.wweco.mm.api;

import com.api.*;
import com.wweco.mm.api.*;

public class MappingAndroid extends Mapping {

	//private String appID = "com.gibson.philips.mojo";
	
	public MappingAndroid(String appId) {
		super(appId);
	}
	
	//each state of item create 1 attribute item and each itemName is sync with MappingiOS
	public String accessWithoutLoginButton(String selectContent) {
		Content select = Content.valueOf(selectContent);
		switch (select) {
			case XPATH:
				return "//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.View[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.EditText[1]";
			case ID:
				return "com.mm.storefront.app:id/guest_login_in_button";
			case NAME:
				return "na";
			case VALUE:
				return "直接开始逛逛";
			case LABEL:
				return "na";
			case XAXIS:
				return "240";
			case YAXIS:
				return "588";
			case WIDTH:
				return "600";
			case HEIGHT:
				return "136";
			case TYPE:
				return "android.widget.EditText";
			default:
				return "no such content";
		}
	}

	
	//Item Attribute

	//Item Attribute

}