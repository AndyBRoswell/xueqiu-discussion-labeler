import org.jfree.data.gantt.Task;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GUICrawling extends JFrame {
	// 本窗体
	final GUICrawling ThisForm = this;

	// 按钮面板
	final JPanel ButtonPanel = new JPanel(new GridBagLayout());
	final JButton btnStartAll = new JButton("全部开始");

	// 任务列表
	static class TaskInfo {
		String TickerSymbol;
		String FileName;
		int Status;

		TaskInfo(String TickerSymbol, String FileName, int Status) { this.TickerSymbol = TickerSymbol; this.FileName = FileName; this.Status = Status; }
	}
	final ArrayList<TaskInfo> Tasks = new ArrayList<>();
	final TaskTableModel TaskListModel = new TaskTableModel();
	final JTable TaskList = new JTable(TaskListModel);
	final JScrollPane TaskListScrollPane = new JScrollPane(TaskList);

	// 任务列表模型
	class TaskTableModel extends AbstractTableModel {
		private final String[] TaskListColumnNames = new String[]{ "股票代码", "文件名", "状态" };

		@Override public int getRowCount() { return Tasks.size(); }

		@Override public int getColumnCount() { return 3; }

		@Override public String getColumnName(int col) { return TaskListColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return switch (columnIndex) {
				case 0 -> Tasks.get(rowIndex).TickerSymbol;
				case 1 -> Tasks.get(rowIndex).FileName;
				case 2 -> Tasks.get(rowIndex).Status;
				default -> null;
			};
		}

		@Override public void setValueAt(Object value, int rowIndex, int ColumnIndex) { // 仅文件名可更改
			Tasks.get(rowIndex).FileName = (String) value;
			fireTableCellUpdated(rowIndex, ColumnIndex);
		}

		@Override public boolean isCellEditable(int row, int col) { return col != 2; } // 仅文件名可更改

		@Override public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	}

	// 加载任务列表（仅在软件启动时调用）
	public GUICrawling() {
		// 窗体的基本属性
		final Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
		super.setSize(Screen.width / 3, Screen.height / 3);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocationRelativeTo(null);
		final GridBagLayout GUIImportFilesLayout = new GridBagLayout();
		super.getContentPane().setLayout(new GridBagLayout());
		super.setDefaultCloseOperation(HIDE_ON_CLOSE); // 点击关闭按钮时隐藏窗口

		final GridBagConstraints TableLayout = new GridBagConstraints();
		TableLayout.gridx = TableLayout.gridy = 0;
		TableLayout.weightx = TableLayout.weighty = 1;
		TableLayout.fill = GridBagConstraints.BOTH;

		final GridBagConstraints ButtonPanelLayout = new GridBagConstraints();
		ButtonPanelLayout.gridx = 1; ButtonPanelLayout.gridy = 0;
		ButtonPanelLayout.weightx = 0; ButtonPanelLayout.weighty = 1;

		// 表格的设置
		TaskList.getColumnModel().getColumn(0).setMinWidth(80);
		TaskList.getColumnModel().getColumn(0).setMaxWidth(80);
		TaskList.getColumnModel().getColumn(2).setMinWidth(80);
		TaskList.getColumnModel().getColumn(2).setMaxWidth(80);

		// 动作监听程序
		btnStartAll.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				for (TaskInfo t : Tasks) {
					new Thread(() -> {
						try {
							final Process p = Runtime.getRuntime().exec("python ");
						}
						catch (IOException ioException) {
							ioException.printStackTrace();
						}
					}).start();
				}
			}
		});

		// 添加控件
		ButtonPanel.add(btnStartAll);
		super.add(ButtonPanel, ButtonPanelLayout);
		super.add(TaskListScrollPane, TableLayout);

		// 显示
		ShowTaskList();
	}

	// 显示任务列表
	public void ShowTaskList(){
		this.setVisible(true);
	}
}
