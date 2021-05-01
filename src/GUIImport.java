import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIImport extends JFrame {
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    public GUIImport(){
        JFrame frame = new JFrame("导入");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("导入路径");
        JTextField textField = new JTextField(30);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel.add(label);
        panel.add(textField);
        panel2.add(yes);
        panel2.add(no);
        container.add(panel);
        container.add(panel2);
        frame.pack();
    }
}
