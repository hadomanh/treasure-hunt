package ngakinz.application;

import com.google.gson.Gson;

import ngakinz.generator.*;

public class ApplicationProvider {
	
	public static String HOST = "localhost";
	
	public static int PORT = 3000;
	
	public static int CAPACITY = 2;
	
	public static int MAX_SPEED = 3;
	
	public static Gson gson = new Gson();
	
	public static GameGenerator generator = new FromFileGenerator("6.txt");
	
	public static void reset() {
		generator = new FromFileGenerator("6.txt");
	}
	
//	public static GameGenerator generator = new RandomGenerator(CAPACITY, 1, 5, 5);
//	
//	public static void reset() {
//		generator = new RandomGenerator(CAPACITY, 1, 5, 5); 
//	}

}
