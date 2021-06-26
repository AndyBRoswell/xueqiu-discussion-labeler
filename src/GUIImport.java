import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class GUIImport extends JFrame {
	private static final Logger logger = Logger.getLogger(GUIImport.class);
	final JButton buttonYes = new JButton("确定");
	final JButton buttonNo = new JButton("取消");
	final JButton buttonChooseFile = new JButton("浏览");
	static final ArrayList<String> list = new ArrayList<String>();
	static int i;

	public GUIImport() {
		final JFrame ImportFrame = new JFrame("导入");
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		ImportFrame.setBounds(new Rectangle(Screen.width / 3, Screen.height / 3));
		ImportFrame.setLocationRelativeTo(null);
		ImportFrame.setVisible(true);
		Container ImportFrameContainer = ImportFrame.getContentPane();
		ImportFrameContainer.setLayout(new BoxLayout(ImportFrameContainer, BoxLayout.Y_AXIS));
		JPanel panel = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel(new FlowLayout());
		JLabel label = new JLabel("导入文件:");
		JTextField textField = new JTextField(30);
		panel.add(label);
		panel.add(textField);
		panel.add(buttonChooseFile);
		panel2.add(buttonYes);
		panel2.add(buttonNo);
		ImportFrameContainer.add(panel);
		ImportFrameContainer.add(panel2);
		ImportFrame.pack();
		buttonYes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String string = textField.getText();
				if (string.equals("")) {
					JOptionPane.showMessageDialog(null, "导入失败！\n请选择正确的文件");
				}
				else {
					setChooseFileName(string);
					ImportFrame.dispose();

					textField.setText("");
				}
			}
		});
		buttonNo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportFrame.dispose();
			}
		});
		//浏览的按钮,选择文件
		buttonChooseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fileChoose();
			}
		});

	}

	public static void fileChoose() {
		JFileChooser chooser = new JFileChooser(Global.DefaultSavePath);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnval = chooser.showDialog(new JLabel(), "选择");
		if (returnval == JFileChooser.APPROVE_OPTION) {
			File[] files = chooser.getSelectedFiles();
			String str = "";
			for (File file : files) {
				if (file.isDirectory())
					str = file.getPath();
				else {
					str = file.getAbsoluteFile().toString();
					list.add(str);
					i = list.indexOf(str) + 1;
					GUIImportList.FileListModel.addRow(new Object[]{ GUIImport.i, GUIImport.list.get(GUIImport.i - 1) });
				}
			}
		}

	}

	private void setChooseFileName(String string) {
		list.add(string);
		i = list.indexOf(string) + 1;
		//System.out.println(i);
	}

}

class GUIImportList extends JFrame {
	//操作面板，用于放置增删改按钮
	private final JPanel controlPanel;
	private final JPanel controlPanel1;
	public static DefaultTableModel FileListModel;

	// 已导入的文件列表
	public static Object[][] datas;
	private JTable FileListTable;
	private JScrollPane FileListScrollPane;

	// 按钮
	private final JButton DeleteButton = new JButton("删除导入");
	private final JButton ConfirmButton = new JButton("确认导入");
	private final JButton AddButton = new JButton("添加任务");
	private final JButton ClearButton = new JButton("全部清空");

	// 主窗体
	final JFrame frame = new JFrame("导入列表");

	public GUIImportList() {
		frame.setBounds(580, 280, 500, 500);
		final String[] head = { "序号", "导入文件" };

		//datas = new Object[][]{{GUIImport.i, filename}};

		FileListModel = new DefaultTableModel(datas, head);
		FileListTable = new JTable(FileListModel);
		FileListScrollPane = new JScrollPane(FileListTable);

		controlPanel1 = new JPanel();
		controlPanel1.add(DeleteButton);
		controlPanel1.add(ClearButton);

		controlPanel = new JPanel();
		controlPanel.add(AddButton);
		controlPanel.add(ConfirmButton);

		frame.add(controlPanel1, BorderLayout.NORTH);
		frame.add(controlPanel, BorderLayout.SOUTH);
		frame.add(FileListScrollPane, BorderLayout.CENTER);

		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		FileListTable.getColumn("序号").setCellRenderer(render);
		FileListTable.getColumn("导入文件").setCellRenderer(render);

		//设置行宽
		DefaultTableColumnModel dcm = (DefaultTableColumnModel) FileListTable.getColumnModel();
		dcm.getColumn(0).setPreferredWidth(60); //设置表格显示的最好宽度，即此时表格显示的宽度。
		dcm.getColumn(0).setMinWidth(45);//设置表格通过拖动列可以的最小宽度。
		dcm.getColumn(0).setMaxWidth(75);//设置表格通过拖动列可以的最大宽度。

		frame.setVisible(true);

		//删除按钮
		DeleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = FileListTable.getSelectedRow();
				//int num= (int) model.getValueAt(row,0);
				//System.out.println(num);
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "请先选择一条记录！");
					return;
				}
				FileListModel.removeRow(FileListTable.getSelectedRow());
				GUIImport.list.remove(row);
			}
		});

		//全部清空按钮
		ClearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int index = FileListTable.getModel().getRowCount() - 1; index >= 0; index--) {
					FileListModel.removeRow(index);
				}
				GUIImport.list.clear();
				JOptionPane.showMessageDialog(null, "清空成功！");
			}
		});

		//确认导入按钮
		ConfirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(GUIImport.list);

				for (String string : GUIImport.list) {
					StorageAccessor.LoadDiscussionFromCSV(string, "gbk");
				}
				Global.MainForm.ShowAllDiscussions();

//                for (DiscussionItem entry : DataManipulator.GetDiscussionList()) {
//			            System.out.println(entry.GetText());
//			            System.out.println(entry.GetLabels().toString());
//                }
				frame.dispose();
			}
		});

		//继续添加按钮
		AddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUIImport.fileChoose();
			}
		});

	}
}
