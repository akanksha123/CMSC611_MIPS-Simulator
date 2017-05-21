package com;


import java.io.BufferedReader;
import java.io.FileReader;

import com.dataDao;


public class Parsedata {
	public static void parse(String s) throws Exception
	{
		String str=null;
		dataDao dd = new dataDao();
		int count=0;
		int address = 0x100;
		System.out.println("file name is:"+s);
		//Open a config file and read line by line
		FileReader fr = new FileReader(s);
		BufferedReader bf = new BufferedReader(fr);
		while((str=bf.readLine())!=null)
		{
			count++;
			str=str.trim();
			int value = Integer.parseInt(str, 2);
			dataDao.setStringAddress(address,str);
			dataDao.setValueToAddress(address,value);
			address++;
		}

	}
}