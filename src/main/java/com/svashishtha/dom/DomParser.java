package com.svashishtha.dom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DomParser {
	
	private DomParser(){
		
	}

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

	/**
	 * Parses the passed <code>java.io.InputStream</code> and creates a
	 * <code>Document</code> object.
	 * 
	 * @param in
	 *            the InputStream object to be parsed.
	 * @return A parsed <code>Document</code> object.
	 * 
	 */
	public static Document parse(InputStream in) {
		Document document = null;
		if (in == null)
			return null;
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(in));
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		document = parser.getDocument();

		return document;
	}

	/**
	 * Given an XML document pointed to by a file path expression, this method
	 * attemps to read the file and parse it as XML. The XML must lie in the
	 * classpath, otherwise it will not parse the XML.
	 * <p>
	 * Example:
	 * <p>
	 * If sample.xml lies in org.xmlutils package, relative path will be
	 * "org/xmlutils/sample.xml"
	 * 
	 * @param relativeXmlPath
	 *            A relative file path expression.
	 * @return A parsed XML document
	 */
	public static Document parseXmlWithRelativeFilePath(String relativeXmlPath) {
		InputStream stream = DomParser.class.getClassLoader()
				.getResourceAsStream(relativeXmlPath);
		try {
			return parse(stream);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

	/**
	 * Given an XML document pointed to by a file path expression, this method
	 * attemps to read the file and parse it as XML.
	 * <p>
	 * Example:
	 * <p>
	 * If sample.xml lies in c:\temp path, absolute path will be
	 * "c:\\temp\\sample.xml"
	 * 
	 * @param absoluteFilePath
	 *            An absolute file path expression.
	 * @return A parsed XML document
	 */
	public static Document parseXmlWithAbsFilePath(String absoluteFilePath) {
		InputStream stream = null;
		try {
			stream = new FileInputStream(absoluteFilePath);
			return parse(stream);
		} catch (FileNotFoundException fnfEx) {
			throw new IllegalArgumentException(fnfEx);
		} finally {
			if(stream != null){
				IOUtils.closeQuietly(stream);
			}
		}
	}

	/**
	 * Given an XML String, this method attemps to parse it as XML and returns
	 * Document object.
	 * <p>
	 * 
	 * @param xmlString
	 *            The xml as string
	 * @return A parsed XML document
	 */
	public static Document parseXmlString(String xmlString) {
		InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
		try {
			return parse(stream);
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
