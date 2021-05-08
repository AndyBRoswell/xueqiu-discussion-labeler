import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;

public class StorageAccessor {
	static final CsvParserSettings ParserSettings = new CsvParserSettings();
	static final CsvWriterSettings WriterSettings = new CsvWriterSettings();
	static final CsvParser parser = new CsvParser(ParserSettings);
	static final CsvWriter writer = new CsvWriter(WriterSettings);

	public static void ReadDiscussionFromCSV(String pathname) throws XPathExpressionException {
		final File file = new File(pathname);

		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
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
}
