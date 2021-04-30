import java.io.*;
import java.util.regex.*;
import javax.xml.parsers.*;
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

	static {
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

	public static void ModifySingleConfigEntry(String xpathexpr, String content) {

	}

	public static void SaveConfig(String pathname) {

	}
}
