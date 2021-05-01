import javax.swing.*;
import java.awt.*;

public class GUIAddTag extends JFrame {
    JButton yes=new JButton("确定");
    public GUIAddTag(){
        JFrame frame = new JFrame("添加标注");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("标注名称");
        JTextField textField = new JTextField(10);
        panel.add(label);
        panel.add(textField);
        panel.add(yes);
        container.add(panel);
        frame.pack();
    }
}
