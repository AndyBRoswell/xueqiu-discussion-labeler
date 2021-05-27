import com.univocity.parsers.common.*;
import com.univocity.parsers.common.processor.*;
import com.univocity.parsers.common.record.*;
import com.univocity.parsers.conversions.*;
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
		ParserSettings.setAutoConfigurationEnabled(false);
		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
		ParserSettings.getFormat().setDelimiter('`');
		ParserSettings.setMaxCharsPerColumn(-1);
		ParserSettings.setProcessorErrorHandler(new RetryableErrorHandler<ParsingContext>() {
			@Override public void handleError(DataProcessingException e, Object[] objects, ParsingContext parsingContext) {
				System.out.println("ERROR when processing row: " + e.getLineIndex() + 1 + ": " + e.getMessage());
				e.markAsNonFatal();
			}
		});

		WriterSettings.getFormat().setDelimiter('`');
		WriterSettings.getFormat().setQuote('\"');
		WriterSettings.getFormat().setQuoteEscape('\"');
	}

	public static void LoadAllAvailableLabels() throws IOException, XPathExpressionException { // 读取全部可选标注
		LabelFileReader = new FileReader(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileReader = new BufferedReader(LabelFileReader);

		String line;
		while ((line = BufferedLabelFileReader.readLine()) != null) { // 逐行读取并解析为标签类：以空格作为分隔符，第0个词为标签类名称，剩下的词都为该类的标签
			ParseSingleLineToLabelCategoryAndAdd(line, DataManipulator.AllLabels);
		}

		BufferedLabelFileReader.close();
	}

	private static void ParseSingleLineToLabelCategoryAndAdd(String line, ConcurrentHashMap<String, HashSet<String>> dest) {
		final String[] LabelCategory = line.split("\\s"); // 按空格分裂一行，第0个词为标签类，剩下的词都为该类的标签
		dest.put(LabelCategory[0], new HashSet<>());
		for (int i = 1; i < LabelCategory.length; ++i) {
			dest.get(LabelCategory[0]).add(LabelCategory[i]); // 为该标签类添加具体的标签项
			HashSet<String> categories = DataManipulator.LabelToCategory.get(LabelCategory[i]);
			if (categories == null) {
				DataManipulator.LabelToCategory.put(LabelCategory[i], new HashSet<>());
				categories = DataManipulator.LabelToCategory.get(LabelCategory[i]);
			}
			categories.add(LabelCategory[0]);
		}
	}

	private static void ParseStringToLabelCategoriesAndAdd(String string, ConcurrentHashMap<String, HashSet<String>> dest) {
		final String[] lines = string.split("\\R+");
		for (String line : lines) {
			ParseSingleLineToLabelCategoryAndAdd(line, dest);
		}
	}

	public static void SaveAllAvailableLabels() throws IOException, XPathExpressionException {
		LabelFileWriter = new FileWriter(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileWriter = new BufferedWriter(LabelFileWriter);

		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.AllLabels.entrySet()) { // 开始逐行写入
			BufferedLabelFileWriter.write(entry.getKey());
			for (String i : entry.getValue()) BufferedLabelFileWriter.write(' ' + i);
			BufferedLabelFileWriter.write(Global.LineSeparator);
		}

		BufferedLabelFileWriter.close();
	}

	private static String MergeLabelCategoriesToString(ConcurrentHashMap<String, HashSet<String>> labels) { // 将全部标签类及其标签转换成字符串的形式，供后续保存用
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, HashSet<String>> entry : labels.entrySet()) { // 逐类保存
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
		ParseCSVFile(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"), 0);
	}

	public static void LoadDiscussionFromCSV(String pathname, String encoding) {
		DiscussionCSVFile = new File(pathname);
		ParseCSVFile(encoding, 0);
	}

	public static void LoadDiscussionFromCSV(String pathname, String encoding, int PreprocessMode) {
		DiscussionCSVFile = new File(pathname);
		ParseCSVFile(encoding, PreprocessMode);
	}

	private static void ParseCSVFile(String encoding, int PreprocessMode) {
		// DO NOT DELETE THESE ROWS OR THE PARSER SETTINGS WILL HAVE NO EFFECTS (E.G. MaxCharsPerColumn == 4096 AND REMAINS UNCHANGED EVEN THOUGH IT IS -1 IN THE EXCEPTION MESSAGES.).
		CsvParserSettings ParserSettings = new CsvParserSettings();
		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
//		ParserSettings.getFormat().setDelimiter('`');
		ParserSettings.setMaxCharsPerColumn(-1);
		CsvParser parser = new CsvParser(ParserSettings);

		parser.beginParsing(DiscussionCSVFile, encoding);

		String[] SingleRow = null;
		switch (PreprocessMode) {
			case 0:
				for (; ; ) { // 逐行解析 CSV 文件中的讨论内容并添加到讨论列表
					try {
						SingleRow = parser.parseNext();
						if (SingleRow == null) break;
					}
					catch (TextParsingException e) {
//						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage());
						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage().split("\\R")[0]);
					}
					DiscussionItem item = new DiscussionItem();
					item.SetText(SingleRow[0]);
					ParseStringToLabelCategoriesAndAdd(SingleRow[1], item.GetLabels());
					DataManipulator.DiscussionList.add(item);
				}
				break;
			case 1:
				for (; ; ) { // 逐行解析保存了刚刚爬取的结果的 CSV 文件中的讨论内容并添加到讨论列表
					try {
						SingleRow = parser.parseNext();
						if (SingleRow == null) break;
					}
					catch (TextParsingException e) {
//						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage());
						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage().split("\\R")[0]);
					}
					DiscussionItem item = new DiscussionItem();
					item.SetText(SingleRow[0]);
					DataManipulator.DiscussionList.add(item);
				}
				break;
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
		CsvWriterSettings WriterSettings = new CsvWriterSettings();
		WriterSettings.getFormat().setDelimiter('`');
		WriterSettings.getFormat().setQuote('\"');
		WriterSettings.getFormat().setQuoteEscape('\"');
		CsvWriter writer = new CsvWriter(WriterSettings);

		CSVFileWriter = new FileWriter(pathname, Charset.forName(encoding));
		BufferedCSVFileWriter = new BufferedWriter(CSVFileWriter);
		final StringBuilder FileContent = new StringBuilder();

		for (DiscussionItem discussion : DataManipulator.DiscussionList) { // 将每条讨论逐行写入 CSV 文件
			String[] row = { discussion.GetText(), MergeLabelCategoriesToString(discussion.GetLabels()) };
			FileContent.append(writer.writeRowToString(row)).append(Global.LineSeparator);
		}
		BufferedCSVFileWriter.append(FileContent);

		BufferedCSVFileWriter.close();
	}
}
