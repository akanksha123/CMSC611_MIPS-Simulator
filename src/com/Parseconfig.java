package com;

import java.io.BufferedReader;
import java.io.FileReader;

import com.configDao;
public class Parseconfig {

	
		
		public void parse(String s) throws Exception
		{
			String str=null;
			String func_unit[],func_no[];
			configDao cd = new configDao();
			int linecount=0;
			System.out.println("file name is:"+s);
			//Open a config file and read line by line
			FileReader fr = new FileReader(s);
			BufferedReader bf = new BufferedReader(fr);
			while((str=bf.readLine())!=null)
			{
				linecount++;
				str=str.trim();
				System.out.println(str);
				func_unit = str.split(":");
				String start = func_unit[0].toUpperCase();
				
				switch(start)
				{
					case "FP ADDER" : 
										func_no=func_unit[1].split(",");
										//System.out.println(func_no[1].trim());
										cd.setFPAdderUnits(Integer.parseInt(func_no[0].trim()));
										cd.setFPAdderCycles(Integer.parseInt(func_no[1].trim()));
										break;
										
					case "FP MULTIPLIER" :
										func_no=func_unit[1].split(",");
										//System.out.println(func_no[1].trim());
										cd.setFPMultUnits(Integer.parseInt(func_no[0].trim()));
										cd.setFPMultCycles(Integer.parseInt(func_no[1].trim()));
										break;
									
					case "FP DIVIDER" :
										func_no=func_unit[1].split(",");
										//System.out.println(func_no[1]);
										cd.setFPDivideUnits(Integer.parseInt(func_no[0].trim()));
										cd.setFPDivideCycles(Integer.parseInt(func_no[1].trim()));
										break;
					case "I-CACHE" :
						func_no=func_unit[1].split(",");
						cd.setI_cache_block_count(Integer.parseInt(func_no[0].trim()));
						cd.setI_cache_block_size(Integer.parseInt(func_no[1].trim()));
						break;
				}
				
			}
		}

	
}
