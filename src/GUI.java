import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
	JFrame frame = new JFrame("雪球网股票评论");
	Container contentPane = frame.getContentPane();
	public GUI() {
		contentPane.setLayout(new BorderLayout());
		frame.setLocation(500, 250);          //窗口显示位置
		frame.setSize(1000, 600);      //窗口大小
		init();
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void init() {
		/*图标*/
		Icon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
		Icon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
		/*按钮*/
		JButton ButtonDownLoad = new JButton();
		ButtonDownLoad.setPreferredSize(new Dimension(40, 40));
		ButtonDownLoad.setIcon(iconDownload);
		ButtonDownLoad.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIDownLoad(); }});
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
		JMenuItem JournalMenuItem = new JMenuItem("日志");
		JMenuItem ExitMenuItem = new JMenuItem("退出");
		FileMenu.add(ImportMenuItem);
		FileMenu.add(ExportMenuItem);
		FileMenu.add(JournalMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);
		ImportMenuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new GUIImport(); }});
		ExportMenuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new GUIExport(); }});
		JournalMenuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new GUIJournal(); }});
		ExitMenuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { System.exit(0); }});
		/*任务子菜单*/
		JMenuItem AddMenuItem = new JMenuItem("添加");
		TaskMenu.add(AddMenuItem);
		AddMenuItem.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) {new GUIAddStock(); }});
		/*备份恢复*/
		BackupAndRestoreMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) { new GUIBackupAndRestore(); }
			public void menuDeselected(MenuEvent e) { }
			public void menuCanceled(MenuEvent e) { }
		});
		StatisticMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) { new GUIStatistic(); }
			public void menuDeselected(MenuEvent e) { }
			public void menuCanceled(MenuEvent e) { }
		});
		/*panel1：搜索框、勾选框、任务列表按钮*/
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextField search = new JTextField(70);
		JCheckBox Marked = new JCheckBox("已标注");
		JCheckBox UnMarked = new JCheckBox("未标注");
		panel1.add(search);
		panel1.add(Marked);
		panel1.add(UnMarked);
		panel1.add(ButtonDownLoad);
		/*panel2：评论表格*/
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		String[] title = { "股票编号", "评论", "已选标签" };
		Object[][] rowData = {
				{ 1, 2, 3 },
				{ 1, 2, 3 }
		};
		JTable table = new JTable(rowData, title);
		JTableHeader tableHeader = table.getTableHeader();
		JScrollPane scrollPane = new JScrollPane(table);
		table.setPreferredScrollableViewportSize(new Dimension((frame.getWidth()) - 50, (frame.getHeight()) - 220));
		table.setRowHeight(30);
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
		tableHeader.setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		panel2.add(scrollPane);
		/*panel3:可选标注*/
		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel TagPanel = new JPanel();
		TagPanel.setLayout(new BorderLayout());
		JLabel ChooseTag = new JLabel("可选标注");
		TagPanel.add(ChooseTag, BorderLayout.NORTH);
		JButton AddTagButton = new JButton();
		AddTagButton.setPreferredSize(new Dimension(40, 40));
		AddTagButton.setIcon(iconAdd);
		TagPanel.add(AddTagButton, BorderLayout.SOUTH);
		AddTagButton.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTagSort(); }});
		panel3.add(TagPanel);
		/*panel4:标注栏*/
		JLabel Evaluation = new JLabel("总体评价");
		JLabel Advertisement = new JLabel("推广情况");
		JLabel Relevance = new JLabel("相关联性");
		JLabel Tendency = new JLabel("短期趋势");
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
		/*TPanel1：总体评价*/
		JPanel TPanel1 = new JPanel(new FlowLayout());
		JLabel good = new LabelSet("好评", Color.LIGHT_GRAY);
		JLabel middle = new LabelSet("中评", Color.LIGHT_GRAY);
		JLabel bad = new LabelSet("差评", Color.LIGHT_GRAY);
		JButton addTag1 = new AddButton();
		addTag1.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		TPanel1.add(Evaluation);
		TPanel1.add(good);
		TPanel1.add(middle);
		TPanel1.add(bad);
		TPanel1.add(addTag1);
		/*TPanel2：推广情况*/
		JPanel TPanel2 = new JPanel(new FlowLayout());
		JLabel have = new LabelSet("有", Color.LIGHT_GRAY);
		JLabel havenot = new LabelSet("无", Color.LIGHT_GRAY);
		JButton addTag2 = new AddButton();
		addTag2.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		TPanel2.add(Advertisement);
		TPanel2.add(have);
		TPanel2.add(havenot);
		TPanel2.add(addTag2);
		/*TPanel3：相关联性*/
		JPanel TPanel3 = new JPanel(new FlowLayout());
		JLabel haverelation = new LabelSet("有关", Color.LIGHT_GRAY);
		JLabel havenrelation = new LabelSet("无关", Color.LIGHT_GRAY);
		JButton addTag3 = new AddButton();
		addTag3.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		TPanel3.add(Relevance);
		TPanel3.add(haverelation);
		TPanel3.add(havenrelation);
		TPanel3.add(addTag3);
		/*TPanel4：短期趋势*/
		JPanel TPanel4 = new JPanel(new FlowLayout());
		JLabel up = new LabelSet("看涨", Color.LIGHT_GRAY);
		JLabel down = new LabelSet("看跌", Color.LIGHT_GRAY);
		JButton addTag4 = new AddButton();
		addTag4.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		TPanel4.add(Tendency);
		TPanel4.add(up);
		TPanel4.add(down);
		TPanel4.add(addTag4);
		/*添加Tpanel到标注栏Panel4*/
		panel4.add(TPanel1);
		panel4.add(TPanel2);
		panel4.add(TPanel3);
		panel4.add(TPanel4);
		
		panel3.add(panel4);

		contentPane.add(panel1, BorderLayout.PAGE_START);
		contentPane.add(panel2, BorderLayout.CENTER);
		contentPane.add(panel3, BorderLayout.SOUTH);
	}

	public static class LabelSet extends JLabel {
		public LabelSet(String text, Color bgColor) {
			super(text);
			setOpaque(true);
			setBackground(bgColor);
			setPreferredSize(new Dimension(40, 20));
			setHorizontalAlignment(SwingConstants.CENTER);
		}
	}

	public static class AddButton extends JButton {
		public AddButton() {
			super();
			Icon iconadd = new ImageIcon(Global.IconPath + "\\add.png");
			setPreferredSize(new Dimension(20, 20));
			setIcon(iconadd);

		}
	}
}
