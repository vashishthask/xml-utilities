package org.xmlutils.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DomEditor {

	/**
	 * Gotta keep Colbertura happy.
	 */
	private DomEditor() {
		super();
	}

	public static void addAttributeToElement(Document doc,
			Element projectElement, String attributeName, String attributeValue) {
		Attr xmlnsAttr = doc.createAttribute(attributeName);
		xmlnsAttr.setValue(attributeValue);
		projectElement.setAttributeNode(xmlnsAttr);
	}

	/**
	 * This method is used for updating the value of a tag in a
	 * <code>Document</code> object.
	 * 
	 * @param doc
	 *            Document object
	 * @param tagName
	 *            name of the tag
	 * @param tagValue
	 *            the updated value of the tag
	 */
	public static void replaceTagValue(Document doc, String tagName,
			String tagValue) {
		NodeList nodeList = doc.getElementsByTagName(tagName);
		int j = nodeList.getLength();
		Node node;
		for (int i = 0; i < j; i++) {
			Node newNode = doc.createTextNode(tagValue);
			node = nodeList.item(i);
			if (node.getFirstChild() != null) {
				node.replaceChild(newNode, node.getFirstChild());
			} else {
				node.appendChild(newNode);
			}
		}
	}

	/**
	 * This method is used to insert a new tag below the tag specified by
	 * <code>appendTo</code> parameter.
	 * 
	 * @param d
	 *            the <code>Document</code> object to which a new tag is to be
	 *            inserted.
	 * @param appendTo
	 *            the tag below which a new tag needs to be inserted.
	 * @param tagName
	 *            the name of new tag
	 * @param tagValue
	 *            the value of new tag
	 */
	public static Element insertNewTagBelow(Document d, String appendTo,
			String tagName, String tagValue) {
		Node element = d.getElementsByTagName(appendTo).item(0);
		if (element == null) {
			element = d.createElement(appendTo);
		}
		Element newElement = d.createElement(tagName);
		element.appendChild(newElement);
		newElement.appendChild(d.createTextNode(tagValue));
		return newElement;
	}

	/**
	 * Inserts a new value for an XML tag specified by <code>tagName</code> name
	 * in a <code>Document</code> object.
	 * 
	 * @param doc
	 *            Document object.
	 * @param tagName
	 *            Name of the tag as String.
	 * @param tagValue
	 *            Value of the tag as String.
	 */
	public static Element insertTagValue(Document doc, String tagName,
			String tagValue) {
		Element element = doc.createElement(tagName);
		doc.getDocumentElement().appendChild(element);
		if (tagValue != null) {
			element.appendChild(doc.createTextNode(tagValue));
		}
		return element;
	}

	/**
	 * Inserts a new value for an XML tag specified by <code>tagName</code> name
	 * in a <code>Element</code> object.
	 * 
	 * @param elementToAppend
	 *            Element object.
	 * @param tagName
	 *            Name of the tag as String.
	 * @param tagValue
	 *            Value of the tag as String.
	 */
	public static Element insertTagInElement(Document document,
			Element elementToAppend, String tagName, String tagValue) {
		Element newElement = document.createElement(tagName);
		elementToAppend.appendChild(newElement);
		newElement.appendChild(document.createTextNode(tagValue));
		return newElement;
	}

	public static void insertOrUpdateTagValue(Document doc, String tagName,
			String tagValue) {
		if (DomEditor.checkTagExists(doc, tagName)) {
			DomEditor.replaceTagValue(doc, tagName, tagValue);
		} else {
			DomEditor.insertTagValue(doc, tagName, tagValue);
		}
	}

	/**
	 * This method takes Element object and String tagName as argument and
	 * returns the value of the first child node it gets. If it does not find
	 * any node it will return null.
	 * 
	 * @param element
	 *            Element object
	 * @param tagName
	 *            Name of the tag.
	 * @return the value of the first occurance of <code>tagName</code> tag in
	 *         <code>element</code> object. Returns null, if doesn't find any.
	 */
	public static String getTagValue(Element element, String tagName) {
		if (element == null) {
			return null;
		}
		NodeList nodes = element.getElementsByTagName(tagName);
		if (nodes == null || nodes.getLength() <= 0) {
			return null;
		}
		return getTagValueOfFirstChild(nodes.item(0));
	}
	
	
	/**
	 * This method takes Element object and String tagName as argument and
	 * returns all values related to <code>tagName</code>. If it does not find
	 * any node it will return empty List.
	 * 
	 * @param element
	 *            Element object
	 * @param tagName
	 *            Name of the tag.
	 * @return list of tag values
	 */
	public static List<String> getTagValues(Element element, String tagName) {
		List<String> tagValues = new ArrayList<String>();
		List<Element> elements = getElements(tagName, element);
		for (Element resultElement : elements) {
			tagValues.add(getTagValue(resultElement, tagName));
		}
		return tagValues;
	}
	
	/**
	 * This method takes Document object and String tagName as argument and
	 * returns all values related to <code>tagName</code>. If it does not find
	 * any node it will return empty List.
	 * 
	 * @param document
	 *            Document object
	 * @param tagName
	 *            Name of the tag.
	 * @return list of tag values
	 */
	public static List<String> getTagValues(Document document, String tagName) {
		Element element = document.getDocumentElement();
		return getTagValues(element, tagName);
	}
	
	/**
	 * This method is used to convert a given XML Element object to XML Document
	 * object.
	 * 
	 * @param element
	 *            The XML Element object
	 * @return <code>Document</code> Returns the XML Document object
	 */
	public static Document getDocument(Element element) {
		Document doc = new DocumentImpl();
		Node node = ((DocumentImpl) doc).importNode(element, true);
		doc.appendChild(node);
		return doc;
	}
	
	/**
	 * This method is used to search all the tags with given key in the
	 * <code>document</code> object.
	 * 
	 * @param key
	 *            Name of the node.
	 * @param doc
	 *            the <code>Document</code> object.
	 * @return the value of the node.
	 * 
	 */
	public static String getNodeValue(String key, Document doc) {
		return getNodeValue(key, doc.getDocumentElement());
	}
	
	/**
	 * This method is used to search all the tags with given key in the document
	 * object.
	 * 
	 * @param key
	 *            Name of the node as String.
	 * @param contentElement
	 *            Element object.
	 * @return <code>String</code> Node value of the tag.
	 * 
	 */
	public static String getNodeValue(String key, Element contentElement) {
		String value = "";
		StringTokenizer tokenizer = new StringTokenizer(key, ".", false);

		NodeList nodes = null;

		while (tokenizer.hasMoreTokens()) {
			String tagName = tokenizer.nextToken();
			nodes = contentElement.getElementsByTagName(tagName);
			contentElement = locateElement(nodes);
			if (contentElement == null)
				return null;
		}

		value = getTagValue(contentElement);
		return value;
	}
	
	/**
	 * This method takes Node object as argument and return the value of the
	 * first child of the node.
	 * <p>
	 * If there is no such node, this will return null.
	 * 
	 * @param node
	 *            Node object
	 * @return <code>String</code> returns the value of the node
	 * 
	 */
	public static String getTagValue(Node node) {
		NodeList childNodeList = node.getChildNodes();
		String value = null;
		if (childNodeList == null)
			value = "";
		else {
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < childNodeList.getLength(); j++) {
				buffer.append(childNodeList.item(j).getNodeValue());
			}
			value = buffer.toString();
		}
		return value;
	}
	
	private static Element locateElement(NodeList nodes) {
		if (nodes == null)
			return null;
		int len = nodes.getLength();
		if (len == 0)
			return null;
		Element elt = null;
		for (int i = 0; i < len; i++) {
			Node n = nodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				elt = (Element) n;
			} else {
				continue;
			}
		}
		return elt;
	}




	/**
	 * This method takes Node object as argument and return the value of the
	 * first child of the node.
	 * <p/>
	 * If there is no such node, this will return null.
	 * 
	 * @param node
	 *            Node object
	 * @return <code>String</code> returns the value of the node
	 */
	public static String getTagValueOfFirstChild(Node node) {
		NodeList childNodeList = node.getChildNodes();
		String value;
		if (childNodeList == null) {
			value = "";
		} else {
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < childNodeList.getLength(); j++) {
				buffer.append(childNodeList.item(j).getNodeValue());
			}
			value = buffer.toString();
		}
		return value;
	}

	/**
	 * Deletes a tag based on passed <code>tagName</code> and <code>doc</code>
	 * object.
	 * 
	 * @param doc
	 *            Document object.
	 * @param tagName
	 *            Name of the tag as String.
	 */
	public static void deleteTag(Document doc, String tagName) {
		NodeList nodeList = doc.getElementsByTagName(tagName);
		int j = nodeList.getLength();
		Node node;
		Node parentNode;
		for (int i = 0; i < j; i++) {
			node = nodeList.item(i);
			parentNode = node.getParentNode();
			parentNode.removeChild(node);
		}
	}

	/**
	 * This method is used to search all the <code>Element</code> objects with
	 * given key in the passed <code>Element</code> object.
	 * 
	 * @param tagName
	 *            Name of the tag as String.
	 * @param input
	 *            Element object.
	 * @return <code>Element[]</code> Returns the array of elements, or an empty
	 *         array in case here is no match.
	 */
	public static List<Element> getElements(String tagName, Element input) {
		NodeList nodes = input.getElementsByTagName(tagName);

		int len = nodes.getLength();
		List<Element> elt = new ArrayList<Element>(len);
		Node node;
		for (int i = 0; i < len; i++) {
			node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elt.add((Element) node);
			}
		}

		return elt;
	}

	/**
	 * This method is used to check whether a node having name tagname is
	 * available in the Document object. If it exists it returs true otherwise
	 * false. This method takes Document object and String tagname as argument
	 * and returns boolean.
	 * 
	 * @param doc
	 *            Document object.
	 * @param tagname
	 *            the name of tag
	 * @return <code>true</code> if tag with <code>tagname</code> exists in the
	 *         <code>Document</code> object. Otherwise returns
	 *         <code>false</code>.
	 */
	public static boolean checkTagExists(Document doc, String tagname) {
		boolean retVal = true;
		NodeList nodes = doc.getElementsByTagName(tagname);
		if ((nodes == null) || (nodes.getLength() == 0)) {
			retVal = false;
		}
		return retVal;
	}
	
	/**
	 * Inserts a new CDATA section in the <code>Document</code> with passed
	 * <code>tagName</code> and <code>tagValue</code> pair.
	 * 
	 * @param doc
	 *            Document object.
	 * @param tagName
	 *            Name of the tag.
	 * @param tagValue
	 *            CDATA value of the tag.
	 * 
	 */
	public static void insertCDATASection(Document doc, String tagName,
			String tagValue) {
		NodeList nodeList = doc.getElementsByTagName(tagName);
		if (nodeList == null)
			return;
		int j = nodeList.getLength();
		Node node = null;

		for (int i = 0; i < j; i++) {

			node = nodeList.item(i);
			node.appendChild(doc.createCDATASection(tagValue));
		}
	}
}