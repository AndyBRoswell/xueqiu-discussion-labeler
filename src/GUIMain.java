import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GUIMain extends JFrame {
	// 默认字体
	static final Font DefaultFont = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 菜单
	final JMenuBar MenuBar = new JMenuBar();

	final JMenu FileMenu = new JMenu("文件");
	final JMenuItem ImportMenuItem = new JMenuItem("导入股票讨论 CSV 文件");
	final JMenuItem ExportMenuItem = new JMenuItem("导出股票讨论 CSV 文件");
	final JMenuItem SetMenuItem = new JMenuItem("设置");
	final JMenuItem JournalMenuItem = new JMenuItem("日志");
	final JMenuItem ExitMenuItem = new JMenuItem("退出");

	final JMenu TaskMenu = new JMenu("任务");
	final JMenuItem AddMenuItem = new JMenuItem("添加爬取任务");

	final JMenu BackupRestoreMenu = new JMenu("备份/恢复");

	final JMenu StatisticMenu = new JMenu("统计");

	// 主界面图标
	static final ImageIcon icoDownload = new ImageIcon(Global.IconPath + "\\download.png");
	static final ImageIcon icoAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	static final ImageIcon icoSmallAdd = new ImageIcon(Global.IconPath + "\\add.png");

	/*按钮*/
	final JButton btnTaskList = new JButton(icoDownload);
	final JButton btnAddAvailableLabelCategory = new JButton(icoAdd);
	final JButton btnSaveAvailableLabels = new JButton("保存可选标注");

	/*快捷筛选复选框*/
	final JCheckBox cbLabeled = new JCheckBox("已标注");
	final JCheckBox cbUnlabeled = new JCheckBox("未标注");

	/*搜索栏*/
	final JTextField tfSearchByText = new JTextField();
	final JTextField tfSearchByLabel = new JTextField();

	/*全部可选标签*/
	final JPanel AllLabelsPanel = new JPanel();
	final JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	final JLabel AllAvailableLabelsLabel = new JLabel("可选标注");

	// 表格
	DiscussionTableModel TableModel;
	JTable DiscussionTable;
	JScrollPane DiscussionScrollPane;
	static final LabeledCountComponent LabeledFor0Times = new LabeledCountComponent(0);

	// 动作监听程序
	final MainFrameListener Listener = new MainFrameListener();

	// 标签按钮（内部类）：点击标签进行添加或删除
	static class LabelButton extends JButton {
		public LabelButton(String Text) {
			super(Text);
//			super.setBorder(null); // Don't show ellipsis.
		}
	}

	// 标签类名称标签控件（内部类）
	static class LabelCategoryComponent extends JLabel {
		public LabelCategoryComponent(String Text) {
			super(Text);
			super.setHorizontalAlignment(JLabel.CENTER);
		}
	}

	// 标签被选中次数标签控件（内部类）
	static class LabeledCountComponent extends JLabel {
		public LabeledCountComponent(int Count) {
			super(String.valueOf(Count));
			super.setHorizontalAlignment(JLabel.CENTER);
		}
	}

	// 股票讨论表表格模型（内部类）
	static class DiscussionTableModel extends AbstractTableModel {
		private final String[] ColumnNames = new String[]{ "股票讨论内容", "标注" };

		@Override public int getRowCount() { return DataManipulator.GetDiscussionList().size(); }

		@Override public int getColumnCount() { return 2; }

		@Override public String getColumnName(int col) { return ColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return switch (columnIndex) {
				case 0 -> DataManipulator.GetDiscussionItem(rowIndex).GetText();
				case 1 -> DataManipulator.GetDiscussionItem(rowIndex).GetLabels();
				default -> null;
			};
		}

		@Override public boolean isCellEditable(int row, int col) { return false; } // 不能直接在表格上编辑，而需要通过全部可选标注面板

		@Override public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	}

	// 主窗体动作监听程序（内部类）
	class MainFrameListener implements ComponentListener {
		static final int w0 = Global.FontSizeD;
		static final int h0 = 2 * Global.FontSizeD;
		static final int wGUILabel = 6 * w0;
		static final int gap = Global.ComponentGapD;
		static final int LabelPadding = Global.StringPaddingInChrD;
		static final int ButtonPadding = 3 * Global.StringPaddingInChrD;

		@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
			final GUIMain MainFrame = (GUIMain) e.getComponent();
			final int X = MainFrame.getContentPane().getWidth();
			final int Y = MainFrame.getContentPane().getHeight();

			/*下载（任务列表）按钮*/
			btnTaskList.setBounds(X - icoDownload.getIconWidth(), h0 / 2, icoDownload.getIconWidth(), icoDownload.getIconHeight());

			/*搜索行*/
			cbLabeled.setBounds(X - wGUILabel - icoDownload.getIconWidth(), 0, wGUILabel, h0);
			cbUnlabeled.setBounds(X - wGUILabel - icoDownload.getIconWidth(), cbLabeled.getHeight(), wGUILabel, h0);
			tfSearchByText.setBounds(0, 0, cbLabeled.getX(), h0);
			tfSearchByLabel.setBounds(0, cbLabeled.getHeight(), cbLabeled.getX(), h0);

			/*表格*/
			DiscussionTable.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
			DiscussionScrollPane.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);

			/*标注添加标签与按钮*/
			AllAvailableLabelsLabel.setBounds(0, DiscussionTable.getY() + DiscussionTable.getHeight(), wGUILabel, h0);
			btnAddAvailableLabelCategory.setBounds(0, AllAvailableLabelsLabel.getY() + AllAvailableLabelsLabel.getHeight(), GUIMain.icoAdd.getIconWidth(), GUIMain.icoAdd.getIconHeight());
			btnAddAvailableLabelCategory.setBorderPainted(false);
			btnSaveAvailableLabels.setBounds(0, btnAddAvailableLabelCategory.getY() + btnAddAvailableLabelCategory.getHeight(), wGUILabel, 2 * h0);
			btnSaveAvailableLabels.setBorderPainted(false);

			/*可选标注滚动面板*/
//			AllLabelsPanel.setBounds(AllAvailableLabelsLabel.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsLabel.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
			AllLabelsPanel.setLayout(null);
			AllLabelsScrollPane.setBounds(AllAvailableLabelsLabel.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsLabel.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
			AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//			AllLabelsScrollPane.setPreferredSize(new Dimension(AllLabelsScrollPane.getWidth(), AllLabelsScrollPane.getHeight()));

			// 可选标注面板内容
			int XC = 0, YC = 0; // 当前摆放控件的位置
			final int XM = AllLabelsScrollPane.getWidth(), YM = AllLabelsScrollPane.getHeight();
			AllLabelsPanel.removeAll(); // 先清除已有的控件，准备重新排布

			// 将全部可选标签布局在可选标签面板上，并显示当前选中的股评条目中，各标签被选中的总次数
			final int[] SelectedRows = DiscussionTable.getSelectedRows();
			final ConcurrentHashMap<String, HashSet<String>> AllLabels = DataManipulator.GetAllLabels();
			for (Map.Entry<String, HashSet<String>> Cat : AllLabels.entrySet()) {
				int w, h;

				// 标签类名称控件
				final String CatName = Cat.getKey();
				final LabelCategoryComponent lbCatName = new LabelCategoryComponent(CatName);
				w = w0 * (CatName.length() + LabelPadding);
				if (w > XM - XC) { XC = 0; YC += h0; } // 控件过长，放到下一行
				lbCatName.setBounds(XC, YC, w, h0);
				AllLabelsPanel.add(lbCatName);
				XC += lbCatName.getWidth();

				// 每一类标签及其使用数据
				for (String LabelName : Cat.getValue()) {
					// 标签控件
					final LabelButton btLabel = new LabelButton(LabelName);
					w = w0 * (LabelName.length() + ButtonPadding);
					if (w > XM - XC) { XC = 0; YC += h0; } // 控件过长，放到下一行
					btLabel.setBounds(XC, YC, w, h0);
					AllLabelsPanel.add(btLabel);
					XC += btLabel.getWidth();
					// 被选中次数标签控件
					int TotalLabeledCount = 0;
					for (int i : SelectedRows) { // 对所有选中的行，查找每个标签被标注的次数
						final HashMap<String, Integer> ContainedLabelsOfThisCat = DataManipulator.GetDiscussionItem(i).GetLabels().get(CatName);
						if (ContainedLabelsOfThisCat != null) { // 在该股评条目的标注中找到了当前的标签类
							final Integer c = ContainedLabelsOfThisCat.get(LabelName);
							if (c != null) TotalLabeledCount += c;
						}
					}
//					LabeledCountComponent lbCount = LabeledFor0Times;
//					if (TotalLabeledCount != 0) {
//						lbCount = new LabeledCountComponent(TotalLabeledCount);
//					}
					LabeledCountComponent lbCount = new LabeledCountComponent(TotalLabeledCount);
					w = w0 + (lbCount.getText().length() + LabelPadding);
					if (w > XM - XC) { XC = 0; YC += h0; } // 控件过长，放到下一行
					lbCount.setBounds(XC, YC, w, h0);
					AllLabelsPanel.add(lbCount);
					XC += lbCount.getWidth();
				}

				// 添加标签按钮
				final JButton btnAddLabel = new JButton(icoSmallAdd);
				w = icoSmallAdd.getIconWidth() * 3 / 2;
				h = icoSmallAdd.getIconHeight() * 3 / 2;
				if (w > XM - XC) { XC = 0; YC += h0; } // 控件过长，放到下一行
				btnAddLabel.setBounds(XC, YC, w, h);
				btnAddLabel.addActionListener(new ActionListener() {
					@Override public void actionPerformed(ActionEvent e) {
						new GUIAddLabel((GUIMain) SwingUtilities.getRoot(btnAddLabel), Cat.getKey());
					}
				});
				AllLabelsPanel.add(btnAddLabel);
				XC += btnAddLabel.getWidth();
			}

			AllLabelsPanel.setPreferredSize(new Dimension(AllLabelsScrollPane.getWidth(), YC + h0)); // 不正确地设置此处的参数会导致可选标注面板的滚动范围不正确
		}

		@Override public void componentMoved(ComponentEvent e) {}

		@Override public void componentShown(ComponentEvent e) {}

		@Override public void componentHidden(ComponentEvent e) {}
	}

	// 初始化主界面
	public GUIMain() throws IOException, SAXException, XPathExpressionException {
		// 读取必要的数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null);
		super.getContentPane().setLayout(null);

		// 添加菜单
		MenuBar.add(FileMenu); MenuBar.add(TaskMenu); MenuBar.add(BackupRestoreMenu); MenuBar.add(StatisticMenu);
		super.setJMenuBar(MenuBar);

		FileMenu.add(ImportMenuItem); FileMenu.add(ExportMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(JournalMenuItem); FileMenu.add(SetMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		TaskMenu.add(AddMenuItem);

		// 添加空白表格用于占位
		TableModel = new DiscussionTableModel();
		DiscussionTable = new JTable(TableModel);
		DiscussionScrollPane = new JScrollPane(DiscussionTable);

		// 添加动作监听程序
		super.addComponentListener(Listener); // 主界面

		btnAddAvailableLabelCategory.addActionListener(new ActionListener() { // 添加标注类按钮
			@Override public void actionPerformed(ActionEvent e) {
				new GUIAddLabelCategory((GUIMain) SwingUtilities.getRoot(btnAddAvailableLabelCategory));
			}
		});

		btnSaveAvailableLabels.addActionListener(new ActionListener() { // 添加标注按钮
			@Override public void actionPerformed(ActionEvent e) {
				try { StorageAccessor.SaveAllAvailableLabels(); }
				catch (IOException | XPathExpressionException IOOrXPathException) { IOOrXPathException.printStackTrace(); }
			}
		});

		// 菜单项的动作监听程序
		ImportMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				new GUIImportList();
			}
		});

		// 添加控件
		super.add(btnTaskList); super.add(cbLabeled); super.add(cbUnlabeled);
		super.add(tfSearchByText); super.add(tfSearchByLabel);
		super.add(AllAvailableLabelsLabel); super.add(btnAddAvailableLabelCategory); super.add(btnSaveAvailableLabels);

		super.add(DiscussionScrollPane);
		super.add(AllLabelsScrollPane);

		// 显示
		super.setVisible(true);
	}

	// 重绘主界面
	public void Refresh() {
		final Dimension d = this.getSize();
		--d.height;
		this.setSize(d);
		++d.height;
		this.setSize(d);
	}

	// 选中动作监听程序（内部类）
	class RowSelectionListener implements ListSelectionListener {
		@Override public void valueChanged(ListSelectionEvent e) {
			Refresh();
		}
	}

	// 用于多行显示的单元格渲染器（内部类）
	static class LineWrapCellRenderer extends JTextArea implements TableCellRenderer {
		public LineWrapCellRenderer() {
			this.setWrapStyleWord(true);
			this.setLineWrap(true);
		}

		@Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof String) this.setText((String) value);
			else { // ConcurrentHashMap<String, HashMap<String, Integer>>
				this.setText("");
				ConcurrentHashMap<String, HashMap<String, Integer>> LabelCategories = (ConcurrentHashMap<String, HashMap<String, Integer>>) value;
				for (Map.Entry<String, HashMap<String, Integer>> CatItem : LabelCategories.entrySet()) {
					this.append(CatItem.getKey()); // 标签类名称
					for (String Label : CatItem.getValue().keySet()) this.append(" " + Label); // 标签名称
					this.append(Global.LineSeparator);
				}
//				System.out.println(LabelCategories);
//				System.out.println("Row " + row + " Column " + column + ":");
//				System.out.println(this.getText());
//				System.out.println("================================================================");
			}
