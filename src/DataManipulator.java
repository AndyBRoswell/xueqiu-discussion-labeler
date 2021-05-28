import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class DiscussionItem {
	private String Text;
	private ConcurrentHashMap<String, HashMap<String, Integer>> Labels;

	DiscussionItem() { Labels = new ConcurrentHashMap<>(); }

	DiscussionItem(String Text) { this.Text = Text; Labels = new ConcurrentHashMap<>(); }

	String GetText() { return Text; }

	ConcurrentHashMap<String, HashMap<String, Integer>> GetLabels() { return Labels; }

	void SetText(String Text) { this.Text = Text; }
}

public class DataManipulator {
	static final ConcurrentHashMap<String, HashSet<String>> AllLabels = new ConcurrentHashMap<>();
	static final ConcurrentHashMap<String, HashSet<String>> LabelToCategory = new ConcurrentHashMap<>();

	static final ArrayList<DiscussionItem> DiscussionList = new ArrayList<>();
	static final HashMap<String, Integer> DiscussionToIndex = new HashMap<>();

	static final ArrayList<ArrayList<Integer>> SearchResults = new ArrayList<>();
//	static final TreeSet<Integer> FinalSearchResult = new TreeSet<>();

	static HashSet<String> GetCategoriesOfLabel(String label) {
		return LabelToCategory.get(label);
	}

	static void AddCategoryOfLabel(String label, String category) {
		HashSet<String> categories = DataManipulator.GetCategoriesOfLabel(label);
		if (categories == null) {
			DataManipulator.LabelToCategory.put(label, new HashSet<>());
			categories = DataManipulator.LabelToCategory.get(label);
		}
		categories.add(category);
	}

	public static DiscussionItem GetDiscussionItem(int index) { return DiscussionList.get(index); }

	public static int GetIndexOfDiscussionItem(DiscussionItem item) { return DiscussionToIndex.get(item.GetText()); }

	public static int GetIndexOfDiscussionItem(String discussion) { return DiscussionToIndex.get(discussion); }

	public static void AddDiscussionItem(DiscussionItem item) {
		try {
			int index = GetIndexOfDiscussionItem(item);
			for (Map.Entry<String, HashMap<String, Integer>> entry : item.GetLabels().entrySet()) {

			}
		}
		catch (IndexOutOfBoundsException e) {
			DiscussionList.add(item);
			DiscussionToIndex.put(item.GetText(), DiscussionList.size() - 1);
		}
	}

	public static void AddLabel(int Index, String Category, String Label) { // 为指定股票讨论添加新标签
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels();
		HashMap<String, Integer> TargetCat = TargetLabels.get(Category);
		if (TargetCat == null) TargetLabels.put(Category, new HashMap<>());
		Integer Count = TargetCat.get(Label);
		if (Count == null) TargetCat.put(Label, 1);
		else TargetCat.put(Label, Count + 1);
	}

	public static void DeleteLabel(int Index, String Category, String Label) { // 为指定股票讨论删除一个标签
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels();
		HashMap<String, Integer> TargetCat = TargetLabels.get(Category);
		int Count = TargetCat.get(Label);
		TargetCat.put(Label, Count - 1);
		if (Count == 1) TargetCat.remove(Label);
		if (TargetCat.size() == 0) TargetLabels.remove(Category);
	}

	public static void DeleteLabel(int Index, String Category) { // 为指定股票讨论删除一类标签
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels();
		TargetLabels.remove(Category);
	}

	public static void Search(int LabeledFlag, String[] Keywords, String[] Labels) { // 搜索功能
		SearchResults.clear();
		if (LabeledFlag != 0) {
			SearchResults.add(new ArrayList<>());
			new Thread(() -> SearchWithLabeledFlag(LabeledFlag, GetSecondToTheLastSearchResult(), GetLastSearchResult())).start();
		}
		if (Keywords != null && Keywords.length != 0) {
			SearchResults.add(new ArrayList<>());
			new Thread(() -> SearchWithKeywords(Keywords, GetSecondToTheLastSearchResult(), GetLastSearchResult())).start();
		}
		if (Labels != null && Labels.length != 0) {
			SearchResults.add(new ArrayList<>());
			new Thread(() -> SearchWithLabels(Labels, GetSecondToTheLastSearchResult(), GetLastSearchResult())).start();
		}
//		FinalSearchResult.clear();
		// 怪事，这里不加延迟结果就不对，添加不进去
//		Thread.sleep(1000);
//		Thread.sleep(100);
//		Thread.sleep(10);
//		Thread.sleep(1);
//		Thread.sleep(0, 1);
//		for (ArrayList<Integer> Result : SearchResults) {
//			FinalSearchResult.addAll(Result);
//		}
//		System.out.println(SearchResults.size());
	}

