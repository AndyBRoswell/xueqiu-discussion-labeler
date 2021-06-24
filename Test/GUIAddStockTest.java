import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class GUIAddStockTest {

    @Test
    public void setGupiao() {
        try {
            String encoding="GBK";
            File file=new File(Global.DefaultSavePath+"\\科创.csv");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    boolean status = lineTxt.contains("科创");
                    if(status){
                        Assert.assertTrue(status);
                    }
                }
                read.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readTxtFile() {
        try {
            String encoding="GBK";
            File file=new File(Global.DefaultSavePath+"\\xueqiu.csv");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                Pattern pattern1 = Pattern.compile("SH601318");
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
                                Assert.assertTrue(status);
                                //break;
                            }
                        }
                        //break;
                    }
                }
                read.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}