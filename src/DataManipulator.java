import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class DiscussionItem {
	private String Text; // 股评文本
	private ConcurrentHashMap<String, HashMap<String, Integer>> Labels; // 标注的全部标签

	DiscussionItem() { Labels = new ConcurrentHashMap<>(); }

	DiscussionItem(String Text) { this.Text = Text; Labels = new ConcurrentHashMap<>(); }

	String GetText() { return Text; }

	ConcurrentHashMap<String, HashMap<String, Integer>> GetLabels() { return Labels; }

	void SetText(String Text) { this.Text = Text; }
}

public class DataManipulator {
	private static final ConcurrentHashMap<String, HashSet<String>> AllLabels = new ConcurrentHashMap<>(); // 所有可用标注
	private static final ConcurrentHashMap<String, HashSet<String>> LabelToCategory = new ConcurrentHashMap<>(); // 标注所属的类（搜索用）

	private static final ArrayList<DiscussionItem> DiscussionList = new ArrayList<>(); // 全部股票讨论
	private static final HashMap<String, Integer> DiscussionToIndex = new HashMap<>(); // 根据股票讨论查找所在位置（合并相同股评含有的不同标注用）

	private static final ArrayList<ArrayList<Integer>> SearchResults = new ArrayList<>(); // 用于存放搜索结果
//	private static final TreeSet<Integer> FinalSearchResult = new TreeSet<>();

	/* 对可选标注的维护 */

	// 获得全部可用标注
	static ConcurrentHashMap<String, HashSet<String>> GetAllLabels() { return AllLabels; }

	// 获得一类标注下的全部可用标注
	static HashSet<String> GetAvailableLabelsOfCategory(String Category) { return AllLabels.get(Category); }

	// 为某一类标注添加新的可用标注
	public static void AddAvailableLabelToCategory(String Category, String Label) {
		HashSet<String> Labels = AllLabels.get(Category); // 先获得该标签所在的类是否存在
		if (Labels == null) { // 如果还没有此类标签，就先添加该标签类
			AllLabels.put(Category, new HashSet<>());
			Labels = AllLabels.get(Category);
		}
		Labels.add(Label); // 在该类标签下添加该可用标签
		AddCategoryOfLabel(Category, Label); // 登记该标签所属的标签类，为搜索维护需要的数据
	}

	// 为某一类标注删除指定的可用标注
	public static void DeleteLabelFromCategory(String Category, String Label) {
		HashSet<String> Labels = AllLabels.get(Category);
		if (Labels == null) return;
		Labels.remove(Label);
		if (Labels.size() == 0) AllLabels.remove(Category);
		DeleteCategoryOfLabel(Category, Label);
	}

	// 获得一个标签属于的全部标签类
	static HashSet<String> GetCategoriesOfLabel(String Label) {
		return LabelToCategory.get(Label);
	}

	// 为某标签登记新的所属标签类
	static void AddCategoryOfLabel(String Category, String Label) {
		HashSet<String> Categories = DataManipulator.GetCategoriesOfLabel(Label);
		if (Categories == null) {
			DataManipulator.LabelToCategory.put(Label, new HashSet<>());
			Categories = DataManipulator.LabelToCategory.get(Label);
		}
		Categories.add(Category);
	}

	// 为某标签删除所属的一类标签类
	static void DeleteCategoryOfLabel(String Category, String Label) {
		HashSet<String> Categories = DataManipulator.GetCategoriesOfLabel(Label);
		if (Categories == null) return;
		Categories.remove(Label);
		if (Categories.size() == 0) LabelToCategory.remove(Category);
	}

	/* 对股票讨论及其标注的维护 */

	// 讨论列表
	public static ArrayList<DiscussionItem> GetDiscussionList() { return DiscussionList; }

	// 根据索引（主界面上选中的行数）来获得股票讨论及其标注
	public static DiscussionItem GetDiscussionItem(int index) { return DiscussionList.get(index); }

