import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GUIConfigTest {

    @Test
    public void getConfig() {
        ArrayList<String> list=new ArrayList<>();
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document doc=builder.parse("file:///"+Global.DefaultConfig);
            GUIConfig.printStorage(doc,list);
            boolean status=list.get(0).contains("备用");
            if(status){
                Assert.assertTrue(status);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void changeConfig() {
        try {
            Config.LoadConfig(Global.DefaultConfig);
            try {
                Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-encoding", "gbk");
                Config.ModifySingleConfigEntry("/config/storage/import-and-export/default-encoding", "utf-8");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void printStorage() {
        ArrayList<String> list=new ArrayList<>();
        DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder= factory.newDocumentBuilder();
            Document doc=builder.parse("file:///"+Global.DefaultConfig);
            GUIConfig.printStorage(doc,list);
            NodeList nodelist=doc.getElementsByTagName("storage");
            Element element=(Element)nodelist.item(0);
            NodeList nodelists_import =element.getElementsByTagName("import-and-export");
            for(int i=0;i<nodelists_import.getLength();i++){
                Element e=(Element)nodelists_import.item(i);
                list.add(e.getElementsByTagName("default-encoding").item(0).getFirstChild().getNodeValue());
                list.add(e.getElementsByTagName("default-filename").item(0).getFirstChild().getNodeValue());
                list.add(e.getElementsByTagName("default-export-dir").item(0).getFirstChild().getNodeValue());
                list.add(e.getElementsByTagName("operation-for-filename-conflict").item(0).getFirstChild().getNodeValue());
            }
            Assert.assertEquals("utf-8",list.get(0));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}