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
    JFrame frame = new JFrame("添加标注类");
    JLabel label = new JLabel("分类名称:");
    JLabel label2 = new JLabel("具体分类:");
    JButton buttonNo = new JButton("取消");
    JButton buttonYes = new JButton("确定");
    JTextField textField = new JTextField(6);
    JTextField textField2 = new JTextField(10);
    JTextField textField3 = new JTextField(10);
    JTextField textField4=new JTextField(10);
    public GUIAddTagSort() {
        frame.setBounds(new Rectangle(400, 200));
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);

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
        frame.add(label);
        frame.add(label2);
        frame.add(textField);
        frame.add(textField2);
        frame.add(textField3);
        frame.add(textField4);
        frame.add(buttonNo);
        frame.add(buttonYes);
        frame.addComponentListener(new FrameListener());
    }

    public class FrameListener implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent e) {
            int x = frame.getWidth();
            int y = frame.getHeight();
            Font font = new Font("微软雅黑", 0, x/30);
            label.setFont(font);
            label2.setFont(font);
            buttonNo.setFont(font);
            buttonYes.setFont(font);
            label.setBounds(20,10,x/5,y/10);
            label2.setBounds(20,10+y/10,x/5,y/10);
            textField.setBounds(20+x/5,10,x*3/5,y/10);
            textField2.setBounds(20+x/5,10+y/10,x*2/5,y/10);
            textField3.setBounds(20+x/5,10+y*2/10,x*2/5,y/10);
            textField4.setBounds(20+x/5,10+y*3/10,x*2/5,y/10);
            buttonYes.setBounds(x/4,10+y/2,x/5,y/10);
            buttonNo.setBounds(x/3+x/5,10+y/2,x/5,y/10);

        }
        @Override public void componentMoved(ComponentEvent e) { }
        @Override public void componentShown(ComponentEvent e) { }
        @Override public void componentHidden(ComponentEvent e) { }
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
