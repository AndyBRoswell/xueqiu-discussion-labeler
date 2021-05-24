import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

	JFrame frame = new JFrame("雪球网股票评论");
	Font font = new Font("微软雅黑", 0, 12);

	ArrayList<ArrayList<String>> LabelData = new ArrayList<>();
	ArrayList<ArrayList<LabelSet>> Labels = new ArrayList<>();
	ArrayList<SortLabelSet> Sort = new ArrayList<>();
	ArrayList<AddButton> LabelButton = new ArrayList<>();

	/*按钮*/
	JButton ButtonDownLoad = new JButton();
	JButton AddTagButton = new JButton();

	/*搜索栏*/
	JTextField SearchText = new JTextField(6);
	JTextField SearchTag = new JTextField(6);

	/*勾选框*/
	JCheckBox Marked = new JCheckBox("已标注");
	JCheckBox UnMarked = new JCheckBox("未标注");

	/*标签*/
	JPanel panel = new JPanel();
	JScrollPane LabelscrollPane = new JScrollPane(panel);
	JLabel ChooseTag = new JLabel("可选标注");

	/*表格*/
	Vector<Object> title = new Vector<>();
	Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	public JTable table;
	public JScrollPane scrollPane;
	public DefaultTableModel model;

	public GUI() throws IOException, InterruptedException {
		frame.setLocation(500, 250);          //窗口显示位置
		frame.setSize(1000, 600);      //窗口大小
		init();
		TableInit();
		LabelInit();
		frame.addComponentListener(new FrameListener());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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
		ButtonDownLoad.addActionListener(new ActionListener() {
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
		frame.setJMenuBar(menuBar);

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
		ImportMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIImport();
			}
		});
		ExportMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIExport();
			}
		});
		SetMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIConfig(Global.DefaultConfig);
			}
		});
		JournalMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIJournal();
			}
		});
		ExitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		/*任务子菜单*/
		JMenuItem AddMenuItem = new JMenuItem("添加");
		TaskMenu.add(AddMenuItem);
		AddMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIAddStock();
			}
		});

		/*备份恢复*/
		BackupAndRestoreMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				new GUIBackupAndRestore();
			}

			public void menuDeselected(MenuEvent e) {}

			public void menuCanceled(MenuEvent e) {}
		});
		StatisticMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				new GUIStatistic();
			}

			public void menuDeselected(MenuEvent e) {}

			public void menuCanceled(MenuEvent e) {}
		});

		/*表格*/
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(frame.getWidth(), frame.getWidth() / 16));

		/*标注*/
		AddTagButton.setIcon(iconAdd);
		AddTagButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GUIAddTagSort();
			}
		});

		/*控件添加*/
		frame.getContentPane().setLayout(null);
		frame.add(SearchText);
		frame.add(SearchTag);
		frame.add(Marked);
		frame.add(UnMarked);
		frame.add(ButtonDownLoad);
		frame.add(LabelscrollPane);
		frame.add(ChooseTag);
		frame.add(AddTagButton);
	}

	public void LabelInit() {
		/*从labels文件中导出标签显示在主界面*/
		File f = new File(Global.LabelFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String str = null;
			int count = 0;
			while ((str = br.readLine()) != null) {
				LabelData.add(new ArrayList<String>());
				String[] Data = str.split(" ");
				for (int i = 0; i < Data.length; i++) {
					LabelData.get(count).add(Data[i]);
				}
				count++;
			}
			br.close();
			for (int i = 0; i < LabelData.size(); i++) {
				Sort.add(new SortLabelSet(LabelData.get(i).get(0)));
				panel.add(Sort.get(i));
				String text = String.valueOf(i);
				LabelButton.add(new AddButton(text));
				panel.add(LabelButton.get(i));
				ArrayList<LabelSet> Label = new ArrayList<>();
				for (int j = 1; j < LabelData.get(i).size(); j++) {
					Label.add(new LabelSet(LabelData.get(i).get(j), Color.GRAY));
				}
				Labels.add(Label);
			}
			for (int i = 0; i < Sort.size(); i++) {
				for (int j = 0; j < Labels.get(i).size(); j++) {
					panel.add(Labels.get(i).get(j));
				}
			}
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void TableInit() {
		title.add("评论");
		title.add("标注");
		StorageAccessor.LoadDiscussionFromCSV(Global.DefaultSavePath + "\\Book1.csv", "GB2312");
		for (DiscussionItem entry : DataManipulator.DiscussionList) {
			Vector<Object> vector = new Vector<Object>();
			vector.add(entry.GetText());
			vector.add(entry.GetLabels().toString());
			rowData.add(vector);
		}
		model = new DefaultTableModel(rowData, title);
		table = new JTable(model);
		JTableHeader tableHeader = table.getTableHeader();
		scrollPane = new JScrollPane(table);
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
		tableHeader.setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		frame.add(scrollPane);
	}

	public class FrameListener implements ComponentListener {
		@Override
		public void componentResized(ComponentEvent e) {
			int x = frame.getWidth();
			int y = frame.getHeight();
			/*字体*/
			Font font = new Font("微软雅黑", 0, x / 75);
			Marked.setFont(font);
			UnMarked.setFont(font);
			/*搜索行*/
			SearchText.setBounds(10, 5, x * 3 / 4, y / 25);
			Marked.setBounds(10 + x * 9 / 12, 5 + y / 45, x / 13, y / 25);
			UnMarked.setBounds(10 + x * 10 / 12, 5 + y / 45, x / 13, y / 25);
			SearchTag.setBounds(10, 5 + y / 25, x * 3 / 4, y / 25);
			SearchTag.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

				}
			});
			SearchText.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

				}
			});
			/*下载按钮*/
			ButtonDownLoad.setBounds(x * 19 / 20, 5 + y / 45, y / 25, y / 25);
			iconDownload.setImage(iconDownload.getImage().getScaledInstance(ButtonDownLoad.getWidth(), ButtonDownLoad.getHeight(), Image.SCALE_DEFAULT));
			ButtonDownLoad.setIcon(iconDownload);
			/*表格*/
			table.setRowHeight(frame.getHeight() / 20);
			scrollPane.setBounds(10, y * 2 / 25 + 5, x - 35, y * 7 / 10);
			table.setBounds(10, y / 25 + 5, x - 35, y * 6 / 8);
			/*标注*/
			ChooseTag.setBounds(10, y * 8 / 10 - 5, x / 13, 30);
			ChooseTag.setFont(font);
			AddTagButton.setBounds(12, y * 10 / 12, x / 30, x / 30);
			iconAdd.setImage(iconAdd.getImage().getScaledInstance(AddTagButton.getWidth(), AddTagButton.getHeight(), Image.SCALE_DEFAULT));
			AddTagButton.setIcon(iconAdd);
			AddTagButton.setBorderPainted(false);
			iconAddSmall.setImage(iconAddSmall.getImage().getScaledInstance(ButtonDownLoad.getWidth(), ButtonDownLoad.getHeight(), Image.SCALE_DEFAULT));
			/*可选标注滚动面板*/
			LabelscrollPane.setBounds(x / 15, y * 8 / 10 - 5, x * 20 / 22, x / 16);
			LabelscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			LabelscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
			int num = 0;
			for (int i = 0; i < Sort.size(); i++) {
				Sort.get(i).setFont(font);
				if (i == 0) {
					Sort.get(i).setBounds(0, x / 64, x / 17, y / 25);
				}
				else {
					num += LabelData.get(i - 1).size() - 1;//前面有的子标签数，此时前面有的标签类数为i
					Sort.get(i).setBounds(i * x / 17 + num * x / 23 + i * ButtonDownLoad.getWidth(), x / 64, x / 17, y / 25);
				}
				for (int j = 0; j < Labels.get(i).size(); j++) {//每个子标签坐标为i*x/17+num*x/23+x/17+(j-1)*x/23
					Labels.get(i).get(j).setFont(font);
					Labels.get(i).get(j).setBounds(i * x / 17 + num * x / 23 + x / 17 + j * x / 23 + i * ButtonDownLoad.getWidth(), x / 64, x / 25, y / 25);
					if (j == Labels.get(i).size() - 1) {
						LabelButton.get(i).setBounds(i * x / 17 + num * x / 23 + x / 17 + (j + 1) * x / 23 + i * ButtonDownLoad.getWidth(), x / 64, ButtonDownLoad.getWidth(), ButtonDownLoad.getHeight());
						LabelButton.get(i).setIcon(iconAddSmall);
						if (i == Sort.size() - 1) {
							panel.setPreferredSize(new Dimension(i * x / 17 + num * x / 23 + x / 17 + (j + 1) * x / 23 + (i + 1) * ButtonDownLoad.getWidth(), x / 16));
						}
					}
				}
			}
		}

		@Override
		public void componentMoved(ComponentEvent e) {}

		@Override
		public void componentShown(ComponentEvent e) {}

		@Override
		public void componentHidden(ComponentEvent e) {}
	}

	class LabelSet extends JLabel {
		public LabelSet(String text, Color bgColor) {
			super(text);
			setOpaque(true);
			setBackground(bgColor);
			setHorizontalAlignment(SwingConstants.CENTER);
			setFont(font);
		}
	}

	class SortLabelSet extends JLabel {
		public SortLabelSet(String text) {
			super(text);
			setFont(font);
		}
	}

	class AddButton extends JButton {
		public AddButton(String text) {
			super(text);
			setBorderPainted(false);
			setFont(new Font("微软雅黑", 0, 0));
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new GUIAddTag(text);
				}
			});
		}
	}
}