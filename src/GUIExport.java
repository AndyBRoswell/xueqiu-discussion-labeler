import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class GUIExport extends JFrame {
    private static Logger logger=Logger.getLogger(GUIExport.class);
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    JButton chooseFile=new JButton("浏览");
    public static String str_ChooseFilePath;//选择的文件路径
    public static String str_FileName;//选择的文件名
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
        JLabel label = new JLabel("导出路径:");
        JLabel label2 = new JLabel("文件名:");
        JTextField textField = new JTextField(30);
        JTextField textField2 = new JTextField(30);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                //logger.info("关闭导出界面");
            }
        });
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                str_ChooseFilePath=textField.getText();
                str_FileName=textField2.getText();
                String str_file=str_ChooseFilePath+"\\"+str_FileName+".csv";
                //System.out.println(str_file);
                if (str_file.equals("\\.csv")){
                    JOptionPane.showMessageDialog(null, "还未输入导出文件！\n请输入正确的文件路径及名称");
                }
                else {
                    try {
                        StorageAccessor.SaveDiscussionToCSV(str_file);
                    } catch (IOException | XPathExpressionException ioException) {
                        JOptionPane.showMessageDialog(null, "文件导出失败！\n请输入正确的文件路径及名称");
                        logger.error("选择文件路径出现错误");
                        ioException.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "文件导出成功！");
                    frame.dispose();
                }
                //logger.info("关闭导出界面");
            }
        });
        JPanel panel4 = new JPanel(new FlowLayout());
        JPanel panel5 = new JPanel(new FlowLayout());
        panel5.add(chooseFile);
        panel4.add(panel);
        panel4.add(panel2);
        panel4.add(panel5);
        panel2.add(textField);
        panel2.add(textField2);
        panel.add(label);
        panel.add(label2);
        panel3.add(yes);
        panel3.add(no);
        container.add(panel4);
        container.add(panel3);
        frame.pack();
        //浏览的按钮,选择文件夹
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
                chooser.setCurrentDirectory(fsv.getHomeDirectory());
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.showDialog(new JLabel(), "选择");
                File file = chooser.getSelectedFile();
                if(file!=null){
                    String filePath=file.getAbsoluteFile().toString();
                    if(filePath!=null){
                        textField.setText(filePath);
                    }
                }
            }
        });
//        frame.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e)
//            {
//                logger.info("关闭导出界面");
//            }
//        });
    }
}
