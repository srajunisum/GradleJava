package com.test;

import java.util.Properties;

import org.w3c.dom.Document;

import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfs.japi.YFSEnvironment;

public class OderLineLevelHold  implements YIFCustomApi{

	
Properties prop; 
	
	@Override
	public void setProperties(Properties prop) throws Exception {
		
		this.prop  = prop;
		
		
	}
	
public Document applyHold(YFSEnvironment env, Document doc) throws Exception {
		
		Document outputDoc = null;
		String strHoldType = prop.getProperty("ORDER_HOLD_TYPE");
		
		if(NSMXMLUtil.isValidString(strHoldType)){
			
			outputDoc = NSMXMLUtil.applyHoldOrderLevel(env, doc, strHoldType);
		
		}
		return outputDoc;
		
	}

	public Document resolveHold(YFSEnvironment env, Document doc) throws Exception {
		
		Document outputDoc = null;
		String strHoldType = prop.getProperty("ORDER_HOLD_TYPE");
		
		if(NSMXMLUtil.isValidString(strHoldType)){
			
			outputDoc = NSMXMLUtil.resolveHoldOrderLevel(env, doc, strHoldType);
		
		}
		return outputDoc;
		
	}

}
