import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIAddLabelCategory extends JFrame {
	static final int h0 = Global.FontSizeD * 2;

	final JTextField tfLabelCategory = new JTextField();
	final ArrayList<JTextField> tfLabel = new ArrayList<>();
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabelCategory() {
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
		super.setTitle("添加新的一类标签");
	}
}
