
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
// �÷�����������һ���ļ�·�����Ƿ��ȡ��JSON�ļ�
// ��������ļ�Ϊ���ڵ�a.json�ļ�������ͨ����
// ��������ļ�Ϊ�����ڵ�b.json�ļ������Բ�ͨ����
    File file_a=new File("D:\\������\\�Ŷ���Ŀ����\\��Ԫ����\\ceshi3\\src\\main\\test\\a.json");//����һ��json�ļ�
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
// ��ʱ���ò���Ϊ���Գ�����ȡ���������Ƿ�����վ���û������۵�����
// ���籾����ȡ������Ϊ���й�ƽ������Ʊ�µ����ۣ������ȡ�����������۽��в���
    JSONArray comments=Ceshi.getcomments();
    Ceshi.jsonName(comments);
    //��������str�����е���ȡ��������ת������test_str��
    test_str=ceshi.getStr();
    String str1="�й�ƽ��";
    String str2="����֧�֣���ǹʵ����֧�֣�";
    String str3="�й�ƽ�������¶ȵĽ��ڣ����޶ȵ��µ���";
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
