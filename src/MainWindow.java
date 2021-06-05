import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainWindow extends JFrame {
	// 主界面
	final JFrame MainFrame = new JFrame("雪球网股票评论");

	// 默认字体
	final Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);

	// 主界面图标
	final ImageIcon iconDownload = new ImageIcon(Global.IconPath + "\\download.png");
	final ImageIcon iconAdd = new ImageIcon(Global.IconPath + "\\addplus.png");
	final ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");

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
	JPanel AllLabelsPanel = new JPanel();
	JScrollPane AllLabelsScrollPane = new JScrollPane(AllLabelsPanel);
	JLabel AllAvailableLabelsTag = new JLabel("可选标注");

	// 表格
	public JTable DiscussionTable;
	public JScrollPane DiscussionScrollPane;
	public DefaultTableModel DiscussionTableModel;

	// 初始化主界面
	public MainWindow() {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		MainFrame.setSize((int) Screen.getWidth() / 2, (int) Screen.getHeight() / 2);
		MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainFrame.setLocationRelativeTo(null);

		MainFrame.setVisible(true);
	}
}
