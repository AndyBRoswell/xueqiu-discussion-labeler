import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;


public class Ceshi {
    public static String str[]=new String[10];

    //��ȡjson�ļ�
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"GBK");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        //String path = test.class.getClassLoader().getResource("xueqiu.json").getPath();
        JSONArray comments=getcomments();
        for (int i = 0 ; i < comments.size();i++){
            JSONObject key = (JSONObject)comments.get(i);
            String name = (String)key.get("comment");
            //����������ݣ���δ����ɸѡ��
            System.out.println(name);
        }
    }

    //��ȡ��������
    public static JSONArray getcomments() {
        String s = readJsonFile("D:\\������\\�Ŷ���Ŀ����\\��Ԫ����\\ceshi3\\xueqiu.json");
        //��json�ļ��е����ݶ�ȡ����
        JSONObject jobj = JSON.parseObject(s);
        //����JSONArray����ʹ������
        JSONArray comment = jobj.getJSONArray("name");
        return comment;
    }
    //�÷������������Դ���������������
    public static void jsonName(JSONArray users) {
        for (int i = 0 ; i < users.size();i++){
            JSONObject key = (JSONObject)users.get(i);
            String name = (String)key.get("comment");
            //����ȡ�������۴�����str������
            str[i]=name;
        }
        setStr(str);
    }

    public static void setStr(String[] str) {
        Ceshi.str = str;
    }

    public String[] getStr() {
        return Ceshi.str;
    }
}