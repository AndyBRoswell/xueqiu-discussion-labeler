import javax.swing.*;
import java.awt.*;

public class GUIAddTagSort extends JFrame {
    public GUIAddTagSort(){
        JFrame frame = new JFrame("添加标注类");
        frame.setBounds(new Rectangle(400, 400));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new FlowLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("分类名称:");
        label.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 10));
        JLabel label2 = new JLabel("具体分类:");
        label2.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 10));
        JButton button = new JButton();
        panel.add(label);
        panel.add(label2);
        container.add(panel);
        JTextField textField = new JTextField(6);
        JTextField textField2 = new JTextField(10);
        JTextField textField3 = new JTextField(10);
        panel2.add(textField);
        panel2.add(textField2);
        panel2.add(textField3);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        container.add(panel2);

        JButton button1 = new JButton("确定");
        panel3.add(button1);
        container.add(panel3);
        frame.pack();
    }
}
