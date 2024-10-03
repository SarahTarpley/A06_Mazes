import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Traveler {
	Maze env;
	public Byte[] loc;
	public Byte icon;
	public Byte locX;
	public Byte locY;
	public Byte[] moveOptions;
	public Byte[] prevLoc;
	public ByteStack moveHistory = new ByteStack("ArrayList");
	public Character resetDir = ' ';
	public int stepCount = 0;
	
	public Traveler(Maze env) {
		this.env = env;
		this.locX = env.startLoc[0];
		this.locY = env.startLoc[1];
		this.loc = new Byte[]{locX, locY};
		this.icon = -1;
		this.moveHistory.Push(this.loc);
		env.Map.get(this.locY).set(this.locX, this.icon);
	}
	
	public void updateLoc() {
		// clear icon from old loc
		this.moveHistory.Push(this.loc);
		this.prevLoc = this.moveHistory.Peek();	
		env.Map.get(prevLoc[1]).set(prevLoc[0], Byte.valueOf("1"));

		// set icon on new loc
		this.loc = new Byte[]{locX, locY};
		env.Map.get(this.locY).set(this.locX, this.icon);
		this.stepCount++;
		resetDir = ' ';
	}
	
	public void undoMove() {
		Byte undoX = locX;
		Byte undoY = locY;
		System.out.println(this.moveHistory.size());
		Byte[] moveBack = this.moveHistory.Pop();
		locX = moveBack[0]; locY = moveBack[1];
		this.prevLoc = this.moveHistory.Peek();
		// erase icon from old loc
		env.Map.get(loc[1]).set(loc[0], Byte.valueOf("1"));
		// set icon on new loc
		this.loc = new Byte[]{locX, locY};
		env.Map.get(this.locY).set(this.locX, this.icon);
		this.stepCount++;
		this.resetDir = senseHeading(undoX, undoY);
	}
	
	public boolean skipMove() {
		boolean isLastMove = (
				prevLoc != null
				&& prevLoc[0] == locX
				&& prevLoc[1] == locY
				);
		if(isLastMove) {
			// cancel move, reset position
			this.locX = loc[0]; this.locY = loc[1];
		}
		return isLastMove;
	}
	
	// check position, updating moveOptions
	public Byte[] checkMove() {
		Byte left;
		Byte right;
		Byte down;
		Byte up;
		
		System.out.println("Checking where to go...");
		
		if(this.locX > 0) {
			left = env.Map.get(this.locY).get(this.locX-1);	
		}
		else {
			left = 0;
		}
		if(this.locX < env.Map.get(this.locY).size()-1) {
			right = env.Map.get(this.locY).get(this.locX+1);
		}
		else {
			right = 0;
		}
		if(this.locY > 0) {
			up = env.Map.get(this.locY-1).get(this.locX);
		}
		else {
			up = 0;
		}
		if(this.locY < env.Map.size()-1 && this.locX <= env.Map.get(this.locY).size()-1) {
			down = env.Map.get(this.locY+1).get(this.locX);
		}
		else {
			down = 0;
		}
		
		
		if(resetDir.equals('L')) {
			System.out.println("Avoiding "+resetDir+"...");
			left = 0;
		}
		if(resetDir.equals('D')) {
			System.out.println("Avoiding "+resetDir+"...");
			down = 0;
		}
		if(resetDir.equals('R')) {
			System.out.println("Avoiding "+resetDir+"...");
			right = 0;
		}
		else if(resetDir.equals('U')) {
			System.out.println("Avoiding "+resetDir+"...");
			up = 0;
		}
		
		System.out.println("Current location is X: " + loc[0] + ", Y: " + loc[1]);
		System.out.println("left is : "+String.valueOf(left));
		System.out.println("down is : "+String.valueOf(down));
		System.out.println("right is : "+String.valueOf(right));
		System.out.println("up is : "+String.valueOf(up));
		
		// display the maze
		for(ArrayList<Byte> row : env.Map) {
			System.out.println(row);
		}
		
		return (new Byte[] {left, down, right, up});
		
	}
	
	public Character senseHeading(Byte undoX, Byte undoY) {
		//resetDir = ' ';
		//Byte startAt;
		// abort if there is no move history
		if(prevLoc == null) {
			return resetDir;
		}
		
		System.out.println("Reorienting...");
		System.out.println(String.valueOf(prevLoc[0]) + " " + String.valueOf(prevLoc[1]));
		System.out.println(String.valueOf(loc[0]) + " " + String.valueOf(loc[1]));
		
		//Testing new method
//		if(prevLoc[0] < loc[0]) {
//			startAt = 3;
//		}
//		if(prevLoc[0] > loc[0]) {
//			startAt = 1;
//		}
//		if(prevLoc[1] < loc[1]) {
//			startAt = 2;
//		}
//		if(prevLoc[1] > loc[1]) {
//			startAt = 0;
//		}
		
		// This method doesn't work when the last step was a pivot onto a different axis.
		if(prevLoc[0] < loc[0] && loc[0] < undoX) {
			resetDir = 'R';
		}
		if(prevLoc[0] > loc[0] && loc[0] > undoX) {
			resetDir = 'L';
		}
		if(prevLoc[1] < loc[1] && loc[1] < undoY) {
			resetDir = 'D';
		}
		if(prevLoc[1] > loc[1] && loc[1] > undoY) {
			resetDir = 'U';
		}
		
//		if(!prevLoc[0].equals(loc[0])) {
//			System.out.println("Avoid X");
//			resetDir = 'X';
//		}
//		else if(!prevLoc[1].equals(loc[1])){
//			System.out.println("Avoid Y");
//			resetDir = 'Y';
//		}
		//System.out.println("Set to "+String.valueOf(resetDir));
		
		return resetDir;
	}
	
	// move to the first availability found
	public void move() {
		System.out.println("Move # " + String.valueOf(this.stepCount));
		System.out.println("Goal : " + String.valueOf(env.endLoc[0]) + ", " + String.valueOf(env.endLoc[1]));
		boolean canMove = false;
		
		moveOptions = checkMove();
		
		for(Byte i = 0; i < moveOptions.length; i++) {
			if(moveOptions[i] == 1) {
				
				canMove = true;
				switch(i) {
				case 0:
					moveLeft();
					break;
				case 1:
					moveDown();
					break;

				case 2:
					moveRight();
					break;
				case 3:
					moveUp();
					break;
				}
				
				if(skipMove()) {
					canMove = false;
				}
				else {
					break;	
				}
			}
		}
		// if no moves were available
		if(canMove == false) {
			undoMove();
		}
		else {
			updateLoc();
		}
		if((env.endLoc[0] != locX || env.endLoc[1] != locY) && stepCount <= 100) {
			move();
		}
		else if(env.endLoc[0] == locX && env.endLoc[1] == locY){
			checkMove();
			System.out.println("COMPLETE!!!");
		}
	}
	// movement actions
	public void moveLeft() {
		try {
			if(env.Map.get(locY).get(locX-1) == 1) {
				System.out.println("Moving left");
				this.locX--;
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
			if(env.Map.get(locY).get(locX+1) == 1) {
				System.out.println("Moving right");
				this.locX++;				
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
			if(env.Map.get(locY-1).get(locX) == 1) {
				System.out.println("Moving up");
				this.locY--;				
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
			if(env.Map.get(locY+1).get(locX) == 1) {
				System.out.println("Moving down");
				this.locY++;				
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move down");
		}
	}
}
