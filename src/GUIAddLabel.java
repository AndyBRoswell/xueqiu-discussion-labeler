import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class GUIAddLabel extends JFrame {
	static final int h0 = Global.FontSizeD * 2;

	final ArrayList<JTextField> tfLabels = new ArrayList<>();
	final JButton btnOK = new JButton("确定");
	final JButton btnAdd = new JButton("添加");
	final JButton btnDelete = new JButton("删除");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabel(GUIMain MainFrame, String Category) {
		// 标签添加窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setResizable(false);
		super.setTitle("输入标签分类 " + Category + " 下的新标签");

		// 动作监听程序
		super.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) e.getComponent();
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();

				int y = 0;
				for (JTextField t : tfLabels) {
					t.setBounds(0, y, X, h0);
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

		btnOK.addActionListener(new ActionListener() { // 应用更改
			@Override public void actionPerformed(ActionEvent e) {
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnOK);
				for (JTextField t : tfLabels) {
					String Label = t.getText();
					if (Label.isBlank() == false) DataManipulator.AddLabelToCategory(Category, Label);
				}
				MainFrame.Refresh();
				GUIAddLabelForm.dispose();
			}
		});

		btnAdd.addActionListener(new ActionListener() { // 添加标签
			@Override public void actionPerformed(ActionEvent e) {
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnOK);
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();
				final JTextField tfLabel = new JTextField();
				tfLabels.add(tfLabel);
				final Dimension d = GUIAddLabelForm.getSize();
				d.height += h0;
				GUIAddLabelForm.setSize(d);
				GUIAddLabelForm.add(tfLabel);
			}
		});

		btnCancel.addActionListener(new ActionListener() { // 删除标签
			@Override public void actionPerformed(ActionEvent e) {
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnOK);
				if (tfLabels.isEmpty() == false) {
					GUIAddLabelForm.remove(tfLabels.get(tfLabels.size() - 1));
					tfLabels.remove(tfLabels.get(tfLabels.size() - 1));
					final Dimension d = GUIAddLabelForm.getSize();
					d.height -= h0;
					GUIAddLabelForm.setSize(d);
				}
			}
		});

		btnCancel.addActionListener(new ActionListener() { // 取消添加
			@Override public void actionPerformed(ActionEvent e) {
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnCancel);
				GUIAddLabelForm.dispose();
			}
		});

		// 添加控件
		tfLabels.add(new JTextField());
		for (JTextField t : tfLabels) { super.add(t); }
		super.add(btnOK); super.add(btnAdd); super.add(btnDelete); super.add(btnCancel);

		// 显示窗体
		super.setVisible(true);
	}
}