	// 根据股票讨论获得其所在的位置（索引）（合并具有不同标注结果的相同股评用）
	public static int GetIndexOfDiscussionItem(DiscussionItem item) { return DiscussionToIndex.get(item.GetText()); }

	public static int GetIndexOfDiscussionItem(String discussion) { return DiscussionToIndex.get(discussion); }

	// 导入新的股票讨论条目
	public static void AddDiscussionItem(DiscussionItem Item) {
		try { // 添加过包含相同股评的条目
			int Index = GetIndexOfDiscussionItem(Item);
			ConcurrentHashMap<String, HashMap<String, Integer>> ExistedLabels = GetDiscussionItem(Index).GetLabels();
			for (Map.Entry<String, HashMap<String, Integer>> e : Item.GetLabels().entrySet()) { // 对于新添加的股评条目的每一类标签
				HashMap<String, Integer> ExistedLabelsOfThisCat = ExistedLabels.get(e.getKey());
				if (ExistedLabelsOfThisCat == null) { // 如果已存在的具有相同股评的条目里，没有该类标签，就直接添加这类标签
					ExistedLabels.put(e.getKey(), e.getValue());
				}
				else { // 否则，写入新条目的该类标签的每一个标签及其被选中的次数到已有条目的对应位置
					for (Map.Entry<String, Integer> f : e.getValue().entrySet()) {
						Integer Count = ExistedLabelsOfThisCat.get(f.getKey());
						if (Count == null) { // 已存在条目的该标签类里没有当前标签，直接写入该标签
							ExistedLabelsOfThisCat.put(f.getKey(), f.getValue());
						}
						else { // 否则，累加该标签的被选中次数
							ExistedLabelsOfThisCat.put(f.getKey(), Count + f.getValue());
						}
					}
				}
			}
		}
		catch (NullPointerException e) { // 之前未添加包含相同股评的条目
			DiscussionList.add(Item); // 添加到股评列表末尾即可
			DiscussionToIndex.put(Item.GetText(), DiscussionList.size() - 1);
		}
	}

	// 为指定的股票讨论添加新的标签
	public static void AddLabel(int Index, String Category, String Label) {
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels(); // 先获得指定股评的全部标签
		HashMap<String, Integer> TargetCat = TargetLabels.get(Category); // 获得该标签所属的类
		if (TargetCat == null) { // 如果没有此类标签，该标签将作为此类标签的首个标签添加
			TargetLabels.put(Category, new HashMap<>());
			TargetCat = TargetLabels.get(Category);
		}
		Integer Count = TargetCat.get(Label); // 增加 1 次被标注次数
		if (Count == null) TargetCat.put(Label, 1);
		else TargetCat.put(Label, Count + 1);
	}

//	static void AddLabelWithCount(int Index, String Category, String Label, int Count) {
//
//	}

	// 为指定股票讨论删除一个标签
	public static void DeleteLabel(int Index, String Category, String Label) {
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels(); // 先获得指定股评的全部标签
		HashMap<String, Integer> TargetCat = TargetLabels.get(Category); // 获得该标签所属的类
		Integer Count = TargetCat.get(Label);
		if (Count == null) return; // 该类标签未包含指定的标签，不采取任何操作
		if (Count == 1) TargetCat.remove(Label); // 如果原先只被标注了一次，就删除此标签
		else TargetCat.put(Label, Count - 1); // 否则，减少 1 次被标注次数
		if (TargetCat.size() == 0) TargetLabels.remove(Category); // 对于删除了仅被标注 1 次的标签的情况，如果该类已不含任何标签，就将该类标签也一并删除
	}

	// 为指定股票讨论删除一类标签
	public static void DeleteLabel(int Index, String Category) {
		ConcurrentHashMap<String, HashMap<String, Integer>> TargetLabels = GetDiscussionItem(Index).GetLabels();
		TargetLabels.remove(Category);
	}

	// 搜索
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
