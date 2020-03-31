package com.test;

import java.util.Map;

import com.yantra.ycp.core.YCPContext;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSGetOrderNoUE;

public class CreateCustomerOrderNumberUE  implements YFSGetOrderNoUE  {

	
	
	@Override
	public String getOrderNo(YFSEnvironment env, Map inMap)
			throws YFSUserExitException {
		
		System.out.println(" started getOrderNo method");
		String seqNoStr="";
		String strPrefix="";
		String strDocumentType="";
		String strOrderNo="";

		try
		{
		strDocumentType = inMap.get("DocumentType").toString();
		if("0001".equals(strDocumentType)){
		strPrefix = "S_";}
		else if("0003".equals(strDocumentType)){
		strPrefix="R_";}
		long seqNo=((YCPContext) env).getNextDBSeqNo("SEQ_YFS_ORDER_NO");
		seqNoStr= Long.toString(seqNo);
		strOrderNo = strPrefix+seqNoStr;
		}
		catch(Exception e1){
		throw new YFSUserExitException("Error in getOrderNo()\n" +e1.getStackTrace());

		}
		System.out.println(" ended getOrderNo method");
		return strOrderNo;
		
		}
	}



