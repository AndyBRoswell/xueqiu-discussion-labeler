import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class GUIConfig extends JFrame {
    private static Logger logger=Logger.getLogger(GUIConfig.class);
    String str_ChooseFileName;
    ArrayList<String> list ;

    public GUIConfig(String defaultConfig){
            logger.info("打开设置界面");
            list = getConfig(defaultConfig);
            JButton buttonTrue=new JButton("确定");
            JButton buttonFalse=new JButton("取消");
            JButton buttonImport=new JButton("导入");
            JButton buttonExport=new JButton("导出");
            JButton buttonChoose=new JButton("浏览");
            JFrame ConfigFrame = new JFrame("设置");
            ConfigFrame.setSize(1000,620);
            ConfigFrame.setLocation(600,300);
            ConfigFrame.setLayout(null);
            ConfigFrame.setResizable(false);
            JLabel label_crawing=new JLabel("爬取—crawling-----------------------------------------------------------");
            JLabel label_file=new JLabel("另存为配置文件：");
            JLabel label_proxy=new JLabel("代理—proxy----------");
            String []rename= {"自动重命名","手动重命名","直接覆盖","弹窗提示"};
            JComboBox jcb=new JComboBox(rename);

            String []encode= {"UTF-8","UTF-16 LE","GB18030"};
            JComboBox jcb1=new JComboBox(encode);

            JLabel label_name=new JLabel(list.get(0));
            JLabel label_age=new JLabel(list.get(1));
            JLabel label_sex=new JLabel(list.get(2));

            JLabel label_storage=new JLabel("存储—storage-------------------------------------------------------------");
            JLabel label_import=new JLabel("导出导入—import-and-export----------");
            JLabel label_encoding=new JLabel("默认编码: ");
            JLabel label_filename=new JLabel("默认文件名(当前时间): ");
            JLabel label_export=new JLabel("默认文件目录: ");
            JLabel label_operation=new JLabel("文件名冲突操作: ");

            JTextField jtf_imexportfile=new JTextField(Global.ConfigPath+"\\ceshi.xml");

            JTextField jtf_encoding=new JTextField(list.get(3));
            JTextField jtf_filename=new JTextField(list.get(4));
            JTextField jtf_export=new JTextField(list.get(5));

            JLabel label_backup=new JLabel("备份恢复—backup-and-restore----------");
            JLabel label_name1=new JLabel(list.get(7));
            JLabel label_age1=new JLabel(list.get(8));
            JLabel label_sex1=new JLabel(list.get(9));

            label_crawing.setBounds(20,20,500,30);
            label_file.setBounds(50,440,100,30);
            label_proxy.setBounds(50,70,500,30);
            label_name.setBounds(70,110,100,30);
            label_age.setBounds(380,110,100,30);
            label_sex.setBounds(700,110,100,30);
            label_storage.setBounds(20,170,500,30);
            label_import.setBounds(50,220,500,30);

            label_encoding.setBounds(70,260,100,30);
            label_filename.setBounds(500,260,200,30);
            label_export.setBounds(70,300,100,30);
            label_operation.setBounds(500,300,200,30);

            jcb1.setBounds(200,260,150,30);
            jtf_filename.setBounds(630,260,200,30);
            jtf_export.setBounds(200,300,200,30);
            jcb.setBounds(630,300,150,30);

            label_backup.setBounds(50,340,500,30);
            label_name1.setBounds(70,380,100,30);
            label_age1.setBounds(380,380,100,30);
            label_sex1.setBounds(700,380,100,30);

            jtf_imexportfile.setBounds(150,440,450,30);

            buttonTrue.setBounds(300,520,80,40);
            buttonFalse.setBounds(600,520,80,40);
            buttonImport.setBounds(720,440,60,30);
            buttonExport.setBounds(790,440,60,30);
            buttonChoose.setBounds(605,440,60,30);

            ConfigFrame.add(label_crawing);
            ConfigFrame.add(label_file);
            ConfigFrame.add(label_proxy);
            ConfigFrame.add(label_name);
            ConfigFrame.add(label_age);
            ConfigFrame.add(label_sex);
            ConfigFrame.add(label_storage);
            ConfigFrame.add(label_import);
            ConfigFrame.add(label_encoding);
            ConfigFrame.add(label_filename);
            ConfigFrame.add(label_export);
            ConfigFrame.add(label_operation);
            ConfigFrame.add(label_backup);
            ConfigFrame.add(buttonTrue);
            ConfigFrame.add(buttonFalse);
            ConfigFrame.add(buttonImport);
            ConfigFrame.add(buttonExport);
            ConfigFrame.add(buttonChoose);
            ConfigFrame.setVisible(true);
            ConfigFrame.add(jtf_encoding);
            ConfigFrame.add(jtf_filename);
            ConfigFrame.add(jtf_export);
            ConfigFrame.add(jtf_imexportfile);
            ConfigFrame.add(jcb);
            ConfigFrame.add(jcb1);
            ConfigFrame.add(label_name1);
            ConfigFrame.add(label_age1);
            ConfigFrame.add(label_sex1);
        ConfigFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                logger.info("关闭设置界面");
            }
        });


            buttonTrue.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    String str_encoding=(String) jcb1.getSelectedItem();
                    String str_filename=jtf_filename.getText();
                    String str_export=jtf_export.getText();
                    String str_operation=(String) jcb.getSelectedItem();
                    try {
                        changeConfig(str_encoding,str_filename,str_export,str_operation);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("数据输入输出流出现问题");
                    } catch (SAXException e) {
                        e.printStackTrace();
                        logger.error("数据SAX出现问题");
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                        logger.error("数据文件路径出现问题");
                    } catch (TransformerException e) {
                        e.printStackTrace();
                        logger.error("数据传输出现问题");
                    }
                    JOptionPane.showMessageDialog(null, "修改成功！");
                    logger.info("成功修改设置信息");
                    ConfigFrame.setVisible(false);
                    new GUIConfig(Global.DefaultConfig);
                }
            });

            buttonFalse.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ConfigFrame.setVisible(false);
                    logger.info("关闭设置界面");
                }
            });
        //浏览的按钮,选择文件
        buttonChoose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.showDialog(new JLabel(), "选择");
                File file = chooser.getSelectedFile();

                jtf_imexportfile.setText(file.getAbsoluteFile().toString());
                String string=jtf_imexportfile.getText();
                getChooseFileName(string);
            }
        });
        //导入的按钮
        buttonImport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if(str_ChooseFileName == null)
                {
                    JOptionPane.showMessageDialog(null, "导入失败，未选择文件路径！");
                    logger.error("导入失败，未选择文件路径");
                }
                else {
                    JOptionPane.showMessageDialog(null, "导入成功！");
                    ConfigFrame.setVisible(false);
                    //System.out.println(str_ChooseFileName);
                    //显示加载保存的配置文件
                    new GUIConfig(str_ChooseFileName);
                    logger.info("导入配置文件成功");
                }
            }
        });
        //导出的按钮
        buttonExport.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String str_encoding=(String) jcb1.getSelectedItem();
                String str_filename=jtf_filename.getText();
                String str_export=jtf_export.getText();
                String str_operation=(String) jcb.getSelectedItem();
                String xmlStr = "<config>\n" +
                        "\t<crawling>\n" +
                        "\t\t<proxy>\n" +
                        "\t\t\t<name>张三</name>\n" +
                        "\t\t\t<sex>男</sex>\n" +
                        "\t\t\t<age>20</age>\n" +
                        "\t\t</proxy>\n" +
                        "\t</crawling>\n" +
                        "\t<storage>\n" +
                        "\t\t<import-and-export>\n" +
                        "\t\t\t<default-encoding>"+str_encoding+"</default-encoding>\n" +
                        "\t\t\t<default-filename>"+str_filename+"</default-filename>\n" +
                        "\t\t\t<default-export-dir>"+str_export+"</default-export-dir>\n" +
                        "\t\t\t<operation-for-filename-conflict>"+str_operation+"</operation-for-filename-conflict>\n" +
                        "\t\t</import-and-export>\n" +
                        "\t\t<backup-and-restore>\n" +
                        "\t\t\t<name>张三</name>\n" +
                        "\t\t\t<sex>男</sex>\n" +
                        "\t\t\t<age>20</age>\n" +
                        "\t\t</backup-and-restore>\n" +
                        "\t</storage>\n" +
                        "</config>";
                StringReader sr = new StringReader(xmlStr);
                InputSource is = new InputSource(sr);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder= null;

                try {
                    builder = factory.newDocumentBuilder();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    logger.error("数据配置出现问题");
                }
                try {
                    Document doc = builder.parse(is);
                } catch (SAXException e) {
                    e.printStackTrace();
                    logger.error("数据SAX出现问题");
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("数据输入输出流出现问题");
                }

                String str_imexport=jtf_imexportfile.getText();
                getChooseFileName(str_imexport);
                String filename = str_ChooseFileName;
                //暂定为cfg下的ceshi.xml
                //String de_filename = Global.ConfigPath+"\\ceshi.xml";

                    BufferedWriter bfw = null;
                    try {
                        bfw = new BufferedWriter(new FileWriter(new File(filename)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("数据输入输出流出现问题");

                    }
                    try {
                        bfw.write(xmlStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("数据输入输出流出现问题");
                    }
                    try {
                        bfw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("数据输入输出流出现问题");
                    }
                    JOptionPane.showMessageDialog(null, "导出成功！");
                logger.info("导出配置文件成功");
            }
        });
    }

    private void getChooseFileName(String string) {
        this.str_ChooseFileName=string;
    }

    private static ArrayList<String> getConfig(String config_file) {
        ArrayList<String> list=new ArrayList<>();
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document doc=builder.parse("file:///"+config_file);
            printCrawling(doc,list);
            printStorage(doc,list);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }


    private static void changeConfig(String str_encoding,String str_filename, String str_export, String str_operation)
            throws IOException, SAXException, XPathExpressionException, TransformerException {
        Config.LoadConfig(Global.DefaultConfig);
        //System.out.println(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-export-dir"));
        Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-encoding", str_encoding);
        if(str_filename.equals("//datetime//")){
//            LocalDateTime dateTime = LocalDateTime.now();
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
//            Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-filename", dateTime.format(formatter));
        }
        else {
            Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-filename", str_filename);
        }
        if(str_export.equals("//default-save-path//")){
//            Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-export-dir", Global.DefaultSavePath);
        }
        else {
            Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-export-dir", str_export);
        }


        Config.ModifySingleConfigEntry("/config/storage/import-and-export/operation-for-filename-conflict", str_operation);
        //System.out.println(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-export-dir"));
        Config.SaveConfig(Global.DefaultConfig);
    }

    private static void printCrawling(Document doc, ArrayList<String> list) {
        NodeList nodelist=doc.getElementsByTagName("crawling");
        Element element=(Element)nodelist.item(0);
        NodeList nodelists =element.getElementsByTagName("proxy");
        for(int i=0;i<nodelists.getLength();i++){
            Element e=(Element)nodelists.item(i);
            list.add("name:     "+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            //printNode(e.getElementsByTagName("name").item(0));
            list.add("age:      "+e.getElementsByTagName("age").item(0).getFirstChild().getNodeValue());
            list.add("sex:      "+e.getElementsByTagName("sex").item(0).getFirstChild().getNodeValue());
        }
    }
    private static void printStorage(Document doc, ArrayList<String> list) {
        NodeList nodelist=doc.getElementsByTagName("storage");
        Element element=(Element)nodelist.item(0);
        NodeList nodelists_import =element.getElementsByTagName("import-and-export");
        NodeList nodelists_backup =element.getElementsByTagName("backup-and-restore");
        for(int i=0;i<nodelists_import.getLength();i++){
            Element e=(Element)nodelists_import.item(i);
            list.add(e.getElementsByTagName("default-encoding").item(0).getFirstChild().getNodeValue());
            list.add(e.getElementsByTagName("default-filename").item(0).getFirstChild().getNodeValue());
            list.add(e.getElementsByTagName("default-export-dir").item(0).getFirstChild().getNodeValue());
            list.add(e.getElementsByTagName("operation-for-filename-conflict").item(0).getFirstChild().getNodeValue());
        }

        for(int i=0;i<nodelists_backup.getLength();i++){
            Element e=(Element)nodelists_backup.item(i);
            list.add("name: "+e.getElementsByTagName("name").item(0).getFirstChild().getNodeValue());
            //printNode(e.getElementsByTagName("name").item(0));
            list.add("age: "+e.getElementsByTagName("age").item(0).getFirstChild().getNodeValue());
            list.add("sex: "+e.getElementsByTagName("sex").item(0).getFirstChild().getNodeValue());
        }
    }
    //识别属性的方法
    private static void printNode(Node name) {
        NamedNodeMap nodemap=name.getAttributes();
        for(int i=0;i<nodemap.getLength();i++){
            Node e=nodemap.item(i);
            System.out.println(e.getNodeName()+" : "+e.getFirstChild().getNodeValue());
        }
    }
}
