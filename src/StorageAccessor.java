import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
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
			//System.out.println(SingleRow.length);
			//System.out.println(Arrays.toString(SingleRow));
			item.SetText(Arrays.toString(SingleRow));
			DataManipulator.DiscussionList.add(item);
		}
	}

	public static void SaveDiscussionToCSV(String pathname) {
		
	}
}
