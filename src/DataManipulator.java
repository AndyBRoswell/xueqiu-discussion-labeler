import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DataManipulator {
	static ArrayList<DiscussionItem> DiscussionList = new ArrayList<>();
	static ConcurrentHashMap<String, ArrayList<String>> AllLabels = new ConcurrentHashMap<>();

	static void AddDiscussionItem(String text) { DiscussionList.add(new DiscussionItem(text)); }

	static DiscussionItem GetDiscussionItem(int index) { return DiscussionList.get(index); }

	static void DeleteDiscussionItem(int index) { DiscussionList.remove(index); }
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
	private ConcurrentHashMap<String, String> Label;

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

	DiscussionItem() {}

	DiscussionItem(String Text) {
		this.Text = Text;
	}

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

	ConcurrentHashMap<String, String> GetLabel() { return Label; }

	void SetText(String Text) { this.Text = Text; }
}
