import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class GUICrawling {
	// 本窗体
	final GUICrawling ThisForm = this;

	// 任务列表模型
	class TaskTableModel extends AbstractTableModel {
		class TaskInfo {
			String FileName;
			int Status;

			TaskInfo(String FileName, int Status) { this.FileName = FileName; this.Status = Status; }
		}

		private final String[] TaskListColumnNames = new String[]{ "文件名", "状态" };
		private final ArrayList<TaskInfo> Tasks = new ArrayList<>();

		@Override public int getRowCount() { return Tasks.size(); }

		@Override public int getColumnCount() { return 2; }

		@Override public String getColumnName(int col) { return TaskListColumnNames[col]; }

		@Override public Object getValueAt(int rowIndex, int columnIndex) {
			return switch (columnIndex) {
				case 0 -> Tasks.get(rowIndex).FileName;
				case 1 -> Tasks.get(rowIndex).Status;
				default -> null;
			};
		}

		@Override public void setValueAt(Object value, int rowIndex, int ColumnIndex) { // 仅文件名可更改
			Tasks.get(rowIndex).FileName = (String) value;
		}

		@Override public boolean isCellEditable(int row, int col) { return col == 0; } // 仅文件名可更改

		@Override public Class getColumnClass(int c) { return getValueAt(0, c).getClass(); }
	}
}
