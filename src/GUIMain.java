import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

public class GUIMain extends JFrame {
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
	final DefaultTableModel DiscussionTableModel = new DefaultTableModel(new Object[][]{}, new Object[]{ "股票讨论内容", "标注" });
	final JTable DiscussionTable = new JTable(DiscussionTableModel);
	final JScrollPane DiscussionScrollPane = new JScrollPane(DiscussionTable);

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
		MenuBar.add(FileMenu); MenuBar.add(TaskMenu); MenuBar.add(BackupRestoreMenu); MenuBar.add(StatisticMenu);
		super.setJMenuBar(MenuBar);

		FileMenu.add(ImportMenuItem); FileMenu.add(ExportMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(JournalMenuItem); FileMenu.add(SetMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		TaskMenu.add(AddMenuItem);

		// 添加控件
		super.add(btnTaskList); super.add(btnAddLabel);
		super.add(cbLabeled); super.add(cbUnlabeled);
		super.add(tfSearchByText); super.add(tfSearchByLabel);

		super.add(DiscussionScrollPane);

		// 动作监听程序（内部类）
		class MainFrameListener implements ComponentListener {
			@Override public void componentResized(ComponentEvent e) {
				final GUIMain MainFrame = (GUIMain) e.getComponent();
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
				cbUnlabeled.setBounds(X - wGUILabel - icoDownload.getIconWidth(), cbLabeled.getHeight(), wGUILabel, h0);
				tfSearchByText.setBounds(0, 0, cbLabeled.getX(), h0);
				tfSearchByLabel.setBounds(0, cbLabeled.getHeight(), cbLabeled.getX(), h0);

				/*表格*/
				DiscussionTable.setRowHeight(h0);
				DiscussionScrollPane.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);
				DiscussionTable.setBounds(0, tfSearchByLabel.getY() + tfSearchByLabel.getHeight(), X, Y * 7 / 10);

				/*标注添加标签与按钮*/
				AllAvailableLabelsLabel.setBounds(0, DiscussionTable.getY() + DiscussionTable.getHeight(), wGUILabel, h0);
				btnAddLabel.setBounds(0, AllAvailableLabelsLabel.getY() + AllAvailableLabelsLabel.getHeight(), GUIMain.icoAdd.getIconWidth(), GUIMain.icoAdd.getIconHeight());
				btnAddLabel.setBorderPainted(false);

				/*可选标注滚动面板*/
				AllLabelsScrollPane.setBounds(AllAvailableLabelsLabel.getWidth(), DiscussionTable.getY() + DiscussionTable.getHeight(), X - AllAvailableLabelsLabel.getWidth(), Y - (DiscussionTable.getY() + DiscussionTable.getHeight()));
				AllLabelsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				AllLabelsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			}

			@Override public void componentMoved(ComponentEvent e) {}

			@Override public void componentShown(ComponentEvent e) {}

			@Override public void componentHidden(ComponentEvent e) {}
		}

		// 添加动作监听程序
		super.addComponentListener(new MainFrameListener());

		// 读取数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		super.setVisible(true);
	}
}
