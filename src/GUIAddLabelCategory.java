import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class GUIAddLabelCategory extends JFrame {
	static final int h0 = Global.FontSizeD * 2;

	final JLabel lbLabelCategory = new JLabel("标签类");
	final JTextField tfLabelCategory = new JTextField();
	final JLabel lbLabel = new JLabel("标签");
	final ArrayList<JTextField> tfLabel = new ArrayList<>();
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabelCategory() {
		// 标签类添加窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
		super.setTitle("添加新的一类标签");

		// 动作监听程序
		super.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) e.getComponent();
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();


			}

			@Override public void componentMoved(ComponentEvent e) {}

			@Override public void componentShown(ComponentEvent e) {}

			@Override public void componentHidden(ComponentEvent e) {}
		});

		// 添加控件

		// 显示
		super.setVisible(true);
	}
}
