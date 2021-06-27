import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class GUICrawling extends JFrame {
	// 本窗体
	final GUICrawling ThisForm = this;

	// 按钮面板
	final JPanel ButtonPanel = new JPanel(new GridBagLayout());
	final JButton btnStartAll = new JButton("全部开始");
	final JButton btnAddTasks = new JButton("添加任务");
	final JButton btnDeleteTasks = new JButton("删除任务");
	final JButton btnClearTasks = new JButton("清空列表");
	final JLabel lbInputTickerSymbols = new JLabel("输入股票代码：");
	final JTextArea TickerSymbolBox = new JTextArea();

	// 任务列表
	static class TaskInfo {
		String TickerSymbol;
		String Pathname;
		int Status;

		TaskInfo(String TickerSymbol, String Pathname) { this.TickerSymbol = TickerSymbol; this.Pathname = Pathname; this.Status = 0; }
		TaskInfo(String TickerSymbol, String Pathname, int Status) { this.TickerSymbol = TickerSymbol; this.Pathname = Pathname; this.Status = Status; }
	}
	final ArrayList<TaskInfo> Tasks = new ArrayList<>();
	final TaskTableModel TaskListModel = new TaskTableModel();
	final JTable TaskList = new JTable(TaskListModel);
	final JScrollPane TaskListScrollPane = new JScrollPane(TaskList);

	// 任务列表模型
	class TaskTableModel extends AbstractTableModel {
		private final String[] TaskListColumnNames = new String[]{ "股票代码", "文件名", "状态" };
		private final String[] StatusNames = new String[]{ "未开始", "爬取中", "已结束", "失败" };

		@Override public int getRowCount() { return Tasks.size(); }

		@Override public int getColumnCount() { return 3; }

		@Override public String getColumnName(int col) { return TaskListColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return switch (columnIndex) {
				case 0 -> Tasks.get(rowIndex).TickerSymbol;
				case 1 -> Tasks.get(rowIndex).Pathname;
				case 2 -> StatusNames[Tasks.get(rowIndex).Status];
				default -> null;
			};
		}

		@Override public void setValueAt(Object value, int rowIndex, int ColumnIndex) { // 仅文件名可更改
			Tasks.get(rowIndex).Pathname = (String) value;
			fireTableCellUpdated(rowIndex, ColumnIndex);
		}

		@Override public boolean isCellEditable(int row, int col) { return col == 1; } // 仅文件名可更改

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

		// 表格的设置
		TaskList.getColumnModel().getColumn(0).setMinWidth(80);
		TaskList.getColumnModel().getColumn(0).setMaxWidth(80);
		TaskList.getColumnModel().getColumn(2).setMinWidth(80);
		TaskList.getColumnModel().getColumn(2).setMaxWidth(80);

		// 动作监听程序
		btnStartAll.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				final HashMap<String, String> Params = new HashMap<>(); // 预设参数
				Params.put("pages", "100");
				Params.put("access-delay", "2");
				Params.put("retry-delay", "5");
				Params.put("random", "");
				for (TaskInfo t : Tasks) {
					new Thread(() -> {
						try {
							// 准备命令行参数并开始
							final StringBuilder argv = new StringBuilder("python xueqiuspider.py");
							argv.append(" --file=\"").append(t.Pathname).append("\"");
							argv.append(" --stock=\"").append(t.TickerSymbol).append("\"");
							for (Map.Entry<String, String> E : Params.entrySet()) argv.append(" --").append(E.getKey()).append("=\"").append(E.getValue()).append("\"");
							t.Status = 1;
							TaskListModel.fireTableDataChanged();
							System.out.println(argv);
							final Process p = Runtime.getRuntime().exec(argv.toString());

							// 等待爬取结束
							final BufferedReader ProcessOutputReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
							while (ProcessOutputReader.readLine() != null) {}
							ProcessOutputReader.close();
							p.waitFor();

							// 爬取完毕
							final File f = new File(t.Pathname);
							if (f.exists() == true) t.Status = 2;
							else t.Status = 3;
							TaskListModel.fireTableDataChanged();
						}
						catch (IOException | InterruptedException Exception) {
							Exception.printStackTrace();
						}
					}).start();
				}
			}
		});

		btnAddTasks.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				TickerSymbolBox.setText(TickerSymbolBox.getText().trim());
				final String[] TickerSymbols = TickerSymbolBox.getText().split("\\R+");
				for (String TickerSymbol : TickerSymbols)
					Tasks.add(new TaskInfo(TickerSymbol,  Global.DefaultSavePath + "\\" + TickerSymbol + "-" + LocalDateTime.now().format(Global.DefaultDateTimeFormatter) + ".csv"));
				TaskListModel.fireTableDataChanged();
			}
		});

		btnDeleteTasks.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				final int[] Row = TaskList.getSelectedRows();
				for (int i = Row.length - 1; i >= 0; --i) { Tasks.remove(Row[i]); }
				TaskListModel.fireTableDataChanged();
			}
		});

		btnClearTasks.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Tasks.clear();
				TaskListModel.fireTableDataChanged();
			}
		});

		// 主要控件的属性与添加
		final GridBagConstraints TableLayout = new GridBagConstraints();
		TableLayout.gridx = TableLayout.gridy = 0;
		TableLayout.weightx = TableLayout.weighty = 1;
		TableLayout.fill = GridBagConstraints.BOTH;

		final GridBagConstraints ButtonPanelLayout = new GridBagConstraints();
		ButtonPanelLayout.gridx = 1; ButtonPanelLayout.gridy = 0;
		ButtonPanelLayout.weightx = 0; ButtonPanelLayout.weighty = 1;
		ButtonPanelLayout.fill = GridBagConstraints.BOTH;

		GridBagConstraints ButtonConstraints;
		ButtonConstraints = new GridBagConstraints();
		ButtonConstraints.gridx = 0; ButtonConstraints.gridy = 0;
		ButtonConstraints.weightx = ButtonConstraints.weighty = 0;
		ButtonPanel.add(btnStartAll, ButtonConstraints);

		ButtonConstraints.gridx = 0; ButtonConstraints.gridy = 1;
		ButtonPanel.add(btnAddTasks, ButtonConstraints);

		ButtonConstraints.gridx = 0; ButtonConstraints.gridy = 2;
		ButtonPanel.add(btnDeleteTasks, ButtonConstraints);

		ButtonConstraints.gridx = 0; ButtonConstraints.gridy = 3;
		ButtonPanel.add(btnClearTasks, ButtonConstraints);

		ButtonConstraints.gridx = 0; ButtonConstraints.gridy = 4;
		ButtonPanel.add(lbInputTickerSymbols, ButtonConstraints);

		// 股票代码输入栏的属性
		TickerSymbolBox.setMinimumSize(new Dimension(btnClearTasks.getWidth(), TickerSymbolBox.getMinimumSize().height));
		TickerSymbolBox.setMaximumSize(new Dimension(btnClearTasks.getWidth(), TickerSymbolBox.getMaximumSize().height));
		final GridBagConstraints TickerSymbolBoxLayout = new GridBagConstraints();
		TickerSymbolBoxLayout.gridx = 0; TickerSymbolBoxLayout.gridy = 5;
		TickerSymbolBoxLayout.weightx = 0; TickerSymbolBoxLayout.weighty = 1;
		TickerSymbolBoxLayout.fill = GridBagConstraints.BOTH;

		// 添加其它控件
		ButtonPanel.add(TickerSymbolBox, TickerSymbolBoxLayout);
		super.add(ButtonPanel, ButtonPanelLayout);
		super.add(TaskListScrollPane, TableLayout);

		// 显示
		ShowTaskList();
	}

	// 显示任务列表
	public void ShowTaskList(){ this.setVisible(true); }
}
