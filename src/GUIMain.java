import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

public class GUIMain extends JFrame {
	// 主界面
//	final JFrame MainFrame = new JFrame("雪球网股票评论");

	// 默认字体
	static final Font DefaultFont = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 主界面图标
	static final ImageIcon icoDownload = new ImageIcon(Global.IconPath + "\\download.png");
	static final ImageIcon icoAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	static final ImageIcon icoSmallAdd = new ImageIcon(Global.IconPath + "\\add.png");

	/*按钮*/
	final JButton btnTaskList = new JButton(icoDownload);
	final JButton btnAddLabel = new JButton(icoAdd);

	/*快捷筛选复选框*/
	final JCheckBox cbLabeled = new JCheckBox("已标注");
	final JCheckBox cbUnlabeled = new JCheckBox("未标注");

	/*搜索栏*/
	final JTextField tfSearchByText = new JTextField(6);
	final JTextField tfSearchByLabel = new JTextField(6);

	/*全部可选标签*/
	final JPanel AllLabelsPanel = new JPanel();
	final JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	final JLabel AllAvailableLabelsLabel = new JLabel("可选标注");

	// 表格
	DefaultTableModel DiscussionTableModel = new DefaultTableModel(new Object[][]{}, new Object[]{ "股票讨论内容", "标注" });
	JTable DiscussionTable = new JTable(DiscussionTableModel);
	JScrollPane DiscussionScrollPane = new JScrollPane(DiscussionTable);

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

	// 初始化主界面
	public GUIMain() throws IOException, SAXException, XPathExpressionException {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setLocationRelativeTo(null);
		super.getContentPane().setLayout(null);

		// 添加菜单
		MenuBar.add(FileMenu);
		MenuBar.add(TaskMenu);
		MenuBar.add(BackupRestoreMenu);
		MenuBar.add(StatisticMenu);
		super.setJMenuBar(MenuBar);

		FileMenu.add(ImportMenuItem);
		FileMenu.add(ExportMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(JournalMenuItem);
		FileMenu.add(SetMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		TaskMenu.add(AddMenuItem);

		// 添加控件
		super.add(btnTaskList); super.add(btnAddLabel);
		super.add(cbLabeled); super.add(cbUnlabeled);
		super.add(tfSearchByText); super.add(tfSearchByLabel);

		super.add(DiscussionScrollPane);

		// 添加动作监听程序
		super.addComponentListener(new MainFrameListener());

		// 读取数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		super.setVisible(true);
	}
}

class MainFrameListener implements ComponentListener {
	@Override public void componentResized(ComponentEvent e) {
		final GUIMain MainFrame = (GUIMain) e.getComponent();
		final int X = MainFrame.getContentPane().getWidth();
		final int Y = MainFrame.getContentPane().getHeight();
		final int w0 = Global.FontSizeD;
		final int h0 = 2 * Global.FontSizeD;
		final int wGUILabel = 6 * w0;

		/*下载按钮*/
		MainFrame.btnTaskList.setBounds(X - GUIMain.icoDownload.getIconWidth(), 12, GUIMain.icoDownload.getIconWidth(), GUIMain.icoDownload.getIconHeight());
		GUIMain.icoDownload.setImage(GUIMain.icoDownload.getImage().getScaledInstance(MainFrame.btnTaskList.getWidth(), MainFrame.btnTaskList.getHeight(), Image.SCALE_DEFAULT));
		MainFrame.btnTaskList.setIcon(GUIMain.icoDownload);

		/*搜索行*/
		MainFrame.cbLabeled.setBounds(X - wGUILabel - GUIMain.icoDownload.getIconWidth(), 0, wGUILabel, h0);
		MainFrame.cbUnlabeled.setBounds(X - wGUILabel - GUIMain.icoDownload.getIconWidth(), 0 + h0, wGUILabel, h0);
		MainFrame.tfSearchByText.setBounds(0, 0, MainFrame.cbLabeled.getX(), h0);
		MainFrame.tfSearchByLabel.setBounds(0, 0 + h0, MainFrame.cbLabeled.getX(), h0);

		/*表格*/
		MainFrame.DiscussionTable.setRowHeight(h0);
		MainFrame.DiscussionScrollPane.setBounds(0, MainFrame.tfSearchByLabel.getY() + MainFrame.tfSearchByLabel.getHeight(), X, Y * 7 / 10);
		MainFrame.DiscussionTable.setBounds(0, MainFrame.tfSearchByLabel.getY() + MainFrame.tfSearchByLabel.getHeight(), X, Y * 7 / 10);

		/*标注添加标签与按钮*/
		MainFrame.AllAvailableLabelsLabel.setBounds(0, MainFrame.DiscussionTable.getY() + MainFrame.DiscussionTable.getHeight(), X / 15, h0);
		MainFrame.btnAddLabel.setBounds(0, MainFrame.AllAvailableLabelsLabel.getY() + MainFrame.AllAvailableLabelsLabel.getHeight(), GUIMain.icoAdd.getIconWidth(), GUIMain.icoAdd.getIconHeight());
		MainFrame.btnAddLabel.setBorderPainted(false);

		/*可选标注滚动面板*/
		MainFrame.AllLabelsScrollPane.setBounds(MainFrame.AllAvailableLabelsLabel.getWidth(), MainFrame.DiscussionTable.getY() + MainFrame.DiscussionTable.getHeight(), X - MainFrame.AllAvailableLabelsLabel.getWidth(), Y - (MainFrame.DiscussionTable.getY() + MainFrame.DiscussionTable.getHeight()));
		MainFrame.AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		MainFrame.AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	}

	@Override public void componentMoved(ComponentEvent e) {}

	@Override public void componentShown(ComponentEvent e) {}

	@Override public void componentHidden(ComponentEvent e) {}
}