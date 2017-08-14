package main;
import java.io.File;

import gui.Authentication;
import gui.Register;

public class Main {  
	public static final String FILEPATH = "password.txt";
	public static final String JSONPATH = "data.json";
	
	public static void main(String[] args) {
		File pf = new File("password.txt");
		if (pf.exists()){
			Authentication frame = new Authentication();
			frame.setVisible(true);
		}else{
			Register frame = new Register();
			frame.setVisible(true);
		}
	}

}
