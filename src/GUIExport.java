import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIExport extends JFrame {
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    public GUIExport(){
        JFrame frame = new JFrame("导出");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JPanel panel3 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("导出路径");
        JLabel label2 = new JLabel("文件名");
        JTextField textField = new JTextField(30);
        JTextField textField2 = new JTextField(30);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(panel);
        panel4.add(panel2);
        panel2.add(textField);
        panel2.add(textField2);
        panel.add(label);
        panel.add(label2);
        panel3.add(yes);
        panel3.add(no);
        container.add(panel4);
        container.add(panel3);
        frame.pack();
    }
}
