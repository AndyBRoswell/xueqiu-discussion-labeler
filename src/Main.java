import java.time.LocalDateTime;

public class Main {
	public static void main(String[] args){
		new GUI();
		System.out.println("TEST:");
		String dt = "2021-04-23T22:26:30";
		DiscussionItem item = new DiscussionItem(
				"神奇梭哈卷",
				"https://xueqiu.com/u/9641911075",
				false,
				"雪球",
				LocalDateTime.parse(dt),
				1,
				2,
				3,
				"Diu nei lou mou");
		System.out.println(item.GetUsername());
		System.out.println(item.GetUserHomePage());
		System.out.println(item.IsModified());
		System.out.println(item.GetClient());
		System.out.println(item.GetDateTime().toString());
		System.out.println(item.GetNumberOfForwards());
		System.out.println(item.GetNumberOfComments());
		System.out.println(item.GetNumberOfLikes());
		System.out.println(item.GetText());
	}
}
