import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException {
		GUI MainForm = new GUI();
//		GUIConfig guiConfig = new GUIConfig(Global.DefaultConfig);
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv");
		StorageAccessor.LoadAllAvailableLabels();
		StorageAccessor.SaveAllAvailableLabels();
	}
}
