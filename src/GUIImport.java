import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIImport extends JFrame {
    private static Logger logger=Logger.getLogger(GUIImport.class);
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
                logger.info("关闭导入界面");
            }
        });
        panel.add(label);
        panel.add(textField);
        panel2.add(yes);
        panel2.add(no);
        container.add(panel);
        container.add(panel2);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                logger.info("关闭导入界面");
            }
        });
    }
}
