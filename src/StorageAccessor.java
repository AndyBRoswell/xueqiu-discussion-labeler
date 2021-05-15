import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
			ParseSingleLineToLabelCategoryAndAdd(line, DataManipulator.AllLabels);
		}

		BufferedLabelFileReader.close();
	}

	private static void ParseSingleLineToLabelCategoryAndAdd(String line, ConcurrentHashMap<String, ArrayList<String>> dest) {
		final String[] LabelCategory = line.split("\\s");
		DataManipulator.AllLabels.put(LabelCategory[0], new ArrayList<>());
		for (int i = 1; i < LabelCategory.length; ++i) dest.get(LabelCategory[0]).add(LabelCategory[i]);
	}

	private static void ParseStringToLabelCategoriesAndAdd(String string, ConcurrentHashMap<String, ArrayList<String>> dest) {
		final String[] lines = string.split("\\R+");
		for (String line : lines) {
			ParseSingleLineToLabelCategoryAndAdd(line, dest);
		}
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

	private static String MergeLabelCategoriesToString(ConcurrentHashMap<String, ArrayList<String>> labels) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, ArrayList<String>> entry : labels.entrySet()) {
			builder.append(entry.getKey());
			for (String i : entry.getValue()) {
				builder.append(' ' + i);
			}
			builder.append(Global.LineSeparator);
		}
		return builder.toString();
	}

	public static void LoadDiscussionFromCSV(String pathname) throws XPathExpressionException {
		DiscussionCSVFile = new File(pathname);
		ParseCSVFile(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	public static void LoadDiscussionFromCSV(String pathname, String encoding) {
		DiscussionCSVFile = new File(pathname);
		ParseCSVFile(encoding);
	}

	private static void ParseCSVFile(String encoding) {
		parser.beginParsing(DiscussionCSVFile, encoding);

		String[] SingleRow;
		final DiscussionItem item = new DiscussionItem();
		while ((SingleRow = parser.parseNext()) != null) {
			item.SetText(SingleRow[0]);
			ParseStringToLabelCategoriesAndAdd(SingleRow[1], item.GetLabels());
			DataManipulator.DiscussionList.add(item);
		}

		parser.stopParsing();
	}

	public static void SaveDiscussionToCSV(String pathname) throws IOException, XPathExpressionException {
		BeginWritingCSVFile(pathname, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	public static void SaveDiscussionToCSV(String pathname, String encoding) throws IOException {
		BeginWritingCSVFile(pathname, encoding);
	}

	private static void BeginWritingCSVFile(String pathname, String encoding) throws IOException {
		CSVFileWriter = new FileWriter(pathname, Charset.forName(encoding));
		BufferedCSVFileWriter = new BufferedWriter(CSVFileWriter);
		final StringBuilder FileContent = new StringBuilder();

		for (DiscussionItem discussion : DataManipulator.DiscussionList) {
			String[] row = { discussion.GetText(), MergeLabelCategoriesToString(discussion.GetLabels()) };
			FileContent.append(writer.writeRowToString(row));
		}
		BufferedCSVFileWriter.append(FileContent);

		BufferedCSVFileWriter.close();
	}
}
