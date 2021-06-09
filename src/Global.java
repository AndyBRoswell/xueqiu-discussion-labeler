public class Global {
	// Controls
	public static final int ComponentGapD = 5;
	public static final int StringPaddingInChrD = 1;

	// Directories
	public static final String AppPath = System.getProperty("user.dir");
	public static final String ConfigPath = AppPath + "\\cfg";
	public static final String CrawlResultsPath = AppPath + "\\crawlresults";
	public static final String DefaultConfig = ConfigPath + "\\config.xml";
	public static final String DefaultSavePath = AppPath + "\\save";
	public static final String IconPath = AppPath + "\\icon";
	public static final String LabelFile = ConfigPath + "\\labels.txt";
	public static final String LineSeparator = System.lineSeparator();
	public static final String TemporaryPath = AppPath + "\\tmp";
	public static final String LogPath = AppPath + "\\log";

	// Fonts
	public static final int FontSizeD = 12;
	public static final int FontSizeL = 16;

	// Main Window
	public static GUIMain MainForm;
}
