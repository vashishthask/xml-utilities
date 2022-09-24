Java based XML related utilities. High level APIs instead of getting into low-level XML DOM stuff.

Library is now available at Maven Central repository.

Current stable version is 1.0.3. If you want to use these APIs in your project, use following dependency:

```xml
<dependency>
  <groupId>com.svashishtha</groupId>
  <artifactId>xml-utilities</artifactId>
  <version>1.0.3</version>
</dependency>
```

# XML Utilities : Coarse Grained XML Java APIs

Here's an example: 

```xml
<books> 
  <book> 
    <title>Book1</title> 
    <prod id="33-657" media="paper"></prod> 
    <chapter name="Introduction to XML"> 
      <para>What is HTML</para> 
      <para>What is XML</para> 
    </chapter> 
    <chapter name="XML Syntax"> 
      <para>Elements must have a closing tag</para> 
      <para>Elements must be properly nested</para> 
    </chapter> 
  </book> 
  <book> 
   <title>Book2</title> ... ... 
  </book> 
</books>
```

From functional point of view, I may be interested to get book names. So instead of going into familiar low-level Element and Node route, here's what I prefer: 

```java
List<String> bookNames = DomEditor.getTagValues("title", document);
``` 

If you see the underlying implementation of `DomEditor`, you will realize that you were actually not interested in its implementation as it seems to be complex enough. 

We created these utility classes a long time back and because of lack of similar APIs I kept on copying these APIs in different projects. As still I couldn't find APIs in `commons-xml` the way I could for String in `commons-lang`, I thought it will be a good idea to create a library just for that. The APIs are available on [Maven Central Repository]
## Some more APIs Examples
DomEditor For getting the first Element with tagname "chapter" of the XML, we can use following API. 

```java
Element elmt = DomEditor.getElement("chapter", doc);
```

As this XML contains more than one elements for "chapter" tag we can get the array of these elements too if needed using following API. 

```java
List<Element> elements = DomEditor.getElements("chapter", doc);
```
Within "chapter" element we have multiple "para" elements which we can retrieve using `getElements(String, Element)` method by passing elmt element. 
```java
List<Element> paraElements = DomEditor.getElements("para", elmt);
``` 
We can directly retrieve the value of all "para" tags using `DomEditor.getTagValues(Element element, String tagName)` method. 

```java
List<String> paraTagValues = DomEditor.getTagValues(elmt, "para");
```

DomEditor also provides APIs for some main XML operations on DOM:
* deleting a tag from a XML Element `deleteTag(Document, String)`
* checking whether tag exists in DOM object `checkTagExists(Document, String)`
* inserting a new tag in the DOM object `insertNewTag(Document, String, String)`
* getting a value of a node based on XPath provided in "." separated String. So in above XML using `DomEditor.getNodeValue("title.prod.para", elmt)` will give "What is HTML" as resultant.
## PrettyPrinter 

Useful for printing formatted XML to a file or to console. 

## DomParser 
Direct APIs for parsing a file, String, InputStream etc.
