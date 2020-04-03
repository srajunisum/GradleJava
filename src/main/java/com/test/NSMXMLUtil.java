package com.test;

/**
 * This class contains all the utility methods which may be used in the application.
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class NSMXMLUtil {

    public static final String YANTRA_ISNAMESPACEWARE = "yantra.document.isnamespaceaware";
    public static final String RAWTYPES = "rawtypes";

    //private static YFCLogCategory logger = (YFCLogCategory) YFCLogCategory.getLogger(NSMXMLUtil.class.getName());

    public static String getXmlString(Document inputDocument)

            throws IllegalArgumentException {

        // Validate input document

        if (inputDocument == null) {
            throw new IllegalArgumentException("Input Document cannot be null in " + "XmlUtils.getXMLString method");
        }
        return YFCDocument.getDocumentFor(inputDocument).getString();

    }

    /**
     * Utility method to check if a given object is void (just null check).
     *
     * @param obj Object for void check.
     * @return true if the given object is null.
     *         <p>
     *         </p>
     */
    public static boolean isVoid(Object obj) {
        // return (obj == null) ? true : false;
        boolean retVal = false;
        if (obj == null) {
            retVal = true;

        }
        return retVal;
    }

    /**
     * Creates a child element under the parent element with given child name.
     * Returns the newly created child element. This method returns null if either
     * parent is null or child name is void.
     *
     * @param parentEle parentElement
     * @param childName childName
     * @return Element
     */
    public static Element createChild(Element parentEle, String childName) {
        Element child = null;
        if (parentEle != null && !isVoid(childName)) {
            child = parentEle.getOwnerDocument().createElement(childName);
            parentEle.appendChild(child);
        }
        return child;
    }

    /**
     * Gets the child element with the given name. If not found returns null. This
     * method returns null if either parent is null or child name is void.
     *
     * @param parentEle parentEle
     * @param childName childName
     * @return Element
     */
    public static Element getChildElement(Element parentEle, String childName) {
        return getChildElement(parentEle, childName, false);
    }

    public static void copyAttributes(Element eleSource, Element eleDest) {

        if (null != eleSource) {
            NamedNodeMap attrMap = eleSource.getAttributes();
            int attrLength = attrMap.getLength();
            for (int count = 0; count < attrLength; count++) {
                Node attr = attrMap.item(count);
                String attrName = attr.getNodeName();
                String attrValue = attr.getNodeValue();
                eleDest.setAttribute(attrName, attrValue);
            }
        }

    }

    /**
     * Gets the child element with the given name. If not found: 1) a new element
     * will be created if "createIfNotExists" is true. OR 2) null will be returned
     * if "createIfNotExists" is false. This method returns null if either parent is
     * null or child name is void.
     *
     * @param parentEle         parentEle
     * @param childName         childName
     * @param createIfNotExists createIfNotExists flag
     * @return Element
     */
    public static Element getChildElement(Element parentEle, String childName, boolean createIfNotExists) {

        Element child = null;
        if (parentEle != null && childName != null) {
            for (Node n = parentEle.getFirstChild(); n != null; n = n.getNextSibling()) {
                if (n.getNodeType() == Node.ELEMENT_NODE && n.getNodeName().equals(childName)) {
                    return (Element) n;
                }
            }
            if (createIfNotExists) {
                child = createChild(parentEle, childName);

            }
        }
        return child;
    }

    public static Document createDocument(String documentElement)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element ele = document.createElement(documentElement);
        document.appendChild(ele);
        return document;
    }

    /**
     *
     * This method takes a document Element as input and returns the XML String.
     *
     * @param element a valid element object for which XML output in String form is
     *                required.
     * @return XML String of the given element
     */

    public static String getElementXMLString(Element element) {
        return NSMXMLUtil.serialize(element);
    }

    /**
     * Returns a formatted XML string for the Node, using encoding 'iso-8859-1'.
     *
     * @param node a valid document object for which XML output in String form is
     *             required.
     *
     * @return the formatted XML string.
     */

    public static String serialize(Node node) {
        return NSMXMLUtil.serialize(node, "iso-8859-1", true);
    }

    /**
     * Return a XML string for a Node, with specified encoding and indenting flag.
     * <p>
     * <b>Note:</b> only serialize DOCUMENT_NODE, ELEMENT_NODE, and
     * DOCUMENT_FRAGMENT_NODE
     *
     * @param node      the input node.
     * @param encoding  such as "UTF-8", "iso-8859-1"
     * @param indenting indenting output or not.
     *
     * @return the XML string
     */
    public static String serialize(Node node, String encoding, boolean indenting) {
        OutputFormat outFmt = null;
        StringWriter strWriter = null;
        XMLSerializer xmlSerializer = null;
        String retVal = null;

        try {
            outFmt = new OutputFormat("xml", encoding, indenting);
            outFmt.setOmitXMLDeclaration(true);

            strWriter = new StringWriter();

            xmlSerializer = new XMLSerializer(strWriter, outFmt);

            short ntype = node.getNodeType();

            switch (ntype) {
                case Node.DOCUMENT_FRAGMENT_NODE:
                    xmlSerializer.serialize((DocumentFragment) node);
                    break;
                case Node.DOCUMENT_NODE:
                    xmlSerializer.serialize((Document) node);
                    break;
                case Node.ELEMENT_NODE:
                    xmlSerializer.serialize((Element) node);
                    break;
                default:
                    throw new IOException("Can serialize only Document, DocumentFragment and Element type nodes");
            }

            retVal = strWriter.toString();
        } catch (IOException e) {
            retVal = e.getMessage();
        } finally {
            try {
                strWriter.close();
            } catch (IOException ie) {
                retVal = ie.getMessage();

            }
        }

        return retVal;
    }

    public static YFCElement importElement(YFCElement parentEle, YFCElement ele2beImported) {
        YFCElement child = null;
        if (parentEle != null && ele2beImported != null) {
            child = (YFCElement) parentEle.getOwnerDocument().importNode(ele2beImported, true);
            parentEle.appendChild(child);
        }
        return child;

    }

    /**
     * Imports an element including the subtree from another document under the
     * parent element. Returns the newly created child element. This method returns
     * null if either parent or element to be imported is null.
     *
     * @param parentEle      Element
     * @param ele2beImported Element
     * @return Element
     */
    public static Element importElement(final Element parentEle, final Element ele2beImported) {
        Element child = null;
        if (parentEle != null && ele2beImported != null) {
            child = (Element) parentEle.getOwnerDocument().importNode(ele2beImported, true);
            parentEle.appendChild(child);
        }
        return child;
    }

    public static void copyElement(Document destDoc, Element srcElem, Element destElem) {
        NamedNodeMap attrMap = srcElem.getAttributes();
        int attrLength = attrMap.getLength();
        for (int count = 0; count < attrLength; count++) {
            Node attr = attrMap.item(count);
            String attrName = attr.getNodeName();
            String attrValue = attr.getNodeValue();
            destElem.setAttribute(attrName, attrValue);
        }

        if (srcElem.hasChildNodes()) {
            NodeList childList = srcElem.getChildNodes();
            int numOfChildren = childList.getLength();
            for (int cnt = 0; cnt < numOfChildren; cnt++) {
                Object childSrcNode = childList.item(cnt);
                if (childSrcNode instanceof CharacterData) {
                    if (childSrcNode instanceof Text) {
                        String data = ((CharacterData) childSrcNode).getData();
                        Node childDestNode = destDoc.createTextNode(data);
                        destElem.appendChild(childDestNode);
                    } else if (childSrcNode instanceof Comment) {
                        String data = ((CharacterData) childSrcNode).getData();
                        Node childDestNode = destDoc.createComment(data);
                        destElem.appendChild(childDestNode);
                    }
                } else {
                    Element childSrcElem = (Element) childSrcNode;
                    Element childDestElem = NSMXMLUtil.appendChild(destDoc, destElem, childSrcElem.getNodeName(), null);
                    NSMXMLUtil.copyElement(destDoc, childSrcElem, childDestElem);
                }
            }
        }
    }

    /**
     * This method is for adding child Nodes to parent node element, the child
     * element has to be created first.
     *
     * @param doc           Documen
     * @param parentElement Parent Element under which the new Element should be
     *                      present
     * @param elementName   Name of the element to be created
     * @param value         Can be either a String ,just the element value if it is
     *                      a single attribute
     * @return Element
     */
    public static Element appendChild(Document doc, Element parentElement, String elementName, Object value) {
        Element childElement = NSMXMLUtil.createElement(doc, elementName, value);
        parentElement.appendChild(childElement);
        return childElement;
    }

    /**
     * Creates an element with the supplied name and attributevalues.
     *
     * @param doc            XML Document on which to create the element
     * @param elementName    the name of the node element
     * @param hashAttributes usually a Hashtable containing name/value pairs for the
     *                       attributes of the element.
     * @return Element
     */
    public static Element createElement(Document doc, String elementName, Object hashAttributes) {
        return NSMXMLUtil.createElement(doc, elementName, hashAttributes, false);
    }

    /**
     * Create an element with either attributes or text node.
     *
     * @param doc            the XML document on which the node has to be created
     * @param elementName    the name of the element to be created
     * @param hashAttributes the value for the text node or the attributes for the
     *                       node element
     * @param textNodeFlag   a flag signifying whether te node to be created is the
     *                       text node
     * @return Element
     */
    @SuppressWarnings(RAWTYPES)
    public static Element createElement(Document doc, String elementName, Object hashAttributes, boolean textNodeFlag) {
        Element elem = doc.createElement(elementName);
        if (hashAttributes != null) {
            if (hashAttributes instanceof String) {
                if (textNodeFlag) {
                    elem.appendChild(doc.createTextNode((String) hashAttributes));
                }
            } else if (hashAttributes instanceof Hashtable) {
                Enumeration e = ((Hashtable) hashAttributes).keys();
                while (e.hasMoreElements()) {
                    String attributeName = (String) e.nextElement();
                    String attributeValue = (String) ((Hashtable) hashAttributes).get(attributeName);
                    elem.setAttribute(attributeName, attributeValue);
                }
            }
        }
        return elem;
    }

    /**
     * Same as getSubNodeList().
     *
     * //@see #getSubNodeList(Element, String).
     * @param startElement startElement
     * @param elemName     element Name
     * @return List
     */
    @SuppressWarnings({RAWTYPES, "unchecked"})
    public static List getElementsByTagName(Element startElement, String elemName) {
        NodeList nodeList = startElement.getElementsByTagName(elemName);
        List elemList = new ArrayList();
        for (int count = 0; count < nodeList.getLength(); count++) {
            elemList.add(nodeList.item(count));
        }
        return elemList;
    }

    public static Document createFromString(String elementXMLString) {

        YFCDocument yfcDocument = YFCDocument.getDocumentFor(elementXMLString);

        return yfcDocument.getDocument();
    }

    /**
     * @param env
     * @param apiName
     * @param inXMLDoc
     * @return outXML doc
     * @throws Exception invoke API within the system, avoids instantiating YIFApi
     *                   in every method/class
     */
    public static Document invokeAPI(YFSEnvironment env, String apiName, Document inXMLDoc) throws Exception {
        try {
            YIFApi yifApi = YIFClientFactory.getInstance().getLocalApi();
            Document outXML = yifApi.invoke(env, apiName, inXMLDoc);
            return outXML;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Document invokeAPI(YFSEnvironment env, String apiName, Document inXMLDoc, Document templateDoc)
            throws Exception {
        try {
            YIFApi yifApi = YIFClientFactory.getInstance().getLocalApi();
            env.setApiTemplate(apiName, templateDoc);
            Document outXML = yifApi.invoke(env, apiName, inXMLDoc);
            env.clearApiTemplate(apiName);
            return outXML;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Document invokeAPI(YFSEnvironment env, String apiName, Document inXMLDoc, String templateStr)
            throws Exception {
        try {

            YIFApi yifApi = null;
            try {
                yifApi = (YIFApi) YIFClientFactory.getInstance().getLocalApi();
            } catch (YIFClientCreationException ycce) {
                throw new YFSException("error! API instantiation \n \n " + ycce.getMessage());
            }
            try {

                env.setApiTemplate(apiName, DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new ByteArrayInputStream(templateStr.getBytes("utf-8")))));

            } catch (SAXException s) {

                s.printStackTrace();
            }

            Document outXML = yifApi.invoke(env, apiName, inXMLDoc);
            env.clearApiTemplate(apiName);

            return outXML;

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     *
     * @param env
     * @param templateName
     * @param apiName
     * @param inDoc
     * @return
     * @throws Exception This method invokesApi with output template file name , the
     *                   template file should be in
     *                   <INSTALL_DIR>/extensions/global/template/api/TEMPLATE_NAME.xml
     */
    public static Document invokeAPI(YFSEnvironment env, String templateName, String apiName, Document inDoc)
            throws Exception {
        env.setApiTemplate(apiName, templateName);
        Document returnDoc = YIFClientFactory.getInstance().getApi().invoke(env, apiName, inDoc);
        env.clearApiTemplate(apiName);
        return returnDoc;
    }

    public static Document executeFlow(YFSEnvironment env, String flowName, Document inXMLDoc) throws Exception {
        try {
            YIFApi yifApi = null;
            try {
                yifApi = (YIFApi) YIFClientFactory.getInstance().getLocalApi();
            } catch (YIFClientCreationException ycce) {
                throw new YFSException("error! API instantiation \n \n " + ycce.getMessage());
            }
            Document outXML = yifApi.executeFlow(env, flowName, inXMLDoc);
            return outXML;
        } catch (Exception e) {
            throw e;
        }
    }

    public static boolean isValidString(String value) {
        return ((value != null) && (value.trim().length() > 0));
    }

    /**
     *
     * @param env
     * @param inDocXML
     * @param holdType
     * @return
     * @throws Exception
     */
    public static Document applyHoldOrderLevel(YFSEnvironment env, Document inDocXML, String holdType) throws Exception {
        String strTemp = "<OrderList><Order OrderNo='' OrderHeaderKey=''><OrderHoldTypes><OrderHoldType HoldType='' Status=''/></OrderHoldTypes></Order></OrderList>";
        boolean isHoldApplied = false;
        Document outputDoc = null;
        YFCDocument yfcDocument = YFCDocument.getDocumentFor(strTemp);
        String strOrderNo = inDocXML.getDocumentElement().getAttribute("OrderNo");

        String strDocumentType = inDocXML.getDocumentElement().getAttribute("DocumentType");
        String strEnterpriseCode = inDocXML.getDocumentElement().getAttribute("EnterpriseCode");
        Document inDoc = createDocument("Order");
        Element inDocELe = inDoc.getDocumentElement();
        inDocELe.setAttribute("OrderNo", strOrderNo);
        inDocELe.setAttribute("DocumentType", strDocumentType);
        inDocELe.setAttribute("EnterpriseCode", strEnterpriseCode);

        if (!isVoid(inDoc) || inDoc != null) {
            Document getOrdLstDoc = invokeAPI(env, "getOrderList", inDoc, yfcDocument.getDocument());
            Element element = getOrdLstDoc.getDocumentElement();
            Element eleOrder = getChildElement(element, "Order");
            Element eleHoldTypes = getChildElement(eleOrder, "OrderHoldTypes");
            NodeList list = eleHoldTypes.getElementsByTagName("OrderHoldType");
            for (int i = 0; i < list.getLength(); i++) {
                Element holdTypeEle = (Element) list.item(i);
                String strHoldType = holdTypeEle.getAttribute("HoldType");
                if (isValidString(holdType) &&
                        holdType.equalsIgnoreCase(strHoldType)) {
                    isHoldApplied = true;
                }
            }
            if (!isHoldApplied) {
                // changeOrder Api to apply the orde hold
                Document changeOrderIP = prepareChangeOrderInput(getOrdLstDoc, holdType);
                outputDoc = invokeAPI(env, "changeOrder", changeOrderIP);
            }
        }
        return outputDoc;
    }

    private static Document prepareChangeOrderInput(Document getOrdLstDoc, String holdType) throws Exception {
        Document document = createDocument("Order");
        Element element = document.getDocumentElement();
        Element element1 = getOrdLstDoc.getDocumentElement();
        Element eleOrder = getChildElement(element1, "Order");
        String strOHKey = eleOrder.getAttribute("OrderHeaderKey");
        element.setAttribute("OrderHeaderKey", strOHKey);
        Element orderHoldTypes = createChild(element, "OrderHoldTypes");
        Element eleOrderHoldType = createChild(orderHoldTypes, "OrderHoldType");
        eleOrderHoldType.setAttribute("HoldType", holdType);
        eleOrderHoldType.setAttribute("Status", "1100");
        return document;
    }

    /**
     *
     * @param env
     * @param inDocXML
     * @param holdType
     * @return
     * @throws Exception
     */
    public static Document resolveHoldOrderLevel(YFSEnvironment env, Document inDocXML, String holdType) throws Exception {
        String strTemp = "<OrderList><Order OrderNo='' OrderHeaderKey=''><OrderHoldTypes><OrderHoldType HoldType='' Status=''/></OrderHoldTypes></Order></OrderList>";
        boolean isHoldApplied = false;
        Document outputDoc = null;
        YFCDocument yfcDocument = YFCDocument.getDocumentFor(strTemp);
        if (!isVoid(inDocXML) || inDocXML != null) {
            Document getOrdLstDoc = invokeAPI(env, "getOrderList", inDocXML, yfcDocument.getDocument());
            Element element = getOrdLstDoc.getDocumentElement();
            Element eleOrder = getChildElement(element, "Order");
            Element eleHoldTypes = getChildElement(eleOrder, "OrderHoldTypes");
            NodeList list = eleHoldTypes.getElementsByTagName("OrderHoldType");
            for (int i = 0; i < list.getLength(); i++) {
                Element holdTypeEle = (Element) list.item(i);
                String strHoldType = holdTypeEle.getAttribute("HoldType");
                if (isValidString(holdType) &&
                        holdType.equalsIgnoreCase(strHoldType)) {
                    isHoldApplied = true;
                }
            }
            if (isHoldApplied) {
                // changeOrder Api to apply the orde hold
                Document changeOrderIP = prepareChangeOrderOutput(getOrdLstDoc, holdType);
                outputDoc = invokeAPI(env, "changeOrder", changeOrderIP);
            }
        }
        return outputDoc;
    }

    private static Document prepareChangeOrderOutput(Document getOrdLstDoc, String holdType) throws Exception {
        Document document = createDocument("Order");
        Element element = document.getDocumentElement();
        Element element1 = getOrdLstDoc.getDocumentElement();
        Element eleOrder = getChildElement(element1, "Order");
        String strOHKey = eleOrder.getAttribute("OrderHeaderKey");
        element.setAttribute("OrderHeaderKey", strOHKey);
        Element orderHoldTypes = createChild(element, "OrderHoldTypes");
        Element eleOrderHoldType = createChild(orderHoldTypes, "OrderHoldType");
        eleOrderHoldType.setAttribute("HoldType", holdType);
        eleOrderHoldType.setAttribute("Status", "1300");
        return document;
    }


}
