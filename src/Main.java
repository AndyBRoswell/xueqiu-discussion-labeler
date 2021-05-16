import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException {
		GUI MainForm = new GUI();
//		GUIConfig guiConfig = new GUIConfig(Global.DefaultConfig);
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();
		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.AllLabels.entrySet()) {
			System.out.println(entry.toString());
		}
//		StorageAccessor.SaveAllAvailableLabels();
		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv", "GB2312");
		for (DiscussionItem entry : DataManipulator.DiscussionList) {
			System.out.println(entry.GetText());
			System.out.println(entry.GetLabels().toString());
		}
//		StorageAccessor.SaveDiscussionToCSV(Global.DefaultSavePath + "\\Book2.csv", "GB2312");
	}
}
