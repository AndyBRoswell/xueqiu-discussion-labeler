import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class GUI extends JFrame {
    JFrame frame=new JFrame("雪球网股票评论");
    Container contentPane= frame.getContentPane();
    public GUI(){
        contentPane.setLayout(new BorderLayout());
        frame.setLocation(500,250);          //窗口显示位置
        frame.setSize(1000,600);      //窗口大小
        init();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private void init()
    {
        /*图标*/
        URL image_download= Main.class.getResource("/Icon/download.png");
        URL image_addplus =Main.class.getResource("/Icon/addplus.png");
        Icon icondownload=new ImageIcon(image_download);
        Icon iconaddplus=new ImageIcon(image_addplus);
        /*按钮*/
        JButton ButtonDownLoad=new JButton();
        JButton yes=new JButton("确定");
        JButton no=new JButton("取消");
        ButtonDownLoad.setPreferredSize(new Dimension(40,40));
        ButtonDownLoad.setIcon(icondownload);
        ButtonDownLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame DownLoad_frame =new JFrame("任务列表");
                DownLoad_frame.setBounds(new Rectangle(800,600));
                DownLoad_frame.setLocationRelativeTo(null);
                DownLoad_frame.setVisible(true);
                Container container=DownLoad_frame.getContentPane();
                JPanel panel=new JPanel();
                JProgressBar progressBar=new JProgressBar();
                String[] title={"股票编号","股票名称","进度"};
                Object[][] rowData = {
                        {1,2,3},
                        {1,2,3}
                };
                JTable table=new JTable(rowData,title);
                table.setPreferredScrollableViewportSize(new Dimension((DownLoad_frame.getWidth()-50),(DownLoad_frame.getHeight()-100)));
                table.setRowHeight(30);
                JTableHeader tableHeader=table.getTableHeader();
                tableHeader.setFont(new Font("微软雅黑",Font.PLAIN,16));
                tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
                tableHeader.setReorderingAllowed(false);
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setViewportView(table);
                panel.add(scrollPane);
                container.add(panel);

            }
        });

        /*一级菜单创建*/
        JMenuBar menuBar=new JMenuBar();
        JMenu FileMenu=new JMenu("文件");
        JMenu TaskMenu=new JMenu("任务");
        JMenu BackupAndRestoreMenu=new JMenu("备份/恢复");
        JMenu StatisticMenu=new JMenu("统计");
        menuBar.add(FileMenu);
        menuBar.add(TaskMenu);
        menuBar.add(BackupAndRestoreMenu);
        menuBar.add(StatisticMenu);
        frame.setJMenuBar(menuBar);
        /*文件子菜单*/
        JMenuItem ImportMenuItem=new JMenuItem("导入");
        JMenuItem ExportMenuItem=new JMenuItem("导出");
        JMenuItem JournalMenuItem=new JMenuItem("日志");
        FileMenu.add(ImportMenuItem);
        FileMenu.add(ExportMenuItem);
        FileMenu.add(JournalMenuItem);

        ImportMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Import_frame =new JFrame("导入");
                Import_frame.setBounds(new Rectangle(500,200));
                Import_frame.setLocationRelativeTo(null);
                Import_frame.setVisible(true);
                Container container=Import_frame.getContentPane();
                container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
                JPanel panel=new JPanel(new FlowLayout());
                JPanel panel2=new JPanel(new FlowLayout());
                JLabel label=new JLabel("导入路径");
                JTextField textField=new JTextField(30);
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Import_frame.dispose();
                    }
                });
                panel.add(label);
                panel.add(textField);
                panel2.add(yes);
                panel2.add(no);
                container.add(panel);
                container.add(panel2);
                Import_frame.pack();
            }
        });
        ExportMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Export_frame =new JFrame("导出");
                Export_frame.setBounds(new Rectangle(500,200));
                Export_frame.setLocationRelativeTo(null);
                Export_frame.setVisible(true);
                Container container=Export_frame.getContentPane();
                container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
                JPanel panel=new JPanel();
                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                JPanel panel2=new JPanel();
                panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
                JPanel panel3=new JPanel(new FlowLayout());
                JLabel label=new JLabel("导出路径");
                JLabel label2=new JLabel("文件名");
                JTextField textField=new JTextField(30);
                JTextField textField2=new JTextField(30);
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Export_frame.dispose();
                    }
                });
                JPanel panel4=new JPanel(new FlowLayout());
                panel4.add(panel);
                panel4.add(panel2);
                panel2.add(textField);
                panel2.add(textField2);
                panel.add(label);
                panel.add(label2);
                panel3.add(yes);
                panel3.add(no);
                container.add(panel4);
                container.add(panel3);
                Export_frame.pack();
            }
        });
        JournalMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Journal_frame =new JFrame("日志");
                JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
                JTextArea textArea=new JTextArea(25,70);
                panel.add(textArea);
                Journal_frame.add(panel);
                Journal_frame.setBounds(new Rectangle(800,500));
                Journal_frame.setLocationRelativeTo(null);
                Journal_frame.setVisible(true);
                Journal_frame.pack();
            }
        });
        /*任务子菜单*/
        JMenuItem AddMenuItem=new JMenuItem("添加");
        JMenuItem ExitMenuItem=new JMenuItem("退出");
        TaskMenu.add(AddMenuItem);
        TaskMenu.add(ExitMenuItem);

        AddMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Add_frame =new JFrame("添加");
                Add_frame.setBounds(new Rectangle(500,200));
                Add_frame.setLocationRelativeTo(null);
                Add_frame.setVisible(true);
                Container container=Add_frame.getContentPane();
                container.setLayout(new BorderLayout());
                JPanel panel=new JPanel(new FlowLayout());
                JPanel panel2=new JPanel(new FlowLayout());
                JLabel label=new JLabel("股票编号");
                JTextField textField=new JTextField(30);
                textField.setBounds(Add_frame.getX()+50,Add_frame.getY()+50,200,50);
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Add_frame.dispose();
                    }
                });
                panel2.add(yes);
                panel2.add(no);
                panel.add(label);
                panel.add(textField);
                container.add(panel,BorderLayout.CENTER);
                container.add(panel2,BorderLayout.SOUTH);
                Add_frame.pack();
            }
        });
        ExitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        /*备份恢复子菜单*/
        JMenuItem BackupMenuItem=new JMenuItem("备份");
        JMenuItem RestoreMenuItem=new JMenuItem("恢复");
        BackupAndRestoreMenu.add(BackupMenuItem);
        BackupAndRestoreMenu.add(RestoreMenuItem);

        BackupMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Backup_frame =new JFrame("备份");
                Backup_frame.setBounds(new Rectangle(500,200));
                Backup_frame.setLocationRelativeTo(null);
                Backup_frame.setVisible(true);
                Container container=Backup_frame.getContentPane();
                container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
                JPanel panel=new JPanel();
                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                JPanel panel2=new JPanel();
                panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
                JPanel panel3=new JPanel(new FlowLayout());
                JLabel label=new JLabel("备份路径");
                JLabel label2=new JLabel("文件名");
                JTextField textField=new JTextField(30);
                JTextField textField2=new JTextField(30);
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Backup_frame.dispose();
                    }
                });
                JPanel panel4=new JPanel(new FlowLayout());
                panel4.add(panel);
                panel4.add(panel2);
                panel2.add(textField);
                panel2.add(textField2);
                panel.add(label);
                panel.add(label2);
                panel3.add(yes);
                panel3.add(no);
                container.add(panel4);
                container.add(panel3);
                Backup_frame.pack();
            }
        });
        RestoreMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Restore_frame =new JFrame("导入");
                Restore_frame.setBounds(new Rectangle(500,200));
                Restore_frame.setLocationRelativeTo(null);
                Restore_frame.setVisible(true);
                Container container=Restore_frame.getContentPane();
                container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
                JPanel panel=new JPanel(new FlowLayout());
                JPanel panel2=new JPanel(new FlowLayout());
                JLabel label=new JLabel("恢复路径");
                JTextField textField=new JTextField(30);
                no.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Restore_frame.dispose();
                    }
                });
                panel.add(label);
                panel.add(textField);
                panel2.add(yes);
                panel2.add(no);
                container.add(panel);
                container.add(panel2);
                Restore_frame.pack();
            }
        });
        StatisticMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                JFrame statistic_frame =new JFrame("统计");
                statistic_frame.setBounds(new Rectangle(800,500));
                JPanel panel=new JPanel();
                JPanel panel1=new JPanel(new FlowLayout(FlowLayout.LEFT));
                Border padding=BorderFactory.createEmptyBorder(50,50,20,20);
                Border padding2=BorderFactory.createEmptyBorder(0,0,20,0);
                panel.setBorder(padding);
                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                JLabel label=new JLabel("标注分类选择");
                label.setFont(new Font("微软雅黑",0,24));
                label.setBorder(padding2);
                panel.add(label);
                Font font=new Font("微软雅黑",0,20);
                JCheckBox checkBox1=new JCheckBox("总体评价：好评/中评/差评");
                JCheckBox checkBox2=new JCheckBox("相关联性：有关/无关");
                JCheckBox checkBox3=new JCheckBox("推广情况：有推广/无推广");
                JCheckBox checkBox4=new JCheckBox("短期趋势：看涨/看跌");
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
                statistic_frame.add(panel1);
                statistic_frame.setAlwaysOnTop(true);
                statistic_frame.setLocationRelativeTo(null);
                statistic_frame.setVisible(true);

                DefaultPieDataset dataSet = new DefaultPieDataset();
                dataSet.setValue("好评", 40);dataSet.setValue("差评", 40);dataSet.setValue("中评",20);
                JFreeChart chart = ChartFactory.createPieChart("", (PieDataset)dataSet, true, true, true);
                Font ft = new Font("SimSun", 30, 20);//宋体
                LegendTitle legend = null;
                PiePlot categoryplot = null;
                legend = chart.getLegend();
                categoryplot = (PiePlot) chart.getPlot();
                categoryplot.setLabelFont(ft);// 设置图片上固定指示文字字体
                legend.setItemFont(ft);// 设置图例字体
                ChartPanel chartPanel=new ChartPanel(chart);
                chartPanel.setPreferredSize(new Dimension(400,400));
                chartPanel.setBorder(BorderFactory.createEmptyBorder(40,0,0,0));
                panel1.add(chartPanel);

            }
            @Override public void menuDeselected(MenuEvent e) { }
            @Override public void menuCanceled(MenuEvent e) { }
        });
        JPanel panel1=new JPanel(new FlowLayout(FlowLayout.CENTER));
        /*搜索框*/
        JTextField search=new JTextField(70);
        panel1.add(search);

        /*勾选框*/
        JCheckBox Marked=new JCheckBox("已标注");
        JCheckBox UnMarked=new JCheckBox("未标注");
        panel1.add(Marked);
        panel1.add(UnMarked);

        panel1.add(ButtonDownLoad);

        contentPane.add(panel1,BorderLayout.PAGE_START);

        JPanel panel2=new JPanel(new FlowLayout(FlowLayout.CENTER));
        /*表格*/
        String[] title={"股票编号","评论","已选标签"};
        Object[][] rowData = {
                {1,2,3},
                {1,2,3}
        };
        JTable table=new JTable(rowData,title);
        table.setPreferredScrollableViewportSize(new Dimension((frame.getWidth())-50,(frame.getHeight())-220));
        table.setRowHeight(30);
        JTableHeader tableHeader=table.getTableHeader();
        tableHeader.setFont(new Font("微软雅黑",Font.PLAIN,16));
        tableHeader.setResizingAllowed(false);               // 设置不允许手动改变列宽
        tableHeader.setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);
        panel2.add(scrollPane);

        contentPane.add(panel2,BorderLayout.CENTER);

        JPanel panel3=new JPanel(new FlowLayout(FlowLayout.LEFT));
        /*可选标注*/
        JPanel TagPanel=new JPanel();
        TagPanel.setLayout(new BorderLayout());
        JLabel ChooseTag=new JLabel("可选标注");
        TagPanel.add(ChooseTag,BorderLayout.NORTH);
        JButton AddTagButton=new JButton();
        AddTagButton.setPreferredSize(new Dimension(40,40));
        AddTagButton.setIcon(iconaddplus);
        TagPanel.add(AddTagButton,BorderLayout.SOUTH);
        AddTagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame =new JFrame("添加标注类");
                frame.setBounds(new Rectangle(400,400));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                Container container=frame.getContentPane();
                container.setLayout(new FlowLayout());
                JPanel panel=new JPanel();
                panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
                JPanel panel2=new JPanel();
                panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));
                JPanel panel3=new JPanel();
                panel3.setLayout(new BoxLayout(panel3,BoxLayout.Y_AXIS));
                JLabel label=new JLabel("分类名称:");
                label.setBorder(BorderFactory.createEmptyBorder(0,20,5,10));
                JLabel label2=new JLabel("具体分类:");
                label2.setBorder(BorderFactory.createEmptyBorder(0,20,5,10));
                JButton button=new AddButton();
                panel.add(label);
                panel.add(label2);
                container.add(panel);
                JTextField textField=new JTextField(6);
                JTextField textField2=new JTextField(10);
                JTextField textField3=new JTextField(10);
                panel2.add(textField);
                panel2.add(textField2);
                panel2.add(textField3);
                panel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
                container.add(panel2);
                container.add(button);
                JButton button1=new JButton("确定");
                panel3.add(button1);
                container.add(panel3);
                frame.pack();

            }
        });
        panel3.add(TagPanel);

        JLabel Evaluation=new JLabel("总体评价");
        JLabel Advertisement=new JLabel("推广情况");
        JLabel Relevance=new JLabel("相关联性");
        JLabel Tendency=new JLabel("短期趋势");

        JPanel panel4=new JPanel();
        panel4.setLayout(new BoxLayout(panel4,BoxLayout.X_AXIS));

        JPanel TPanel1=new JPanel(new FlowLayout());
        JLabel good=new LabelSet("好评",Color.LIGHT_GRAY);
        JLabel middle=new LabelSet("中评",Color.LIGHT_GRAY);
        JLabel bad=new LabelSet("差评",Color.LIGHT_GRAY);
        JButton addTag1=new AddButton();
        addTag1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Add_frame =new JFrame("添加标注");
                Add_frame.setBounds(new Rectangle(500,200));
                Add_frame.setLocationRelativeTo(null);
                Add_frame.setVisible(true);
                Container container=Add_frame.getContentPane();
                container.setLayout(new BorderLayout());
                JPanel panel=new JPanel(new FlowLayout());
                JLabel label=new JLabel("标注名称");
                JTextField textField=new JTextField(10);
                panel.add(label);
                panel.add(textField);
                panel.add(yes);
                container.add(panel);
                Add_frame.pack();

            }
        });
        TPanel1.add(Evaluation);
        TPanel1.add(good);
        TPanel1.add(middle);
        TPanel1.add(bad);
        TPanel1.add(addTag1);

        JPanel TPanel2=new JPanel(new FlowLayout());
        JLabel have=new LabelSet("有",Color.LIGHT_GRAY);
        JLabel havenot=new LabelSet("无",Color.LIGHT_GRAY);
        JButton addTag2=new AddButton();
        addTag2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Add_frame =new JFrame("添加标注");
                Add_frame.setBounds(new Rectangle(500,200));
                Add_frame.setLocationRelativeTo(null);
                Add_frame.setVisible(true);
                Container container=Add_frame.getContentPane();
                container.setLayout(new BorderLayout());
                JPanel panel=new JPanel(new FlowLayout());
                JLabel label=new JLabel("标注名称");
                JTextField textField=new JTextField(10);
                panel.add(label);
                panel.add(textField);
                panel.add(yes);
                container.add(panel);
                Add_frame.pack();

            }
        });
        TPanel2.add(Advertisement);
        TPanel2.add(have);
        TPanel2.add(havenot);
        TPanel2.add(addTag2);

        JPanel TPanel3=new JPanel(new FlowLayout());
        JLabel haverelation=new LabelSet("有关",Color.LIGHT_GRAY);
        JLabel havenrelation=new LabelSet("无关",Color.LIGHT_GRAY);
        JButton addTag3=new AddButton();
        addTag3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Add_frame =new JFrame("添加标注");
                Add_frame.setBounds(new Rectangle(500,200));
                Add_frame.setLocationRelativeTo(null);
                Add_frame.setVisible(true);
                Container container=Add_frame.getContentPane();
                container.setLayout(new BorderLayout());
                JPanel panel=new JPanel(new FlowLayout());
                JLabel label=new JLabel("标注名称");
                JTextField textField=new JTextField(10);
                panel.add(label);
                panel.add(textField);
                panel.add(yes);
                container.add(panel);
                Add_frame.pack();

            }
        });
        TPanel3.add(Relevance);
        TPanel3.add(haverelation);
        TPanel3.add(havenrelation);
        TPanel3.add(addTag3);

        JPanel TPanel4=new JPanel(new FlowLayout());
        JLabel up=new LabelSet("看涨",Color.LIGHT_GRAY);
        JLabel down=new LabelSet("看跌",Color.LIGHT_GRAY);
        JButton addTag4=new AddButton();
        addTag4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame Add_frame =new JFrame("添加标注");
                Add_frame.setBounds(new Rectangle(500,200));
                Add_frame.setLocationRelativeTo(null);
                Add_frame.setVisible(true);
                Container container=Add_frame.getContentPane();
                container.setLayout(new BorderLayout());
                JPanel panel=new JPanel(new FlowLayout());
                JLabel label=new JLabel("标注名称");
                JTextField textField=new JTextField(10);
                panel.add(label);
                panel.add(textField);
                panel.add(yes);
                container.add(panel);
                Add_frame.pack();

            }
        });
        TPanel4.add(Tendency);
        TPanel4.add(up);
        TPanel4.add(down);
        TPanel4.add(addTag4);

        panel4.add(TPanel1);
        panel4.add(TPanel2);
        panel4.add(TPanel3);
        panel4.add(TPanel4);
        panel3.add(panel4);

        contentPane.add(panel3,BorderLayout.SOUTH);
    }
    private static class LabelSet extends JLabel{
        public LabelSet(String text,Color bgColor)
        {
            super(text);
            setOpaque(true);
            setBackground(bgColor);
            setPreferredSize(new Dimension(40,20));
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    private static  class  AddButton extends JButton{
        public AddButton()
        {
            super();
            URL image_add=Main.class.getResource("/Icon/add.png");
            Icon iconadd=new ImageIcon(image_add);
            setPreferredSize(new Dimension(20,20));
            setIcon(iconadd);

        }
    }
}
