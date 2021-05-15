import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class StorageAccessor {
	static final CsvParserSettings ParserSettings = new CsvParserSettings();
	static final CsvWriterSettings WriterSettings = new CsvWriterSettings();
	static final CsvParser parser = new CsvParser(ParserSettings);
	static final CsvWriter writer = new CsvWriter(WriterSettings);
	static FileReader LabelFileReader = null;
	static BufferedReader BufferedLabelFileReader = null;
	static FileWriter LabelFileWriter = null;
	static BufferedWriter BufferedLabelFileWriter = null;
	static File DiscussionCSVFile = null;
	static FileWriter CSVFileWriter = null;
	static BufferedWriter BufferedCSVFileWriter = null;

	static {
		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
		WriterSettings.getFormat().setQuote('\"');
		WriterSettings.getFormat().setQuoteEscape('\"');
	}

	public static void LoadAllAvailableLabels() throws IOException, XPathExpressionException {
		LabelFileReader = new FileReader(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileReader = new BufferedReader(LabelFileReader);

		String line;
		while ((line = BufferedLabelFileReader.readLine()) != null) {
			ParseLineToLabelCategory(line);
		}
	}

	private static void ParseLineToLabelCategory(String line) {
		String[] LabelCategory;
		LabelCategory = line.split("\\s");
		DataManipulator.AllLabels.put(LabelCategory[0], new ArrayList<>());
		for (int i = 1; i < LabelCategory.length; ++i) DataManipulator.AllLabels.get(LabelCategory[0]).add(LabelCategory[i]);
	}

	public static void SaveAllAvailableLabels() throws IOException, XPathExpressionException {
		LabelFileWriter = new FileWriter(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileWriter = new BufferedWriter(LabelFileWriter);

		for (Map.Entry<String, ArrayList<String>> entry : DataManipulator.AllLabels.entrySet()) {
			BufferedLabelFileWriter.write(entry.getKey());
			for (String i : entry.getValue()) BufferedLabelFileWriter.write(' ' + i);
			BufferedLabelFileWriter.write(Global.LineSeparator);
		}

		BufferedLabelFileWriter.close();
	}

	private static void MergeLabelCategoryToLine() {

	}

	public static void LoadDiscussionFromCSV(String pathname) throws XPathExpressionException {
		DiscussionCSVFile = new File(pathname);
		BeginParsingCSVFile(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	public static void LoadDiscussionFromCSV(String pathname, String encoding) throws XPathExpressionException {
		DiscussionCSVFile = new File(pathname);
		BeginParsingCSVFile(encoding);
	}

	private static void BeginParsingCSVFile(String encoding) throws XPathExpressionException {
		parser.beginParsing(DiscussionCSVFile, encoding);

		String[] SingleRow;
		DiscussionItem item = new DiscussionItem();
		while ((SingleRow = parser.parseNext()) != null) {
			item.SetText(Arrays.toString(SingleRow));
			DataManipulator.DiscussionList.add(item);
		}
	}

	public static void SaveDiscussionToCSV(String pathname) throws IOException, XPathExpressionException {
		BeginWritingCSVFile(pathname, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	public static void SaveDiscussionToCSV(String pathname, String encoding) throws IOException, XPathExpressionException {
		BeginWritingCSVFile(pathname, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	private static void BeginWritingCSVFile(String pathname, String encoding) throws XPathExpressionException, IOException {
		CSVFileWriter = new FileWriter(pathname, Charset.forName(encoding));
		BufferedCSVFileWriter = new BufferedWriter(CSVFileWriter);
		StringBuilder FileContent = new StringBuilder();

		for (DiscussionItem discussion : DataManipulator.DiscussionList) {
			FileContent.append(writer.writeRowToString(discussion));
		}
		BufferedCSVFileWriter.append(FileContent);

		BufferedCSVFileWriter.close();
	}
}
