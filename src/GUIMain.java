import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class GUIMain extends JFrame {
	// 本窗体
	final GUIMain ThisForm = this;

	// 所有已打开的子窗口
	GUIImportFiles ImportForm;
	GUIExportFiles ExportForm;
	GUISettings SettingsForm;
	GUIJournal LogForm;
	final GUICrawling TaskListForm = new GUICrawling();
	GUIStatistic StatForm;

	// 默认字体
	static final Font DefaultFont = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 菜单
	final JMenuBar MenuBar = new JMenuBar();

	final JMenu FileMenu = new JMenu("文件");
	final JMenuItem ImportMenuItem = new JMenuItem("导入股票讨论 CSV 文件");
	final JMenuItem ExportMenuItem = new JMenuItem("导出股票讨论 CSV 文件");
	final JMenuItem SettingsMenuItem = new JMenuItem("设置");
	final JMenuItem JournalMenuItem = new JMenuItem("日志");
	final JMenuItem ExitMenuItem = new JMenuItem("退出");

	final JMenu TaskMenu = new JMenu("任务");
	final JMenuItem ViewTaskListMenuItem = new JMenuItem("查看任务列表");

	final JMenu BackupRestoreMenu = new JMenu("备份/恢复");

	final JMenu StatisticsMenu = new JMenu("统计");
	final JMenuItem ViewStatisticsMenuItem = new JMenuItem("查看标注统计图");

	// 主界面图标
	static final ImageIcon icoAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	static final ImageIcon icoSmallAdd = new ImageIcon(Global.IconPath + "\\add.png");

	/*按钮*/
	final JButton btnAddAvailableLabelCategory = new JButton(icoAdd);
	final JButton btnSaveAvailableLabels = new JButton("保存可选标注");
	final JButton btnSearch = new JButton("搜索");
	final JButton btnBack = new JButton("返回");
	final JButton btnClearDiscussionList = new JButton("清空股评列表");

	/*快捷筛选复选框*/
	final JCheckBox cbLabeled = new JCheckBox("已标注");
	final JCheckBox cbUnlabeled = new JCheckBox("未标注");

	/*搜索栏*/
	final JTextField tfSearchByText = new JTextField();
	final JTextField tfSearchByLabel = new JTextField();

	/*全部可选标签*/
	int[] SelectedRows;
	final JPanel AllLabelsPanel = new JPanel();
	final JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
//	final JLabel AllAvailableLabelsLabel = new JLabel("可选标注");

	// 表格
	DiscussionTableModel DiscussionModel;
	JTable DiscussionTable;
	JScrollPane DiscussionScrollPane;
	static final LabeledCountComponent LabeledFor0Times = new LabeledCountComponent(0);

	// 搜索结果
	DiscussionTableModel SearchResultModel;
	JTable SearchResultTable;
	JScrollPane SearchResultScrollPane;

	// 标签按钮（内部类）：点击标签进行添加或删除
	static class LabelButton extends JButton {
		String Category;

		public LabelButton(String Label, String Category) {
			super(Label);
			this.Category = Category;
//			super.setBorder(null); // Don't show ellipsis.
		}
	}

	// 标签类名称标签控件（内部类）
	static class LabelCategoryComponent extends JLabel {
		public LabelCategoryComponent(String LabelCategory) {
			super(LabelCategory);
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
		private final boolean IsSearchResults;

		public DiscussionTableModel() { this.IsSearchResults = false; }

		public DiscussionTableModel(boolean IsSearchResults) { this.IsSearchResults = IsSearchResults; }

		@Override public int getRowCount() {
			if (IsSearchResults == false) return DataManipulator.GetDiscussionList().size();
			return DataManipulator.GetLastSearchResult() == null ? 0 : DataManipulator.GetLastSearchResult().size();
		}

		@Override public int getColumnCount() { return 2; }

		@Override public String getColumnName(int col) { return ColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			if (IsSearchResults == false) { // 显示全部股评
				return switch (columnIndex) {
					case 0 -> DataManipulator.GetDiscussionItem(rowIndex).GetText();
					case 1 -> DataManipulator.GetDiscussionItem(rowIndex).GetLabels();
					default -> null;
				};
			}
			else { // 显示搜索结果
				return switch (columnIndex) {
					case 0 -> DataManipulator.GetDiscussionItem(DataManipulator.GetLastSearchResult().get(rowIndex)).GetText();
					case 1 -> DataManipulator.GetDiscussionItem(DataManipulator.GetLastSearchResult().get(rowIndex)).GetLabels();
					default -> null;
				};
			}
		}

		@Override public boolean isCellEditable(int row, int col) { return false; } // 不能直接在表格上编辑，而需要通过全部可选标注面板

		@Override public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	}

	// 主窗体动作监听程序（内部类）
	class MainFrameListener implements ComponentListener {
		@Override public void componentResized(ComponentEvent e) { Refresh(); }

		@Override public void componentMoved(ComponentEvent e) {}

		@Override public void componentShown(ComponentEvent e) {}

		@Override public void componentHidden(ComponentEvent e) {}
	}

	private void RearrangeComponents() { // 调整各控件的位置与大小
		final int w0 = Global.FontSizeD;
		final int h0 = 2 * Global.FontSizeD;
		final int wGUILabel = 6 * w0;
		final int gap = Global.ComponentGapD;
		final int LabelPadding = Global.StringPaddingInChrD;
		final int ButtonPadding = 3 * Global.StringPaddingInChrD;

		final int X = ThisForm.getContentPane().getWidth();
		final int Y = ThisForm.getContentPane().getHeight();

		/*搜索*/
		btnSearch.setBounds(X - w0 * (btnSearch.getText().length() + ButtonPadding), 0, w0 * (btnSearch.getText().length() + ButtonPadding), h0);
		btnBack.setBounds(X - w0 * (btnBack.getText().length() + ButtonPadding), h0, w0 * (btnBack.getText().length() + ButtonPadding), h0);
		cbLabeled.setBounds(btnSearch.getX() - wGUILabel, 0, wGUILabel, h0);
		cbUnlabeled.setBounds(btnSearch.getX() - wGUILabel, cbLabeled.getHeight(), wGUILabel, h0);
		tfSearchByText.setBounds(0, 0, cbLabeled.getX(), h0);
		tfSearchByLabel.setBounds(0, cbLabeled.getHeight(), cbLabeled.getX(), h0);

		/*表格*/
		DiscussionTable.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
		DiscussionScrollPane.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
		if (SearchResultScrollPane != null) { // 如果有搜索结果，将覆盖原来显示全部股评的位置
			SearchResultTable.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
			SearchResultScrollPane.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
		}

		/*标注添加标签与按钮*/
		btnClearDiscussionList.setBounds(0, DiscussionTable.getY() + DiscussionTable.getHeight(), 108, h0);
		btnAddAvailableLabelCategory.setBounds(0, btnClearDiscussionList.getY() + btnClearDiscussionList.getHeight(), GUIMain.icoAdd.getIconWidth(), GUIMain.icoAdd.getIconHeight());
		btnAddAvailableLabelCategory.setBorderPainted(false);
		btnSaveAvailableLabels.setBounds(0, btnAddAvailableLabelCategory.getY() + btnAddAvailableLabelCategory.getHeight(), 108, h0);
		btnSaveAvailableLabels.setBorderPainted(false);

		/*可选标注滚动面板*/
//		AllLabelsPanel.setBounds(AllAvailableLabelsLabel.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsLabel.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
		AllLabelsPanel.setLayout(null);
		AllLabelsScrollPane.setBounds(btnClearDiscussionList.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - btnClearDiscussionList.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
		AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		AllLabelsScrollPane.setPreferredSize(new Dimension(AllLabelsScrollPane.getWidth(), AllLabelsScrollPane.getHeight()));
//		for (int i = 0; i < DiscussionModel.getRowCount(); ++i)
//			for (int j = 0; j < DiscussionModel.getColumnCount(); ++j)
//				DiscussionModel.fireTableCellUpdated(i, j); // 通过引发单元格更新（tableChanged）事件，来激活设置行高的动作监听程序
		for (int i = 0; i < DiscussionModel.getRowCount(); ++i)
			DiscussionModel.fireTableChanged(new TableModelEvent(DiscussionModel, i));
//		DiscussionModel.fireTableDataChanged();
		if (SearchResultModel != null) { // 如果存在搜索结果
//			for (int i = 0; i < SearchResultModel.getRowCount(); ++i)
//				for (int j = 0; j < SearchResultModel.getColumnCount(); ++j)
//					SearchResultModel.fireTableCellUpdated(i, j); // 通过引发单元格更新（tableChanged）事件，来激活设置行高的动作监听程序
			for (int i = 0; i < SearchResultModel.getRowCount(); ++i)
				SearchResultModel.fireTableChanged(new TableModelEvent(SearchResultModel, i));
//			SearchResultModel.fireTableDataChanged();
		}

		{
			int XC = 0, YC = 0; // 当前摆放控件的位置
			final int XM = AllLabelsScrollPane.getWidth(), YM = AllLabelsScrollPane.getHeight();

			SelectedRows = DiscussionTable.getSelectedRows();
			int Ci = 0;
			final Component[] Components = AllLabelsPanel.getComponents();
			final Map<String, HashSet<String>> AllLabels = DataManipulator.GetAllLabels();
			for (Map.Entry<String, HashSet<String>> Cat : AllLabels.entrySet()) {
				int w, h;

				// 更改标签类名称控件的位置与大小
				final String CatName = Cat.getKey();
				final LabelCategoryComponent lbCatName = (LabelCategoryComponent) Components[Ci];
				w = w0 * (CatName.length() + LabelPadding);
				if (w > XM - XC) {
					XC = 0;
					YC += h0;
				} // 控件过长，放到下一行
				lbCatName.setBounds(XC, YC, w, h0);
				XC += lbCatName.getWidth();
				++Ci; // 取下一个部件

				// 该标签类下的全部标签及其使用数据对应的控件的大小
				for (String LabelName : Cat.getValue()) {
					// 标签控件
					final LabelButton btLabel = (LabelButton) Components[Ci];
					w = w0 * (LabelName.length() + ButtonPadding);
					if (w > XM - XC) {
						XC = 0;
						YC += h0;
					} // 控件过长，放到下一行
					btLabel.setBounds(XC, YC, w, h0);
					XC += btLabel.getWidth();
					++Ci; // 取下一个部件

					// 被选中次数标签控件（更新被选中次数）
					int TotalLabeledCount = 0;
					for (int i : SelectedRows) { // 对所有选中的行，查找每个标签被标注的次数
						final Map<String, LabelStatus> ContainedLabels = DataManipulator.GetDiscussionItem(i).GetLabels().get(CatName); // 对每一条选中的股评，都要查找是否包含此标签类
						if (ContainedLabels != null) { // 该股评条目的标注确包含该标签类
							final LabelStatus ThisLabelStatus = ContainedLabels.get(LabelName);
							if (ThisLabelStatus != null) TotalLabeledCount += ThisLabelStatus.LabeledCount;
						}
					}
					LabeledCountComponent lbCount = (LabeledCountComponent) Components[Ci];
					lbCount.setText(String.valueOf(TotalLabeledCount));
					w = w0 + (lbCount.getText().length() + LabelPadding);
					if (w > XM - XC) {
						XC = 0;
						YC += h0;
					} // 控件过长，放到下一行
					lbCount.setBounds(XC, YC, w, h0);
					XC += lbCount.getWidth();
					++Ci; // 取下一个部件
				}

				// 更改添加标签按钮的位置
				final JButton btnAddLabel = (JButton) Components[Ci];
				w = icoSmallAdd.getIconWidth() * 3 / 2;
				h = icoSmallAdd.getIconHeight() * 3 / 2;
				if (w > XM - XC) {
					XC = 0;
					YC += h0;
				} // 控件过长，放到下一行
				btnAddLabel.setBounds(XC, YC, w, h);
				XC += btnAddLabel.getWidth();
				++Ci; // 取下一个部件
			}

			AllLabelsPanel.setPreferredSize(new Dimension(AllLabelsScrollPane.getWidth(), YC + h0)); // 不正确地设置此处的参数会导致可选标注面板的滚动范围不正确
		}
	}

	// 初始化主界面
	public GUIMain() throws IOException, SAXException, XPathExpressionException {
		// 读取必要的数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(Screen.width / 2, Screen.height / 2);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null);
		super.getContentPane().setLayout(null);

		// 添加菜单
		MenuBar.add(FileMenu); MenuBar.add(TaskMenu); MenuBar.add(BackupRestoreMenu); MenuBar.add(StatisticsMenu);
		super.setJMenuBar(MenuBar);

		FileMenu.add(ImportMenuItem); FileMenu.add(ExportMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(JournalMenuItem); FileMenu.add(SettingsMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		TaskMenu.add(ViewTaskListMenuItem);

		StatisticsMenu.add(ViewStatisticsMenuItem);

		// 添加空白表格用于占位
		DiscussionModel = new DiscussionTableModel(false);
		DiscussionTable = new JTable(DiscussionModel);
		DiscussionScrollPane = new JScrollPane(DiscussionTable);

		// 添加动作监听程序
		super.addComponentListener(new MainFrameListener()); // 主界面
		super.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				Exit();
			}
		});

		btnSearch.addActionListener(new ActionListener() { // 搜索按钮
			@Override public void actionPerformed(ActionEvent e) {
				try { ShowSearchResult(); }
				catch (InterruptedException interruptedException) { interruptedException.printStackTrace(); }
			}
		});
		btnBack.addActionListener(new ActionListener() { // 从搜索结果返回按钮
			@Override public void actionPerformed(ActionEvent e) {
				ClearSearchResult();
			}
		});

		btnClearDiscussionList.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				DataManipulator.ClearDiscussionList();
				ThisForm.remove(DiscussionScrollPane);
				DiscussionModel = new DiscussionTableModel(false);
				DiscussionTable = new JTable(DiscussionModel);
				DiscussionScrollPane = new JScrollPane(DiscussionTable);
//				DiscussionModel.fireTableDataChanged();
//				Refresh();
				final Dimension CurrentSize = ThisForm.getSize();
				--CurrentSize.height;
				ThisForm.setSize(CurrentSize);
				++CurrentSize.height;
				ThisForm.setSize(CurrentSize);
			}
		});
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
				try { ImportForm = new GUIImportFiles(); }
				catch (XPathExpressionException xPathExpressionException) { xPathExpressionException.printStackTrace(); }
			}
		});
		ExportMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try { ExportForm = new GUIExportFiles(); }
				catch (XPathExpressionException xPathExpressionException) { xPathExpressionException.printStackTrace(); }
			}
		});
		JournalMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { LogForm = new GUIJournal(); }
		});
		SettingsMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try { SettingsForm = new GUISettings(); }
				catch (IOException ioException) { ioException.printStackTrace(); }
			}
		});
		ExitMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { Exit(); }
		});

		ViewTaskListMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { TaskListForm.setVisible(true); }
		});

		ViewStatisticsMenuItem.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { StatForm = new GUIStatistic(); }
		});

		AddComponentsForAllLabelsPanel();

		// 添加控件
		super.add(btnSearch); super.add(btnBack);
		super.add(cbLabeled); super.add(cbUnlabeled);
		super.add(tfSearchByText); super.add(tfSearchByLabel);
		super.add(btnClearDiscussionList); super.add(btnAddAvailableLabelCategory); super.add(btnSaveAvailableLabels);

		super.add(DiscussionScrollPane);
		super.add(AllLabelsScrollPane);

		// 显示
		super.setVisible(true);
	}

	public void AddComponentsForAllLabelsPanel(){ // 将全部可选标签布局在可选标签面板上，并显示当前选中的股评条目中，各标签被选中的总次数
		for (Map.Entry<String, HashSet<String>> Cat : DataManipulator.GetAllLabels().entrySet()) {
			AllLabelsPanel.add(new LabelCategoryComponent(Cat.getKey())); // 标签类名称控件
			for (String LabelName : Cat.getValue()) { // 该标签类下的全部标签及其使用数据
				final LabelButton btLabel = new LabelButton(LabelName, Cat.getKey());
				btLabel.addMouseListener(new MouseListener() {
					@Override public void mouseClicked(MouseEvent e) {
						LabelButton LabelClicked = (LabelButton) e.getSource();
						switch (e.getButton()) {
//							case MouseEvent.BUTTON1: case MouseEvent.BUTTON3: // 鼠标左键和鼠标右键
//								for (int i : SelectedRows) { // 对所有选中的股评，都要添加或删除此标签
//									final LabeledResponse Response = DataManipulator.LabeledAtThisTime(i, LabelClicked.Category, LabelClicked.getText());
//									if (Response.LabeledAtThisTime == false) { // 指定的行在本轮标注中未添加此标签
//										DataManipulator.AddLabelWhenNotLabeledAtThisTime(i, Response.TargetCat, LabelClicked.Category, LabelClicked.getText());
//									}
//									else {
//										DataManipulator.DeleteLabelWhenLabeledAtThisTime(i, Response.TargetCat, LabelClicked.Category, LabelClicked.getText());
//									}
//								}
//								break;
							case MouseEvent.BUTTON1:// 鼠标左键
								for (int i : SelectedRows) { // 对所有选中的股评，都要添加此标签
									final LabeledResponse Response = DataManipulator.LabeledAtThisTime(i, LabelClicked.Category, LabelClicked.getText());
									if (Response.LabeledAtThisTime == false) { // 指定的行在本轮标注中未添加此标签
										DataManipulator.AddLabelWhenNotLabeledAtThisTime(i, Response.TargetCat, LabelClicked.Category, LabelClicked.getText());
									}
								}
								break;
							case MouseEvent.BUTTON3: // 鼠标右键
								for (int i : SelectedRows) { // 对所有选中的股评，都要删除此标签
									final LabeledResponse HasLabeled = DataManipulator.LabeledAtThisTime(i, LabelClicked.Category, LabelClicked.getText());
									if (HasLabeled.LabeledAtThisTime == true) {// 指定的行在本轮标注中已添加此标签
										DataManipulator.DeleteLabelWhenLabeledAtThisTime(i, HasLabeled.TargetCat, LabelClicked.Category, LabelClicked.getText());
									}
								}
						}
						Refresh();
					}

					@Override public void mousePressed(MouseEvent e) {}

					@Override public void mouseReleased(MouseEvent e) {}

					@Override public void mouseEntered(MouseEvent e) {}

					@Override public void mouseExited(MouseEvent e) {}
				});
				AllLabelsPanel.add(btLabel);
				AllLabelsPanel.add(new LabeledCountComponent(0)); // 被选中次数标签控件
			}
			final JButton btnAddLabel = new JButton(icoSmallAdd); // 添加标签控件
			btnAddLabel.addActionListener(new ActionListener() {
				@Override public void actionPerformed(ActionEvent e) {
					new GUIAddLabel((GUIMain) SwingUtilities.getRoot(btnAddLabel), Cat.getKey());
				}
			});
			AllLabelsPanel.add(btnAddLabel);
		}
	}

	// 重绘主界面
	public void Refresh() {
		RearrangeComponents();
		for (int i = 0; i < DiscussionModel.getRowCount(); ++i) SetRowHeightAdaptively(DiscussionModel, DiscussionTable, i);
	}

	// 选中动作监听程序（内部类）
	class RowSelectionListener implements ListSelectionListener {
		@Override public void valueChanged(ListSelectionEvent e) { Refresh(); }
	}

	// 用于多行显示的单元格渲染器（内部类）
	static class LineWrapCellRenderer extends JTextArea implements TableCellRenderer {
		static final Color UnselectedBkgndColor = UIManager.getColor("Table.dropCellForeground");
		static final Color SelectedBkgndColor = UIManager.getColor("Table.dropCellBackground");

		public LineWrapCellRenderer() {
			this.setWrapStyleWord(true);
			this.setLineWrap(true);
		}

		// 不能在此方法内设定行高，因为行高的改变会令此方法被调用，导致无限循环
		@Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (value instanceof String) this.setText((String) value);
			else { // ConcurrentHashMap<String, HashMap<String, Integer>>
				this.setText("");
				Map<String, Map<String, Integer>> LabelCategories = (Map<String, Map<String, Integer>>) value;
				for (Map.Entry<String, Map<String, Integer>> CatItem : LabelCategories.entrySet()) {
					this.append(CatItem.getKey()); // 标签类名称
					for (String Label : CatItem.getValue().keySet()) this.append(" " + Label); // 标签名称
					this.append(Global.LineSeparator);
				}
			}
			final int FontHeight = this.getFontMetrics(this.getFont()).getHeight();
			final int TextPixelLength = this.getFontMetrics(this.getFont()).stringWidth(this.getText());
			final int CellWidth = table.getColumnModel().getColumn(column).getWidth();
			final int LineCount = Math.max((int) Math.ceil((double) TextPixelLength / CellWidth), 1);
			this.setSize(new Dimension(CellWidth, FontHeight * LineCount));
			if (Arrays.stream(table.getSelectedRows()).anyMatch(r -> r == row) == true) this.setBackground(SelectedBkgndColor);
			else this.setBackground(UnselectedBkgndColor);
			return this;
		}
	}

	private void SetRowHeightAdaptively(DiscussionTableModel Model, JTable Table, int Row) {
		int Height = 0;
		for (int Column = 0; Column < Model.getColumnCount(); ++Column) {
			Component comp = Table.prepareRenderer(Table.getCellRenderer(Row, Column), Row, Column);
			Height = Math.max(Height, comp.getHeight());
		}
		Table.setRowHeight(Row, Height);
	}

	// 显示股票讨论（内部用）
	private void ShowDiscussions(DiscussionTableModel Model, JTable Table, JScrollPane ScrollPane) {
		// 动作监听程序与单元格渲染程序
		Model.addTableModelListener(new TableModelListener() { // 表格内容改变时，行高自适应改变
			@Override public void tableChanged(TableModelEvent e) {
				final int Row = e.getFirstRow();
				SetRowHeightAdaptively(Model, Table, Row);
			}
		});
		Table.getSelectionModel().addListSelectionListener(new RowSelectionListener()); // 当选中股评时，可选标注面板显示各个标签被选中的数量
		Table.getColumnModel().getColumn(0).setCellRenderer(new LineWrapCellRenderer());
		Table.getColumnModel().getColumn(1).setCellRenderer(new LineWrapCellRenderer());

//		for (int i = 0; i < Model.getRowCount(); ++i)
//			for (int j = 0; j < Model.getColumnCount(); ++j)
//				Model.fireTableCellUpdated(i, j);
		for (int i = 0; i < DiscussionModel.getRowCount(); ++i)
			DiscussionModel.fireTableChanged(new TableModelEvent(DiscussionModel, i));
//		Model.fireTableDataChanged();

		this.add(ScrollPane);
		Refresh();
	}

	public void ShowAllDiscussions() {
		// 添加表格需要的组件
		this.remove(DiscussionScrollPane);
		DiscussionModel = new DiscussionTableModel(false);
		DiscussionTable = new JTable(DiscussionModel);
		DiscussionScrollPane = new JScrollPane(DiscussionTable);
		ShowDiscussions(DiscussionModel, DiscussionTable, DiscussionScrollPane);
	}

	public void ShowSearchResult() throws InterruptedException {
		int LabeledFlag = 0;
		if (cbLabeled.isSelected() == true) LabeledFlag |= DataManipulator.SEARCHPARAMLABELED;
		if (cbUnlabeled.isSelected() == true) LabeledFlag |= DataManipulator.SEARCHPARAMUNLABELED;
		DataManipulator.Search(LabeledFlag, tfSearchByText.getText().split("\\s+"), tfSearchByLabel.getText().split("\\s+"));
		SearchResultModel = new DiscussionTableModel(true);
		SearchResultTable = new JTable(SearchResultModel);
		SearchResultScrollPane = new JScrollPane(SearchResultTable);
		super.add(SearchResultScrollPane);
		ShowDiscussions(SearchResultModel, SearchResultTable, SearchResultScrollPane);
		DiscussionTable.setVisible(false);
		DiscussionScrollPane.setVisible(false);
		Refresh();
	}

	public void ClearSearchResult() {
		DataManipulator.ClearSearchResult();
		super.remove(SearchResultScrollPane);
		Refresh();
		DiscussionTable.setVisible(true);
		DiscussionScrollPane.setVisible(true);
	}

	public void Exit() {
//		if (ImportForm != null) ImportForm.dispatchEvent(new WindowEvent(ImportForm, WindowEvent.WINDOW_CLOSING));
//		if (ExportForm != null) ExportForm.dispatchEvent(new WindowEvent(ExportForm, WindowEvent.WINDOW_CLOSING));
//		if (SettingsForm != null) SettingsForm.dispatchEvent(new WindowEvent(SettingsForm, WindowEvent.WINDOW_CLOSING));
//		if (LogForm != null) LogForm.dispatchEvent(new WindowEvent(LogForm, WindowEvent.WINDOW_CLOSING));
//		TaskListForm.dispatchEvent(new WindowEvent(TaskListForm, WindowEvent.WINDOW_CLOSING));
//		if (StatForm != null) StatForm.dispatchEvent(new WindowEvent(StatForm, WindowEvent.WINDOW_CLOSING));
//		Global.MainForm.dispose();
		System.exit(0);
	}
}
