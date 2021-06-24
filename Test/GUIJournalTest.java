import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.*;

public class GUIJournalTest {

    @Test
    public void txt2String() {
        File file = new File(Global.LogPath+"\\logs.log");
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            s = br.readLine();
            Assert.assertEquals("2021-05-19 22:01:52  [ GUIConfig.<init>(GUIConfig.java:25) ] - [ INFO ]    打开设置界面",s);
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}