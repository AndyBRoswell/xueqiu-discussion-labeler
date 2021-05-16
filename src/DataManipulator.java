import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataManipulator {
	static ArrayList<DiscussionItem> DiscussionList = new ArrayList<>();
	static ArrayList<ArrayList<Integer>> SearchResults = new ArrayList<>();
	static ArrayList<Integer> FinalSearchResult = new ArrayList<>();
	static ConcurrentHashMap<String, ArrayList<String>> AllLabels = new ConcurrentHashMap<>();
	static ConcurrentHashMap<String, String> LabelToCategory = new ConcurrentHashMap<>();

	public static void AddDiscussionItem(String text) { DiscussionList.add(new DiscussionItem(text)); }

	public static DiscussionItem GetDiscussionItem(int index) { return DiscussionList.get(index); }

	public static void DeleteDiscussionItem(int index) { DiscussionList.remove(index); }

	public static void Search(int LabeledFlag, String[] Keywords, String[] Labels) {
		SearchResults.clear();
		if (LabeledFlag != 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> FlagSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithLabeledFlag(LabeledFlag, FlagSearchResult));
		}
		if (Keywords == null || Keywords.length == 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> KeywordsSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithKeywords(Keywords, KeywordsSearchResult));
		}
		if (Labels == null || Labels.length == 0) {
			SearchResults.add(new ArrayList<>());
			ArrayList<Integer> LabelsSearchResult = SearchResults.get(SearchResults.size() - 1);
			new Thread(() -> SearchWithLabels(Labels, LabelsSearchResult));
		}
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
		final int CPUThreadCount = Runtime.getRuntime().availableProcessors();
		//final ArrayList<Integer> IndexBound = new ArrayList<>();
		int LastEndIndex = 0;
		for (int i = 1; i <= CPUThreadCount; ++i) {
			int StartIndex = LastEndIndex;
			int EndIndex = DiscussionList.size() * i / CPUThreadCount;
			new Thread(() -> {
				for (int j = StartIndex; j < EndIndex; ++j) {
					boolean found = true;
					for (int k = 0; k < Keywords.length; ++k) {
						if (DiscussionList.get(j).GetText().contains(Keywords[k]) == false) { found = false; break; }
					}
					if (found == true) {
						synchronized (SearchResult) {
							SearchResult.add(j);
						}
					}
				}
			});
			LastEndIndex = EndIndex;
		}

	}

	private static void SearchWithLabels(String[] Labels, ArrayList<Integer> SearchResult) {

	}
}

class DiscussionItem {
//	private String Username;
//	private String UserHomePage;
//	private boolean Modified;
//	private String Client;
//	private LocalDateTime DateTime;
//	private int NumberOfForwards;
//	private int NumberOfComments;
//	private int NumberOfLikes;

	private String Text;
	private ConcurrentHashMap<String, ArrayList<String>> Labels;

//	DiscussionItem(String Username, String UserHomePage, boolean Modified, String Client, LocalDateTime DateTime, int NumberOfForwards, int NumberOfComments, int NumberOfLikes, String Text) {
//		this.Username = Username;
//		this.UserHomePage = UserHomePage;
//		this.Modified = Modified;
//		this.Client = Client;
//		this.DateTime = DateTime;
//		this.NumberOfForwards = NumberOfForwards;
//		this.NumberOfComments = NumberOfComments;
//		this.NumberOfLikes = NumberOfLikes;
//		this.Text = Text;
//	}

	DiscussionItem() { Labels = new ConcurrentHashMap<>(); }

	DiscussionItem(String Text) { this.Text = Text; Labels = new ConcurrentHashMap<>(); }

//	String GetUsername() { return Username; }
//
//	String GetUserHomePage() { return UserHomePage; }
//
//	boolean IsModified() { return Modified; }
//
//	String GetClient() { return Client; }
//
//	LocalDateTime GetDateTime() { return DateTime; }
//
//	int GetNumberOfForwards() { return NumberOfForwards; }
//
//	int GetNumberOfComments() { return NumberOfComments; }
//
//	int GetNumberOfLikes() { return NumberOfLikes; }

	String GetText() { return Text; }

	ConcurrentHashMap<String, ArrayList<String>> GetLabels() { return Labels; }

	void SetText(String Text) { this.Text = Text; }
}
