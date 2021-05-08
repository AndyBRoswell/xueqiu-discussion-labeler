import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StorageAccessor {
	static final CsvParserSettings ParserSettings = new CsvParserSettings();
	static final CsvWriterSettings WriterSettings = new CsvWriterSettings();
	static final CsvParser parser = new CsvParser(ParserSettings);
	static final CsvWriter writer = new CsvWriter(WriterSettings);

	public static void InitCSVParserWriterSettings() {
		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
	}

	public static void LoadDiscussionFromCSV(String pathname) throws XPathExpressionException {
		final File file = new File(pathname);

		parser.beginParsing(file, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));

		String[] SingleRow;
		DiscussionItem item = new DiscussionItem();
		while ((SingleRow = parser.parseNext()) != null) {
			item.SetText(Arrays.toString(SingleRow));
			DataManipulator.DiscussionList.add(item);
		}
	}

	public static void SaveDiscussionToCSV(String pathname) {
		String FileContent = null;
		for (DiscussionItem discussion : DataManipulator.DiscussionList) {

		}
	}

	public static void LoadAllAvailableLabels() throws XPathExpressionException, FileNotFoundException {
		File file = new File(Global.FileOfAllLabels);
		Scanner scanner = new Scanner(file, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
		while (scanner.hasNextLine()) {
			String Line = scanner.nextLine();
			String[] LabelCategory = Line.split("\\s");
			DataManipulator.AllLabels.put(LabelCategory[0], new ArrayList<>());
			for (int i = 1; i < LabelCategory.length; ++i) DataManipulator.AllLabels.get(LabelCategory[0]).add(LabelCategory[i]);
		}
	}

	public static void SaveAllAvailableLabels() {

	}
}
