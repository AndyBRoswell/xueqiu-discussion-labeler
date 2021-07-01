import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.regex.*;
import javax.xml.crypto.Data;
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

	public static void LoadConfig(String Pathname) throws IOException, SAXException { // 配置文件强制 UTF-8 编码
		File CfgXMLFile = new File(Pathname);
		ConfigXML = builder.parse(CfgXMLFile);
		ConfigXML.getDocumentElement().normalize();
	}

	public static String LoadConfigXMLWithoutParsing(String Pathname) throws IOException { // 配置文件强制 UTF-8 编码
		final BufferedReader CfgFileReader = new BufferedReader(new FileReader(Pathname, StandardCharsets.UTF_8));
		StringBuilder ConfigXML = new StringBuilder();
		String Line;
		while ((Line = CfgFileReader.readLine()) != null) {
			ConfigXML.append(Line).append(Global.LineSeparator);
		}
		CfgFileReader.close();
		return ConfigXML.toString();
	}

	public static String QuerySingleConfigEntry(String XPathExpr) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(XPathExpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.NODESET);
		return Config.ReplaceVariable(EvalResult.item(0).getTextContent()).trim();
	}

	private static String ReplaceVariable(String Str) {
		Str = Str.replaceAll("//app-path//", Matcher.quoteReplacement(Global.AppPath));
		Str = Str.replaceAll("//config-path//", Matcher.quoteReplacement(Global.AppPath));
		Str = Str.replaceAll("//default-save-path//", Matcher.quoteReplacement(Global.DefaultSavePath));
		Str = Str.replaceAll("//datetime//", Matcher.quoteReplacement(LocalDateTime.now().format(Global.DefaultDateTimeFormatter)));
		Str = Str.replaceAll("//last-file-imported//", Matcher.quoteReplacement(DataManipulator.LastFileImported));
		return Str;
	}

	public static void ModifySingleConfigEntry(String XPathExpr, String Contents) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(XPathExpr);
		NodeList EvalResult = (NodeList) expr.evaluate(ConfigXML, XPathConstants.NODESET);
		EvalResult.item(0).setTextContent(Contents);
	}

	public static void SaveConfig(String Pathname) throws TransformerException { // 配置文件强制 UTF-8 编码
		source = new DOMSource(ConfigXML);
		File cfgxmlfile = new File(Pathname);
		StreamResult result = new StreamResult(cfgxmlfile);
		transformer.transform(source, result);
	}

	public static void SaveConfig(String ConfigXML, String Pathname) throws IOException {
		final BufferedWriter CfgFileWriter = new BufferedWriter(new FileWriter(Pathname, StandardCharsets.UTF_8));
		CfgFileWriter.write(ConfigXML);
		CfgFileWriter.close();
	}
}
