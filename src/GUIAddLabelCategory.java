import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	final JButton btnAdd = new JButton("添加");
	final JButton btnDelete = new JButton("删除");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabelCategory() {
		// 标签类添加窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
//		super.setLayout(null);
		super.setLayout(new GridBagLayout());
		super.setResizable(false); // not resizable by the user
		super.setTitle("添加新的一类标签");

		// 预设 4 个标签填写框
		for (int i = 0; i < 4; ++i) tfLabels.add(new JTextField());

		// 动作监听程序
		super.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
				final GUIAddLabelCategory GUIAddLabelForm = (GUIAddLabelCategory) e.getComponent();
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();

				lbLabelCategory.setBounds(0, 0, w0 * (lbLabelCategory.getText().length() + padding), h0);
				tfLabelCategory.setBounds(lbLabelCategory.getX() + lbLabelCategory.getWidth(), 0, X - (lbLabelCategory.getX() + lbLabelCategory.getWidth()), h0);
				lbLabel.setBounds(0, h0, w0 * (lbLabel.getText().length() + padding), h0);
				int x = tfLabelCategory.getX(), y = h0;
				for (JTextField t : tfLabels) {
					t.setBounds(x, y, X - x, h0);
					y += h0;
				}
				btnOK.setBounds(0, y, X / 4, h0);
				btnAdd.setBounds(X / 4, y, X / 4, h0);
				btnDelete.setBounds(X / 2, y, X / 4, h0);
				btnCancel.setBounds(X * 3 / 4, y, X / 4, h0);

				GUIAddLabelForm.setSize((int) Screen.getWidth() / 4, y + h0 + 40);
				GUIAddLabelForm.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
			}

			@Override public void componentMoved(ComponentEvent e) {}

			@Override public void componentShown(ComponentEvent e) {}

			@Override public void componentHidden(ComponentEvent e) {}
		});
		btnAdd.addActionListener(new ActionListener() { // 添加标签
			@Override public void actionPerformed(ActionEvent e) {
				final GUIAddLabelCategory GUIAddLabelForm = (GUIAddLabelCategory) SwingUtilities.getRoot(btnAdd);
				tfLabels.add(new JTextField());
				final Dimension d = GUIAddLabelForm.getSize();
				d.height += h0;
				GUIAddLabelForm.setSize(d);
				GUIAddLabelForm.repaint();
			}
		});

		// 添加控件
		super.add(lbLabelCategory); super.add(tfLabelCategory);
		super.add(lbLabel);
		for (JTextField t : tfLabels) {
			super.add(t);
		}
		super.add(btnOK); super.add(btnAdd); super.add(btnDelete); super.add(btnCancel);

		// 显示
		super.setVisible(true);
	}
}
