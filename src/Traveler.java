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
		
		System.out.println("Current location is X: " + loc[0] + ", Y: " + loc[1]);
		System.out.println("left is : "+String.valueOf(left));
		System.out.println("right is : "+String.valueOf(right));
		System.out.println("up is : "+String.valueOf(up));
		System.out.println("down is : "+String.valueOf(down));
		
		// display the maze
		for(ArrayList<Byte> row : env.Map) {
			System.out.println(row);
		}
		
		return (new Byte[]{left, right, up, down});
		
	}
	// move to the first availability found
	public void move() {
		
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
						moveRight();
						break;
				case 2:
						moveUp();
						break;
				case 3:
						moveDown();
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
	
	public void undoMove() {
		System.out.println(this.moveHistory.size());
		Byte[] moveBack = this.moveHistory.Pop();
		locX = moveBack[0]; locY = moveBack[1];
		updateLoc();
	}
}
