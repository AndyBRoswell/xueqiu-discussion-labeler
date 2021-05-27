import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

class DiscussionItem {
	private String Text;
	private ConcurrentHashMap<String, HashSet<String>> Labels;

	DiscussionItem() { Labels = new ConcurrentHashMap<>(); }

	DiscussionItem(String Text) { this.Text = Text; Labels = new ConcurrentHashMap<>(); }

	String GetText() { return Text; }

	ConcurrentHashMap<String, HashSet<String>> GetLabels() { return Labels; }

	void SetText(String Text) { this.Text = Text; }
}

public class DataManipulator {
	static final ArrayList<DiscussionItem> DiscussionList = new ArrayList<>();
	static final ArrayList<ArrayList<Integer>> SearchResults = new ArrayList<>();
	static final TreeSet<Integer> FinalSearchResult = new TreeSet<>();
	static final ConcurrentHashMap<String, HashSet<String>> AllLabels = new ConcurrentHashMap<>();
	static final ConcurrentHashMap<String, HashSet<String>> LabelToCategory = new ConcurrentHashMap<>();

	public static DiscussionItem GetDiscussionItem(int index) { return DiscussionList.get(index); }

	public static void AddLabel(int Index, String Category, String Label) {
		ConcurrentHashMap<String, HashSet<String>> TargetLabels = DiscussionList.get(Index).GetLabels();
		HashSet<String> TargetCat = TargetLabels.get(Category);
		if (TargetCat == null) TargetLabels.put(Category, new HashSet<>());
		TargetCat.add(Label);
	}

	public static void ModifyLabel(int Index, String Category, String OldLabel, String NewLabel) {
		ConcurrentHashMap<String, HashSet<String>> TargetLabels = DiscussionList.get(Index).GetLabels();
		HashSet<String> TargetCat = TargetLabels.get(Category);
		TargetCat.remove(OldLabel);
		TargetCat.add(NewLabel);
	}

	public static void DeleteLabel(int Index, String Category) {
		ConcurrentHashMap<String, HashSet<String>> TargetLabels = DiscussionList.get(Index).GetLabels();
		TargetLabels.remove(Category);
	}

	public static void DeleteLabel(int Index, String Category, String Label) {
		ConcurrentHashMap<String, HashSet<String>> TargetLabels = DiscussionList.get(Index).GetLabels();
		HashSet<String> TargetCat = TargetLabels.get(Category);
		TargetCat.remove(Label);
		if (TargetCat.size() == 0) TargetLabels.remove(Category);
	}

	public static void Search(int LabeledFlag, String[] Keywords, String[] Labels) throws InterruptedException {
		SearchResults.clear();
		if (LabeledFlag != 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> FlagSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithLabeledFlag(LabeledFlag, FlagSearchResult)).start();
		}
		if (Keywords != null && Keywords.length != 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> KeywordsSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithKeywords(Keywords, KeywordsSearchResult)).start();
		}
		if (Labels != null && Labels.length != 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> LabelsSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithLabels(Labels, LabelsSearchResult)).start();
		}
		FinalSearchResult.clear();
		// 怪事，这里不加延迟结果就不对，添加不进去
//		Thread.sleep(1000);
//		Thread.sleep(100);
		Thread.sleep(10);
//		Thread.sleep(1);
//		Thread.sleep(0, 1);
		for (ArrayList<Integer> Result : SearchResults) {
			FinalSearchResult.addAll(Result);
		}
//		System.out.println(SearchResults.size());
	}

	private static void SearchWithLabeledFlag(int LabeledFlag, ArrayList<Integer> SearchResult) {
		switch (LabeledFlag) {
			case 1: // Unlabeled
				for (int i = 0; i < DiscussionList.size(); ++i) {
					if (DiscussionList.get(i).GetLabels().size() == 0) SearchResult.add(i);
				}
				break;
			case 2: // Labeled
				for (int i = 0; i < DiscussionList.size(); ++i) {
					if (DiscussionList.get(i).GetLabels().size() > 0) SearchResult.add(i);
				}
				break;
			default:
				break;
		}
	}

	private static void SearchWithKeywords(String[] Keywords, ArrayList<Integer> SearchResult) {
		final int AvailableCPUThreadCount = Runtime.getRuntime().availableProcessors();
		int LastEndIndex = 0;
		for (int i = 1; i <= AvailableCPUThreadCount; ++i) {
			int StartIndex = LastEndIndex;
			int EndIndex = DiscussionList.size() * i / AvailableCPUThreadCount;
			new Thread(() -> {
				for (int j = StartIndex; j < EndIndex; ++j) {
					boolean found = true;
					for (String keyword : Keywords) {
						if (DiscussionList.get(j).GetText().contains(keyword) == false) { found = false; break; }
					}
					if (found == true) {
						synchronized (SearchResult) {
							SearchResult.add(j);
						}
					}
				}
			}).start();
			LastEndIndex = EndIndex;
		}
	}

	private static void SearchWithLabels(String[] Labels, ArrayList<Integer> SearchResult) {
		for (int i = 0; i < DiscussionList.size(); ++i) {
			boolean Found = true;
			for (String Label : Labels) {
				if (DiscussionList.get(i).GetLabels().containsKey(Label)) continue; // 该标签恰好为该条股票讨论包含的一个标签类的名称，符合条件，继续考察其它标签
				HashSet<String> Categories = LabelToCategory.get(Label); // 否则，先查询该标签属于的标签类
				if (Categories == null) { return; } // 该标签不属于任何已知的标签类（每个标签属于的类在读入全部可用标签与指定的股票讨论 CSV 文件时都会被登记），不符合条件
				boolean FoundSingle = false;
				for (String Category : Categories) { // 查找该条股票讨论是否包含该标签所属的某一个类；如果包含，则在类中查找
					HashSet<String> LabelsOfThisCatOfThisItem = DiscussionList.get(i).GetLabels().get(Category);
					if (LabelsOfThisCatOfThisItem == null) { continue; }
					if (LabelsOfThisCatOfThisItem.contains(Label) == true) { FoundSingle = true; break; }
				}
				if (FoundSingle == false) { Found = false; break; }
			}
			if (Found == true) SearchResult.add(i);
		}
	}
}
