import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GUIJournal extends JFrame {
    private static Logger logger=Logger.getLogger(GUIJournal.class);

    public GUIJournal(){
        logger.info("打开日志界面");
        JFrame frame = new JFrame("日志");
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextArea textArea = new JTextArea(25, 70);
        String string=txt2String(Global.LogPath+"/logs.log");
        textArea.setText(string);
        textArea.setEditable(false);
        panel.add(textArea);
        frame.add(panel);
        frame.setBounds(new Rectangle(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                logger.info("关闭日志界面");
            }
        });
    }
    public static String txt2String(String filename){
        File file = new File(filename);
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            while((s = br.readLine())!=null){
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }
}
