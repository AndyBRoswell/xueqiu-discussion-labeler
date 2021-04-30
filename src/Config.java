import java.io.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Config {
	static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	static DocumentBuilder builder;

	static {
		try { builder = factory.newDocumentBuilder(); }
		catch (ParserConfigurationException e) { e.printStackTrace(); }
	}

	public static void LoadConfig(String pathname) throws IOException, SAXException {
		File cfgFile = new File(pathname);
		Document document = builder.parse(cfgFile);
		document.getDocumentElement().normalize();
	}
}
