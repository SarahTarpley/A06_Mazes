import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Traveler {
	
	public Byte icon = -1;
	public Byte loc = 0;
	public Byte locY = 3;
	public Byte locX = 1;
	public Byte left = 0;
	public Byte right = 0;
	public Byte down = 0;
	public Byte up = 0;
	public Byte[] moveOptions;
	public ByteStack moveHistory = new ByteStack("ArrayList");
	private List<ArrayList<Byte>> MazeMap = importMaze();

	public List<ArrayList<Byte>> importMaze() {
		// Define the delimiter used in the CSV file (in this case, a comma)
		//String delimiter = ",";
		List<ArrayList<Byte>> MazeMap = new ArrayList<ArrayList<Byte>>();
		String delimiter = " ";

		// Try-with-resources: Automatically close the BufferedReader after use
		try (BufferedReader br = new BufferedReader(new FileReader("maze.txt"))) {
			Pattern regDigit = Pattern.compile("\\d+");
			String line;  // Variable to hold each line read from the CSV
			// Read each subsequent line of the CSV (skipping the header)
			while ((line = br.readLine()) != null) {
				// Split the line into values using the defined delimiter (comma)
				String[] values = line.split(delimiter);
				System.out.println(line);
				ArrayList<Byte> MazeRow = new ArrayList<Byte>();
				// Check if the essential fields are not empty before creating the Book object
				for (String value : values) {
					//System.out.println(value);
					if(regDigit.matcher(value).matches()) {
						MazeRow.add(Byte.valueOf(value));	
					}
				}
				if(MazeRow.size() > 0) {
					MazeMap.add(MazeRow);
				}
			}
			//System.out.println(MazeMap);
		}
		catch (Exception e){
			System.out.println(e);
		}
		this.moveHistory.Push(new Byte[]{this.locX, this.locY});
		//System.out.println(moveHistory.size());
		return MazeMap;
	}
	
	// check position, updating moveOptions, and then move accordingly
	public void checkMove() {		
		//loc = MazeMap.get(locY).get(locX);
		System.out.println("Checking where to go...");
		
		if(locX > 0) {
			left = MazeMap.get(locY).get(locX-1);	
		}
		else {
			left = 0;
		}
		if(locX < MazeMap.get(locY).size()-1) {
			right = MazeMap.get(locY).get(locX+1);
		}
		else {
			right = 0;
		}
		if(locY > 0) {
			up = MazeMap.get(locY-1).get(locX);
		}
		else {
			up = 0;
		}
		if(locY < MazeMap.size()-1 && locX <= MazeMap.get(locY).size()-1) {
			down = MazeMap.get(locY+1).get(locX);
		}
		else {
			down = 0;
		}
		
		System.out.println("Current location is : " + String.valueOf(loc));
		System.out.println("left is : "+String.valueOf(left));
		System.out.println("right is : "+String.valueOf(right));
		System.out.println("up is : "+String.valueOf(up));
		System.out.println("down is : "+String.valueOf(down));
		
		this.moveOptions = new Byte[]{left, right, up, down};
		this.move();
		
		for(ArrayList<Byte> row : MazeMap) {
			System.out.println(row);
		}
		
	}
	// move to the first availability found
	public void move() {
		boolean canMove = false;
		for(Byte i = 0; i < moveOptions.length; i++) {
			if(moveOptions[i] == 1) {
				canMove = true;
				switch(i) {
				case 0:
						moveLeft();
						return;
				case 1:
						moveRight();
						return;
				case 2:
						moveUp();
						return;
				case 3:
						moveDown();
						return;
				}
			}
		}
		if(canMove == false) {
			// if no moves were available
			System.out.println(this.moveHistory.size());
			checkLastMove();
			Byte[] moveBack = this.moveHistory.Pop();
			MazeMap.get(moveBack[1]).set(moveBack[0], icon);	
		}
		else {
			// mark the location on the array with the traveler icon
			MazeMap.get(locY).set(locX, icon);
			// add this movement to the history stack
			moveHistory.Push(new Byte[]{locX, locY});
		}
	}
	// movement actions
	public void moveLeft() {
		try {
			if(MazeMap.get(locY).get(locX+1) == 1) {
				System.out.println("Moving left");
				locX++;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move left");
		}
	}
	public void moveRight() {
		try {
			if(MazeMap.get(locY).get(locX-1) == 1) {
				System.out.println("Moving right");
				locX--;				
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move right");
		}
	}
	public void moveUp() {
		try {
			if(MazeMap.get(locY-1).get(locX) == 1) {
				System.out.println("Moving up");
				locY--;				
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move up");
		}
	}
	public void moveDown() {
		try {
			if(MazeMap.get(locY+1).get(locX) == 1) {
				System.out.println("Moving down");
				locY++;				
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move down");
		}
	}
	
	public void checkLastMove() {
		for(Byte loc : moveHistory.Peek()) {
			System.out.println("num "+String.valueOf(loc));
		}
	}
	//List<ArrayList<Byte>> MazeMap = Driver.importMaze();
}
