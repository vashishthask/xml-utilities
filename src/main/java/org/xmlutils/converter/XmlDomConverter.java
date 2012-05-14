package org.xmlutils.converter;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

public class XmlDomConverter {
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory
			.newInstance();

	public static Document convertFileToDom(File xmlFile) {
		Document document;
		try {
			document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(
					xmlFile);
		} catch (Exception e) {
			throw new IllegalStateException("Incoming file is not valid xml", e);
		}
		return document;
	}

	public static Document convertStringToDom(String xml) {
		Document document;
		ByteArrayInputStream stream = null;
		try {
			stream = new ByteArrayInputStream(xml.getBytes());
			document = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(
					stream);
		} catch (Exception e) {
			throw new IllegalStateException("Incoming text is not valid xml", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
		return document;
	}

}
