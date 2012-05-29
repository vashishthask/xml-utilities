package com.svashishtha.dom.io;

import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class PrettyPrinter {
	
	private PrettyPrinter() {
	}

	public static void printToConsole(Document document) {
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
	
	public static void printToFile(Document document,
			File directoryToWriteTo, String fileName) {
		// Prepare the DOM document for writing
		Source source = new DOMSource(document);

		// Prepare the output file
		File file = new File(directoryToWriteTo, fileName);
		Result result = new StreamResult(file);

		// Write the DOM document to the file
		try {
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new IllegalStateException(e);
		} catch (TransformerException e) {
			throw new IllegalStateException(e);
		}

	}

}
