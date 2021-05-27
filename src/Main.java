import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, InterruptedException {
//		GUI MainForm = new GUI();

		/*读取全部可用标注*/
//		StorageAccessor.LoadAllAvailableLabels();
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.AllLabels.entrySet()) {
//			System.out.println(entry.toString());
//		}

		/*保存全部可用标注*/
//		StorageAccessor.SaveAllAvailableLabels();

		/*读取指定文件中的股票讨论及其标注*/
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv", "gbk", 0);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\xueqiu.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\中国平安.csv", "gbk", 1);
		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\科创.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\贵州茅台.csv", "gbk", 1);
//		for (DiscussionItem entry : DataManipulator.DiscussionList) {
//			System.out.println(entry.GetText());
//			System.out.println(entry.GetLabels().toString());
//		}
//		System.out.println(DataManipulator.DiscussionList.size());
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.LabelToCategory.entrySet()) {
//			System.out.println(entry.toString());
//		}
		System.out.println(DataManipulator.DiscussionList.size());

		/*搜索功能*/
//		DataManipulator.Search(2, null, null);
//		DataManipulator.Search(0, new String[]{ "2A" }, null);
//		DataManipulator.Search(0, null, new String[]{ "是" });
//		DataManipulator.Search(0, new String[]{ "梭哈" }, null);

		/*最终的搜索结果*/
//		System.out.println(DataManipulator.FinalSearchResult.size());
//		for (int i : DataManipulator.FinalSearchResult) {
//			System.out.println(i);
//		}

		/*保存修改*/
//		StorageAccessor.SaveDiscussionToCSV(Global.DefaultSavePath + "\\Book2.csv", "GB2312");
	}
}
