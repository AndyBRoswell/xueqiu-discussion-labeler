import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class GUIImport extends JFrame {
    private static Logger logger=Logger.getLogger(GUIImport.class);
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");
    JButton chooseFile=new JButton("浏览");
    static ArrayList<String> list = new ArrayList<String>();
    static int i;
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
                    frame.dispose();
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
                fileChoose();
            }
        });

    }

    public static void fileChoose() {
        JFileChooser chooser = new JFileChooser(Global.DefaultSavePath);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnval=chooser.showDialog(new JLabel(), "选择");
        if(returnval==JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            String str = "";
            for (File file : files) {
                if (file.isDirectory())
                    str = file.getPath();
                else {
                    str = file.getAbsoluteFile().toString();
                    list.add(str);
                    i=list.indexOf(str)+1;
                    GUIImportList.model.addRow(new Object[] {GUIImport.i,GUIImport.list.get(GUIImport.i-1)});
                }
            }
        }

    }

    private void setChooseFileName(String string) {
        list.add(string);
        i=list.indexOf(string)+1;
        //System.out.println(i);
    }

}

class GUIImportList extends JFrame{
    public static Object[][] datas;
    //操作面板，用于放置增删改按钮
    private JPanel controlPanel;
    private JPanel controlPanel1;
    public static DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton deleteBtn=new JButton("删除导入");
    private JButton yesBtn=new JButton("确认导入");
    private JButton addBtn =new JButton("添加任务");
    private JButton allDeleteBtn =new JButton("全部清空");
    JFrame frame = new JFrame("导入列表");
    public GUIImportList(){
        frame.setBounds(580, 280, 500, 500);
        String[] head = {"序号", "导入文件"};

        //datas = new Object[][]{{GUIImport.i, filename}};

        model = new DefaultTableModel(datas, head);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        controlPanel1 = new JPanel();
        controlPanel1.add(deleteBtn);
        controlPanel1.add(allDeleteBtn);
        controlPanel = new JPanel();
        controlPanel.add(addBtn);
        controlPanel.add(yesBtn);
        frame.add(controlPanel1,BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        DefaultTableCellRenderer render =new DefaultTableCellRenderer();
        render.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumn("序号").setCellRenderer(render);
        table.getColumn("导入文件").setCellRenderer(render);
        //设置行宽
        DefaultTableColumnModel dcm =(DefaultTableColumnModel)table.getColumnModel();
        dcm.getColumn(0).setPreferredWidth(60); //设置表格显示的最好宽度，即此时表格显示的宽度。
        dcm.getColumn(0).setMinWidth(45);//设置表格通过拖动列可以的最小宽度。
        dcm.getColumn(0).setMaxWidth(75);//设置表格通过拖动列可以的最大宽度。

        frame.setVisible(true);

        //删除按钮
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                //int num= (int) model.getValueAt(row,0);
                //System.out.println(num);
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "请先选择一条记录！");
                    return;
                }
                model.removeRow(table.getSelectedRow());
                GUIImport.list.remove(row);
            }
        });
        //全部清空按钮
        allDeleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int index = table.getModel().getRowCount() - 1; index >= 0; index--) {
                    model.removeRow(index);
                }
                GUIImport.list.clear();
                JOptionPane.showMessageDialog(null, "清空成功！");
            }
        });
        //确认导入按钮
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(GUIImport.list);

                for(String string :GUIImport.list){
                    StorageAccessor.LoadDiscussionFromCSV(string,"gbk");
                }
                for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
			            System.out.println(entry.GetText());
			            System.out.println(entry.GetLabels().toString());
                }
                frame.dispose();
            }
        });
        //继续添加按钮
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIImport.fileChoose();
            }
        });

    }
}

