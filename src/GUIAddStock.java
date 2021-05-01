import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIAddStock extends JFrame {
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    public GUIAddStock(){
        JFrame frame = new JFrame("添加");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("股票编号");
        JTextField textField = new JTextField(30);
        textField.setBounds(frame.getX() + 50, frame.getY() + 50, 200, 50);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel2.add(yes);
        panel2.add(no);
        panel.add(label);
        panel.add(textField);
        container.add(panel, BorderLayout.CENTER);
        container.add(panel2, BorderLayout.SOUTH);
        frame.pack();
    }
}
