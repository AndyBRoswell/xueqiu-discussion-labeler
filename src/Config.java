import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
	static final TransformerFactory tfactory = TransformerFactory.newInstance();
	static Transformer transformer;
	static DOMSource source;

	static {
		try { transformer = tfactory.newTransformer(); }
		catch (TransformerConfigurationException e) { e.printStackTrace(); }
		try { builder = dfactory.newDocumentBuilder(); }
		catch (ParserConfigurationException e) { e.printStackTrace(); }
	}

	public static void LoadConfig(String pathname) throws IOException, SAXException { // 配置文件强制 UTF-8 编码
		File cfgxmlfile = new File(pathname);
		ConfigXML = builder.parse(cfgxmlfile);
		ConfigXML.getDocumentElement().normalize();
	}

	public static String QuerySingleConfigEntry(String xpathexpr) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathexpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.NODESET);
		return Config.ReplaceVariable(EvalResult.item(0).getTextContent());
	}

	public static String ReplaceVariable(String string) {
		string = string.replaceAll("//app-path//", Matcher.quoteReplacement(Global.AppPath));
		string = string.replaceAll("//config-path//", Matcher.quoteReplacement(Global.AppPath));
		string = string.replaceAll("//default-save-path//", Matcher.quoteReplacement(Global.DefaultSavePath));
		return string;
	}

	public static void ModifySingleConfigEntry(String xpathexpr, String content) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(xpathexpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.NODESET);
		EvalResult.item(0).setTextContent(content);
	}

	public static void SaveConfig(String pathname) throws TransformerException { // 配置文件强制 UTF-8 编码
		source = new DOMSource(ConfigXML);
		File cfgxmlfile = new File(pathname);
		StreamResult result = new StreamResult(cfgxmlfile);
		transformer.transform(source, result);
	}
}