	private static ArrayList<Integer> GetSecondToTheLastSearchResult() { // 返回倒数第二个搜索结果（供搜索函数内部使用）
		if (SearchResults.size() < 2) return null;
		return SearchResults.get(SearchResults.size() - 2);
	}

	public static ArrayList<Integer> GetLastSearchResult() { // 返回最新的搜索结果
		if (SearchResults.size() == 0) return null;
		return SearchResults.get(SearchResults.size() - 1);
	}

	private static void SearchWithLabeledFlag(int LabeledFlag, ArrayList<Integer> SearchRange, ArrayList<Integer> SearchResult) { // 按快捷筛选条件（目前主要有已标注、未标注两种）搜索
		switch (LabeledFlag) {
			case 1: // Unlabeled
				if (SearchRange == null) {
					for (int i = 0; i < DiscussionList.size(); ++i) {
						if (DiscussionList.get(i).GetLabels().size() == 0) SearchResult.add(i);
					}
				}
				else {
					for (int i : SearchRange) {
						if (DiscussionList.get(i).GetLabels().size() == 0) SearchResult.add(i);
					}
				}
				break;
			case 2: // Labeled
				if (SearchRange == null) {
					for (int i = 0; i < DiscussionList.size(); ++i) {
						if (DiscussionList.get(i).GetLabels().size() > 0) SearchResult.add(i);
					}
				}
				else {
					for (int i : SearchRange) {
						if (DiscussionList.get(i).GetLabels().size() > 0) SearchResult.add(i);
					}
				}
				break;
			default:
				break;
		}
	}

	private static void SearchWithKeywords(String[] Keywords, ArrayList<Integer> SearchRange, ArrayList<Integer> SearchResult) { // 按关键词搜索
		final int AvailableCPUThreadCount = Runtime.getRuntime().availableProcessors();
		int LastEndIndex = 0;
		if (SearchRange == null) {
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
							synchronized (SearchResult) { SearchResult.add(j); }
						}
					}
				}).start();
				LastEndIndex = EndIndex;
			}
		}
		else {
			for (int i = 1; i <= AvailableCPUThreadCount; ++i) {
				int StartIndex = LastEndIndex;
				int EndIndex = SearchRange.size() * i / AvailableCPUThreadCount;
				new Thread(() -> {
					for (int j = StartIndex; j < EndIndex; ++j) {
						boolean found = true;
						for (String keyword : Keywords) {
							if (DiscussionList.get(SearchRange.get(j)).GetText().contains(keyword) == false) { found = false; break; }
						}
						if (found == true) {
							synchronized (SearchResult) { SearchResult.add(j); }
						}
					}
				}).start();
				LastEndIndex = EndIndex;
			}
		}
	}

	private static void SearchWithLabels(String[] Labels, ArrayList<Integer> SearchRange, ArrayList<Integer> SearchResult) { // 按标签搜索
		if (SearchRange == null) {
			for (int i = 0; i < DiscussionList.size(); ++i) {
				SearchDiscussionItemWithLabels(Labels, i, SearchResult);
			}
		}
		else {
			for (int i : SearchRange) {
				SearchDiscussionItemWithLabels(Labels, i, SearchResult);
			}
		}
	}

	private static void SearchDiscussionItemWithLabels(String[] Labels, int index, ArrayList<Integer> SearchResult) { // 按标签搜索（内部使用）
		boolean Found = true;
		for (String Label : Labels) {
			if (DiscussionList.get(index).GetLabels().containsKey(Label)) continue; // 该标签恰好为该条股票讨论包含的一个标签类的名称，符合条件，继续考察其它标签
			HashSet<String> Categories = GetCategoriesOfLabel(Label); // 否则，先查询该标签属于的标签类
			if (Categories == null) { return; } // 该标签不属于任何已知的标签类（每个标签属于的类在读入全部可用标签与指定的股票讨论 CSV 文件时都会被登记），不符合条件
			boolean FoundSingle = false;
			for (String Category : Categories) { // 查找该条股票讨论是否包含该标签所属的某一个类；如果包含，则在类中查找
				HashMap<String, Integer> LabelsOfThisCatOfThisItem = DiscussionList.get(index).GetLabels().get(Category);
				if (LabelsOfThisCatOfThisItem == null) { continue; }
				if (LabelsOfThisCatOfThisItem.containsKey(Label) == true) { FoundSingle = true; break; }
			}
			if (FoundSingle == false) { Found = false; break; }
		}
		if (Found == true) SearchResult.add(index);
	}
}
