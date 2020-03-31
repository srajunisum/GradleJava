package com.agent;

import com.test.NSMXMLUtil;
import com.yantra.ycp.japi.util.YCPBaseTaskAgent;
import com.yantra.yfs.japi.YFSEnvironment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AutoHoldResolve extends YCPBaseTaskAgent {
    @Override
    public Document executeTask(YFSEnvironment yfsEnvironment, Document document) throws Exception {
        System.out.println("AutoHoldResolve start");
        String orderHeaderKey = document.getDocumentElement().getAttribute("DataKey");
        Document getOrderListOutDoc = getOrderListInput(yfsEnvironment, orderHeaderKey);
        Document response = invokeChangeOrderAPI(yfsEnvironment, getOrderListOutDoc);
        System.out.println("AutoHoldResolve end");
        return response;
    }
    private static Document getOrderListInput( YFSEnvironment env, String orderHeaderKey) throws Exception{
        String strOrderListTemp = "<OrderList><Order OrderNo='' OrderHeaderKey=''><OrderHoldTypes><OrderHoldType HoldType='' Status=''/></OrderHoldTypes></Order></OrderList>";
        Document document = NSMXMLUtil.createDocument("Order");
        Element element = document.getDocumentElement();
        element.setAttribute("OrderHeaderKey", orderHeaderKey);
        Document response = NSMXMLUtil.invokeAPI(env,"getOrderList", document , NSMXMLUtil.createFromString(strOrderListTemp));

        return response;
    }

    private static Document invokeChangeOrderAPI(YFSEnvironment env, Document document) throws  Exception{
        Document changeOrder = NSMXMLUtil.createDocument("Order");
        Element rootElement = changeOrder.getDocumentElement();
        Element element = NSMXMLUtil.getChildElement(document.getDocumentElement(), "Order");
        String strOHKey = element.getAttribute("OrderHeaderKey");
        rootElement.setAttribute("OrderHeaderKey", strOHKey );
        Element eleOrderHoldType = NSMXMLUtil.createChild(rootElement, "OrderHoldTypes");
        Element eleOrderHold = NSMXMLUtil.createChild(eleOrderHoldType, "OrderHoldType");

        Element orderHoldTypesElem = NSMXMLUtil.getChildElement(element, "OrderHoldTypes");
        NodeList nodeList = orderHoldTypesElem.getElementsByTagName("OrderHoldType");
        for(int i = 0 ; i< nodeList.getLength(); i ++){
            Element elem = (Element)nodeList.item(i);
            NSMXMLUtil.copyElement(changeOrder, elem, eleOrderHold);
            String status =  eleOrderHold.getAttribute("Status");
            if("1100".equalsIgnoreCase(status)){
                eleOrderHold.setAttribute("Status", "1300");
            }
        }

        System.out.println(" change order input xml " +NSMXMLUtil.getXmlString(changeOrder));
        Document response = NSMXMLUtil.invokeAPI(env,"changeOrder", changeOrder );
        System.out.println("change order Response : " + NSMXMLUtil.getXmlString(response));
        Element orderHoldTypeElem = NSMXMLUtil.getChildElement(orderHoldTypesElem, "OrderHoldType");
        return response;
    }

}
