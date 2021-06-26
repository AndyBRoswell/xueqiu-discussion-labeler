import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GUIImportFiles extends JFrame {
	// 本窗体
	final GUIImportFiles ThisForm = this;

	// 按钮
	final JPanel ButtonPanel = new JPanel(new GridLayout(1, 6));
	final JButton btnConfirmImport = new JButton("确认导入");
	final JButton btnAddFiles = new JButton("添加文件");
	final JButton btnDeleteFiles = new JButton("删除文件");
	final JButton btnClearList = new JButton("清空列表");
//	final JLabel lbEncoding = new JLabel("文件编码：");
	final JComboBox cbEncoding = new JComboBox(Global.EncodingNames);

	// 导入的文件列表
	final ArrayList<String> ImportedFiles = new ArrayList<>();
	final FileTableModel FileListModel = new FileTableModel();
	final JTable FileList = new JTable(FileListModel);
	final JScrollPane FileListScrollPane = new JScrollPane(FileList);

	// 文件对话框
	final JFileChooser FileDialog = new JFileChooser(Global.DefaultSavePath);

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

	// 文件表格模型
	class FileTableModel extends AbstractTableModel {
		private final String[] FileListColumnNames = new String[]{ "路径名" };

		@Override public int getRowCount() { return ImportedFiles.size(); }

		@Override public int getColumnCount() { return 1; }

		@Override public String getColumnName(int col) { return FileListColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) { return ImportedFiles.get(rowIndex); }

		@Override public boolean isCellEditable(int row, int col) { return false; }

		@Override public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	}

	// 加载导入文件窗口
	public GUIImportFiles() throws XPathExpressionException {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(Screen.width / 3, Screen.height / 3);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocationRelativeTo(null);
		final GridBagLayout GUIImportFilesLayout = new GridBagLayout();
		super.getContentPane().setLayout(new GridBagLayout());

		final GridBagConstraints TableLayout = new GridBagConstraints();
		TableLayout.gridx = TableLayout.gridy = 0;
		TableLayout.weightx = TableLayout.weighty = 1;
		TableLayout.fill = GridBagConstraints.BOTH;

		final GridBagConstraints ButtonPanelLayout = new GridBagConstraints();
		ButtonPanelLayout.gridx = 0; ButtonPanelLayout.gridy = 1;
		ButtonPanelLayout.weightx = 1; ButtonPanelLayout.weighty = 0;
		ButtonPanelLayout.fill = GridBagConstraints.BOTH;

		// 文件对话框的设置
		FileDialog.setMultiSelectionEnabled(true); // 允许多选
		FileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileDialog.setFileFilter(new CSVFilter());

		// 默认编码
		cbEncoding.setSelectedIndex(Arrays.asList(Global.EncodingNames).indexOf(Config.QuerySingleConfigEntry("/config/storage/import-and-export/default-import-encoding")));

		// 动作监听程序
		btnConfirmImport.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				for (String Pathname : ImportedFiles) {
					StorageAccessor.LoadDiscussionFromCSV(Pathname, (String) cbEncoding.getSelectedItem());
					Global.MainForm.ShowAllDiscussions();
				}
				ThisForm.dispose();
			}
		});

		btnAddFiles.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				int ret = FileDialog.showDialog(null, "导入股票讨论 CSV 文件");
				if (ret == JFileChooser.APPROVE_OPTION) { // 在对话框中选择了 “确定”
					File[] SelectedFiles = FileDialog.getSelectedFiles();
					for (File EachFile : SelectedFiles) {
						ImportedFiles.add(EachFile.getAbsolutePath());
					}
				}
				FileListModel.fireTableDataChanged();
			}
		});

		btnDeleteFiles.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				int[] Rows = FileList.getSelectedRows();
				for (int i = Rows.length - 1; i >= 0; --i) {
					ImportedFiles.remove(i);
				}
				FileListModel.fireTableDataChanged();
			}
		});

		btnClearList.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				ImportedFiles.clear();
				FileListModel.fireTableDataChanged();
			}
		});

		// 添加控件
		ButtonPanel.add(btnConfirmImport);
		ButtonPanel.add(btnAddFiles);
		ButtonPanel.add(btnDeleteFiles);
		ButtonPanel.add(btnClearList);
//		ButtonPanel.add(lbEncoding);
		ButtonPanel.add(cbEncoding);
		super.add(ButtonPanel, ButtonPanelLayout);
		super.add(FileListScrollPane, TableLayout);

		// 显示
		super.setVisible(true);
	}
}
