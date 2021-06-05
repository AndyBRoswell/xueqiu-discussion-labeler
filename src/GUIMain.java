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
	static final Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 主界面图标
	static final ImageIcon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
	static final ImageIcon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	static final ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");

	/*按钮*/
	final JButton TaskListButton = new JButton();
	final JButton AddTagButton = new JButton();

	/*快捷筛选复选框*/
	final JCheckBox Labeled = new JCheckBox("已标注");
	final JCheckBox Unlabeled = new JCheckBox("未标注");

	/*搜索栏*/
	final JTextField SearchText = new JTextField(6);
	final JTextField SearchTag = new JTextField(6);

	/*全部可选标签*/
	final JPanel AllLabelsPanel = new JPanel();
	final JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	final JLabel AllAvailableLabelsTag = new JLabel("可选标注");

	// 表格
	JTable DiscussionTable;
	JScrollPane DiscussionScrollPane;
	DefaultTableModel DiscussionTableModel;

	// 菜单
	final JMenuBar menuBar = new JMenuBar();

	final JMenu FileMenu = new JMenu("文件");
	final JMenuItem ImportMenuItem = new JMenuItem("导入股票讨论 CSV 文件");
	final JMenuItem ExportMenuItem = new JMenuItem("导出股票讨论 CSV 文件");
	final JMenuItem SetMenuItem = new JMenuItem("设置");
	final JMenuItem JournalMenuItem = new JMenuItem("日志");
	final JMenuItem ExitMenuItem = new JMenuItem("退出");

	final JMenu TaskMenu = new JMenu("任务");
	final JMenuItem AddMenuItem = new JMenuItem("添加爬取任务");

	final JMenu BackupAndRestoreMenu = new JMenu("备份/恢复");

	final JMenu StatisticMenu = new JMenu("统计");

	// 初始化主界面
	public GUIMain() throws IOException, SAXException, XPathExpressionException {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		MainFrame.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);

		// 添加控件

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