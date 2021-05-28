package mainview;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.MainController;
import model.Contract;

public class Application {
//	   public static final String API_KEY_FILE_NAME = "/Users/yuanleslie/Desktop/Final_FIT3077/Source Code/src/apiKey"; // change this to your file's name
	// public static final String API_KEY_FILE_NAME = "Source Code/apiKey"; // change this to your file's name
	public static final String API_KEY_FILE_NAME = "C:/Users/chaup/project/Source Code/src/apiKey"; // change this to your file's name
	public static String myApiKey;
	public static final String rootUrl = "https://fit3077.com/api/v2";
    
	public static void main(String[] args) throws FileNotFoundException {

		Scanner reader = new Scanner(new File(API_KEY_FILE_NAME));
		myApiKey = reader.nextLine();
		reader.close();
		
		///// Delete all contracts created from Assignment 2 //////
//		Contract.deleteAllContracts();

		new MainController();
	}
}
