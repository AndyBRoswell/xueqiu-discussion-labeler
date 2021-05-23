import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Map;

public class Main {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, InterruptedException {
//		System.out.println(System.getProperty("java.version"));
//		System.out.println(ManagementFactory.getRuntimeMXBean().getSpecVersion());
		GUI MainForm = new GUI();
//		GUIConfig guiConfig = new GUIConfig(Global.DefaultConfig);
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.AllLabels.entrySet()) {
//			System.out.println(entry.toString());
//		}

//		StorageAccessor.SaveAllAvailableLabels();

		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\xueqiu.csv", "gbk", 1);
		for (DiscussionItem entry : DataManipulator.DiscussionList) {
			System.out.println(entry.GetText());
			System.out.println(entry.GetLabels().toString());
		}
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.LabelToCategory.entrySet()) {
//			System.out.println(entry.toString());
//		}

//		DataManipulator.Search(2, null, null);
//		DataManipulator.Search(0, new String[]{ "2A" }, null);
//		DataManipulator.Search(0, null, new String[]{ "是" });

		//while (DataManipulator.FinalSearchResult.size() == 0) ;
//		Thread.sleep(5000);
//		System.out.println(DataManipulator.FinalSearchResult.size());
//		for (int i : DataManipulator.FinalSearchResult) {
//			System.out.println(i);
//		}

//		StorageAccessor.SaveDiscussionToCSV(Global.DefaultSavePath + "\\Book2.csv", "GB2312");
	}
}