//			final int FontHeight = this.getFontMetrics(this.getFont()).getHeight();
			final int FontHeight = Global.FontSizeD;
//			final int TextLength = this.getText().length();
			final int TextPixelLength = this.getFontMetrics(this.getFont()).stringWidth(this.getText());
			final int CellWidth = table.getColumnModel().getColumn(column).getWidth();
//			final int CellWidth = table.getCellRect(row, column, false).width;
//			final Container ContentPane = table.getParent();
//			final int CellWidth = ContentPane.getWidth() / 2;
			final int LineCount = Math.max(TextPixelLength / CellWidth, 1);
//			table.setRowHeight(row, Math.max(table.getRowHeight(), FontHeight * LineCount));
//			this.setSize(table.getColumnModel().getColumn(column).getWidth(), table.getRowHeight(row));
//			this.validate();
//			this.setPreferredSize(new Dimension(CellWidth, FontHeight * LineCount));
			this.setSize(new Dimension(CellWidth, FontHeight * LineCount));
//			System.out.println("Expected cell size at <" + row + ", " + column + ">: " + FontHeight * LineCount);
//			System.out.println("Method getTableCellRendererComponent completed.");
			return this;
		}
	}

	// 显示股票讨论
	public void ShowDiscussions() {
		// 添加表格需要的组件
		this.remove(DiscussionScrollPane);
		TableModel = new DiscussionTableModel();
		DiscussionTable = new JTable(TableModel);
		DiscussionScrollPane = new JScrollPane(DiscussionTable);

		// 动作监听程序与单元格渲染程序
		TableModel.addTableModelListener(new TableModelListener() { // 表格内容改变时，行高自适应改变
			@Override public void tableChanged(TableModelEvent e) {
				final int Row = e.getFirstRow();
				int Height = 0;
				for (int Column = 0; Column < TableModel.getColumnCount(); ++Column) {
					Component comp = DiscussionTable.prepareRenderer(DiscussionTable.getCellRenderer(Row, Column), Row, Column);
//					Height = Math.max(Height, comp.getPreferredSize().height);
					Height = Math.max(Height, comp.getHeight());
//					System.out.println("Set cell size at <" + Row + ", " + Column + ">: " + Height);
				}
				DiscussionTable.setRowHeight(Row, Height);
//				System.out.println("Method tableChanged completed.");
			}
		});
		DiscussionTable.getSelectionModel().addListSelectionListener(new RowSelectionListener()); // 当选中股评时，可选标注面板显示各个标签被选中的数量
		DiscussionTable.getColumnModel().getColumn(0).setCellRenderer(new LineWrapCellRenderer());
		DiscussionTable.getColumnModel().getColumn(1).setCellRenderer(new LineWrapCellRenderer());

		for (int i = 0; i < TableModel.getRowCount(); ++i)
			for (int j = 0; j < TableModel.getColumnCount(); ++j)
				TableModel.fireTableCellUpdated(i, j);

		this.add(DiscussionScrollPane);
		Refresh();
	}
}
