import java.io.*;
import java.util.regex.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Config {
	static final DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
	static DocumentBuilder builder;
	static Document ConfigXML;
	static final XPathFactory xfactory = XPathFactory.newInstance();
	static final XPath xpath = xfactory.newXPath();
	static final Pattern ShellVariablePattern = Pattern.compile("\\$[\\w]+[^\\w]");
	static final TransformerFactory tfactory = TransformerFactory.newInstance();
	static Transformer transformer;
	static DOMSource source;

	static {
		try { transformer = tfactory.newTransformer(); }
		catch (TransformerConfigurationException e) { e.printStackTrace(); }
		try { builder = dfactory.newDocumentBuilder(); }
		catch (ParserConfigurationException e) { e.printStackTrace(); }
	}

	public static void LoadConfig(String pathname) throws IOException, SAXException {
		File cfgxmlfile = new File(pathname);
		ConfigXML = builder.parse(cfgxmlfile);
		ConfigXML.getDocumentElement().normalize();
	}

	public static String QuerySingleConfigEntry(String xpathexpr) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathexpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.STRING);
		return EvalResult.item(0).getTextContent();
	}

	public static String ReplaceShellVariable(String string) {
		string = string.replaceAll("\\$AppPath", Global.AppPath);
		string = string.replaceAll("\\$ConfigPath", Global.AppPath);
		return string;
	}

	public static void ModifySingleConfigEntry(String xpathexpr, String content) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathexpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.STRING);
		EvalResult.item(0).setTextContent(content);
	}

	public static void SaveConfig(String pathname) throws TransformerException {
		source = new DOMSource(ConfigXML);
		File cfgxmlfile = new File(pathname);
		StreamResult result = new StreamResult(cfgxmlfile);
		transformer.transform(source, result);
	}
}
