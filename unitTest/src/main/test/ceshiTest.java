
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.File;


/** 
* ceshi Tester. 
* 
* @author <Authors name> 
* @since <pre>04/27/2021</pre> 
* @version 1.0 
*/ 
public class ceshiTest {
    Ceshi ceshi;
    boolean flag;
    public static String[] test_str =new String[10];
@Before
public void Setup() throws Exception {
    ceshi=new Ceshi();
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: readJsonFile(String fileName) 
* 
*/ 
@Test

public void testReadJsonFile() throws Exception { 
//TODO: Test goes here...
// 该方法测试输入一个文件路径，是否获取到JSON文件
// 若输入的文件为存在的a.json文件，测试通过；
// 若输入的文件为不存在的b.json文件，测试不通过；
    File file_a=new File("D:\\大三下\\团队项目开发\\单元测试\\ceshi3\\src\\main\\test\\a.json");//任意一个json文件
    //String path=file_a.getCanonicalPath();
    String str=ceshi.readJsonFile(file_a.getPath());
    if(JSON.parseObject(str)!=null){
        flag = true;
    }
    else
        flag=false;
    Assert.assertEquals(flag,true);
} 

/** 
* 
* Method: main(String[] args) 
* 
*/ 
@Test
public void testMain() throws Exception { 
//TODO: Test goes here...
// 暂时设置测试为测试初步爬取出的数据是否有网站上用户所评论的内容
// 例如本次爬取的评论为“中国平安”股票下的评论，随机抽取其中三条评论进行测试
    JSONArray comments=Ceshi.getcomments();
    Ceshi.jsonName(comments);
    //将储存在str数组中的爬取到的评论转到数组test_str中
    test_str=ceshi.getStr();
    String str1="中国平安";
    String str2="继续支持！荷枪实弹的支持！";
    String str3="中国平安：有温度的金融，无限度的下跌！";
    for(int i=0;i<test_str.length;i++){
        String str=test_str[i];
        if(str.contains(str1)||str.contains(str2)||str.contains(str3))
            flag=true;
        else
            flag=false;
    }
    Assert.assertEquals(flag,true);
}
} 
