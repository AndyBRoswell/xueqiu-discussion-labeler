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
	private static Logger logger = Logger.getLogger(GUIJournal.class);

	public GUIJournal() {
		logger.info("打开日志界面");
//		JFrame frame = new JFrame("日志");
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JTextArea textArea = new JTextArea(25, 70);
		String string = txt2String(Global.LogPath + "/logs.log");
		textArea.setText(string);
		textArea.setEditable(false);
		panel.add(textArea);
		super.add(panel);
		super.setBounds(new Rectangle(800, 500));
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		super.pack();
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("关闭日志界面");
			}
		});
		super.setTitle("日志");
		super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static String txt2String(String filename) {
		File file = new File(filename);
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
