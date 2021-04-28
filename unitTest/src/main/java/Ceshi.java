import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;


public class Ceshi {
    public static String str[]=new String[10];

    //读取json文件
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
            //输出评论数据（还未进行筛选）
            System.out.println(name);
        }
    }

    //获取构造数组
    public static JSONArray getcomments() {
        String s = readJsonFile("D:\\大三下\\团队项目开发\\单元测试\\ceshi3\\xueqiu.json");
        //将json文件中的数据读取出来
        JSONObject jobj = JSON.parseObject(s);
        //构建JSONArray数组使用下面
        JSONArray comment = jobj.getJSONArray("name");
        return comment;
    }
    //该方法用来给测试传递评论数据数组
    public static void jsonName(JSONArray users) {
        for (int i = 0 ; i < users.size();i++){
            JSONObject key = (JSONObject)users.get(i);
            String name = (String)key.get("comment");
            //将爬取到的评论储存在str数组中
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