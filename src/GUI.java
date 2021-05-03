import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class GUI extends JFrame {
	ImageIcon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
	ImageIcon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");
	JFrame frame = new JFrame("雪球网股票评论");
	String[] title = { "股票编号", "评论", "已选标签" };
	Object[][] rowData = {
			{ 1, 2, 3 },
			{ 1, 2, 3 }
	};
	/*按钮*/
	JButton ButtonDownLoad = new JButton();
	JButton AddTagButton = new JButton();
	JButton addTag1 = new JButton();
	JButton addTag2 = new JButton();
	JButton addTag3 = new JButton();
	JButton addTag4 = new JButton();
	/*搜索栏*/
	JTextField search = new JTextField(6);
	/*勾选框*/
	JCheckBox Marked = new JCheckBox("已标注");
	JCheckBox UnMarked = new JCheckBox("未标注");
	/*标签*/
	JLabel ChooseTag = new JLabel("可选标注");
	JLabel Tendency = new JLabel("短期趋势:");
	JLabel up = new LabelSet("看涨", Color.LIGHT_GRAY);
	JLabel down = new LabelSet("看跌", Color.LIGHT_GRAY);

	JLabel Relevance = new JLabel("相关联性:");
	JLabel Relative = new LabelSet("有关", Color.LIGHT_GRAY);
	JLabel UnRelative = new LabelSet("无关", Color.LIGHT_GRAY);

	JLabel Evaluation = new JLabel("总体评价:");
	JLabel good = new LabelSet("好评", Color.LIGHT_GRAY);
	JLabel middle = new LabelSet("中评", Color.LIGHT_GRAY);
	JLabel bad = new LabelSet("差评", Color.LIGHT_GRAY);

	JLabel Advertisement = new JLabel("推广情况:");
	JLabel have = new LabelSet("有", Color.LIGHT_GRAY);
	JLabel havenot = new LabelSet("无", Color.LIGHT_GRAY);
	/*表格*/
	JTable table = new JTable(rowData, title);
	JTableHeader tableHeader = table.getTableHeader();
	JScrollPane scrollPane = new JScrollPane(table);

	/*字体*/
	public GUI() {
		frame.setLocation(500, 250);          //窗口显示位置
		frame.setSize(1000, 600);      //窗口大小
		init();
		frame.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				int x=frame.getWidth();
				int y=frame.getHeight();
				Font font = new Font("微软雅黑", 0, x/75);
				search.setBounds(10,5,x*3/4,y/25);
				Marked.setBounds(10+x*9/12,5,x/13,y/25);
				Marked.setFont(font);
				UnMarked.setFont(font);
				UnMarked.setBounds(10+x*10/12,5,x/13,y/25);
				table.setRowHeight(frame.getHeight()/20);
				ButtonDownLoad.setBounds(x*19/20,5,y/30,y/30);
				iconDownload.setImage(iconDownload.getImage().getScaledInstance(ButtonDownLoad.getWidth(),ButtonDownLoad.getHeight(),Image.SCALE_DEFAULT));
				ButtonDownLoad.setIcon(iconDownload);
				scrollPane.setBounds(10,y/25+5,x-35,y*6/8);
				table.setBounds(10,y/25+5,x-35,y*6/8);
				ChooseTag.setBounds(10,y*8/10-5,x/13,30);
				ChooseTag.setFont(font);
				AddTagButton.setBounds(12,y*10/12,x/30,x/30);
				iconAdd.setImage(iconAdd.getImage().getScaledInstance(AddTagButton.getWidth(),AddTagButton.getHeight(),Image.SCALE_DEFAULT));
				AddTagButton.setIcon(iconAdd);
				AddTagButton.setBorderPainted(false);

				Tendency.setFont(font);
				up.setFont(font);
				down.setFont(font);
				Relative.setFont(font);
				Relevance.setFont(font);
				UnRelative.setFont(font);
				Evaluation.setFont(font);
				good.setFont(font);
				middle.setFont(font);
				bad.setFont(font);
				Advertisement.setFont(font);
				have.setFont(font);
				havenot.setFont(font);

				Tendency.setBounds(AddTagButton.getX()+AddTagButton.getWidth()+x/60,y*10/12,x/17,y/25);
				up.setBounds(Tendency.getX()+Tendency.getWidth(),y*10/12,x/25,y/25);
				down.setBounds(up.getX()+up.getWidth()+x/200,y*10/12,x/25,y/25);
				addTag1.setBounds(down.getX()+down.getWidth()+x/200,y*10/12,y/25,y/25);
				iconAddSmall.setImage(iconAddSmall.getImage().getScaledInstance(addTag1.getWidth(),addTag1.getHeight(),Image.SCALE_DEFAULT));
				addTag1.setIcon(iconAddSmall);
				addTag1.setBorderPainted(false);

				Relevance.setBounds(addTag1.getX()+addTag1.getWidth()+x/60,y*10/12,x/17,y/25);
				Relative.setBounds(Relevance.getX()+Relevance.getWidth()+x/200,y*10/12,x/25,y/25);
				UnRelative.setBounds(Relative.getX()+Relative.getWidth()+x/200,y*10/12,x/25,y/25);
				addTag2.setBounds(UnRelative.getX()+UnRelative.getWidth()+x/200,y*10/12,y/25,y/25);
				addTag2.setIcon(iconAddSmall);
				addTag2.setBorderPainted(false);

				Evaluation.setBounds(addTag2.getX()+addTag2.getWidth()+x/60,y*10/12,x/17,y/25);
				good.setBounds(Evaluation.getX()+Evaluation.getWidth()+x/200,y*10/12,x/25,y/25);
				middle.setBounds(good.getX()+good.getWidth()+x/200,y*10/12,x/25,y/25);
				bad.setBounds(middle.getX()+middle.getWidth()+x/200,y*10/12,x/25,y/25);
				addTag3.setBounds(bad.getX()+bad.getWidth()+x/200,y*10/12,y/25,y/25);
				addTag3.setIcon(iconAddSmall);
				addTag3.setBorderPainted(false);

				Advertisement.setBounds(addTag3.getX()+addTag3.getWidth()+x/60,y*10/12,x/17,y/25);
				have.setBounds(Advertisement.getX()+Advertisement.getWidth()+x/200,y*10/12,x/25,y/25);
				havenot.setBounds(have.getX()+have.getWidth()+x/200,y*10/12,x/25,y/25);
				addTag4.setBounds(havenot.getX()+havenot.getWidth()+x/200,y*10/12,y/25,y/25);
				addTag4.setIcon(iconAddSmall);
				addTag4.setBorderPainted(false);
			}
			@Override public void componentMoved(ComponentEvent e) { }
			@Override public void componentShown(ComponentEvent e) { }
			@Override public void componentHidden(ComponentEvent e) { }
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private void init() {
		/*按钮*/
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
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
		tableHeader.setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		AddTagButton.setIcon(iconAdd);
		AddTagButton.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTagSort(); }});
		addTag1.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		addTag2.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		addTag3.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});
		addTag4.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(); }});

		frame.getContentPane().setLayout(null);
		frame.add(search);
		frame.add(Marked);
		frame.add(UnMarked);
		frame.add(ButtonDownLoad);
		frame.add(scrollPane);
		frame.add(ChooseTag);
		frame.add(AddTagButton);
		frame.add(Tendency);
		frame.add(up);
		frame.add(down);
		frame.add(Relative);
		frame.add(Relevance);
		frame.add(UnRelative);
		frame.add(Evaluation);
		frame.add(good);
		frame.add(middle);
		frame.add(bad);
		frame.add(Advertisement);
		frame.add(have);
		frame.add(havenot);
		frame.add(addTag1);
		frame.add(addTag2);
		frame.add(addTag3);
		frame.add(addTag4);
	}

	public static class LabelSet extends JLabel {
		public LabelSet(String text, Color bgColor) {
			super(text);
			setOpaque(true);
			setBackground(bgColor);
			setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
}
