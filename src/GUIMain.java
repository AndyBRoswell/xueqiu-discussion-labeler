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
	final JFrame MainFrame = new JFrame("雪球网股票评论");

	// 默认字体
	static final Font DefaultFont = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 主界面图标
	static final ImageIcon icoDownload = new ImageIcon(Global.IconPath + "\\download.png");
	static final ImageIcon icoAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	static final ImageIcon icoSmallAdd = new ImageIcon(Global.IconPath + "\\add.png");

	/*按钮*/
	final JButton btnTaskList = new JButton(icoDownload);
	final JButton btnLabelButton = new JButton(icoAdd);

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
	JTable DiscussionTable;
	JScrollPane DiscussionScrollPane;
	DefaultTableModel DiscussionTableModel;

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
		MainFrame.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);
		MainFrame.getContentPane().setLayout(null);

		// 添加控件
		MainFrame.add(btnTaskList); MainFrame.add(btnLabelButton);
		MainFrame.add(cbLabeled); MainFrame.add(cbUnlabeled);
		MainFrame.add(tfSearchByText); MainFrame.add(tfSearchByLabel);

		// 添加菜单
		MenuBar.add(FileMenu);
		MenuBar.add(TaskMenu);
		MenuBar.add(BackupRestoreMenu);
		MenuBar.add(StatisticMenu);
		MainFrame.setJMenuBar(MenuBar);

		FileMenu.add(ImportMenuItem);
		FileMenu.add(ExportMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(JournalMenuItem);
		FileMenu.add(SetMenuItem);
		FileMenu.addSeparator();
		FileMenu.add(ExitMenuItem);

		TaskMenu.add(AddMenuItem);

		// 添加动作监听程序

		// 读取数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		MainFrame.setVisible(true);
	}
}

class MainFrameListener implements ComponentListener {
	@Override public void componentResized(ComponentEvent e) {

	}

	@Override public void componentMoved(ComponentEvent e) {}

	@Override public void componentShown(ComponentEvent e) {}

	@Override public void componentHidden(ComponentEvent e) {}
}