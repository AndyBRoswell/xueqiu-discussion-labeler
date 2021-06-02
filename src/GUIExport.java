import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUIExport extends JFrame {

    private static Logger logger = Logger.getLogger(GUIExport.class);
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    JButton button = new JButton("选择");

    public GUIExport() {
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
                logger.info("关闭导出界面");
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
        panel3.add(button);
        /*root.add(new JLabel("文件"));
        root.add(textField);
        root.add(button);*/
        /*button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                test3();
            }
        });*/
        container.add(panel4);
        container.add(panel3);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logger.info("关闭导出界面");
            }
        });
    }
}
    /*
        // 选择打开文件
        private void test1()
        {
            JFileChooser chooser = new JFileChooser();

            //FileNameExtensionFilter: 文件名后缀过滤器
            FileNameExtensionFilter filter = new FileNameExtensionFilter("图片文件", "jpg", "jpeg", "png");
            chooser.setFileFilter(filter);

            // 显示对话框
            int ret = chooser.showOpenDialog(this);
            // 获取用户选择的结果
            if (ret == JFileChooser.APPROVE_OPTION)
            {
                // 结果为：已经存在的一个文件
                File file = chooser.getSelectedFile();
                textField.setText(file.getAbsolutePath());
            }
        }

        // 选择保存文件
        private void test2()
        {
            JFileChooser chooser = new JFileChooser();

            //FileNameExtensionFilter: 文件名后缀过滤器
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML文件", "xml");
            chooser.setFileFilter(filter);

            // 显示对话框 showSaveDialog
            int ret = chooser.showSaveDialog(this);
            // 获取用户选择的结果
            if (ret == JFileChooser.APPROVE_OPTION)
            {
                // 结果为：用户要保存的文件的路径
                File file = chooser.getSelectedFile();
                textField.setText(file.getAbsolutePath());
            }
        }
    private void test3()
    {
        JFileChooser chooser = new JFileChooser();

        // 设置模式：仅选择目录
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        // 显示对话框
        int ret = chooser.showOpenDialog(this);
        // 获取用户选择的结果
        if (ret == JFileChooser.APPROVE_OPTION)
        {
            // 结果为： 已经存在的一个目录
            File dir = chooser.getSelectedFile();
            textField.setText(dir.getAbsolutePath());
        }
    }

}
*/