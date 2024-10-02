import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Driver{	
	public static void main(String[] args){
		Maze env = new Maze();
		Traveler walker = new Traveler(env);
		walker.move();
  }
}
