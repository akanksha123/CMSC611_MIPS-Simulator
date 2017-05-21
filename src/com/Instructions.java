package com;
import java.util.ArrayList;
import com.Label;
import java.util.Arrays;
import com.configDao;
public class Instructions {
		
		/**
		 * Initialize variables
		 */
		Label l  = new Label();
		String instruction_label = "";
		String instruction_type=" ";
	    
	    int offset;
	    Boolean isReadyForRead=true;
	    Boolean isExecutable=true;
	    int instruction_current_count=0;
	    int cycle_count=0;
	    int instructions_count;
	    String opcode = "";
	    String operands_list = "";
	    String text=" ";
	    public ArrayList<String> source_register;
	    public ArrayList<String> dest_register;
	    public static ArrayList<String> DATA_LIST = new ArrayList<>(Arrays.asList("L.D", "S.D","LW", "SW"));
	    public static ArrayList<String> ARITHMETIC_LIST = new ArrayList<>(Arrays.asList("DADD", "DADDI", "DSUB", "DSUBI", "AND", "ANDI", "OR", "ORI", "LI", "LUI"));
	    public static ArrayList<String> MULTIPLIER_LIST = new ArrayList<>(Arrays.asList("MUL.D"));
	    public static ArrayList<String> ADDER_LIST = new ArrayList<>(Arrays.asList("ADD.D", "SUB.D"));
	    public static ArrayList<String> DIVIDER_LIST = new ArrayList<>(Arrays.asList("DIV.D"));
	    public static ArrayList<String> BRANCH_UNCOND_LIST = new ArrayList<>(Arrays.asList("J"));
	    public static ArrayList<String> BRANCH_COND_LIST = new ArrayList<>(Arrays.asList("BEQ", "BNE", "BGTZ", "BLTZ", "BGEZ", "BLEZ"));
	    public static ArrayList<String> CONTROL_LIST = new ArrayList<>(Arrays.asList(("HLT")));
	    public static ArrayList<String> STORE_LIST = new ArrayList<>(Arrays.asList("S.D", "SW"));
		
	    
	    
	    
		public void set_configuration()
		{
			if(this.BRANCH_COND_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.branch_cycles;
				this.instruction_type = "CONDITIONAL_BRANCH";
			}
			if(this.BRANCH_UNCOND_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.branch_cycles;
				this.instruction_type = "UNCONDITIONAL_BRANCH";
				
			}
			if(this.CONTROL_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.control_cycles;
				this.instruction_type = "CONTROL";
			}
			if(this.DATA_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.data_cycles;
				this.instruction_type = "DATA";
			}
			if(this.ARITHMETIC_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.arithmetic_cycles;
				this.instruction_type = "ARITHMETIC";
			}
			if(this.MULTIPLIER_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.FPMultCycles;
				this.instruction_type = "MULTIPLIER";
			}
			if(this.ADDER_LIST.contains(this.opcode))
			{
				System.out.println("set adder cycles are:"+configDao.FPAdderCycles);
				//this.total=configDao.FPAdderCycles;
				this.cycle_count=configDao.getFPAdderCycles();
				this.instruction_type = "ADDER";
			}
			if(this.DIVIDER_LIST.contains(this.opcode))
			{
				this.cycle_count=configDao.FPDivideCycles;
				this.instruction_type = "DIVIDER";
			}
			
		}
		
		public Instructions(int number)
		{
			this.instructions_count=number;
			this.l  = new Label();
			this.instruction_label = "";
			this.instruction_type=" ";
			this.opcode = "";
			this.operands_list = "";
		    this.text=" ";
		    this.offset = 0;
		    this.isReadyForRead=true;
		    this.isExecutable=true;
		    this.instruction_current_count=0;
		    this.cycle_count=0;
		    source_register = new ArrayList<String>();
		    dest_register = new ArrayList<String>();
		}
	    public boolean isFinished()
	    {
	    	this.instruction_current_count = this.instruction_current_count+1;
	    	if(this.instruction_current_count==this.cycle_count)
	    	{
	    		this.instruction_current_count=0;
	    		return true;
	    	}
	    	else
	    		return false;
	    		
	    }
	    public void setSource_register(String source_register) {
			this.source_register.add(source_register);
		}
		public void setDest_register(String dest_register) {
			this.dest_register.add(dest_register);
			System.out.println("Dest register set to:"+dest_register);
		}
		public String getLabel() {
			return instruction_label;
		}
		public void setLabel(String label) {
			this.instruction_label = label;
			l.addNewLabel(this.instruction_label,this.instructions_count);
			
		}
		public String getOpcode() {
			return opcode;
		}
		public void setOpcode(String opcode) {
			this.opcode = opcode;
			set_configuration();
			if(CONTROL_LIST.contains(this.opcode)|| BRANCH_UNCOND_LIST.contains(this.opcode))
				this.isReadyForRead=false;
			if(BRANCH_COND_LIST.contains(this.opcode))
				this.isExecutable = false;
			
		}
		public String getOperands_list() {
			return operands_list;
		}
		public void setOperands_list(String operands_list) {
			this.operands_list = operands_list;
			String[] destop;
			destop = operands_list.split(" ",2);
			if(this.opcode.equals("SW")|| this.opcode.equals("S.D"))
			{
				source_register.add(destop[0]);
				offset = Integer.parseInt(destop[1].substring(0,destop[1].indexOf("(")));
				dest_register.add(destop[1].substring(destop[1].indexOf("(")+1,destop[1].indexOf(")")));
				
			}
			else{
			dest_register.add(destop[0]);
			//In case of LI its only immediate value
			if(this.opcode.equals("LI"))
			source_register.add(destop[1]);
			
			else{
			String[] src = destop[1].split(" ",2);
			//regex = re.compile("([0-9])+\(([a-zA-Z\d]+)\)")
			
			for(int i=0;i<src.length;i++)
			{
				
				if(src[i].contains("(")==false)
				{
					source_register.add(src[i]);
				}
				else{
				String s = src[i];
				if(s.contains("("))
				{
					offset = Integer.parseInt(s.substring(0,s.indexOf("(")));
				source_register.add(s.substring(s.indexOf("(")+1,s.indexOf(")")));
				}
				}
			}
			
			}
			}
		}
		/*public void setOperands_list(String operands_list) {
			this.operands_list = operands_list;
			String[] destop;
			destop = operands_list.split(" ",2);
			dest_register.add(destop[0]);
			StringTokenizer st = new StringTokenizer(destop[1]," ");
			while(st.hasMoreTokens())
			{
				if(st.nextToken().contains("[0-9]+")==false)
				{
					if(st.nextToken().contains("("))
					{
						offset = Integer.parseInt(st.toString().substring(0,st.toString().indexOf("(")));
						source_register.add(st.nextToken().substring(st.nextToken().indexOf("(")+1,st.nextToken().indexOf(")")));
					}
					else
						source_register.add(st.nextToken());
				}
				else
				{
					source_register.add(st.nextToken());
				}
			}
		}*/

		public ArrayList<String> getDest_register()
		{
			return dest_register;
		}
		public ArrayList<String> getSrc_register()
		{
			return source_register;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
}
