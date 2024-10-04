
// Main method
public class Driver{	
	public static void main(String[] args){		
		Maze env = new Maze();
		
		try {
			Traveler walker = new Traveler(env);
			Gui mainMenuFrame = new Gui(env);
			mainMenuFrame.setVisible(true);
			walker.move();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
  }
}
