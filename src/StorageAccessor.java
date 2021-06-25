import com.univocity.parsers.common.*;
import com.univocity.parsers.csv.*;

import javax.xml.xpath.XPathExpressionException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StorageAccessor {
	//	static final CsvParserSettings ParserSettings = new CsvParserSettings();
	//	static final CsvWriterSettings WriterSettings = new CsvWriterSettings();
	//	static final CsvParser parser = new CsvParser(ParserSettings);
	//	static final CsvWriter writer = new CsvWriter(WriterSettings);
	static FileReader LabelFileReader = null;
	static BufferedReader BufferedLabelFileReader = null;
	static FileWriter LabelFileWriter = null;
	static BufferedWriter BufferedLabelFileWriter = null;
	static File DiscussionCSVFile = null;
	static FileWriter CSVFileWriter = null;
	static BufferedWriter BufferedCSVFileWriter = null;

	static {
//		ParserSettings.setAutoConfigurationEnabled(false);
//		ParserSettings.getFormat().setLineSeparator(Global.LineSeparator);
//		ParserSettings.getFormat().setDelimiter('`');
//		ParserSettings.setMaxCharsPerColumn(-1);
//		ParserSettings.setProcessorErrorHandler(new RetryableErrorHandler<ParsingContext>() {
//			@Override public void handleError(DataProcessingException e, Object[] objects, ParsingContext parsingContext) {
//				System.out.println("ERROR when processing row: " + e.getLineIndex() + 1 + ": " + e.getMessage());
//				e.markAsNonFatal();
//			}
//		});
//
//		WriterSettings.getFormat().setDelimiter('`');
//		WriterSettings.getFormat().setQuote('\"');
//		WriterSettings.getFormat().setQuoteEscape('\"');
	}

	public static void LoadAllAvailableLabels() throws IOException, XPathExpressionException { // 读取全部可选标注
		LabelFileReader = new FileReader(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileReader = new BufferedReader(LabelFileReader);

		String line;
		while ((line = BufferedLabelFileReader.readLine()) != null) { // 逐行读取并解析为标签类：以空格作为分隔符，第0个词为标签类名称，剩下的词都为该类的标签
			ParseSingleLabelLineToLabelCategoryAndAdd(line, DataManipulator.GetAllLabels());
		}

		BufferedLabelFileReader.close();
	}

	private static void ParseSingleLabelLineToLabelCategoryAndAdd(String line, ConcurrentHashMap<String, HashSet<String>> dest) {
		final String[] LabelCategory = line.split("\\s"); // 按空格分裂一行，第0个词为标签类，剩下的词都为该类的标签
		dest.put(LabelCategory[0], new HashSet<>());
		for (int i = 1; i < LabelCategory.length; ++i) {
			dest.get(LabelCategory[0]).add(LabelCategory[i]); // 为该标签类添加具体的标签项
			DataManipulator.AddCategoryOfLabel(LabelCategory[i], LabelCategory[0]); // 添加该标签所属的标签类
		}
	}

	private static void ParseSingleLabelLineToLabelCategoryWithCountAndAdd(String LabelLine, ConcurrentHashMap<String, HashMap<String, LabelStatus>> Dest) {
		final String[] LabelCategory = LabelLine.split("\\s"); // 按空格分裂一行，第0个词为标签类，剩下的词都为该类的标签，以及被选中（标注）次数
		Dest.put(LabelCategory[0], new HashMap<>());
		for (int i = 1; i < LabelCategory.length; i += 2) {
			Dest.get(LabelCategory[0]).put(LabelCategory[i], new LabelStatus(Integer.parseInt(LabelCategory[i + 1]))); // 为该标签类添加具体的标签项及被标注次数
			DataManipulator.AddCategoryOfLabel(LabelCategory[i], LabelCategory[0]); // 添加该标签所属的标签类
		}
	}

	private static void ParseStringToLabelCategoriesWithCountsAndAdd(String string, ConcurrentHashMap<String, HashMap<String, LabelStatus>> dest) {
		// 原格式：在单个单元格中存储全部类别的标签。新格式：股评后的每个单元格存储一类标签。此方法不受影响。
		final String[] lines = string.split("\\s"); // 空行将被忽略
		for (String line : lines) {
			ParseSingleLabelLineToLabelCategoryWithCountAndAdd(line, dest);
		}
	}

	private static void ParseStringToLabelCategoriesAndAdd(String string, ConcurrentHashMap<String, HashSet<String>> dest) {
		final String[] lines = string.split("\\R+"); // 空行将被忽略
		for (String line : lines) {
			ParseSingleLabelLineToLabelCategoryAndAdd(line, dest);
		}
	}

	public static void SaveAllAvailableLabels() throws IOException, XPathExpressionException {
		LabelFileWriter = new FileWriter(Global.LabelFile, Charset.forName(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding")));
		BufferedLabelFileWriter = new BufferedWriter(LabelFileWriter);

		for (Map.Entry<String, HashSet<String>> entry : DataManipulator.GetAllLabels().entrySet()) { // 开始逐行写入
			BufferedLabelFileWriter.write(entry.getKey());
			for (String i : entry.getValue()) BufferedLabelFileWriter.write(' ' + i);
			BufferedLabelFileWriter.write(Global.LineSeparator);
		}

		BufferedLabelFileWriter.close();
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
		ParserSettings.getFormat().setDelimiter('`');
		ParserSettings.setMaxCharsPerColumn(-1);
		CsvParser parser = new CsvParser(ParserSettings);

		parser.beginParsing(DiscussionCSVFile, encoding);

		String[] SingleRow = null;
		switch (PreprocessMode) {
			case 0: // 逐行解析 CSV 文件中的讨论内容并添加到讨论列表
				for (; ; ) {
					try {
						SingleRow = parser.parseNext();
						if (SingleRow == null) break;
					}
					catch (TextParsingException e) {
//						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage());
						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage().split("\\R")[0]);
					}
					DiscussionItem item = new DiscussionItem(SingleRow[0]);
//					ParseStringToLabelCategoriesWithCountsAndAdd(SingleRow[1], item.GetLabels());
					for (int i = 1; i < SingleRow.length; ++i) { // 新格式：股评后的每个单元格存储一类标签。
						ParseSingleLabelLineToLabelCategoryWithCountAndAdd(SingleRow[i], item.GetLabels());
					}
					DataManipulator.AddDiscussionItem(item);
				}
				break;
			case 1: // 逐行解析保存了刚刚爬取的结果（不含任何标注）的 CSV 文件中的讨论内容并添加到讨论列表
				for (; ; ) {
					try {
						SingleRow = parser.parseNext();
						if (SingleRow == null) break;
					}
					catch (TextParsingException e) {
//						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage());
						System.out.println(e.getLineIndex() + 1 + ": " + e.getMessage().split("\\R")[0]);
					}
					DiscussionItem item = new DiscussionItem(SingleRow[0]);
					DataManipulator.AddDiscussionItem(item);
				}
				break;
		}

		parser.stopParsing();
	}

	public static void SaveDiscussionToCSV(String pathname) throws IOException, XPathExpressionException {
		WriteToCSVFile(pathname, Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-encoding"));
	}

	public static void SaveDiscussionToCSV(String pathname, String encoding) throws IOException {
		WriteToCSVFile(pathname, encoding);
	}

	private static void WriteToCSVFile(String pathname, String encoding) throws IOException {
		CsvWriterSettings WriterSettings = new CsvWriterSettings();
		WriterSettings.getFormat().setDelimiter('`');
		WriterSettings.getFormat().setQuote('\"');
		WriterSettings.getFormat().setQuoteEscape('\"');
		CsvWriter writer = new CsvWriter(WriterSettings);

		CSVFileWriter = new FileWriter(pathname, Charset.forName(encoding));
		BufferedCSVFileWriter = new BufferedWriter(CSVFileWriter);
		final StringBuilder FileContent = new StringBuilder();

		for (DiscussionItem discussion : DataManipulator.GetDiscussionList()) { // 将每条讨论逐行写入 CSV 文件
			// 原格式：在单个单元格中存储全部类别的标签。
//			String[] row = { discussion.GetText(), MergeLabelCategoriesWithCountToString(discussion.GetLabels()) };
			// 新格式：股评后的每个单元格存储一类标签。
			ArrayList<String> row = new ArrayList<>();
			row.add(discussion.GetText()); // 最左一列为股评
			for (Map.Entry<String, HashMap<String, LabelStatus>> entry : discussion.GetLabels().entrySet()) {
				final StringBuilder builder = new StringBuilder();
				builder.append(entry.getKey()); // 标注类名称
				for (Map.Entry<String, LabelStatus> e : entry.getValue().entrySet()) { // 逐步构建该类标注数据为字符串
					builder.append(' ').append(e.getKey()).append(' ').append(e.getValue().toString());
				}
				row.add(builder.toString()); // 将该类标签写入该行的一个单元格
			}
			FileContent.append(writer.writeRowToString(row)).append(Global.LineSeparator);
		}
		BufferedCSVFileWriter.append(FileContent);

		BufferedCSVFileWriter.close();
	}
}
