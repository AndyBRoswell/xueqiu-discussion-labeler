import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class GUIDownLoad extends JFrame {
    public GUIDownLoad(){
        JFrame frame = new JFrame("任务列表");
        frame.setBounds(new Rectangle(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        JPanel panel = new JPanel();
        JProgressBar progressBar = new JProgressBar();
        String[] title = { "股票编号", "股票名称", "进度" };
        Object[][] rowData = {
                { 1, 2, 3 },
                { 1, 2, 3 }
        };
        JTable table = new JTable(rowData, title);
        table.setPreferredScrollableViewportSize(new Dimension((frame.getWidth() - 50), (frame.getHeight() - 100)));
        table.setRowHeight(30);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
        tableHeader.setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);
        panel.add(scrollPane);
        container.add(panel);
    }
}
