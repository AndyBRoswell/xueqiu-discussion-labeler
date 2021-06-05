import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
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
	final JTable DiscussionTable;
	final JScrollPane DiscussionScrollPane;
	final DefaultTableModel DiscussionTableModel;

	// 初始化主界面
	public GUIMain() throws IOException, SAXException, XPathExpressionException {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		MainFrame.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);

		// 动作监听程序

		// 读取数据
		Config.LoadConfig(Global.DefaultConfig);
		StorageAccessor.LoadAllAvailableLabels();

		MainFrame.setVisible(true);
	}
}
