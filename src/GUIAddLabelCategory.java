import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class GUIAddLabelCategory extends JFrame {
	static final int w0 = Global.FontSizeD;
	static final int h0 = Global.FontSizeD * 2;
	static final int padding = Global.StringPaddingInChrD;

	final JLabel lbLabelCategory = new JLabel("标签类");
	final JTextField tfLabelCategory = new JTextField();
	final JLabel lbLabel = new JLabel("标签");
	final ArrayList<JTextField> tfLabels = new ArrayList<>();
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabelCategory() {
		// 标签类添加窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
//		super.setLayout(null);
		super.setLayout(new GridBagLayout());
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
		super.setResizable(false); // not resizable by the user
		super.setTitle("添加新的一类标签");

		// 动作监听程序
		super.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) e.getComponent();
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();
				GridBagConstraints c = null;

				lbLabelCategory.setBounds(0, 0, w0 * (lbLabelCategory.getText().length() + padding), h0);
				c = new GridBagConstraints();
				c.gridx = 0; c.gridy = 0;
				tfLabelCategory.setBounds(lbLabelCategory.getX() + lbLabelCategory.getWidth(), 0, X - (lbLabelCategory.getX() + lbLabelCategory.getWidth()), h0);
				c = new GridBagConstraints();
				c.gridx = 0; c.gridy = 1;
				lbLabel.setBounds(0, h0, w0 * (lbLabel.getText().length() + padding), h0);
				for (JTextField t : tfLabels) {
//					t.setPreferredSize();
				}


			}

			@Override public void componentMoved(ComponentEvent e) {}

			@Override public void componentShown(ComponentEvent e) {}

			@Override public void componentHidden(ComponentEvent e) {}
		});

		// 添加控件
		super.add(lbLabelCategory); super.add(tfLabelCategory);
		super.add(lbLabel);
		for (JTextField t : tfLabels) {
			super.add(t);
		}
		super.add(btnOK); super.add(btnCancel);

		// 显示
		super.setVisible(true);
	}
}
