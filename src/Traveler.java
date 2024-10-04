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
	
	public void undoMove() {
		//System.out.println(this.moveHistory.size());
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
	
	public boolean isBackwards(Byte[] moveCoord) {
		boolean isLastMove = (
				prevLoc != null
				&& prevLoc[0] == moveCoord[0]
				&& prevLoc[1] == moveCoord[1]
				);
		return isLastMove;
	}
	
	// check position, updating moveOptions
	public void checkMove() {
		System.out.println("Checking where to go...");
		System.out.println("Current location is X: " + loc[0] + ", Y: " + loc[1]);
		// display the maze
		for(ArrayList<Byte> row : env.Map) {
			System.out.println(row);
		}		
	}
	
	// move to the first availability found
	public boolean move() {
		System.out.println("Move # " + String.valueOf(this.stepCount));
		System.out.println("Goal : " + String.valueOf(env.endLoc[0]) + ", " + String.valueOf(env.endLoc[1]));
		boolean canMove = false;
		boolean complete = false;
		checkMove();

		canMove = moveLeft() || moveDown() || moveRight() || moveUp();
		
		// if no moves were available
		if(canMove == false) {
			undoMove();
		}
		else {
			updateLoc();
		}
		
		complete = env.endLoc[0] == loc[0] && env.endLoc[1] == loc[1];
		
		if(!complete && stepCount <= limitStep) {
			move();
		}
		else if(complete){
			checkMove();
			System.out.println("COMPLETE!!!");
		}
		else { // went over step limit
			System.out.println("Maze could not be solved");
		}
		return complete;
	}
	
	public boolean moveLeft() {
		boolean hasMoved = false;
		Byte[] moveCoord = {(byte) (locX-1), locY};

		try {
			if(!isBackwards(moveCoord) && env.Map.get(moveCoord[1]).get(moveCoord[0]) == 1) {
				System.out.println("Moving left");
				this.locX--;
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
				this.locX++;
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
				this.locY--;
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
				this.locY++;
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
