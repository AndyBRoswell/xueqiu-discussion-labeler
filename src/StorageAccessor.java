import com.univocity.parsers.common.*;
import com.univocity.parsers.common.processor.*;
import com.univocity.parsers.common.record.*;
import com.univocity.parsers.conversions.*;
import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class StorageAccessor {
	static final CsvParserSettings settings = new CsvParserSettings();
	static final CsvParser parser = new CsvParser(settings);

	public static void ReadCSV(String pathname) throws XPathExpressionException {
		final File file = new File(pathname);

		settings.getFormat().setLineSeparator(Global.LineSeparator);
		parser.beginParsing(file, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));

		String[] SingleRow;
		DiscussionItem item = new DiscussionItem();
		while ((SingleRow = parser.parseNext()) != null) {
			//System.out.println(SingleRow.length);
			//System.out.println(Arrays.toString(SingleRow));
			item.SetText(Arrays.toString(SingleRow));
		}
	}
}
