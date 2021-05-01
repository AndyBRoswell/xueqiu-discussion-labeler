import javax.swing.*;
import java.awt.*;

public class GUIJournal extends JFrame {
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    public GUIJournal(){
        JFrame frame = new JFrame("日志");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea textArea = new JTextArea(25, 70);
        panel.add(textArea);
        frame.add(panel);
        frame.setBounds(new Rectangle(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }
}
