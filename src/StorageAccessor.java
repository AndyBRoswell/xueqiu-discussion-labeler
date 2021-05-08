import com.univocity.parsers.common.*;
import com.univocity.parsers.common.processor.*;
import com.univocity.parsers.common.record.*;
import com.univocity.parsers.conversions.*;
import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.util.List;

public class StorageAccessor {
	static final CsvParserSettings settings = new CsvParserSettings();
	static final CsvParser parser = new CsvParser(settings);

	public static void ReadCSV(String pathname) throws XPathExpressionException {
		File file = new File(pathname);

		settings.getFormat().setLineSeparator(Global.LineSeparator);
		List<String[]> Rows =  parser.parseAll(file, Config.QuerySingleConfigEntry("/storage/import-and-export/default-encoding"));
	}
}
