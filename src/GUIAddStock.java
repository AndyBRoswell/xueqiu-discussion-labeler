import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIAddStock extends JFrame {
    static String str_text;
    static String[] str_textPa=new String[2];

    private static Logger logger=Logger.getLogger(GUIAddStock.class);
    static String[] str_gupiaoNum=new String[2];
    static String[] str_gupiaoName=new String[2];
    static String[] dataFile=new String[2];
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
                    if(str_gupiaoNum[0] == null){
                        JOptionPane.showMessageDialog(null, "第一支股票代码输入成功！");
                        str_gupiaoNum[0]=GUIAddStock.str_text;
                        str_textPa[0]="正在爬取";
                        str_gupiaoName[0] = "无";
                        String[][] rowData1 = {
                                {str_gupiaoNum[0], str_gupiaoName[0], str_textPa[0]},
                                {"无","无", "待爬取"}
                        };
                        setRowData(rowData1);
                        frame.dispose();
                        //flag[0]="未中断";
                        MyThread1 myThread1=new MyThread1();
                        myThread1.start();
                    }
                    else if((str_textPa[0].equals("正在爬取")||str_textPa[0].equals("爬取完毕")) && str_gupiaoNum[1] == null){
                        JOptionPane.showMessageDialog(null, "第二支股票代码输入成功！");
                        str_gupiaoNum[1]=GUIAddStock.str_text;
                        str_textPa[1]="正在爬取";
                        str_gupiaoName[1] = "无";
                        String[][] rowData2 = {
                                {str_gupiaoNum[0], str_gupiaoName[0], str_textPa[0]},
                                {str_gupiaoNum[1], str_gupiaoName[1], str_textPa[1]}
                        };
                        setRowData(rowData2);
                        frame.dispose();
                        //flag[1]="未中断";
                        MyThread2 myThread2=new MyThread2();
                        myThread2.start();
//                        if(flag[1].equals("中断")){
//                            //myThread2.interrupt();
//                            File file2=new File(Global.AppPath+"\\"+GUIAddStock.dataFile[1]);
//                            if(file2.exists()){
//                                if(file2.delete()){
//                                    JOptionPane.showMessageDialog(null,"任务取消成功！");
//                                }
//                            }
//                        }
                    }
                    else if((str_textPa[0].equals("正在爬取")||str_textPa[0].equals("爬取完毕")) && (str_textPa[1].equals("正在爬取")||str_textPa[1].equals("爬取完毕"))) {
                        JOptionPane.showMessageDialog(null, "目前任务列表中已经存在两个任务！");
                    }
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

    public static void changeRowData(String[] gupiaoNum,String[] gupiaoName,String[] textPa) {
        if(gupiaoNum != null){
            String[][] rowData1 = {
                    {gupiaoNum[0], gupiaoName[0], textPa[0]},
                    {gupiaoNum[1],gupiaoName[1], textPa[1]}
            };
            setRowData(rowData1);
        }
    }

    public static void setGupiao(String str_gupiaoNum,String dataFile) {
        String[] aaa = new String[]{"py", Global.AppPath+"\\src\\xueqiuspider.py",dataFile,str_gupiaoNum};
        try {
            Process proc = Runtime.getRuntime().exec(aaa);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
            }
            in.close();
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

    public static String readTxtFile(String gupiaoNum,String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                Pattern pattern1 = Pattern.compile(gupiaoNum);
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
}
class MyThread1 extends Thread {
    public void run() {
        show();
    }
    public void show() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        GUIAddStock.dataFile[0]=dateTime.format(formatter)+"-"+GUIAddStock.str_gupiaoNum[0]+".csv";

        GUIAddStock.setGupiao(GUIAddStock.str_gupiaoNum[0],GUIAddStock.dataFile[0]);
        GUIAddStock.str_textPa[0]="爬取完毕";
        //GUIAddStock.str_textPa[1]="待爬取";
        String str_gupiaoName= GUIAddStock.readTxtFile(GUIAddStock.str_gupiaoNum[0],Global.AppPath+"\\"+ GUIAddStock.dataFile[0]);
        GUIAddStock.str_gupiaoName[0] = str_gupiaoName;
        //GUIAddStock.str_gupiaoName[1] = "无";
        //GUIAddStock.str_gupiaoNum[1]="无";
        GUIAddStock.changeRowData(GUIAddStock.str_gupiaoNum,GUIAddStock.str_gupiaoName,GUIAddStock.str_textPa);
    }
}
class MyThread2 extends Thread {
    public void run() {
        show();
    }
    public void show() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        GUIAddStock.dataFile[1]=dateTime.format(formatter)+"-"+GUIAddStock.str_gupiaoNum[1]+".csv";
        GUIAddStock.setGupiao(GUIAddStock.str_gupiaoNum[1],GUIAddStock.dataFile[1]);
        GUIAddStock.str_textPa[1]="爬取完毕";
        String str_gupiaoName= GUIAddStock.readTxtFile(GUIAddStock.str_gupiaoNum[1],Global.AppPath+"\\"+GUIAddStock.dataFile[1]);
        GUIAddStock.str_gupiaoName[1] = str_gupiaoName;
        //System.out.println(GUIAddStock.str_gupiaoNum[1]+GUIAddStock.str_gupiaoName[1]+GUIAddStock.str_textPa[1]);
        GUIAddStock.changeRowData(GUIAddStock.str_gupiaoNum,GUIAddStock.str_gupiaoName,GUIAddStock.str_textPa);
    }
}
