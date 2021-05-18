import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class GUIAddTagSort extends JFrame {
    ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");
    JFrame frame = new JFrame("添加标注类");
    JLabel label = new JLabel("分类名称:");
    JLabel label2 = new JLabel("具体分类:");
    JButton buttonNo = new JButton("取消");
    JButton buttonYes = new JButton("确定");
    JButton add=new JButton();
    JTextField textField = new JTextField(6);
    JTextField textField2 = new JTextField(10);
    JTextField textField3 = new JTextField(10);
    JTextField textField4=new JTextField(10);
    public GUIAddTagSort() {
        frame.setBounds(new Rectangle(400, 200));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);
        Font font = new Font("微软雅黑", 0, 13);
        label.setFont(font);
        label2.setFont(font);
        buttonNo.setFont(font);
        buttonYes.setFont(font);
        label.setBounds(20,10,80,20);
        label2.setBounds(20,30,80,20);
        textField.setBounds(100,10,240,20);
        textField2.setBounds(100,30,160,20);
        textField3.setBounds(100,50,160,20);
        textField4.setBounds(100,70,160,20);
        buttonYes.setBounds(100,110,80,20);
        buttonNo.setBounds(213,110,80,20);
        add.setBounds(30,50,20,20);
        buttonNo.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) { frame.dispose(); }});
        buttonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeTxt(textField.getText());
                if (!(textField2.getText().equals(""))) {writeTxt(" ");}
                writeTxt(textField2.getText());
                if (!(textField3.getText().equals(""))) {writeTxt(" ");}
                writeTxt(textField3.getText());
                if (!(textField4.getText().equals(""))) {writeTxt(" ");}
                writeTxt(textField4.getText());
                writeTxt("\n");
                frame.dispose();
            }
        });
        final int[] count = {4};
        iconAddSmall.setImage(iconAddSmall.getImage().getScaledInstance(add.getWidth(),add.getHeight(),Image.SCALE_DEFAULT));
        add.setIcon(iconAddSmall);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setBounds(new Rectangle(400, 200+(count[0]-3)*20));
                frame.setLocationRelativeTo(null);
                frame.add(new JTextField(10)).setBounds(100,70+20*(count[0]-3),160,20);
                buttonYes.setBounds(100,110+20*(count[0]-3),80,20);
                buttonNo.setBounds(213,110+20*(count[0]-3),80,20);
                count[0]++;
            }
        });
        frame.add(label);
        frame.add(label2);
        frame.add(textField);
        frame.add(textField2);
        frame.add(textField3);
        frame.add(textField4);
        frame.add(buttonNo);
        frame.add(buttonYes);
        frame.add(add);
    }
    public void writeTxt(String str){
        File f = new File(Global.LabelFile);
        try {
            if(str!=null) {
                OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(f,true), "UTF-8");
                outputStream.write(str);
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}