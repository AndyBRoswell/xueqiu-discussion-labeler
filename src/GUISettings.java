import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUISettings extends JFrame {
	// 本窗体
	final GUISettings ThisForm = this;

	// 按钮
	final JPanel ButtonPanel = new JPanel(new GridLayout(1, 5));
	final JButton btnOK = new JButton("确定");
	final JButton btnCancel = new JButton("取消");
	final JButton btnApply = new JButton("应用");
	final JButton btnImportSettings = new JButton("导入设置");
	final JButton btnExportSettings = new JButton("导出设置");

	// 设置编辑区
	final JTextArea SettingsEditingArea = new JTextArea();

	// 文件对话框
	final JFileChooser ImportSettingsDialog = new JFileChooser(Global.ConfigPath);
	final JFileChooser ExportSettingsDialog = new JFileChooser(Global.ConfigPath);

	// 文件类型过滤器
	class XMLFilter extends FileFilter {
		@Override public boolean accept(File f) {
			final String extension = f.getName().substring(f.getName().lastIndexOf('.') + 1);
			if (extension != null) {
				return extension.equals("xml");
			}
			return false;
		}

		@Override public String getDescription() { return "XML Files"; }
	}

	// 加载设置窗口
	public GUISettings() {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(Screen.width / 3, Screen.height / 3);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocationRelativeTo(null);
		final GridBagLayout GUIImportFilesLayout = new GridBagLayout();
		super.getContentPane().setLayout(new GridBagLayout());

		final GridBagConstraints SettingsEditingAreaLayout = new GridBagConstraints();
		SettingsEditingAreaLayout.gridx = SettingsEditingAreaLayout.gridy = 0;
		SettingsEditingAreaLayout.weightx = 1; SettingsEditingAreaLayout.weighty = 0;
		SettingsEditingAreaLayout.fill = GridBagConstraints.BOTH;

		final GridBagConstraints ButtonPanelLayout = new GridBagConstraints();
		ButtonPanelLayout.gridx = 0; ButtonPanelLayout.gridy = 1;
		ButtonPanelLayout.weightx = 1; ButtonPanelLayout.weighty = 0;
		ButtonPanelLayout.fill = GridBagConstraints.BOTH;

		// 文件对话框的设置
		ImportSettingsDialog.setMultiSelectionEnabled(true); // 允许多选
		ImportSettingsDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ImportSettingsDialog.setFileFilter(new XMLFilter());
		ExportSettingsDialog.setMultiSelectionEnabled(true); // 允许多选
		ExportSettingsDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		ExportSettingsDialog.setFileFilter(new XMLFilter());

		// 动作监听程序
		btnOK.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try { Config.SaveConfig(Global.DefaultConfig); }
				catch (TransformerException transformerException) { transformerException.printStackTrace(); }
				ThisForm.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ThisForm.dispose();
			}
		});

		btnApply.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try { Config.SaveConfig(Global.DefaultConfig); }
				catch (TransformerException transformerException) { transformerException.printStackTrace(); }
			}
		});

		btnImportSettings.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ImportSettingsDialog.showOpenDialog(null);
			}
		});

		btnExportSettings.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ImportSettingsDialog.showSaveDialog(null);
			}
		});

		// 添加控件
		ButtonPanel.add(btnOK);
		ButtonPanel.add(btnCancel);
		ButtonPanel.add(btnApply);
		ButtonPanel.add(btnImportSettings);
		ButtonPanel.add(btnExportSettings);
		super.add(ButtonPanel, ButtonPanelLayout);
		super.add(SettingsEditingArea, SettingsEditingAreaLayout);

		// 显示
		super.setVisible(true);
	}
}
