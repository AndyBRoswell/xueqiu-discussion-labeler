import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIDownLoad extends JFrame {
    JButton clean = new JButton("清空已完成的任务");

    public GUIDownLoad() {
        JFrame frame = new JFrame("任务列表");
        frame.setBounds(new Rectangle(800, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        JPanel panel = new JPanel();
        JProgressBar progressBar = new JProgressBar();
        String[] title = {"股票编号", "股票名称", "进度"};
        String[][] rowData = GUIAddStock.rowData;
        JTable table = new JTable(rowData, title);
        table.setPreferredScrollableViewportSize(new Dimension((frame.getWidth() - 50), (frame.getHeight() - 100)));
        table.setRowHeight(30);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        tableHeader.setResizingAllowed(false);   // 设置不允许手动改变列宽
        tableHeader.setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);
        panel.add(scrollPane);
        //panel.add(clean1);
        panel.add(clean);
        container.add(panel);

        //
        clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (GUIAddStock.rowData[0][2].equals("待爬取")) {
                    JOptionPane.showMessageDialog(null, "目前没有任务！");
                } else if (GUIAddStock.str_textPa[0].equals("爬取完毕") && GUIAddStock.str_textPa[1] == null) {
                    int option = JOptionPane.showConfirmDialog(
                            GUIDownLoad.this, "还有一个任务栏还未使用，确定清空任务栏? ", "提示 ", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        GUIAddStock.rowData = new String[][]{
                                {"无", "无", "待爬取"},
                                {"无", "无", "待爬取"}
                        };
                        GUIAddStock.str_gupiaoNum = new String[2];
                        GUIAddStock.str_gupiaoName = new String[2];
                        GUIAddStock.str_textPa = new String[2];
                        GUIAddStock.dataFile = new String[2];
                        frame.dispose();
                        new GUIDownLoad();
                    }
                } else if (GUIAddStock.str_textPa[0].equals("爬取完毕") && GUIAddStock.str_textPa[1].equals("爬取完毕")) {
                    JOptionPane.showMessageDialog(null, "任务均完成，进行清空！");
                    GUIAddStock.rowData = new String[][]{
                            {"无", "无", "待爬取"},
                            {"无", "无", "待爬取"}
                    };
                    GUIAddStock.str_gupiaoNum = new String[2];
                    GUIAddStock.str_gupiaoName = new String[2];
                    GUIAddStock.str_textPa = new String[2];
                    GUIAddStock.dataFile = new String[2];
                    frame.dispose();
                    new GUIDownLoad();
                } else {
                    JOptionPane.showMessageDialog(null, "还有未完成的任务，无法清空\n请任务都完成后再清空！");
                }
            }
        });
    }
}
