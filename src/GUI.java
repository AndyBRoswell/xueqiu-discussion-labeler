// THIS CLASS IS DEPRECATED.

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GUI extends JFrame {
	/*主界面图标*/
	final ImageIcon icoDownload = new ImageIcon(Global.IconPath + "\\download.png");
	final ImageIcon icoAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	final ImageIcon icoSmallAdd = new ImageIcon(Global.IconPath + "\\add.png");

	JFrame MainFrame = new JFrame("雪球网股票评论");
	final Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	ArrayList<ArrayList<String>> LabelsAndCategories = new ArrayList<>(); // 标签类及标签的名称
	ArrayList<ArrayList<ConcreteLabelControl>> LabelControls = new ArrayList<>();
	ArrayList<LabelCategoryControl> LabelCategoryControls = new ArrayList<>();
	ArrayList<AddButton> AddLabelButtons = new ArrayList<>();

	/*按钮*/
	final JButton btnTaskList = new JButton();
	final JButton btnAddLabel = new JButton();

	/*搜索栏*/
	final JTextField tfSearchByText = new JTextField(6);
	final JTextField tfSearchByLabel = new JTextField(6);

	/*快捷筛选复选框*/
	final JCheckBox cbLabeled = new JCheckBox("已标注");
	final JCheckBox cbUnlabeled = new JCheckBox("未标注");

	/*全部可选标签*/
	JPanel AllLabelsPanel = new JPanel();
	JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	JLabel AllAvailableLabelsLabel = new JLabel("可选标注");

	/*表格*/
	Vector<Object> DiscussionListTitle = new Vector<>();
	Vector<Vector<Object>> DiscussionRows = new Vector<Vector<Object>>();
	public JTable DiscussionTable;
	public JScrollPane DiscussionScrollPane;
	public DefaultTableModel DiscussionTableModel;

	public GUI() throws XPathExpressionException, IOException, SAXException {
		MainFrame.setLocation(500, 250);									//窗口显示位置
		MainFrame.setSize(1000, 600);								//窗口大小
		Config.LoadConfig(Global.DefaultConfig);								//读取默认配置文件
		init();																	//窗口部件初始化
		TableInit();
		LabelInit();
		MainFrame.addComponentListener(new FrameListener());
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);
		MainFrame.setVisible(true);
	}

	public void init() {
		/*任务列表按钮*/
		btnTaskList.addActionListener(new ActionListener() { // 打开任务列表界面
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIDownLoad();
			}
		});

		/*一级菜单创建*/
		final JMenuBar menuBar = new JMenuBar();
		final JMenu FileMenu = new JMenu("文件");
		final JMenu TaskMenu = new JMenu("任务");
		final JMenu BackupAndRestoreMenu = new JMenu("备份/恢复");
		final JMenu StatisticMenu = new JMenu("统计");
		menuBar.add(FileMenu);
		menuBar.add(TaskMenu);
		menuBar.add(BackupAndRestoreMenu);
		menuBar.add(StatisticMenu);
		MainFrame.setJMenuBar(menuBar);

		/*文件子菜单*/
		final JMenuItem ImportMenuItem = new JMenuItem("导入");
		final JMenuItem ExportMenuItem = new JMenuItem("导出");
		final JMenuItem SetMenuItem = new JMenuItem("设置");
		final JMenuItem JournalMenuItem = new JMenuItem("日志");
		final JMenuItem ExitMenuItem = new JMenuItem("退出");
		FileMenu.add(ImportMenuItem);
		FileMenu.add(ExportMenuItem);
		FileMenu.add(JournalMenuItem);
		FileMenu.add(SetMenuItem);
		FileMenu.add(SetMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		/*各菜单项的事件处理程序*/
		ImportMenuItem.addActionListener(new ActionListener() { // 弹出导入窗口
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIImport();
			}
		});
		ExportMenuItem.addActionListener(new ActionListener() { // 弹出导出窗口
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIExport();
			}
		});
		SetMenuItem.addActionListener(new ActionListener() { // 弹出设置窗口
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIConfig(Global.DefaultConfig);
			}
		});
		JournalMenuItem.addActionListener(new ActionListener() { // 弹出日志窗口
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIJournal();
			}
		});
		ExitMenuItem.addActionListener(new ActionListener() { // 退出本软件
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		/*任务子菜单*/
		final JMenuItem AddMenuItem = new JMenuItem("添加");
		TaskMenu.add(AddMenuItem);
		AddMenuItem.addActionListener(new ActionListener() { // 添加新的爬取任务
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIAddStock();
			}
		});

		/*备份恢复菜单项*/
		BackupAndRestoreMenu.addMenuListener(new MenuListener() { // 弹出备份恢复界面
			@Override
			public void menuSelected(MenuEvent e) {
				new GUIBackupAndRestore();
			}

			public void menuDeselected(MenuEvent e) {}

			public void menuCanceled(MenuEvent e) {}
		});

		/*统计图菜单项*/
		StatisticMenu.addMenuListener(new MenuListener() { // 弹出统计图界面
			@Override
			public void menuSelected(MenuEvent e) {
				new GUIStatistic();
			}

			public void menuDeselected(MenuEvent e) {}

			public void menuCanceled(MenuEvent e) {}
		});

		/*全部可选标注面板*/
		AllLabelsPanel.setLayout(null);

		/*添加一类新标注*/
		btnAddLabel.setIcon(icoAdd);
		btnAddLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIAddTagSort(MainFrame);
			}
		});

		/*部分动作监听程序*/
		tfSearchByLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});
		tfSearchByText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});

		/*控件添加*/
		MainFrame.getContentPane().setLayout(null);
		MainFrame.add(tfSearchByText);
		MainFrame.add(tfSearchByLabel);
		MainFrame.add(cbLabeled);
		MainFrame.add(cbUnlabeled);
		MainFrame.add(btnTaskList);
		MainFrame.add(AllLabelsScrollPane);
		MainFrame.add(AllAvailableLabelsLabel);
		MainFrame.add(btnAddLabel);
	}

	public void LabelInit() throws XPathExpressionException, IOException { //从labels.txt文件中导出标签显示在主界面
		File f = new File(Global.LabelFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String str;
			int RowCount = 0;
			while ((str = br.readLine()) != null) {
				LabelsAndCategories.add(new ArrayList<String>());
				String[] Data = str.split(" "); // 分离该行的标签类与标签，空格隔开
				for (int i = 0; i < Data.length; i++) {
					LabelsAndCategories.get(RowCount).add(Data[i]); // 将该标签类及其含有的标签内容放入数据结构
				}
				RowCount++;
			}
			br.close();
			for (int i = 0; i < LabelsAndCategories.size(); i++) {
				LabelCategoryControls.add(new LabelCategoryControl(LabelsAndCategories.get(i).get(0))); // 添加类名标签控件到数据结构
				AllLabelsPanel.add(LabelCategoryControls.get(i)); // 添加标签类到主界面
				String text = String.valueOf(i); // int to string
				AddLabelButtons.add(new AddButton(text)); // 产生该类标签的添加按钮并添加到数据结构
				AddLabelButtons.get(AddLabelButtons.size() - 1).setIcon(icoSmallAdd);
				AllLabelsPanel.add(AddLabelButtons.get(i)); // 将该类标签的添加按钮添加到主界面
				ArrayList<ConcreteLabelControl> Label = new ArrayList<>(); // 该类股评标签控件的集合
				for (int j = 1; j < LabelsAndCategories.get(i).size(); j++) {
					Label.add(new ConcreteLabelControl(LabelsAndCategories.get(i).get(j), Color.GRAY)); // 添加新的股评标签控件到数据结构
				}
				LabelControls.add(Label); // 添加该类股评标签的控件到数据结构
			}
			for (int i = 0; i < LabelCategoryControls.size(); i++) {
				for (int j = 0; j < LabelControls.get(i).size(); j++) {
					AllLabelsPanel.add(LabelControls.get(i).get(j)); // 将数据结构中的各类标签的控件添加到主界面
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

//		StorageAccessor.LoadAllAvailableLabels(); // 从 \cfg\labels.txt 读取全部可用标签类及标签

//		for (Map.Entry<String, HashSet<String>> SingleCat : DataManipulator.AllLabels.entrySet()) {
//			AllLabelsPanel.add(new LabelCategoryControl(SingleCat.getKey())); // 产生该标签类名称的标签控件并添加到主界面
//			for (String s : SingleCat.getValue()) {
//				AllLabelsPanel.add(new ConcreteLabelControl(s, Color.GRAY)); // 产生该标签类的标签控件并添加到主界面
//			}
//			AllLabelsPanel.add(new AddButton("+")); // 为该类标签产生添加按钮并添加到主界面
//		}

//		for (Map.Entry<String, HashSet<String>> SingleCat : DataManipulator.AllLabels.entrySet()) {
//			LabelCategoryControls.add(new LabelCategoryControl(SingleCat.getKey())); // 添加类名标签控件到数据结构
//			AllLabelsPanel.add(LabelCategoryControls.get(LabelCategoryControls.size() - 1)); // 添加标签类到主界面
//			LabelButton.add(new AddButton("+")); // 产生该类标签的添加按钮并添加到数据结构
//			AllLabelsPanel.add(LabelButton.get(LabelButton.size() - 1)); // 将该类标签的添加按钮添加到主界面
//			ArrayList<ConcreteLabelControl> Label = new ArrayList<>(); // 该类股评标签控件的集合
//			for (String s : SingleCat.getValue()) {
//				Label.add(new ConcreteLabelControl(s, Color.GRAY)); // 添加新的股评标签控件到数据结构
//			}
//			LabelControls.add(Label); // 添加该类股评标签的控件到数据结构
//			for (int i = 0; i < LabelCategoryControls.size(); i++) {
//				for (int j = 0; j < LabelControls.get(i).size(); j++) {
//					AllLabelsPanel.add(LabelControls.get(i).get(j)); // 将数据结构中的各类标签的控件添加到主界面
//				}
//			}
//		}

//		AllLabelsPanel.revalidate();
//		AllLabelsPanel.repaint();

	}

	public void TableInit() {
		/*获得股票讨论内容*/
		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv", "GB2312");
		for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
			Vector<Object> vector = new Vector<Object>();
			vector.add(entry.GetText());
			vector.add(entry.GetLabels().toString());
			DiscussionRows.add(vector);
		}

		/*添加表头*/
		DiscussionListTitle.add("评论");
		DiscussionListTitle.add("标注");

		/*根据表格数据和表头创建表格模型，并根据表格模型创建表格*/
		DiscussionTableModel = new DefaultTableModel(DiscussionRows, DiscussionListTitle);
		DiscussionTable = new JTable(DiscussionTableModel);

		/*设置表格中的表头控件*/
		JTableHeader tableHeader = DiscussionTable.getTableHeader();
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, Global.FontSizeL));
		tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
		tableHeader.setReorderingAllowed(false);

		/*设置并添加滚动面板到主界面*/
		DiscussionScrollPane = new JScrollPane(DiscussionTable);
		DiscussionScrollPane.setViewportView(DiscussionTable);
		MainFrame.add(DiscussionScrollPane);
	}

	public class FrameListener implements ComponentListener {
		@Override
		public void componentResized(ComponentEvent e) {
			final int X = MainFrame.getContentPane().getWidth();
			final int Y = MainFrame.getContentPane().getHeight();
			final int w0 = Global.FontSizeD;
			final int h0 = 2 * Global.FontSizeD;
			final int wGUILabel = 6 * w0;

			/*下载按钮*/
			btnTaskList.setBounds(X - icoDownload.getIconWidth(), 12, icoDownload.getIconWidth(), icoDownload.getIconHeight());
			icoDownload.setImage(icoDownload.getImage().getScaledInstance(btnTaskList.getWidth(), btnTaskList.getHeight(), Image.SCALE_DEFAULT));
			btnTaskList.setIcon(icoDownload);

			/*搜索行*/
			cbLabeled.setBounds(X - wGUILabel - icoDownload.getIconWidth(), 0, wGUILabel, h0);
			cbUnlabeled.setBounds(X - wGUILabel - icoDownload.getIconWidth(), 0 + h0, wGUILabel, h0);
			tfSearchByText.setBounds(0, 0, cbLabeled.getX(), h0);
			tfSearchByLabel.setBounds(0, 0 + h0, cbLabeled.getX(), h0);

			/*表格*/
			DiscussionTable.setRowHeight(h0);
			DiscussionScrollPane.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
			DiscussionTable.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);

			/*标注添加标签与按钮*/
			AllAvailableLabelsLabel.setBounds(0, DiscussionTable.getY() + DiscussionTable.getHeight(), X / 15, h0);
			btnAddLabel.setBounds(0, AllAvailableLabelsLabel.getY() + AllAvailableLabelsLabel.getHeight(), icoAdd.getIconWidth(), icoAdd.getIconHeight());
			btnAddLabel.setBorderPainted(false);

			/*可选标注滚动面板*/
			AllLabelsScrollPane.setBounds(AllAvailableLabelsLabel.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsLabel.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
			AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			int XAllLabelsPanel = 0;
			int YAllLabelsPanel = 0;
			int max = 0;
			AllLabelsPanel.removeAll();
			for (int i = 0; i < LabelCategoryControls.size(); ++i) {
				LabelCategoryControl cat = LabelCategoryControls.get(i);
				final int gap = Global.ComponentGapD;
				int l = LabelsAndCategories.get(i).get(0).length() + 2;
				if (XAllLabelsPanel + (LabelsAndCategories.get(i).size()) * l * w0 + icoSmallAdd.getIconWidth() * 3 / 2 > AllLabelsScrollPane.getWidth()) {
					YAllLabelsPanel += 1;
					XAllLabelsPanel = 0;
				}
				cat.setBounds(XAllLabelsPanel, YAllLabelsPanel * (h0 + gap), l * w0, h0);
				XAllLabelsPanel += l * w0 + gap;
				AllLabelsPanel.add(cat);

				for (int j = 1; j < LabelsAndCategories.get(i).size(); ++j) {
					ConcreteLabelControl label = LabelControls.get(i).get(j - 1);
					l = LabelsAndCategories.get(i).get(j).length() + 2;
					label.setBounds(XAllLabelsPanel, YAllLabelsPanel * (h0 + gap), l * w0, h0);
					XAllLabelsPanel += l * w0 + gap;
					AllLabelsPanel.add(label);
				}

				AddButton button = AddLabelButtons.get(i);
				button.setBounds(XAllLabelsPanel, YAllLabelsPanel * (h0 + gap), icoSmallAdd.getIconWidth() * 3 / 2, icoSmallAdd.getIconHeight() * 3 / 2);
				XAllLabelsPanel += 2 * icoSmallAdd.getIconWidth() + gap;
				if (XAllLabelsPanel > max) {
					max = XAllLabelsPanel;
				}
				AllLabelsPanel.add(button);

			}
			AllLabelsPanel.setPreferredSize(new Dimension(max, (YAllLabelsPanel + 1) * (h0 + 5)));
		}

		@Override
		public void componentMoved(ComponentEvent e) {}

		@Override
		public void componentShown(ComponentEvent e) {}

		@Override
		public void componentHidden(ComponentEvent e) {}
	}

	class ConcreteLabelControl extends JLabel {
		public ConcreteLabelControl(String text, Color bgColor) {
			super(text);
			setOpaque(true);
			setBackground(bgColor);
			setHorizontalAlignment(SwingConstants.CENTER);
			setFont(font);
		}
	}

	class LabelCategoryControl extends JLabel {
		public LabelCategoryControl(String text) {
			super(text);
			setFont(font);
		}
	}

	class AddButton extends JButton {
		public AddButton(String text) {
			super(text);
			setBorderPainted(false);
			setFont(new Font("微软雅黑", Font.PLAIN, 0));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new GUIAddTag(text, MainFrame);
				}
			});
		}
	}
}