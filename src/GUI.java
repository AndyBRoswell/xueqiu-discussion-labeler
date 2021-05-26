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
	ImageIcon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
	ImageIcon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");

	JFrame MainFrame = new JFrame("雪球网股票评论");
	Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	ArrayList<ArrayList<String>> LabelsAndCategories = new ArrayList<>(); // 标签类及标签的名称
	ArrayList<ArrayList<ConcreteLabelControl>> LabelControls = new ArrayList<>();
	ArrayList<LabelCategoryControl> LabelCategoryControls = new ArrayList<>();
	ArrayList<AddButton> AddLabelButtons = new ArrayList<>();

	/*按钮*/
	JButton TaskListButton = new JButton();
	JButton AddTagButton = new JButton();

	/*搜索栏*/
	JTextField SearchText = new JTextField(6);
	JTextField SearchTag = new JTextField(6);

	/*快捷筛选复选框*/
	JCheckBox Labeled = new JCheckBox("已标注");
	JCheckBox Unlabeled = new JCheckBox("未标注");

	/*全部可选标签*/
	JPanel AllLabelsPanel = new JPanel();
	JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	JLabel AllAvailableLabelsTag = new JLabel("可选标注");

	/*表格*/
	Vector<Object> DiscussionListTitle = new Vector<>();
	Vector<Vector<Object>> DiscussionRows = new Vector<Vector<Object>>();
	public JTable DiscussionTable;
	public JScrollPane DiscussionScrollPane;
	public DefaultTableModel model;

	public GUI() throws XPathExpressionException, IOException, SAXException {
		MainFrame.setLocation(500, 250);								//窗口显示位置
		MainFrame.setSize(1000, 600);							//窗口大小
		Config.LoadConfig(Global.DefaultConfig);							//读取默认配置文件
		init();																//窗口部件初始化
		TableInit();
		LabelInit();
		MainFrame.addComponentListener(new FrameListener());
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);
		MainFrame.setVisible(true);
	}

	/*public void Directory() throws IOException{
		WatchService watchService = FileSystems.getDefault().newWatchService();
		Path p = Paths.get(Global.ConfigPath);
		p.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
		Thread thread = new Thread(() -> {
			try {
				while(true){
					WatchKey watchKey = watchService.take();
					List watchEvents = watchKey.pollEvents();
					if(watchEvents!=null)
					{
						frame.setVisible(false);
						try {
							new GUI();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					watchKey.reset();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		thread.setDaemon(false);
		thread.start();
		// 增加jvm关闭的钩子来关闭监听
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				watchService.close();
			} catch (Exception e) {
			}
		}));
	}*/

	private void init() {
		/*按钮*/
		TaskListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) { new GUIDownLoad(); }
		});

		/*一级菜单创建*/
		JMenuBar menuBar = new JMenuBar();
		JMenu FileMenu = new JMenu("文件");
		JMenu TaskMenu = new JMenu("任务");
		JMenu BackupAndRestoreMenu = new JMenu("备份/恢复");
		JMenu StatisticMenu = new JMenu("统计");
		menuBar.add(FileMenu);
		menuBar.add(TaskMenu);
		menuBar.add(BackupAndRestoreMenu);
		menuBar.add(StatisticMenu);
		MainFrame.setJMenuBar(menuBar);

		/*文件子菜单*/
		JMenuItem ImportMenuItem = new JMenuItem("导入");
		JMenuItem ExportMenuItem = new JMenuItem("导出");
		JMenuItem SetMenuItem = new JMenuItem("设置");
		JMenuItem JournalMenuItem = new JMenuItem("日志");
		JMenuItem ExitMenuItem = new JMenuItem("退出");
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
		JMenuItem AddMenuItem = new JMenuItem("添加");
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
		AddTagButton.setIcon(iconAdd);
		AddTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIAddTagSort();
			}
		});

		/*部分动作监听程序*/
		SearchTag.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});
		SearchText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {}
		});

		/*控件添加*/
		MainFrame.getContentPane().setLayout(null);
		MainFrame.add(SearchText);
		MainFrame.add(SearchTag);
		MainFrame.add(Labeled);
		MainFrame.add(Unlabeled);
		MainFrame.add(TaskListButton);
		MainFrame.add(AllLabelsScrollPane);
		MainFrame.add(AllAvailableLabelsTag);
		MainFrame.add(AddTagButton);
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
				AddLabelButtons.get(AddLabelButtons.size() - 1).setIcon(iconAddSmall);
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
		}
		catch (Exception e1) {
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
		for (DiscussionItem entry : DataManipulator.DiscussionList) {
			Vector<Object> vector = new Vector<Object>();
			vector.add(entry.GetText());
			vector.add(entry.GetLabels().toString());
			DiscussionRows.add(vector);
		}

		/*添加表头*/
		DiscussionListTitle.add("评论");
		DiscussionListTitle.add("标注");

		/*根据表格数据和表头创建表格模型，并根据表格模型创建表格*/
		model = new DefaultTableModel(DiscussionRows, DiscussionListTitle);
		DiscussionTable = new JTable(model);

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

			/*下载按钮*/
			TaskListButton.setBounds(X - iconDownload.getIconWidth(), 12, iconDownload.getIconWidth(), iconDownload.getIconHeight());
			iconDownload.setImage(iconDownload.getImage().getScaledInstance(TaskListButton.getWidth(), TaskListButton.getHeight(), Image.SCALE_DEFAULT));
			TaskListButton.setIcon(iconDownload);

			/*搜索行*/
			Labeled.setBounds(X - 6 * w0 - iconDownload.getIconWidth(), 0, 6 * w0, h0);
			Unlabeled.setBounds(X - 6 * w0 - iconDownload.getIconWidth(), 0 + 2 * w0, 6 * w0, h0);
			SearchText.setBounds(0, 0, Labeled.getX(), h0);
			SearchTag.setBounds(0, 0 + h0, Labeled.getX(), h0);

			/*表格*/
			DiscussionTable.setRowHeight(h0);
			DiscussionScrollPane.setBounds(0, SearchTag.getY() + SearchTag.getHeight(), X, Y * 7 / 10);
			DiscussionTable.setBounds(0, SearchTag.getY() + SearchTag.getHeight(), X, Y * 7 / 10);

			/*标注添加标签与按钮*/
			AllAvailableLabelsTag.setBounds(0, DiscussionTable.getY() + DiscussionTable.getHeight(), X / 15, h0);
			AddTagButton.setBounds(0, AllAvailableLabelsTag.getY() + AllAvailableLabelsTag.getHeight(), iconAdd.getIconWidth(), iconAdd.getIconHeight());
			AddTagButton.setBorderPainted(false);

			/*可选标注滚动面板*/
			AllLabelsScrollPane.setBounds(AllAvailableLabelsTag.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsTag.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
			AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//			AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//			AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			/*全部可选标签类及其标签*/
//			int LabelControlsAdded = 0;
//			for (int i = 0; i < LabelCategoryControls.size(); i++) {
//				if (i == 0) {
//					LabelCategoryControls.get(i).setBounds(0, X / 64, X / 17, Y / 25); // 第一个标签类控件
//				}
//				else { // 第二个及以后的标签类控件
//					LabelControlsAdded += LabelsAndCategories.get(i - 1).size() - 1; // 在添加该标签类控件时，累计已经添加的子标签控件数量（减去标签类一个）
//					LabelCategoryControls.get(i).setBounds(i * X / 17 + LabelControlsAdded * X / 23 + i * TaskListButton.getWidth(), X / 64, X / 17, Y / 25);
//				}
//				for (int j = 0; j < LabelControls.get(i).size(); j++) { //每个子标签控件的坐标为：i*x/17+num*x/23+x/17+(j-1)*x/23
//					LabelControls.get(i).get(j).setBounds(i * X / 17 + LabelControlsAdded * X / 23 + X / 17 + j * X / 23 + i * TaskListButton.getWidth(), X / 64, X / 25, Y / 25);
//					if (j == LabelControls.get(i).size() - 1) {
//						AddLabelButtons.get(i).setBounds(i * X / 17 + LabelControlsAdded * X / 23 + X / 17 + (j + 1) * X / 23 + i * TaskListButton.getWidth(), X / 64, TaskListButton.getWidth(), TaskListButton.getHeight());
//						if (i == LabelCategoryControls.size() - 1) {
//							AllLabelsPanel.setPreferredSize(new Dimension(i * X / 17 + LabelControlsAdded * X / 23 + X / 17 + (j + 1) * X / 23 + (i + 1) * TaskListButton.getWidth(), X / 16));
//						}
//					}
//				}
//			}
			int XAllLabelsPanel = 0;
			int YAllLabelsPanel = 0;
			for (int i = 0; i < LabelCategoryControls.size(); ++i) {
				LabelCategoryControl cat = LabelCategoryControls.get(i);
				final int gap = Global.ComponentGapD;
				int l = LabelsAndCategories.get(i).get(0).length() + 2;
				cat.setBounds(XAllLabelsPanel, 0, l * w0, h0);
				XAllLabelsPanel += l * w0 + gap;
				AllLabelsPanel.add(cat);

				for (int j = 1; j < LabelsAndCategories.get(i).size(); ++j) {
					ConcreteLabelControl label = LabelControls.get(i).get(j - 1);
					l = LabelsAndCategories.get(i).get(j).length() + 2;
					label.setBounds(XAllLabelsPanel, 0, l * w0, h0);
					XAllLabelsPanel += l * w0 + gap;
					AllLabelsPanel.add(label);
				}

				AddButton button = AddLabelButtons.get(i);
				button.setBounds(XAllLabelsPanel, 0, iconAddSmall.getIconWidth() * 3 / 2, iconAddSmall.getIconHeight() * 3 / 2);
				XAllLabelsPanel += 2 * iconAddSmall.getIconWidth() + gap;
				AllLabelsPanel.add(button);
			}
			System.out.println(XAllLabelsPanel);
			System.out.println(X - AllAvailableLabelsTag.getWidth());
			AllLabelsPanel.setPreferredSize(new Dimension(XAllLabelsPanel, Y - (DiscussionTable.getY() + DiscussionTable.getHeight())));
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
					new GUIAddTag(text);
				}
			});
		}
	}
}