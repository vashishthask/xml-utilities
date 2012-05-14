package org.xmlutils.util;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.converters.DOMConverter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.xmlutils.XmlDomEditor;
import org.xmlutils.converter.XmlDomConverter;

public class XMLUtilTest {

    private org.w3c.dom.Document document;
    private static final String DOCUMENT_AS_STRING = "<numbers><first>1</first><tenth>1</tenth><tenth>1</tenth><second>2</second></numbers>";

    private static final String EVENT_READER_SAMPLE = "<?xml version=\"1.0\"?>\n<foo><bar value=\"1\"/></foo>" ;
    private static final DOMImplementation DOM_IMPLEMENTATION = org.apache.xerces.dom.DOMImplementationImpl.getDOMImplementation();

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Before
    public void setUp() throws ParsingException, IOException {
        document = DOMConverter.convert(new Builder().build(new StringReader(DOCUMENT_AS_STRING)), DOM_IMPLEMENTATION);
    }

    @Test
    public void shouldFindTagsCorrectly() throws ParsingException, IOException {
        assertThat(XmlDomEditor.checkTagExists(document, "foo"), is(false));
        assertThat(XmlDomEditor.checkTagExists(document, "numbers"), is(true));
    }

    @Test
    public void shouldReplaceTagValue() {
        XmlDomEditor.replaceTagValue(document, "first", "numero uno");
        Document result = DOMConverter.convert(document);
        assertThat(result.query("//first").get(0).getValue(), is("numero uno"));
    }

    @Test
    public void shouldCorrectlyReturnTagValue() {
        assertThat(XmlDomEditor.getTagValue(document.getDocumentElement(), "first"), is("1"));
    }

    @Test
    public void shouldReturnNullInCaseOfNullElement() {
        assertThat(XmlDomEditor.getTagValue(null, "first"), is(nullValue()));
    }

    @Test
    public void shouldReturnEmptyStringInCaseOfNullNodeList() {
        org.w3c.dom.Element element = document.createElement("blaat");
        assertThat(XmlDomEditor.getTagValueOfFirstChild(element), is(""));
    }

    @Test
    public void shouldCorrectlyReturnNullForNonExistingTag() {
        assertThat(XmlDomEditor.getTagValue(document.getDocumentElement(), "foobar"), is(nullValue()));
    }

    @Test
    public void shouldRemoveElement() {
        XmlDomEditor.deleteTag(document, "first");
        assertThat(document.getElementsByTagName("first").getLength(), is(0));
    }

    @Test
    public void shouldInsertCorrectly() {
        XmlDomEditor.insertNewTagBelow(document, "numbers", "fourth", "4");
        Document result = DOMConverter.convert(document);
        Nodes nodes = result.query("/numbers/fourth");
        assertThat(nodes.size(), is(greaterThan(0)));
        assertThat(nodes.get(0).getValue(), is("4"));
    }

    @Test
    public void shouldInsertIfAbsent() {
        XmlDomEditor.insertOrUpdateTagValue(document, "fifth", "5");
        Document result = DOMConverter.convert(document);
        Nodes nodes = result.query("/numbers/fifth");
        assertThat(nodes.size(), is(greaterThan(0)));
        assertThat(nodes.get(0).getValue(), is("5"));
    }

    @Test
    public void shouldUpdateIfPresent() {
        XmlDomEditor.insertOrUpdateTagValue(document, "first", "one");
        Document result = DOMConverter.convert(document);
        Nodes nodes = result.query("/numbers/first");
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).getValue(), is("one"));
    }

    @Test
    public void shouldInsertTagInElement() {
        org.w3c.dom.Element element = (org.w3c.dom.Element) document.getDocumentElement().getFirstChild();
        XmlDomEditor.insertTagInElement(document, element, "nr1", "1");
        Document result = DOMConverter.convert(document);
        Nodes nodes = result.query("/numbers/first/nr1/text()");
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).getValue(), is("1"));
    }

    @Test
    public void shouldInsertTagValue() {
        XmlDomEditor.insertTagValue(document, "sixth", "6");
        Document result = DOMConverter.convert(document);
        Nodes nodes = result.query("/numbers/sixth/text()");
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).getValue(), is("6"));
    }

    @Test
    public void shouldGiveListOfElements(){
    	List<Element> elements = XmlDomEditor.getElements("tenth", document.getDocumentElement());
    	assertTrue(elements.size()==2);
    }
    
    @Test
    public void shouldConvertXMLFileToDocument() throws IOException{
    	File file = temp.newFile("sample-event-reader.xml");
        FileWriter output = new FileWriter(file);
        IOUtils.copy(new StringReader(EVENT_READER_SAMPLE), output);
        output.close();
    	org.w3c.dom.Document document = XmlDomConverter.convertFileToDom(file);
    	assertNotNull(document);
    }
   
    @Test
    public void shouldGetElements() {
        List<Element> list = XmlDomEditor.getElements("first", document.getDocumentElement());
        assertThat(list.size(), is(1));
    }
}
