package org.xmlutils.dom;

import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapToDom {
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory
			.newInstance();
	
	private MapToDom() {
	}
	public static <K, V> String mapToXml(Map<K, V> map, String rootName,
			String childName, String keyName, String valueName)
			throws TransformerException, ParserConfigurationException {
		Document document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder()
				.newDocument();

		Element root = document.createElement(rootName);
		document.appendChild(root);

		for (Map.Entry<K, V> errorCodeReport : map.entrySet()) {
			Element s = document.createElement(childName);
			root.appendChild(s);

			s.setAttribute(keyName, errorCodeReport.getKey().toString());
			s.setAttribute(valueName, errorCodeReport.getValue().toString());
		}

		return DomToString.getStringFromDocument(document);
	}
}
