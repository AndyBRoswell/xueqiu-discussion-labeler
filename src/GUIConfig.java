import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class GUIConfig extends JFrame {

    ArrayList<String> list = getConfig();

    public GUIConfig(){
            JButton buttonTrue=new JButton("确定");
            JButton buttonFalse=new JButton("取消");
            JFrame ConfigFrame = new JFrame("设置");
            ConfigFrame.setSize(1000,550);
            ConfigFrame.setLocation(600,300);
            ConfigFrame.setLayout(null);
            ConfigFrame.setResizable(false);
            JLabel label_crawing=new JLabel("爬取—crawling-----------------------------------------------------------");
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
            JTextField jtf_encoding=new JTextField(list.get(3));
            JTextField jtf_filename=new JTextField(list.get(4));
            JTextField jtf_export=new JTextField(list.get(5));

            JLabel label_backup=new JLabel("备份恢复—backup-and-restore----------");
            JLabel label_name1=new JLabel(list.get(7));
            JLabel label_age1=new JLabel(list.get(8));
            JLabel label_sex1=new JLabel(list.get(9));

            label_crawing.setBounds(20,20,500,30);
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

            buttonTrue.setBounds(300,450,80,40);
            buttonFalse.setBounds(600,450,80,40);

            ConfigFrame.add(label_crawing);
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
            ConfigFrame.setVisible(true);
            ConfigFrame.add(jtf_encoding);
            ConfigFrame.add(jtf_filename);
            ConfigFrame.add(jtf_export);
            ConfigFrame.add(jcb);
            ConfigFrame.add(jcb1);
            ConfigFrame.add(label_name1);
            ConfigFrame.add(label_age1);
            ConfigFrame.add(label_sex1);

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
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (XPathExpressionException e) {
                        e.printStackTrace();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "修改成功！");
                    ConfigFrame.setVisible(false);
                    getConfig();
                    new GUIConfig();
                }
            });

            buttonFalse.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ConfigFrame.setVisible(false);
                }
            });
    }

    private static ArrayList<String> getConfig() {
        ArrayList<String> list=new ArrayList<>();
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document doc=builder.parse("file:///"+Global.DefaultConfig); //这个是正确的
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
