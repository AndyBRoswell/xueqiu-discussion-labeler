import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws XPathExpressionException, IOException, SAXException, InterruptedException {
		Global.MainForm = new GUIMain();

		// 列出可选标注面板的全部控件
		Thread.sleep(1000);
		for (var Component : Global.MainForm.AllLabelsPanel.getComponents()) {
			if (Component instanceof  GUIMain.LabelCategoryComponent) {
				System.out.println("LabelCategoryComponent: " + ((GUIMain.LabelCategoryComponent) Component).getText());
			}
			else if (Component instanceof GUIMain.LabelButton) {
				System.out.println("LabelButton: " + ((GUIMain.LabelButton) Component).getText());
			}
			else if (Component instanceof GUIMain.LabeledCountComponent) {
				System.out.println("LabeledCountComponent: " + ((GUIMain.LabeledCountComponent) Component).getText());
			}
			else { System.out.println("Other components."); }
		}

		// 读取设置
//		Config.LoadConfig(Global.DefaultConfig);

		// 读取全部可用标注
//		StorageAccessor.LoadAllAvailableLabels();
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.AllLabels.entrySet()) {
//			System.out.println(entry.toString());
//		}

		// 保存全部可用标注
//		StorageAccessor.SaveAllAvailableLabels();

		// 随机生成测试数据

		// 读取指定文件中的股票讨论及其标注
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv", "gbk", 0);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\xueqiu.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\中国平安.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\科创.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\贵州茅台.csv", "gbk", 1);
//		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\NVDA-20210601-100408.csv", "gbk", 1);
//		for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
//			System.out.println("Main.java - <调试> " + entry.GetText());
//			System.out.println("Main.java - <调试> " + entry.GetLabels().toString());
//		}
//		System.out.println(DataManipulator.GetDiscussionList().size());

		// 通过股评进行哈希查找，返回其在线性表中的位置
//		System.out.println(DataManipulator.GetDiscussionItem(42).GetText());
//		System.out.println(DataManipulator.GetIndexOfDiscussionItem(new DiscussionItem("核弹厂四千亿市值了。然鹅农企还在为突破1000亿在努力。英伟达 AMD")));

		// 查询标签所在的类
//		System.out.println(DataManipulator.LabelToCategory.size());
//		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.LabelToCategory.entrySet()) {
//			System.out.println(entry.toString());
//		}

		// 搜索功能
//		DataManipulator.Search(2, null, null);
//		DataManipulator.Search(0, new String[]{ "2A" }, null);
//		DataManipulator.Search(0, null, new String[]{ "是" });
//		DataManipulator.Search(0, new String[]{ "梭哈" }, null);

		// 最终的搜索结果
//		System.out.println(DataManipulator.GetLastSearchResult().size());
//		for (int i : DataManipulator.GetLastSearchResult()) {
//			System.out.println(i);
//		}

		// 添加标注
		// 贵州茅台
//		DataManipulator.AddLabel(0, "总体评价", "好评");
//		DataManipulator.AddLabel(0, "详细分析", "无");
//		DataManipulator.AddLabel(1, "短期趋势", "看涨");
//		DataManipulator.AddLabel(4, "是否有关", "无关");
		// NVDA
//		DataManipulator.AddLabel(45, "总体评价", "中评");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.DeleteLabel(45, "总体评价", "中评");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddLabel(45, "感性程度", "较高");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddLabel(45, "感性程度", "较高");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.DeleteLabel(45, "感性程度", "较高");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddLabel(45, "感性程度", "高");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddLabel(45, "总体评价", "好评");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddLabel(45, "总体评价", "好评");
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());
//		DataManipulator.AddDiscussionItem(DataManipulator.GetDiscussionItem(45));
//		System.out.println(DataManipulator.GetDiscussionItem(45).GetLabels());

		// 保存修改
//		StorageAccessor.SaveDiscussionToCSV(Global.DefaultSavePath + "\\Book2.csv", "GBK");
//		StorageAccessor.SaveDiscussionToCSV(Global.DefaultSavePath + "\\贵州茅台-简单添加标注.csv", "gbk");
	}
}
