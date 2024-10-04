import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// Contains the functionality of a 'Stack' system allowing move history to be undone
public class ByteStack{
	String type;
	public List<Byte[]> struct;
	public int index = -1;
	
	// Construct with either LinkedList or ArrayList
	public ByteStack(String type){
		this.type = type;
		if(this.type == "ArrayList") {
			struct = new ArrayList<Byte[]>();
		}
		else if(this.type == "LinkedList") {
			struct = new LinkedList<Byte[]>();
		}
	}

	//Add new item to stack
	public void Push(Byte[] x) {
		this.struct.add(x);
		this.index ++;
	}
	
	//Remove most recent item and display the contents
	public Byte[] Pop() throws ArrayIndexOutOfBoundsException{
		if(this.index >= 0) {
			Byte[] x = this.struct.get(this.index);
			this.struct.remove(this.index);
			this.index --;
			return x;
		}
		else {
			throw new ArrayIndexOutOfBoundsException("There are no more elements to pop in the stack.");
		}
	}
	//Display most recent item
	public Byte[] Peek() {
		Byte[] x = this.struct.get(this.index);
		return x;
	}
	
	public boolean isEmpty() {		
		return this.index < 0;
	}
	
	public int size() {
		return this.struct.size();
	}
}