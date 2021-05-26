import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class GUIAddTagSort extends JFrame {
    ImageIcon iconAddSmall = new ImageIcon(Global.IconPath + "\\add.png");
    JFrame frame = new JFrame("添加标注类");
    JLabel labelCatName = new JLabel("分类名称:");
    JLabel labelConcreteTag = new JLabel("具体分类:");
    JButton buttonNo = new JButton("取消");
    JButton buttonYes = new JButton("确定");
    JButton addButton = new JButton();
    JTextField textField = new JTextField(6);
    ArrayList<JTextField> text = new ArrayList<JTextField>();

    public GUIAddTagSort() {
        frame.setBounds(new Rectangle(400, 200));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);

        Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);
        labelCatName.setFont(font);
        labelConcreteTag.setFont(font);
        buttonNo.setFont(font);
        buttonYes.setFont(font);

        textField.setBounds(100, 10, 240, 20);
        for (int i = 0; i < 4; i++) {
            text.add(new JTextField(10));
            frame.add(text.get(i)).setBounds(100, 30 + 20 * i, 160, 20);
        }

        labelCatName.setBounds(20, 10, 80, 20);
        labelConcreteTag.setBounds(20, 30, 80, 20);
        buttonYes.setBounds(100, 110, 80, 20);
        buttonNo.setBounds(213, 110, 80, 20);
        addButton.setBounds(30, 50, 20, 20);

        buttonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField.getText().equals("")) {
                    writeTxt(textField.getText());
                    writeTxt(" ");
                }
                int i = 0;
                while (i < text.size() && !text.get(i).getText().equals("")) {
                    if (i == text.size() - 1) {
                        writeTxt(text.get(i).getText());
                    } else {
                        if ((i + 1) < text.size() && !text.get(i + 1).getText().equals("")) {
                            writeTxt(text.get(i).getText());
                            writeTxt(" ");
                        } else {
                            writeTxt(text.get(i).getText());
                        }
                    }
                    i++;
                }
                writeTxt("\n");
                frame.dispose();
            }
        });

        iconAddSmall.setImage(iconAddSmall.getImage().getScaledInstance(addButton.getWidth(), addButton.getHeight(), Image.SCALE_DEFAULT));
        addButton.setIcon(iconAddSmall);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.add(new JTextField(10));
                frame.setBounds(new Rectangle(400, 200 + (text.size() - 4) * 20));
                frame.setLocationRelativeTo(null);
                frame.add(text.get(text.size() - 1)).setBounds(100, 90 + 20 * (text.size() - 4), 160, 20);
                buttonYes.setBounds(100, 110 + 20 * (text.size() - 4), 80, 20);
                buttonNo.setBounds(213, 110 + 20 * (text.size() - 4), 80, 20);
            }
        });

        frame.add(labelCatName);
        frame.add(labelConcreteTag);
        frame.add(textField);
        frame.add(buttonNo);
        frame.add(buttonYes);
        frame.add(addButton);
    }

    public void writeTxt(String str) {
        File f = new File(Global.LabelFile);
        try {
            if (str != null) {
                OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(f, true), "UTF-8");
                outputStream.write(str);
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}