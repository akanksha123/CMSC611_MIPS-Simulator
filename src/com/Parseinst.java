package com;


import java.io.BufferedReader;
import com.Label;
import com.Instructions;
import java.io.FileReader;
import java.util.ArrayList;

public class Parseinst {
	//static Map<Integer,String> instructions = new TreeMap<Integer,String>();
	 static ArrayList<Instructions> instructions = new ArrayList<Instructions>();
	
	public static void parse(String s) throws Exception
	{
		
		String str=null;

		int instcount=0;
		String inst_unit[];
		System.out.println("file name is:"+s);

		FileReader fr = new FileReader(s);
		BufferedReader bf = new BufferedReader(fr);
		int i = 0;
		while((str=bf.readLine())!=null)
		{
			Instructions instruction = new Instructions(i++);
			Label l = new Label();
			str=str.trim();
			String inst = str;
			inst=inst.replaceAll(","," ");
			inst=inst.replaceAll(" +"," ");
			inst_unit = inst.split(" ",2);
			str=str.replaceAll(","," ");
			str=str.replaceAll(" +"," ");
			//String str1[] = str.split(" ",2);
			if(str.contains(":"))
			{
				int loopindex = str.lastIndexOf(":");
				//Store the label of the loop
				instruction.setLabel(str.substring(0,loopindex));
				//Label.label_instruction_mapping.put(str.substring(0,loopindex), instcount);
				l.addNewLabel(str.substring(0,loopindex), instcount);
				String sep1 = str.substring((loopindex+1));
				sep1 = sep1.trim();
				String[] separate = sep1.split(" ",2); 
				instruction.setOpcode(separate[0]);
				//instruction.setOperands_list(str.substring(loopindex+1)); //Drop : from instruction
				instruction.setOperands_list(separate[1]);
				str.trim();
				
			}
			else	
			{			
				String opcode = inst_unit[0].trim().toUpperCase();
				instruction.setOpcode(opcode);
				if(instruction.opcode.equals("HLT")==false)	
				{	
					String operands_list = inst_unit[1].trim().toUpperCase();
					instruction.setOperands_list(operands_list);
				}
			}
			
			instruction.setText(inst);
			if (instruction.opcode.equals("HLT") == false) {
				instructions.add(instruction);
			}
			
			
			System.out.println("inst is:"+instruction.instruction_current_count+" "+instruction.operands_list+" "+instruction.instruction_label+" "+instruction.opcode+" "+instruction.operands_list+" "+instruction.offset+" "+instruction.instruction_type+" "+instruction.cycle_count);
			instcount++;
		}

	}
	
	public ArrayList<Instructions> getinst()
	{
		return instructions;
	}
	
}
