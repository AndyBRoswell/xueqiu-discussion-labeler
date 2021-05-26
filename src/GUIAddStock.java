import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIAddStock extends JFrame {
    static String str_text;
    static String str_textPa="正在爬取";
    private static Logger logger=Logger.getLogger(GUIAddStock.class);
    static String str_gupiaoNum;
    static String str_gupiaoName="无";
    static String[][] rowData = new String[][]{
        {"无", "无", "待爬取"},
        {"无","无", "待爬取"}
    };
    JButton yes = new JButton("确定");
    JButton no = new JButton("取消");

    public GUIAddStock(){
        JFrame frame = new JFrame("添加");
        frame.setBounds(new Rectangle(500, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JLabel label = new JLabel("股票编号");
        JTextField textField = new JTextField(30);
        textField.setBounds(frame.getX() + 50, frame.getY() + 50, 200, 50);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                str_text=textField.getText();
                if(str_text.length()!=8)
                {
                    JOptionPane.showMessageDialog(null, "请输入正确的股票编号");
                }
                else{
                    JOptionPane.showMessageDialog(null, "股票代码输入成功！");
                    str_gupiaoNum=GUIAddStock.str_text;
                    String[][] rowData1 = {
                            {str_gupiaoNum, "无", str_textPa},
                            {"无","无", "待爬取"}
                    };
                    setRowData(rowData1);
                    frame.dispose();
                    MyThread myThread=new MyThread();
                    myThread.start();
                }
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel2.add(yes);
        panel2.add(no);
        panel.add(label);
        panel.add(textField);
        container.add(panel, BorderLayout.CENTER);
        container.add(panel2, BorderLayout.SOUTH);
        frame.pack();
    }

    public static void changeRowData() {
        if(str_gupiaoNum != null){
            String[][] rowData1 = {
                    {str_gupiaoNum, str_gupiaoName, str_textPa},
                    {"无","无", "待爬取"}
            };
            setRowData(rowData1);
        }
    }

    public static void setGupiao() {
        String[] aaa = new String[]{"py", Global.AppPath+"\\src\\xueqiuspider.py",str_gupiaoNum};
        try {
            Process proc = Runtime.getRuntime().exec(aaa);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                str_textPa="正在爬取";
            }
            in.close();
            str_textPa="爬取完毕";
            try {
                proc.waitFor();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTxtFile(String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                Pattern pattern1 = Pattern.compile(str_gupiaoNum);
                Matcher matcher1 = pattern1.matcher("");
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    matcher1.reset(lineTxt);
                    if (matcher1.find()) {
                        String result = matcher1.group();
                        //System.out.println("股票编号：" + result);
                        for(int i=lineTxt.indexOf(result)-2;i>0;i--){
                            String str_gupiaoName=lineTxt.substring(i, lineTxt.indexOf(result)-1);//截取股票名称
                            boolean status = str_gupiaoName.contains("$");
                            if(status){
                                return str_gupiaoName;
                                //break;
                            }
                        }
                        //break;
                    }
                }
                read.close();
            }else{
                logger.error("找不到指定的文件");
            }
        } catch (Exception e) {
            logger.error("读取文件内容出错");
            e.printStackTrace();
        }
        return "";
    }

    public static void setRowData(String[][] rowData) {
        GUIAddStock.rowData = rowData;
    }
    public static void setStr_gupiaoName(String str) {
        GUIAddStock.str_gupiaoName = str;
    }
}
class MyThread extends Thread {
    public void run() {
        show();
    }
    public void show() {
        GUIAddStock.setGupiao();
        String str_gupiaoName= GUIAddStock.readTxtFile(Global.AppPath+"\\xueqiu.csv");
        GUIAddStock.setStr_gupiaoName(str_gupiaoName);
        GUIAddStock.changeRowData();
    }
}
