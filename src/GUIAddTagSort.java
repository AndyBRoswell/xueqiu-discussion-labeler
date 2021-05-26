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
    JLabel catNameLabel = new JLabel("分类名称:");
    JLabel concreteLabelLabel = new JLabel("具体分类:");
    JButton buttonNo = new JButton("取消");
    JButton buttonYes = new JButton("确定");
    JButton addButton = new JButton();
    JTextField catTextField = new JTextField(6);
    ArrayList<JTextField> labelTextFields = new ArrayList<JTextField>();

    public GUIAddTagSort() {
        frame.setBounds(new Rectangle(400, 200));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setVisible(true);

        Font font = new Font("微软雅黑", Font.PLAIN, Global.FontSizeD);
        catNameLabel.setFont(font);
        concreteLabelLabel.setFont(font);
        buttonNo.setFont(font);
        buttonYes.setFont(font);

        final int X = frame.getContentPane().getWidth();
        final int Y = frame.getContentPane().getHeight();
        final int h0 = 20;

        catTextField.setBounds(60, 0, X - 60, h0);
        for (int i = 0; i < 4; i++) {
            labelTextFields.add(new JTextField());
            frame.add(labelTextFields.get(i)).setBounds(60, (i + 1) * h0, X - 60, h0);
        }

        catNameLabel.setBounds(0, 0, 60, h0);
        concreteLabelLabel.setBounds(0, 0 + h0, 60, h0);
        buttonYes.setBounds(X / 2 - 100, Y - h0, 100, h0);
        buttonNo.setBounds(X / 2, Y - h0, 100, h0);
        addButton.setBounds(30, 50, h0, h0);

        buttonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!catTextField.getText().equals("")) {
                    writeTxt(catTextField.getText());
                    writeTxt(" ");
                }
                int i = 0;
                while (i < labelTextFields.size() && !labelTextFields.get(i).getText().equals("")) {
                    if (i == labelTextFields.size() - 1) {
                        writeTxt(labelTextFields.get(i).getText());
                    } else {
                        if ((i + 1) < labelTextFields.size() && !labelTextFields.get(i + 1).getText().equals("")) {
                            writeTxt(labelTextFields.get(i).getText());
                            writeTxt(" ");
                        } else {
                            writeTxt(labelTextFields.get(i).getText());
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
                labelTextFields.add(new JTextField(10));
                frame.setBounds(new Rectangle(400, 200 + (labelTextFields.size() - 4) * 20));
                frame.setLocationRelativeTo(null);
                frame.add(labelTextFields.get(labelTextFields.size() - 1)).setBounds(100, 90 + 20 * (labelTextFields.size() - 4), 160, 20);
                buttonYes.setBounds(100, 110 + 20 * (labelTextFields.size() - 4), 80, 20);
                buttonNo.setBounds(213, 110 + 20 * (labelTextFields.size() - 4), 80, 20);
            }
        });

        frame.add(catNameLabel);
        frame.add(concreteLabelLabel);
        frame.add(catTextField);
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