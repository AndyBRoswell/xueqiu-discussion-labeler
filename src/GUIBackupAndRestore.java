import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIBackupAndRestore extends JFrame {
    JButton BackUp=new JButton("备份");
    JButton Restore=new JButton("恢复");
    public GUIBackupAndRestore(){
        JFrame BackUpFrame = new JFrame("备份/恢复");
        BackUpFrame.setBounds(new Rectangle(500, 200));
        BackUpFrame.setLocationRelativeTo(null);
        BackUpFrame.setAlwaysOnTop(true);
        BackUpFrame.setVisible(true);

        Container container = BackUpFrame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        JPanel panel3 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("备份路径");
        JLabel label2 = new JLabel("文件名");
        JLabel label3 = new JLabel("恢复路径");
        JTextField textField = new JTextField(30);
        JTextField textField2 = new JTextField(30);
        JTextField textField3= new JTextField(30);
        JPanel panel4 = new JPanel(new FlowLayout());
        panel4.add(panel);
        panel4.add(panel2);

        panel.add(label);
        panel.add(label2);
        panel.add(label3);

        panel2.add(textField);
        panel2.add(textField2);
        panel2.add(textField3);

        panel3.add(BackUp);
        panel3.add(Restore);

        container.add(panel4);
        container.add(panel3);
        BackUpFrame.pack();
    }
}
