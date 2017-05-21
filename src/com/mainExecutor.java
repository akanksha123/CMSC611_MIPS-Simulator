package com;

import java.util.ArrayList;
import com.Parseconfig;
import com.scoreboard;


public class mainExecutor{
		
	public static void main(String[] args) throws Exception
	{
		
	
		mainExecutor me = new mainExecutor();
		parse(args[0],args[1],args[2],args[3]);
	}

	public static void parse(String f1,String f2,String f3,String f4) throws Exception
	{
		ArrayList<Instructions>imap = new ArrayList<Instructions>();
		Parseinst parseinst= new Parseinst();
		Parsedata parsedata = new Parsedata();
		Parseconfig parseconfig = new Parseconfig();
		parseconfig.parse(f3);
		parsedata.parse(f2);
		parseinst.parse(f1);
		
		
		
		scoreboard sc = new scoreboard();
		sc.execute(f4);
		//sc.printInstructions();
		
		//Scoreboard Execution
		//scoreboard sb = new scoreboard();
	}
}
