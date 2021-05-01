import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GUIStatistic extends JFrame {
    public GUIStatistic(){
        JFrame frame = new JFrame("统计");
        frame.setBounds(new Rectangle(800, 500));
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Border padding = BorderFactory.createEmptyBorder(50, 50, 20, 20);
        Border padding2 = BorderFactory.createEmptyBorder(0, 0, 20, 0);
        panel.setBorder(padding);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("标注分类选择");
        label.setFont(new Font("微软雅黑", 0, 24));
        label.setBorder(padding2);
        panel.add(label);
        Font font = new Font("微软雅黑", 0, 20);
        JCheckBox checkBox1 = new JCheckBox("总体评价：好评/中评/差评");
        JCheckBox checkBox2 = new JCheckBox("相关联性：有关/无关");
        JCheckBox checkBox3 = new JCheckBox("推广情况：有推广/无推广");
        JCheckBox checkBox4 = new JCheckBox("短期趋势：看涨/看跌");
        checkBox1.setFont(font);
        checkBox2.setFont(font);
        checkBox3.setFont(font);
        checkBox4.setFont(font);
        checkBox1.setSelected(true);
        panel.add(checkBox1);
        panel.add(checkBox2);
        panel.add(checkBox3);
        panel.add(checkBox4);
        panel1.add(panel);
        frame.add(panel1);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("好评", 40); dataSet.setValue("差评", 40); dataSet.setValue("中评", 20);
        JFreeChart chart = ChartFactory.createPieChart("", (PieDataset) dataSet, true, true, true);
        Font ft = new Font("SimSun", 30, 20);//宋体
        LegendTitle legend = null;
        PiePlot categoryplot = null;
        legend = chart.getLegend();
        categoryplot = (PiePlot) chart.getPlot();
        categoryplot.setLabelFont(ft);// 设置图片上固定指示文字字体
        legend.setItemFont(ft);// 设置图例字体
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(400, 400));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        panel1.add(chartPanel);
    }
}
