package org.xmlutils;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class ConsolePrinter {

	public static void prettyPrint(Document document) {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			// Setup indenting to "pretty print"
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");

			serializer.transform(new DOMSource(document), new StreamResult(
					System.out));
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

}
