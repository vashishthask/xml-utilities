package org.xmlutils.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

public class XmlDomToFileWriter {

	public static void writeDocumentToDisk(Document document,
			File directoryToWriteTo, String fileName) {
		DOMImplementation domImplementation = document.getImplementation();
		if (domImplementation.hasFeature("LS", "3.0")
				&& domImplementation.hasFeature("Core", "2.0")) {
			DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementation
					.getFeature("LS", "3.0");
			LSSerializer lsSerializer = domImplementationLS
					.createLSSerializer();
			DOMConfiguration domConfiguration = lsSerializer.getDomConfig();
			if (domConfiguration.canSetParameter("format-pretty-print",
					Boolean.TRUE)) {
				lsSerializer.getDomConfig().setParameter("format-pretty-print",
						Boolean.TRUE);
				LSOutput lsOutput = domImplementationLS.createLSOutput();
				lsOutput.setEncoding("UTF-8");
				try {
					lsOutput.setByteStream(new FileOutputStream(new File(
							directoryToWriteTo, fileName)));
				} catch (FileNotFoundException e) {
					throw new IllegalStateException(e);
				}
				lsSerializer.write(document, lsOutput);
			}
		}

	}

	public static void writeDocument(Document document,
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
