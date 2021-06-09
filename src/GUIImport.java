import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
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
                    //System.out.println(str_ChooseFileName);

//                    if(list.size()==1){
//                        new GUIImportList(string);
//                    }

                    GUIImportList.model.addRow(new Object[] {GUIImport.i,GUIImport.list.get(GUIImport.i-1)});

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
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.showDialog(new JLabel(), "选择");
                File file = chooser.getSelectedFile();
                if(file!=null){
                    String filePath=file.getAbsoluteFile().toString();
                    if(filePath!=null){
                        textField.setText(filePath);
                    }
                }
            }
        });

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
            }
        });
        //继续添加按钮
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUIImport();
            }
        });

    }
}
/*class GUIImportList extends JFrame {
    JButton yes = new JButton("&#x786E;&#x5B9A;");
    JButton no = new JButton("&#x7EE7;&#x7EED;&#x6DFB;&#x52A0;");
    JButton deleteFile =new JButton("x");
    JButton deleteFile2 =new JButton("x");
    JButton deleteFile3 =new JButton("x");
    GUIImportList(String[] fileName){

        JFrame frame = new JFrame("&#x5BFC;&#x5165;&#x5217;&#x8868;&#x9884;&#x89C8;");

        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JPanel panel3 = new JPanel(new FlowLayout());
        JPanel panel4 = new JPanel(new FlowLayout());

        JLabel label1 = new JLabel("--------&#x9700;&#x8981;&#x5BFC;&#x5165;&#x7684;&#x6587;&#x4EF6;--------");
        JLabel label = new JLabel("&middot; 1 :  ");

        JTextField textField = new JTextField(30);
        textField.setText(fileName[0]);
        textField.setEditable(false);


        JLabel label2 = new JLabel("&middot; 2 :  ");

        JTextField textField2 = new JTextField(30);
        textField2.setText(fileName[1]);
        textField2.setEditable(false);


        JLabel label3 = new JLabel("&middot; 3 :  ");

        JTextField textField3 = new JTextField(30);
        textField3.setText(fileName[2]);
        textField3.setEditable(false);

        panel1.add(label1);
        panel.add(label);
        panel.add(textField);
        panel.add(deleteFile);
        panel2.add(label2);
        panel2.add(textField2);
        panel2.add(deleteFile2);
        panel3.add(label3);
        panel3.add(textField3);
        panel3.add(deleteFile3);

        panel4.add(yes);
        panel4.add(no);
        container.add(panel1);
        container.add(panel);
        container.add(panel2);
        container.add(panel3);
        container.add(panel4);
        frame.pack();
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                for(int i=0;i<3;i++){
//                    System.out.println(fileName[i]);
//                }
                if(fileName[0]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[0]);
//                        for (DiscussionItem entry : DataManipulator.DiscussionList) {
//			                System.out.println(entry.GetText());
//			                System.out.println(entry.GetLabels().toString());
//		                }
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x5931;&#x8D25;&#xFF01;&#x8BF7;&#x8F93;&#x5165;&#x6B63;&#x786E;&#x7684;&#x6587;&#x4EF6;");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
                        System.out.println(entry.GetText());
                        System.out.println(entry.GetLabels().toString());
                    }
                    JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x6210;&#x529F;&#xFF01;");
                    GUIImport.str_ChooseFileName[0]=null;
                    frame.dispose();
                }
                else if(fileName[1]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[1]);
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x5931;&#x8D25;&#xFF01;&#x8BF7;&#x8F93;&#x5165;&#x6B63;&#x786E;&#x7684;&#x6587;&#x4EF6;");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x6210;&#x529F;&#xFF01;");
                    GUIImport.str_ChooseFileName[1]=null;
                    frame.dispose();
                }
                else if(fileName[2]!=null){
//                    try {
//                        StorageAccessor.LoadDiscussionFromCSV(fileName[2]);
//                    } catch (XPathExpressionException xPathExpressionException) {
//                        JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x5931;&#x8D25;&#xFF01;&#x8BF7;&#x8F93;&#x5165;&#x6B63;&#x786E;&#x7684;&#x6587;&#x4EF6;");
//                        xPathExpressionException.printStackTrace();
//                    }
                    StorageAccessor.LoadDiscussionFromCSV(fileName[0],"gbk");
                    JOptionPane.showMessageDialog(null, "&#x5BFC;&#x5165;&#x6210;&#x529F;&#xFF01;");
                    GUIImport.str_ChooseFileName[2]=null;
                    frame.dispose();
                }
                GUIImport.str_ChooseFileName=new String[3];//&#x5BFC;&#x5165;&#x6210;&#x529F;&#x4E4B;&#x540E;&#x6E05;&#x7A7A;&#x6570;&#x636E;
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        deleteFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[0]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
        deleteFile2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[1]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
        deleteFile3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileName[2]=null;
                frame.dispose();
                new GUIImportList(fileName);
            }
        });
    }
}*/
