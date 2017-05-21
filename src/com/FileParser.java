package com;
import com.Parseconfig;
import com.Parsedata;
import com.Parseinst;


public class FileParser {
	public static Parseinst instruction_file;
	public static Parseconfig config_file;
	public static Parsedata data_file;
	
	public static void parser(String dataPath, String configPath, String instructionPath)
	{
		config_file = new Parseconfig();
		try {
			config_file.parse(configPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		instruction_file = new Parseinst();
		try {
			instruction_file.parse(instructionPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data_file = new Parsedata();
		try {
			data_file.parse(dataPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
