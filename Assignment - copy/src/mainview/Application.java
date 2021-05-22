package mainview;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Application {
	public static final String API_KEY_FILE_NAME = "/Users/yuanleslie/Desktop/FIT3077_A3/Assignment - copy/src/APIManager"; // change this to your file's name
	public static String myApiKey;
	public static final String rootUrl = "https://fit3077.com/api/v2";
    
	public static void main(String[] args) throws FileNotFoundException {

		Scanner reader = new Scanner(new File(API_KEY_FILE_NAME));
		myApiKey = reader.nextLine();
		reader.close();

		new Controller();
	}
}
