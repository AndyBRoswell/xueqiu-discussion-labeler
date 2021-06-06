import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class GUIAddLabel extends JFrame {
	final JTextField tfLabel = new JTextField();
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");

	public GUIAddLabel(String Category) {
		// 标签添加窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setLocationRelativeTo(null); // 先设置大小，再改变相对位置原点，否则窗口的左上角将位于屏幕中央
		super.setResizable(false);
		super.setTitle("输入标签分类 " + Category + " 下的新标签");

		// 动作监听程序
		super.addComponentListener(new ComponentListener() {
			@Override public void componentResized(ComponentEvent e) { // 设置各控件的位置与大小
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) e.getComponent();
				final int X = GUIAddLabelForm.getContentPane().getWidth();
				final int Y = GUIAddLabelForm.getContentPane().getHeight();

				tfLabel.setBounds(0, 0, X, Global.FontSizeD * 2);
				btnOK.setBounds(X / 3, tfLabel.getHeight(), X / 6, Y - tfLabel.getHeight());
				btnCancel.setBounds(X / 2, tfLabel.getHeight(), X / 6, Y - tfLabel.getHeight());
			}

			@Override public void componentMoved(ComponentEvent e) {}

			@Override public void componentShown(ComponentEvent e) {}

			@Override public void componentHidden(ComponentEvent e) {}
		});
		btnOK.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
//				final JButton btnCancel = (JButton) e.getSource();
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnOK);
				if (GUIAddLabelForm.tfLabel.getText().isBlank() == false) {

				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
//				final JButton btnCancel = (JButton) e.getSource();
				final GUIAddLabel GUIAddLabelForm = (GUIAddLabel) SwingUtilities.getRoot(btnCancel);
				GUIAddLabelForm.dispose();
			}
		});

		// 添加控件
		super.add(tfLabel);
		super.add(btnOK);
		super.add(btnCancel);

		// 显示窗体
		super.setVisible(true);
	}
}
