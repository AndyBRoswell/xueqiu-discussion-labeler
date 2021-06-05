import javax.swing.*;
import java.awt.*;

public class GUIAddLabel extends JFrame {
	final JTextField tfLabel = new JTextField();
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabel(String Category) {
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
		super.setTitle("输入标签分类 " + Category + " 下的新标签");
		super.add(tfLabel);
		super.setVisible(true);
	}
}
