import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, SAXException, XPathExpressionException {
		GUI MainForm = new GUI();
		System.out.println(Global.AppPath);
		Config.LoadConfig(Global.DefaultConfig);
		System.out.println(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-export-dir"));
	}
}
