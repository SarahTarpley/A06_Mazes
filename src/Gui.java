import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class Gui extends JFrame{
	Maze maze;
	JTable recordDisplay;
	public static CusTblModel recordsDTM;
	public static JLabel isCompletable;
	// JPanels
	private JPanel mainPanel;

	public Gui(Maze maze) {
		this.maze = maze;
		//System.out.println(matrix[0]);
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
	
	public static void endResult(String display) {
		JDialog end = new JDialog();
		JLabel result = new JLabel();
		result.setText(display);
		end.setBounds(700, 400, 200, 150);
		end.add(result, BorderLayout.CENTER);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		end.setVisible(true);
		
	}
	
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
		    	//cell.setBackground( Color.gray );
		    	
	            if( amount == 0 )
	            {
	                cell.setBackground( Color.gray );
	                // You can also customize the Font and Foreground this way
	                // cell.setForeground();
	                // cell.setFont();
	            }
	            else if( amount.intValue() == 2) {
	            	cell.setBackground( Color.red );	
	            }
	            else if( amount.intValue() == -1) {
	            	cell.setBackground( Color.yellow );
	            }
	            else
	            {
	                cell.setBackground( Color.white );
	            }
	        cell.setForeground(getBackground());
	        return cell;
	    }
	}
	
	public class CusTblModel extends AbstractTableModel {
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
	
	public JScrollPane MazeTable(){
		//Object[][] matrix;
		// Create main panel with BorderLayout
		// DefaultTableModel
		//matrix = maze.Map.stream().map(row -> row.stream().map(Byte::valueOf).toArray()).collect(Collectors.toList()).toArray(new Object[0][0]);
		Byte[] Columns = {0,1,2,3,4,5,6,7,8,9};
		recordsDTM = new CusTblModel();
//		DefaultTableModel recordsDTM = new DefaultTableModel(matrix, Columns) {
//			public Class<?> getColumnClass(int column){
//					return Byte.class;
//			}
//		};
		CustomTableCellRenderer renderer = new CustomTableCellRenderer();
		recordDisplay = new JTable();		
		try {
			recordDisplay.setRowHeight(50);
			recordDisplay.setModel(recordsDTM);
			recordDisplay.setDefaultRenderer( Class.forName( "java.lang.Byte" ), renderer);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		recordDisplay.setDefaultEditor(Object.class, null);
		//recordDisplay.setAutoCreateRowSorter(true);
		JScrollPane sp = new JScrollPane(recordDisplay);
		recordDisplay.add(renderer);
		sp.enableInputMethods(false);

		return sp;
	}
}
