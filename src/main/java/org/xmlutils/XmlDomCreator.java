package org.xmlutils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlDomCreator {

	public static Document getDocument(File xmlFile) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			return docBuilder.parse(xmlFile);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public static Document getDocument(InputStream stream) {
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			return docBuilder.parse(stream);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

	public static Document getDocument(String xmlString) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xmlString));
			return builder.parse(is);
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}

}
