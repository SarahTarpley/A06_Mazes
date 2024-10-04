import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
public class Maze {

	public Byte[] mazeDimensions;
	public Byte[] startLoc;
	public Byte[] endLoc;
	public List<ArrayList<Byte>> Map = importMaze();
	
	public List<ArrayList<Byte>> importMaze(){
		List<ArrayList<Byte>> MazeMap = new ArrayList<ArrayList<Byte>>();
		String delimiter = " ";

		// Try-with-resources: Automatically close the BufferedReader after use
		try (BufferedReader br = new BufferedReader(new FileReader("maze.txt"))) {
			Pattern regDigit = Pattern.compile("\\d+"); // RegEx pattern to verify the item is a digit
			String line;  // Variable to hold each line read
			// Read each subsequent line
			while ((line = br.readLine()) != null) {
				// Split the line into values using the defined delimiter (comma)
				String[] values = line.split(delimiter);
				if(values.length == 2) {
					if(this.mazeDimensions == null) {
						this.mazeDimensions = new Byte[]{Byte.parseByte(values[0]), Byte.parseByte(values[1])};
					}
					else if(this.startLoc == null) {
						this.startLoc = new Byte[]{Byte.parseByte(values[1]), Byte.parseByte(values[0])};
					}
					else if(this.endLoc == null) {
						this.endLoc = new Byte[]{Byte.parseByte(values[1]), Byte.parseByte(values[0])};
					}
				}
				else {
					System.out.println(line);
					ArrayList<Byte> MazeRow = new ArrayList<Byte>();
	
					for (String value : values) {
						if(regDigit.matcher(value).matches()) {
							MazeRow.add(Byte.valueOf(value));	
						}
					}
					if(MazeRow.size() > 0) {
						MazeMap.add(MazeRow);
					}
				}
			}
		}
		catch (NumberFormatException ne) {
			System.out.println("Maximum maze dimensions are 127x127");
		}
		catch (Exception e){
			System.out.println(e);
		}
		//System.out.println(moveHistory.size());
		return MazeMap;
	}
}
