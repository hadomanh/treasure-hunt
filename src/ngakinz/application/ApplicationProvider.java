package ngakinz.application;

import com.google.gson.Gson;

import ngakinz.generator.FromFileGenerator;
import ngakinz.generator.GameGenerator;

public class ApplicationProvider {
	
	public static String HOST = "localhost";
	
	public static int PORT = 3000;
	
	public static int CAPACITY = 2;
	
	public static Gson gson = new Gson();
	
	public static GameGenerator generator = new FromFileGenerator("4.txt");

}
