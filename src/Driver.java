import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Driver{	
	public static void main(String[] args){		
		Maze env = new Maze();
		
		try {
			Traveler walker = new Traveler(env);
			Gui mainMenuFrame = new Gui(env);
			mainMenuFrame.setVisible(true);
			walker.move();
			//mainMenuFrame.recordsDTM.fireTableDataChanged();
			//TimeUnit.SECONDS.sleep(5);
			//mainMenuFrame.recordDisplay.setValueAt((byte)100, 5, 5);
			//System.out.println(mainMenuFrame.recordDisplay.getValueAt(5, 5));
			//env.Map.get(5).set(5, (byte) -1);
			//mainMenuFrame.recordDisplay.setValueAt(-1, 5, 5);
			//mainMenuFrame.actionPerformed();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
  }
}
