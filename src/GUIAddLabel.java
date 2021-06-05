import javax.swing.*;
import java.awt.*;

public class GUIAddLabel extends JFrame {
	static final JTextField tfLabel = new JTextField();

	public GUIAddLabel(JFrame MainFrame, String Category) {
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setLocationRelativeTo(null);
		super.setLayout(null);
		super.setSize((int) Screen.getWidth() / 4, 100);
		super.setTitle("输入标签分类 " + Category + " 下的新标签");
		super.add(tfLabel);
		super.setVisible(true);
	}
}
