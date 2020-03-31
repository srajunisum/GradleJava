package com.test;
import java.util.Map;


import com.yantra.ycp.japi.YCPDynamicCondition;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfs.japi.YFSEnvironment;
 
public class CustomDynamicCondition implements YCPDynamicCondition {
 
	
	/* 
	 *  name is the name of the condition configured in the database
	 *  mapData contains the name-value pair of strings. Keys are same as the variables available in condition builder.
	 *  xmlData : XML Data
	 */
	@Override
	public boolean evaluateCondition(YFSEnvironment env, String name, Map mapData, String xmlData) {		
		try {
			System.out.println("started evaluateCondition method");
            YFCDocument xml = YFCDocument.getDocumentFor(xmlData);
            // walk thru the xml and evaluate condition
            // Let us say, in this condition we are checking if value of the attribute "OrderNumber"
            // under the root node is empty or not  
            /*<Order OrderNumber="">*/
            String attrVal = xml.getDocumentElement().getAttribute("EnterpriseCode");
            if (attrVal == null || attrVal.trim().length() == 0)
            return false;
            else
            return true;
		}
		catch (Exception ex) {
		    throw new RuntimeException("CustomDynamicCondition failed due to ...");
		}
	}
 
}