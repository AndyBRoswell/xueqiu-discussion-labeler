import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

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
	final JFileChooser SaveDialog = new JFileChooser(Global.DefaultSavePath);

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
	public GUIExportFiles() throws XPathExpressionException {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(Screen.width / 3, Screen.height / 3);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocationRelativeTo(null);
		final GridBagLayout GUIImportFilesLayout = new GridBagLayout();
		super.getContentPane().setLayout(new GridBagLayout());

		final GridBagConstraints PathnameBoxLayout = new GridBagConstraints();
		PathnameBoxLayout.gridx = PathnameBoxLayout.gridy = 0;
		PathnameBoxLayout.weightx = PathnameBoxLayout.weighty = 0;
		PathnameBoxLayout.fill = GridBagConstraints.HORIZONTAL;

		final GridBagConstraints ButtonPanelLayout = new GridBagConstraints();
		ButtonPanelLayout.gridx = 0; ButtonPanelLayout.gridy = 1;
		ButtonPanelLayout.weightx = 1; ButtonPanelLayout.weighty = 0;
		ButtonPanelLayout.fill = GridBagConstraints.HORIZONTAL;

		// 文件对话框的设置
		SaveDialog.setMultiSelectionEnabled(false);
		SaveDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		SaveDialog.setFileFilter(new CSVFilter());

		// 默认编码
		cbEncoding.setSelectedIndex(Arrays.asList(Global.EncodingNames).indexOf(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-export-encoding")));

		// 动作监听程序


		// 添加控件
		ButtonPanel.add(btnOK);
		ButtonPanel.add(btnCancel);
		ButtonPanel.add(btnBrowse);
		ButtonPanel.add(cbEncoding);
		super.add(ButtonPanel, ButtonPanelLayout);
		super.add(PathnameBox, PathnameBoxLayout);

		// 显示
		super.setVisible(true);
	}
}
