// THIS CLASS IS DEPRECATED.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;
import java.util.ArrayList;

public class GUIAddTag extends JFrame {
    JFrame frame = new JFrame("添加标注");
    JLabel label = new JLabel("标注名称:");
    JButton buttonNo = new JButton("取消");
    JButton buttonYes = new JButton("确定");
    JTextField textField = new JTextField(6);

    public GUIAddTag(String text, JFrame parent) {
        super(text);
        frame.setBounds(new Rectangle(400, 200));
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setVisible(true);
        buttonNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File f = new File(Global.LabelFile);
                try {
                    BufferedReader br = new BufferedReader(new FileReader(f));
                    ArrayList<String> Data = new ArrayList<String>();
                    String data = null;
                    int count = 0;
                    while ((data = br.readLine()) != null) {
                        if (count == Integer.valueOf(text)) {
                            data = data + " " + textField.getText();
                        }
                        Data.add(data);
                        count++;
                    }
                    br.close();
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                    for (int i = 0; i < Data.size(); i++) {
                        bw.write(Data.get(i));
                        bw.write("\n");
                    }
                    bw.close();
                    frame.dispose();
                    parent.dispose();
                    new GUI();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        frame.add(label);
        frame.add(textField);
        frame.add(buttonNo);
        frame.add(buttonYes);
        frame.addComponentListener(new GUIAddTag.FrameListener());
    }

    public class FrameListener implements ComponentListener {
        @Override
        public void componentResized(ComponentEvent e) {
            int x = frame.getWidth();
            int y = frame.getHeight();
            Font font = new Font("微软雅黑", 0, x / 30);
            label.setFont(font);
            buttonNo.setFont(font);
            buttonYes.setFont(font);
            label.setBounds(x / 15, y / 5, x / 5, y / 10);
            textField.setBounds(x / 4, y / 5, x * 3 / 5, y / 10);
            buttonYes.setBounds(x / 4, 10 + y / 2, x / 5, y / 10);
            buttonNo.setBounds(x / 3 + x / 5, 10 + y / 2, x / 5, y / 10);

        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }
    }
}