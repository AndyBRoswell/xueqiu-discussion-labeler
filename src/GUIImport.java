import org.apache.log4j.Logger;

import javax.swing.*;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUIImport extends JFrame {
    private static Logger logger=Logger.getLogger(GUIImport.class);
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    JButton chooseFile=new JButton("浏览");
    public static String[] str_ChooseFileName=new String[3];//选择的文件名

    public GUIImport(){
        JFrame frame = new JFrame("导入");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("导入文件:");
        JTextField textField = new JTextField(30);

        panel.add(label);
        panel.add(textField);
        panel.add(chooseFile);
        panel2.add(yes);
        panel2.add(no);
        container.add(panel);
        container.add(panel2);
        frame.pack();
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string=textField.getText();
                if(string.equals("")){
                    JOptionPane.showMessageDialog(null, "导入失败！\n请选择正确的文件");
                }
                else{
                    setChooseFileName(string);
                    //System.out.println(str_ChooseFileName);
                    new GUIImportList(str_ChooseFileName);
                    textField.setText("");
                }
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        //浏览的按钮,选择文件
        chooseFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
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
//                logger.info("关闭导入界面");
//            }
//        });

    }
    private void setChooseFileName(String string) {
        if(str_ChooseFileName[0]==null){
            str_ChooseFileName[0]=string;
        }
        else if(str_ChooseFileName[0]!=null&&str_ChooseFileName[1]==null){
            str_ChooseFileName[1]=string;
        }
        else if(str_ChooseFileName[1]!=null&&str_ChooseFileName[2]==null){
            str_ChooseFileName[2]=string;
        }
    }
}

class GUIImportList extends JFrame {
    JButton yes = new JButton("确定");
    JButton no = new JButton("继续添加");
    JButton deleteFile =new JButton("x");
    JButton deleteFile2 =new JButton("x");
    JButton deleteFile3 =new JButton("x");
    GUIImportList(String[] fileName){
        JFrame frame = new JFrame("导入列表预览");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JPanel panel3 = new JPanel(new FlowLayout());
        JPanel panel4 = new JPanel(new FlowLayout());
        JLabel label1 = new JLabel("--------需要导入的文件--------");
        JLabel label = new JLabel("· 1 :  ");
        JTextField textField = new JTextField(30);
        textField.setText(fileName[0]);
        textField.setEditable(false);

        JLabel label2 = new JLabel("· 2 :  ");
        JTextField textField2 = new JTextField(30);
        textField2.setText(fileName[1]);
        textField2.setEditable(false);

        JLabel label3 = new JLabel("· 3 :  ");
        JTextField textField3 = new JTextField(30);
        textField3.setText(fileName[2]);
        textField3.setEditable(false);

        panel1.add(label1);
        panel.add(label);
        panel.add(textField);
        panel.add(deleteFile);
        panel2.add(label2);
        panel2.add(textField2);
        panel2.add(deleteFile2);
        panel3.add(label3);
        panel3.add(textField3);
        panel3.add(deleteFile3);

        panel4.add(yes);
        panel4.add(no);
        container.add(panel1);
        container.add(panel);
        container.add(panel2);
        container.add(panel3);
        container.add(panel4);
        frame.pack();
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                for(int i=0;i<3;i++){
//                    System.out.println(fileName[i]);
//                }
                if(fileName[0]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[0]);
//                        for (DiscussionItem entry : DataManipulator.DiscussionList) {
//			                System.out.println(entry.GetText());
//			                System.out.println(entry.GetLabels().toString());
//		                }
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "导入失败！请输入正确的文件");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    		for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
			System.out.println(entry.GetText());
			System.out.println(entry.GetLabels().toString());
		}
                    JOptionPane.showMessageDialog(null, "导入成功！");
                    GUIImport.str_ChooseFileName[0]=null;
                    frame.dispose();
                }
                else if(fileName[1]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[1]);
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "导入失败！请输入正确的文件");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    JOptionPane.showMessageDialog(null, "导入成功！");
                    GUIImport.str_ChooseFileName[1]=null;
                    frame.dispose();
                }
                else if(fileName[2]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[2]);
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "导入失败！请输入正确的文件");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    JOptionPane.showMessageDialog(null, "导入成功！");
                    GUIImport.str_ChooseFileName[2]=null;
                    frame.dispose();
                }
                GUIImport.str_ChooseFileName=new String[3];//导入成功之后清空数据
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        deleteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
        deleteFile2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[1]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
        deleteFile3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[2]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
    }
}
