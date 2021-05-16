import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GUI extends JFrame {
	ImageIcon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
	ImageIcon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");
	JFrame frame = new JFrame("雪球网股票评论");
	String[] title = { "评论", "已选标签" };
	Object[][] rowData = {
			{ 1, 2},
			{ 1, 2}
	};
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
	JLabel ChooseTag = new JLabel("可选标注");
	/*表格*/
	JTable table = new JTable(rowData, title);
	JTableHeader tableHeader = table.getTableHeader();
	JScrollPane scrollPane = new JScrollPane(table);
	public GUI() {
		frame.setLocation(500, 250);          //窗口显示位置
		frame.setSize(1000, 600);      //窗口大小
		init();
		frame.addComponentListener(new FrameListener());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	private void init() {
		/*按钮*/
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
		/*表格*/
		tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
		tableHeader.setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		/*标注*/
		AddTagButton.setIcon(iconAdd);
		AddTagButton.addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTagSort(); }});
		/*控件添加*/
		frame.getContentPane().setLayout(null);
		frame.add(SearchText);
		frame.add(SearchTag);
		frame.add(Marked);
		frame.add(UnMarked);
		frame.add(ButtonDownLoad);
		frame.add(scrollPane);
		frame.add(ChooseTag);
		frame.add(AddTagButton);
	}
	public class FrameListener implements ComponentListener {
		@Override
		public void componentResized(ComponentEvent e) {
			int x=frame.getWidth();
			int y=frame.getHeight();
			/*字体*/
			Font font = new Font("微软雅黑", 0, x/75);
			Marked.setFont(font);
			UnMarked.setFont(font);
			/*搜索行*/
			SearchText.setBounds(10,5,x*3/4,y/25);
			Marked.setBounds(10+x*9/12,5+y/45,x/13,y/25);
			UnMarked.setBounds(10+x*10/12,5+y/45,x/13,y/25);
			SearchTag.setBounds(10,5+y/25,x*3/4,y/25);
			/*下载按钮*/
			ButtonDownLoad.setBounds(x*19/20,5+y/45,y/25,y/25);
			iconDownload.setImage(iconDownload.getImage().getScaledInstance(ButtonDownLoad.getWidth(),ButtonDownLoad.getHeight(),Image.SCALE_DEFAULT));
			ButtonDownLoad.setIcon(iconDownload);
			/*表格*/
			table.setRowHeight(frame.getHeight()/20);
			scrollPane.setBounds(10,y*2/25+5,x-35,y*7/10);
			table.setBounds(10,y/25+5,x-35,y*6/8);
			/*标注*/
			ChooseTag.setBounds(10,y*8/10-5,x/13,30);
			ChooseTag.setFont(font);
			AddTagButton.setBounds(12,y*10/12,x/30,x/30);
			iconAdd.setImage(iconAdd.getImage().getScaledInstance(AddTagButton.getWidth(),AddTagButton.getHeight(),Image.SCALE_DEFAULT));
			AddTagButton.setIcon(iconAdd);
			AddTagButton.setBorderPainted(false);
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
			class AddButton extends JButton{
				public AddButton(String text){
					super(text);
					iconAddSmall.setImage(iconAddSmall.getImage().getScaledInstance(ButtonDownLoad.getWidth(),ButtonDownLoad.getHeight(),Image.SCALE_DEFAULT));
					setIcon(iconAddSmall);
					setBorderPainted(false);
					setFont(new Font("微软雅黑", 0, 0));
					addActionListener(new ActionListener() {@Override public void actionPerformed(ActionEvent e) { new GUIAddTag(text);}});
				}
			}
			File f=new File(Global.LabelFile);
			try {
				BufferedReader br=new BufferedReader(new FileReader(f));
				String str=null;
				String Data[][]=new String[10][4];
				int count=0;
				int i=0;
				while ((str=br.readLine())!=null)
				{
					String[] Data1=str.split(" ");
					for(i=0;i<Data1.length;i++) {
						Data[count][i] = Data1[i];
						frame.add(new SortLabelSet(Data[count][0])).setBounds(x*(3*count+1)/14,y*10/12,x/17,y/25);
						if(i>0) {
							frame.add(new LabelSet(Data[count][i], Color.LIGHT_GRAY)).setBounds(x*(3*count+1)/14+i*x/24+x/60,y*10/12,x/25,y/25);
							if(i==Data1.length-1)
							{
								String text=String.valueOf(count);
								frame.add(new AddButton(text)).setBounds(x*(3*count+1)/14+(i+1)*x/24+x/60,y*10/12,ButtonDownLoad.getWidth(),ButtonDownLoad.getHeight());
							}
						}
					}
					count++;
				}
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		@Override public void componentMoved(ComponentEvent e) { }
		@Override public void componentShown(ComponentEvent e) { }
		@Override public void componentHidden(ComponentEvent e) { }
	}
}