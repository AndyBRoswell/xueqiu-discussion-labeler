import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class GUIExportFiles extends JFrame {
	// 按钮
	final JPanel ButtonPanel = new JPanel(new GridLayout(1, 4));
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");
	final JButton btnBrowse = new JButton("浏览");
	final JComboBox cbEncoding = new JComboBox(Global.EncodingNames);

	// 路径框
	final JTextField PathnameBox = new JTextField();

	// 文件对话框
	

	// 文件类型过滤器
	class CSVFilter extends FileFilter {
		@Override public boolean accept(File f) {
			final String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1);
			if (extension != null) {
				return extension.equals("csv");
			}
			return false;
		}

		@Override public String getDescription() { return "CSV Files"; }
	}

	// 加载导出文件窗口
	public GUIExportFiles() {

	}
}
