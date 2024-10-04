import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class Gui extends JFrame{
	Maze maze;
	JTable recordDisplay;
	private JPanel mainPanel;
	// Static items will be accessed by Traveler to provide updates
	public static CusTblModel recordsDTM;
	public static JLabel isCompletable;

	public Gui(Maze maze) {
		this.maze = maze;;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Maze Traveler");
		setBounds(400, 100, 700, 700);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(mainPanel);
		mainPanel.setLayout(new BorderLayout(0,0));
		mainPanel.setBackground(new Color(234, 219, 203));
		isCompletable = new JLabel();
		isCompletable.setText("In progress...");
		isCompletable.setSize(100, 100);
		// Add Sub Panels to the main Panel
		mainPanel.add(MazeTable(),BorderLayout.CENTER);
		mainPanel.add(isCompletable,BorderLayout.SOUTH);

	}
	
	// Render the maze into blocks of colors rather than digits
	public class CustomTableCellRenderer extends DefaultTableCellRenderer 
	{
		@Override
		public Component getTableCellRendererComponent
			(
			JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column
			){
			Component cell = super.getTableCellRendererComponent
			   (table, value, isSelected, hasFocus, row, column);
			
			Byte amount = (Byte) value;
			
			// Marks the walls
			if( amount == 0 )
			{
			    cell.setBackground( Color.gray );
			}
			// Marks areas that have been tried and failed
			else if( amount.intValue() == 2) {
				cell.setBackground( Color.red );	
			}
			// Marks current position
			else if( amount.intValue() == -1) {
				cell.setBackground( Color.yellow );
			}
			// Marks the open spaces
			else
			{
			    cell.setBackground( Color.white );
			}
			// Hide cell contents
	        cell.setForeground(getBackground());
	        return cell;
	    }
	}
	
	// Displaying the maze through a custom JTable
	public class CusTblModel extends AbstractTableModel {
		// Needed direct access to array so could receive updates
		public List<ArrayList<Byte>> tableData = maze.Map;
		
		public int getRowCount() {
			return maze.mazeDimensions[1];
		}

		public int getColumnCount() {
			return maze.mazeDimensions[0];
		}
		
		public String getColumnName(int index) {
			return String.valueOf(index);
		}
		
		public Object getValueAt(int row, int col) {
			return tableData.get(row).get(col);
		}
		public Class<?> getColumnClass(int column){
			return Byte.class;
		}
		
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			tableData.get(rowIndex).set(columnIndex, (byte) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
	
	// Create the maze visuals and apply a colored cell renderer
	public JScrollPane MazeTable(){
//		-- DefaultTableModel did not fit needs, since it required re-instantiating the data source --
//		-- This prevented the table from receiving updates as the maze was being progressed --		
//		Byte[] Columns = {0,1,2,3,4,5,6,7,8,9};
//		matrix = maze.Map.stream().map(row -> row.stream().map(Byte::valueOf).toArray()).collect(Collectors.toList()).toArray(new Object[0][0]);
//		DefaultTableModel recordsDTM = new DefaultTableModel(matrix, Columns) {
//			public Class<?> getColumnClass(int column){
//					return Byte.class;
//			}
//		};
		recordsDTM = new CusTblModel();
		CustomTableCellRenderer renderer = new CustomTableCellRenderer();
		recordDisplay = new JTable();		
		try {
			recordDisplay.setRowHeight(50);
			recordDisplay.setModel(recordsDTM);
			recordDisplay.setDefaultRenderer( Class.forName( "java.lang.Byte" ), renderer);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		recordDisplay.setDefaultEditor(Object.class, null);
		JScrollPane sp = new JScrollPane(recordDisplay);
		recordDisplay.add(renderer);
		sp.enableInputMethods(false);

		return sp;
	}
	
	public static void endResult(String display) {
		JDialog end = new JDialog();
		JLabel result = new JLabel();
		result.setText(display);
		end.setBounds(700, 400, 300, 150);
		end.add(result, BorderLayout.CENTER);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		end.setVisible(true);
		
	}
}
