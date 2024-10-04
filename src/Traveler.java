import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

//Contains methods for navigating the maze and updating map with progress
public class Traveler {
	public Maze env;
	public Byte[] loc;
	public Byte icon;
	public Byte locX;
	public Byte locY;
	public Byte[] moveOptions;
	public Byte[] prevLoc;
	public ByteStack moveHistory = new ByteStack("ArrayList");
	public int stepCount = 0;
	public int limitStep = 0;
	
	public Traveler(Maze env) {
		this.env = env;
		this.limitStep = env.mazeDimensions[0] * env.mazeDimensions[1];
		this.locX = env.startLoc[0];
		this.locY = env.startLoc[1];
		this.loc = new Byte[]{locX, locY};
		this.icon = -1;
		this.moveHistory.Push(this.loc);
		env.Map.get(this.locY).set(this.locX, this.icon);
	}
	//Update move history, and reset the marker position on map
	public void updateLoc() {
		// clear icon from old loc
		this.moveHistory.Push(this.loc);
		this.prevLoc = this.moveHistory.Peek();	
		env.Map.get(prevLoc[1]).set(prevLoc[0], Byte.valueOf("1"));

		// set icon on new loc
		this.loc = new Byte[]{locX, locY};
		env.Map.get(this.locY).set(this.locX, this.icon);
		this.stepCount++;
	}
	//When no other options except to move backwards
	public void undoMove() {
		Byte[] moveBack = this.moveHistory.Pop();
		locX = moveBack[0]; locY = moveBack[1];
		this.prevLoc = this.moveHistory.Peek();
		// mark as already tried
		env.Map.get(loc[1]).set(loc[0], Byte.valueOf("2"));
		// set icon on new loc
		this.loc = new Byte[]{locX, locY};
		env.Map.get(this.locY).set(this.locX, this.icon);
		this.stepCount++;
	}
	
	//Boolean used to check if the prospective move would be retracing steps
	public boolean isBackwards(Byte[] moveCoord) {
		boolean isLastMove = (
				prevLoc != null
				&& prevLoc[0] == moveCoord[0]
				&& prevLoc[1] == moveCoord[1]
				);
		return isLastMove;
	}
	
	// This originally contained logic used to make a decision on the next move
	// Now it is only used to print progress to the console & can be deprecated
	public void checkMove() {
		System.out.println("Checking where to go...");
		System.out.println("Current location is X: " + loc[0] + ", Y: " + loc[1]);
		System.out.println("Move # " + String.valueOf(this.stepCount));
		System.out.println("Goal : " + String.valueOf(env.endLoc[0]) + ", " + String.valueOf(env.endLoc[1]));
		// display the maze
		for(ArrayList<Byte> row : env.Map) {
			System.out.println(row);
		}		
	}
	
	// move to the first availability found
	public boolean move() {
		boolean canMove = false;
		boolean complete = false;
		checkMove();

		// Try each direction and short circuit once it works
		canMove = moveLeft() || moveDown() || moveRight() || moveUp();
		
		// if no moves were available
		if(canMove == false) {
			undoMove();
		}
		else {
			updateLoc();
		}

		// After location has been updated, check if complete
		complete = env.endLoc[0] == loc[0] && env.endLoc[1] == loc[1];
		
		// Recursively continue moving if not complete
		if(!complete && stepCount <= limitStep) {
			try { // 0.5 sec delay between moves
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Update the table before entering the next recursive loop
			Gui.recordsDTM.fireTableDataChanged();
			move();
		}
		else if(complete){ // Pop open a dialogue box at end of recursion
			checkMove();
			Gui.endResult("This maze has been solved in " + String.valueOf(stepCount) +" steps!");
		}
		else { // went over step limit
			Gui.endResult("Maze could not be solved within the limit of "+String.valueOf(limitStep)+" steps.");
		}
		return complete;
	}
	
	// The 4 functions to move in a direction
	// Each check if next move is allowed and is not backwards
	// They will update the transient variables locX & locY
	// Return a boolean if a move was possible
	// Could be condensed into a single function looping array moveCoord (X+1, X-1, Y+1, Y-1)
	public boolean moveLeft() {
		boolean hasMoved = false;
		Byte[] moveCoord = {(byte) (locX-1), locY};

		try {
			if(!isBackwards(moveCoord) && env.Map.get(moveCoord[1]).get(moveCoord[0]) == 1) {
				System.out.println("Moving left");
				this.locX = moveCoord[0];
				this.locY = moveCoord[1];
				hasMoved = true;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move left");
		}
		return hasMoved;
	}
	
	public boolean moveRight() {
		boolean hasMoved = false;
		Byte[] moveCoord = {(byte) (locX+1), locY};
		try {
			if(!isBackwards(moveCoord) && env.Map.get(moveCoord[1]).get(moveCoord[0]) == 1) {
				System.out.println("Moving right");
				this.locX = moveCoord[0];
				this.locY = moveCoord[1];
				hasMoved = true;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move right");
		}
		return hasMoved;
	}
	
	public boolean moveUp() {
		boolean hasMoved = false;
		Byte[] moveCoord = {locX, (byte) (locY-1)};

		try {
			if(!isBackwards(moveCoord) && env.Map.get(moveCoord[1]).get(moveCoord[0]) == 1) {
				System.out.println("Moving up");
				this.locX = moveCoord[0];
				this.locY = moveCoord[1];
				hasMoved = true;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move up");
		}
		return hasMoved;
	}
	
	public boolean moveDown() {
		boolean hasMoved = false;
		Byte[] moveCoord = {locX, (byte) (locY+1)};
		try {
			if(!isBackwards(moveCoord) && env.Map.get(moveCoord[1]).get(moveCoord[0]) == 1) {
				System.out.println("Moving down");
				this.locX = moveCoord[0];
				this.locY = moveCoord[1];
				hasMoved = true;
			}
			else {
				throw new Exception();
			}
		}
		catch (Exception e){
			System.out.println("Cannot move down");
		}
		return hasMoved;
	}
}
