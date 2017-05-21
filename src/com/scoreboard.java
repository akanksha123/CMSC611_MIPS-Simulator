package com;

/**
 * Adding dependencies
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.Results;
import com.Label;
import com.FileParser;
import com.Instructions;
import com.Register;
import com.RegisterObject;
import com.Floating;

public class scoreboard {
	
	
	/**
	 * Initialize variables
	 */
	static int data_cache_count = 0;
	static int data_cache_hit_count = 0;
	
	ArrayList<Instructions> instructions = FileParser.instruction_file.instructions;
	ArrayList<InstructionUnit> adder_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> multiplier_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> divider_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> arithmetic_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> data_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> control_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> conditional_branch_units= new ArrayList<InstructionUnit>();
	ArrayList<InstructionUnit> unconditional_branch_units = new ArrayList<InstructionUnit>();
	
	
	ArrayList<Results> Result = new ArrayList<Results>();
	Map <String,Integer> registers = new HashMap<String,Integer>();
	int instcount = instructions.size();
	public scoreboard()
	{
			for(int k=0;k<configDao.FPDivideUnits;k++)
			{
				divider_units.add(new InstructionUnit());
			}
			for(int k=0;k<configDao.branch_count;k++)
			{
				conditional_branch_units.add(new InstructionUnit());
			}
			for(int k=0;k<1;k++)
			{
				unconditional_branch_units.add(new InstructionUnit());
			}
			for(int k=0;k<1;k++)
			{
				control_units.add(new InstructionUnit());
			}
			for(int k=0;k< configDao.FPAdderUnits;k++)
			{
				adder_units.add(new InstructionUnit());
			}
			for(int k=0;k<configDao.FPMultUnits;k++)
			{
				multiplier_units.add(new InstructionUnit());
			}
			for(int k=0;k<configDao.data_count;k++)
			{
				data_units.add(new InstructionUnit());
			}
			for(int k=0;k<configDao.arithmetic_count;k++)
			{
				arithmetic_units.add(new InstructionUnit());
			}
	}
	
	
	
public void execute(String f4) throws Exception {
		
		int initial_clock_cycle = 1;
		int  current_clock_cycle = initial_clock_cycle;
        int last_inst_index = 0;
        
        LinkedHashMap<Integer,Results> inst_result_list = new LinkedHashMap<Integer,Results>();
        LinkedHashMap<Integer,Results>current_result = new LinkedHashMap<Integer,Results>();
        Label label = new Label();
        Map<Integer,Integer>index_mapping = new HashMap<Integer,Integer>();
        boolean is_branch_set = false;
		
		Boolean is_ready_for_fetch = true;
		Boolean icache_flag = false;
		Boolean last_issue_flag = false;
		Boolean is_halt = false;
		Boolean is_branch_taken = false;
	
		
        ArrayList<Integer> final_read_list= new ArrayList<Integer>();
        ArrayList<Integer> final_execution_list= new ArrayList<Integer>();
        ArrayList<Integer> final_write_list= new ArrayList<Integer>();
        ArrayList<Integer> final_fetch_list= new ArrayList<Integer>();
        ArrayList<Integer> final_issue_list= new ArrayList<Integer>();
        
        	
		ICache.initialize();
		DataCache.initialize();
		final_fetch_list.add(last_inst_index);
		index_mapping.put(last_inst_index,current_clock_cycle);
		int last_issue_clock_cycle = current_clock_cycle;
		int bne_count =0;
		int halt_count =0;
		
		while(true)
        {
			ArrayList<Integer> intial_issue= new ArrayList<Integer>();
        	ArrayList<Integer> intial_fetch= new ArrayList<Integer>();
        	ArrayList<Integer> intial_read= new ArrayList<Integer>();
        	ArrayList<Integer> intial_execution= new ArrayList<Integer>();
        	ArrayList<Integer> intial_write= new ArrayList<Integer>();
        	
        	// breanking condition
        	if(final_issue_list.size() == 0 && final_fetch_list.size() == 0 && final_read_list.size() == 0 && final_execution_list.size() == 0 && final_write_list.size() == 0)
        	{
        		System.out.println("All instructions completed");
        		break;
        	}
        	
        	
        	// check whether branch is set
        	if(is_branch_set==false)
        	{
        		if(final_issue_list.size()!=0)
        		{
        			if(this.isHalt(final_issue_list.get(0)))
        			{
        				is_halt=true;
        			}
      
	        		if(is_halt)
	        		{
	        			final_fetch_list.clear();
	        			intial_fetch.clear();
	        		}
	        		else if(is_ready_for_fetch)
	        		{
	        			last_inst_index = last_inst_index+1;
	        			if(last_inst_index<this.instcount)
	        			{
	        				boolean isInsideFetch = false;
	        				for(int i=0;i<final_fetch_list.size();i++)
	        				{
		        				if(final_fetch_list.get(i).intValue() ==last_inst_index)  //New
		        				{
		        					isInsideFetch = true;
		        				}
	        				}
	        				if (isInsideFetch == false) {

		        				final_fetch_list.add(last_inst_index);
	        				}
	        			}
	        			is_ready_for_fetch=false;
	        		}
	        		
	        		// check waw flag
	        		Boolean waw_flag = isWAWFlagSet(final_issue_list.get(0));
	        		
	        		// check for struct flag
	                Boolean struct_flag = isStructuralFlag(final_issue_list.get(0));
	
	                if(waw_flag || struct_flag)
	                {
	                    if(waw_flag){
	                    	inst_result_list.get(final_issue_list.get(0)).setWaw_hazard('Y');
	                    	current_result.get(index_mapping.get(final_issue_list.get(0))).setWaw_hazard('Y');
	                    }
	                    else
	                    	set_register(final_issue_list.get(0));
	                    if(struct_flag)
	                    {
	                    	inst_result_list.get(final_issue_list.get(0)).setStruct_hazard('Y');
	                    	current_result.get(index_mapping.get(final_issue_list.get(0))).setStruct_hazard('Y');
	                    	
	                    }
	                }
	                else
	                {
	                	set_register(final_issue_list.get(0));
	                	inst_result_list.get(final_issue_list.get(0)).issue_stage = current_clock_cycle;
	                	current_result.get(index_mapping.get(final_issue_list.get(0))).issue_stage = current_clock_cycle;
	                	if(instructions.get(final_issue_list.get(0)).isReadyForRead)
 	                		intial_read.add(final_issue_list.get(0));
	                	if(isConditionalBranch(final_issue_list.get(0)))
	                	{
	                		bne_count = bne_count+1;
	                		is_branch_set=true;
	                	}
	                	final_issue_list.remove(final_issue_list.get(0));
	                	last_issue_clock_cycle = current_clock_cycle;
	                	
	                	
	                }
        		}
        	}
        	
       // fetch stage and add it to read
        	if(final_fetch_list.size()!=0)
        	{
        		if(icache_flag==false)
        		{
        			if(ICache.isResourceHeld(final_fetch_list.get(0)))
        			{
        				if(ICache.isPenaltyCompleted())
        					icache_flag=true;
        			}
        			else
        			{
        				if(ICache.cacheMiss(final_fetch_list.get(0)))
        				{
        					if(DataCache.isResourceBusy)
        						icache_flag=false;
        					else if(ICache.isICacheAvailable(final_fetch_list.get(0)))
        						ICache.addToCache(final_fetch_list.get(0));
        				}
        				else
        					icache_flag= true;
        			}
        		}
        		
        		if(last_issue_flag==false)
        		{
        			if(last_issue_clock_cycle==current_clock_cycle)
        				last_issue_flag=true;
        		}
        		if(last_issue_flag && icache_flag)
        		{
        			inst_result_list.put(final_fetch_list.get(0),new Results());
        			index_mapping.put(final_fetch_list.get(0),current_clock_cycle);
        			current_result.put(index_mapping.get(final_fetch_list.get(0)),new Results());
        			inst_result_list.get(final_fetch_list.get(0)).setInstruction(instructions.get(final_fetch_list.get(0)));
        			current_result.get(index_mapping.get(final_fetch_list.get(0))).setInstruction(instructions.get(final_fetch_list.get(0)));
        			inst_result_list.get(final_fetch_list.get(0)).fetch_stage = current_clock_cycle;
        			current_result.get(index_mapping.get(final_fetch_list.get(0))).fetch_stage = current_clock_cycle;
        			
        			if(is_branch_taken)
        			{
        				is_branch_taken = false;
        				intial_issue.add(final_fetch_list.get(0)); //new
        				last_issue_clock_cycle = current_clock_cycle+1;
        			}
        			else
        				intial_issue.add(final_fetch_list.get(0));
        			
        			final_fetch_list.remove(final_fetch_list.get(0));
        			icache_flag=false;
        			last_issue_flag = false;
        			is_ready_for_fetch=true;
        		}
        	}
        	
        	//Read stage starts 
        	if(final_read_list.size()!=0)
        	{
        		int i=0;
        		while(i<final_read_list.size())
        		{
        			boolean raw_flag = isRAWFlagSet(final_read_list.get(i));
                    if(raw_flag)
                    {
                    	inst_result_list.get(final_read_list.get(i)).setRaw_hazard('Y');
                    	current_result.get(index_mapping.get(final_read_list.get(i))).setRaw_hazard('Y');
                        i =i+1;
                    }
                    else
                    {    
                    
                    	inst_result_list.get(final_read_list.get(i)).read_stage = current_clock_cycle;
                    	current_result.get(index_mapping.get(final_read_list.get(i))).read_stage = current_clock_cycle;
                    	if(instructions.get(final_read_list.get(i)).isExecutable)
                    		intial_execution.add(final_read_list.get(i));
                    	if(isConditionalBranch(final_read_list.get(i)))
                    	{
                    		if(check_branch_condition(final_read_list.get(i)))
              				{
                    			String operandList = instructions.get(final_read_list.get(i)).operands_list;
                    			String[] operandListArray = operandList.split(" ");
                    			String current_label = operandListArray[operandListArray.length - 1];
                    			System.out.println("Current label is:"+current_label);
                    			int new_index = Label.getLabelByName(current_label);
                    			System.out.println("branch index is:"+new_index);
                    			intial_fetch.add(new_index);
                    			last_inst_index = new_index;
                    			last_issue_clock_cycle = current_clock_cycle+1;
                    			//System.out.println("last and current are:" +last_issue_clock_cycle+current_clock);
                    			is_branch_set=false;
                    			final_issue_list.clear();
                    			is_branch_taken=true;             			
                    		}
                    		else
                    			is_branch_set = false;
                    	}
                        final_read_list.remove(final_read_list.get(i));

        		   }
        	}
       }
       // end read
        	
       //Execute stage starts
        	int i=0;
        	while(i<final_execution_list.size())
        	{
        		boolean execution_flag=false;
        		if(Instructions.DATA_LIST.contains(instructions.get(final_execution_list.get(i)).opcode))
        			execution_flag = DCacheExecute(final_execution_list.get(i));
        		else
        			execution_flag = instructions.get(final_execution_list.get(i)).isFinished();
        		if(execution_flag)
        		{
        			inst_result_list.get(final_execution_list.get(i)).exec_stage = current_clock_cycle;
        			current_result.get(index_mapping.get(final_execution_list.get(i))).exec_stage = current_clock_cycle;
        			
        			parse_loaded_instruction_set(final_execution_list.get(i));
        			intial_write.add(final_execution_list.get(i));
        			final_execution_list.remove(final_execution_list.get(i));
        		}
        		else
        			i=i+1;
        	}
        	// end execute
        
        // write stage starts
        	i=0;
        	while(i<final_write_list.size())
        	{
        		inst_result_list.get(final_write_list.get(i)).write_stage = current_clock_cycle;
        		current_result.get(index_mapping.get(final_write_list.get(i))).write_stage = current_clock_cycle;
        		free_held_resource(final_write_list.get(i));
        		free_held_register(final_write_list.get(i));
        		final_write_list.remove(final_write_list.get(i));
        	}
        	current_clock_cycle = current_clock_cycle+1;
        	
        	final_fetch_list.addAll(intial_fetch);
        	//new to keep HLT at the end
        	if (final_fetch_list.size()>1) {
       		 int opcode = final_fetch_list.get(0);
       		 if(this.isHalt(final_fetch_list.get(0)))
    			{
    				final_fetch_list.remove(0);
    				
    				final_fetch_list.add(1,opcode);
    			}
       	}
        	
        	//new end
        	final_issue_list.addAll(intial_issue);
        	final_read_list.addAll(intial_read);
        	final_execution_list.addAll(intial_execution);
        	final_write_list.addAll(intial_write);
        	
        	
        	
        }//main while true
		
		
		
		
		System.out.println("Output is:");
		
		
		Iterator it = current_result.entrySet().iterator();
		int result_count = current_result.size();
	    /*while (it.hasNext()) {
	        Map.Entry<Integer, Results> pair = (Map.Entry)it.next();
	        
	        pair.getValue().printOutput();
	        it.remove(); 
	    }*/
	    System.out.println("Total number of access requests for instruction cache: "+ result_count);
	    System.out.println("Number of instruction cache hits:"+(result_count-bne_count-halt_count));
	    System.out.println("Total number of access requests for data cache: "+ (data_cache_count*2));
	    System.out.println("Number of data cache hits: "+ data_cache_hit_count);
	    
	    
	    //File write
	    final String instructionOutputFormatString = " %-25s  %-10s  %-10s  %-10s  %-10s  %-3s  %-3s  %-3s  %-2s";

		//System.out.println("Instruction"+"     "+"Fetch"+"  "+"Issue"+"  "+"Read"+"  "+"Execute"+"  "+"Write_Back"+"  "+"RAW"+"  "+"WAW"+"  "+"Struc");
		System.out.println(String.format(instructionOutputFormatString,"Instruction","Fetch","Issue","Read","Execute","Write_Back","RAW","WAW","Struc"));
		String header = String.format(instructionOutputFormatString,"Instruction","Fetch","Issue","Read","Execute","Write_Back","RAW","WAW","Struc");
		//bw.write("hello");
		//bw.write(String.format(instructionOutputFormatString,"Instruction","Fetch","Issue","Read","Execute","Write_Back","RAW","WAW","Struc"));
		System.out.println();
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f4, false))); 
		pw.println(header);
		while(it.hasNext())
		{
			Map.Entry<Integer, Results> pair = (Map.Entry)it.next();
			
			//bw.write(Result.get(k).toString());
			System.out.print(pair.getValue().toString());
			//String str=(String) Result.get(k);
			pw.println(pair.getValue().toString());
			System.out.println();
			it.remove();
			//bw.write()
		}
		pw.println();
		pw.print("Total no. of access requets for instruction cache :");
		pw.println(result_count);
		pw.print("No. of instruction cache hits :");
		pw.println((result_count-bne_count-halt_count));
		pw.print("Total number of access requests for data cache: ");
		pw.println((data_cache_count*2));
		pw.print("Number of data cache hits: ");
		pw.println(data_cache_hit_count);
		
		pw.close();

	    
        	
}// execute      	

	
private void parse_loaded_instruction_set(Integer index) throws Exception {
	// TODO Auto-generated method stub
	if(instructions.get(index).opcode.equals("LI"))
	{
    	if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
    	{
    		Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	}   
		int val = Integer.parseInt(instructions.get(index).getSrc_register().get(0));
		Register.value.get(instructions.get(index).getDest_register().get(0)).value = val;
	}	
	else if(instructions.get(index).opcode.equals("L.D"))
	{
    	if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
    	{
    		
    		Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	}
    	
    	data_cache_count += 1;
    	
    
    /*	int base = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
   	 base = base+instructions.get(index).offset;
   	 System.out.println("base is:"+base);
   	 base = base-256;
   	 base = base/4;
   	 base=base+256;
   	 base  = dataDao.getValueFromAddress(base);
   	System.out.println("Memory value loaded in reg is:"+base);
   	Floating.value.get(instructions.get(index).getDest_register().get(0)).value = base;*/
	}
	else if(instructions.get(index).opcode.equals("S.D"))
	{
    	if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
    	{
    		
    		Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	}
    	data_cache_count +=1;
    	
    	/*int base = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
    	 base = base+instructions.get(index).offset;
    	 System.out.println("base is:"+base);
    		base  = dataDao.getValueFromAddress(base);
    		System.out.println("Memory value loaded in reg is:"+base);
    		Floating.value.get(instructions.get(index).getDest_register().get(0)).value = base;*/
	}
	else if(instructions.get(index).opcode.equals("SW"))
	{
    	/*if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
    	{
    		
    		Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	}*/
    
    	int base = Register.value.get(instructions.get(index).getDest_register().get(0)).value;
    	 base = base+instructions.get(index).offset;
    	 System.out.println("base is:"+base);
    		//base  = dataDao.getValueFromAddress(base);
    		System.out.println("Memory value loaded in reg is:"+base);
    		int value = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
    		dataDao.setValueToAddress(base, value);
	}
	else if(instructions.get(index).opcode.equals("ADD.D"))
	{
		      	if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	        	{
	        		
	        		Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	        	} 
	     
	        	for(String register: instructions.get(index).source_register)
	        {
				if(Floating.value.containsKey(register)==false)
				{
				
					Floating.value.put(register,new RegisterObject());
				} 
			
			}
				int calc = Floating.value.get(instructions.get(index).getSrc_register().get(0)).value;
				int rval = Floating.value.get(instructions.get(index).getSrc_register().get(1)).value;
				
				calc = calc + rval;
				Floating.value.get(instructions.get(index).getDest_register().get(0)).value = calc;
	}
	else if(instructions.get(index).opcode.equals("SUB.D"))
	{
		      	if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	        	{
	        		
	        		Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	        	} 
	     
		      	for(String register: instructions.get(index).source_register)
	        {
				if(Floating.value.containsKey(register)==false)
				{
				
					Floating.value.put(register,new RegisterObject());
				} 
			
			}
				/*int calc = Floating.value.get(instructions.get(index).getSrc_register().get(0)).value;
				int rval = Floating.value.get(instructions.get(index).getSrc_register().get(1)).value;
				
				calc = calc - rval;
				Floating.value.get(instructions.get(index).getDest_register().get(0)).value = calc;*/
	}
	else if(instructions.get(index).opcode.equals("MUL.D")){
        
		if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
			Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	
		for(String register: instructions.get(index).source_register)
        {	
        	if(Floating.value.containsKey(register) == false)
        		Floating.value.put(register, new RegisterObject());
        		
        }
			/*int calc = Floating.value.get(instructions.get(index).getSrc_register().get(0)).value;
			int rval = Floating.value.get(instructions.get(index).getSrc_register().get(1)).value;
			
			calc = calc * rval;
			Floating.value.get(instructions.get(index).getDest_register().get(0)).value = calc;*/
	}
	else if(instructions.get(index).opcode.equals("DIV.D")){
        
		if(Floating.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
			Floating.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	
		for(String register: instructions.get(index).source_register)
        {	
        	if(Floating.value.containsKey(register) == false)
        		Floating.value.put(register, new RegisterObject());
        		
        }
			/*int calc = Floating.value.get(instructions.get(index).getSrc_register().get(0)).value;
			int rval = Floating.value.get(instructions.get(index).getSrc_register().get(1)).value;
			
			calc = calc * rval;
			Floating.value.get(instructions.get(index).getDest_register().get(0)).value = calc;*/
	}
	else if(instructions.get(index).opcode.equals("DADDI"))
	{
		if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	       Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	    if(Register.value.containsKey(instructions.get(index).getSrc_register().get(0))==false)
	      Register.value.put(instructions.get(index).getSrc_register().get(0),new RegisterObject());
	        			 
	    int calc = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		int rval = Integer.parseInt(instructions.get(index).getSrc_register().get(1).toString());
		calc = calc + rval;
		Register.value.get(instructions.get(index).getDest_register().get(0)).value = calc;
		
	}
	else if(instructions.get(index).opcode.equals("DSUBI"))
	{
		if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	       Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	    if(Register.value.containsKey(instructions.get(index).getSrc_register().get(0))==false)
	      Register.value.put(instructions.get(index).getSrc_register().get(0),new RegisterObject());
	        			 
	    int calc = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		int rval = Integer.parseInt(instructions.get(index).getSrc_register().get(1).toString());
		calc = calc - rval;
		//System.out.println("DSUB val is:"+);
		Register.value.get(instructions.get(index).getDest_register().get(0)).value = calc;
		
		
	}
	else if(instructions.get(index).opcode.equals("DSUB"))
	{
		if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	       Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	    
		
		for(String register: instructions.get(index).source_register)
        {	
        	if(Register.value.containsKey(register) == false)
        		Register.value.put(register, new RegisterObject());
        		
        }
		
	    int calc = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		int rval = Register.value.get(instructions.get(index).getSrc_register().get(1)).value;
		calc = calc - rval;
		Register.value.get(instructions.get(index).getDest_register().get(0)).value = calc;
		System.out.println("DSUB val is:"+Register.value.get(instructions.get(index).getDest_register().get(0)).value);
		
		
	}
	else if(instructions.get(index).opcode.equals("DADD"))
	{
		if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
	       Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
	    
		
		for(String register: instructions.get(index).source_register)
        {	
        	if(Register.value.containsKey(register) == false)
        		Register.value.put(register, new RegisterObject());
        		
        }
		
	    int calc = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		int rval = Register.value.get(instructions.get(index).getSrc_register().get(1)).value;
		calc = calc + rval;
		Register.value.get(instructions.get(index).getDest_register().get(0)).value = calc;
		
	}
	else if(instructions.get(index).opcode.equals("LW"))
	{
    	if(Register.value.containsKey(instructions.get(index).getDest_register().get(0))==false)
    	{
    		
    		Register.value.put(instructions.get(index).getDest_register().get(0),new RegisterObject());
    	}
    
    	/*int base = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
    	 base = base+instructions.get(index).offset;
    	 System.out.println("base is:"+base);
    	 base = base-256;
    	 base = base/4;
    	 base=base+256;
    	 base  = dataDao.getValueFromAddress(base);
    	System.out.println("Memory value loaded in reg is:"+base);
    	Register.value.get(instructions.get(index).getDest_register().get(0)).value = base;*/
	}
	
	
			
}

private boolean isRAWFlagSet(Integer index) {
	// TODO Auto-generated method stub
	boolean raw_flag=false;
	if(instructions.get(index).source_register.size()>0)
	{
		for(String register: instructions.get(index).source_register)
		{
			if(registers.containsKey(register))
			{
				if(registers.get(register) != index)
				{
					if(registers.get(register) > index)
						raw_flag = false;
					else
						return true;
				}
			}
		}
	}
	return raw_flag;
}

public void free_held_resource(Integer index) {
	if(instructions.get(index).instruction_type.equals("DATA"))
	{
		for(InstructionUnit unit: data_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("ARITHMETIC"))
	{
		for(InstructionUnit unit: arithmetic_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("MULTIPLIER"))
	{
		for(InstructionUnit unit: multiplier_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("ADDER"))
	{
		for(InstructionUnit unit: adder_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("DIVIDER"))
	{
		for(InstructionUnit unit: divider_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("CONDITIONAL_BRANCH"))
	{
		for(InstructionUnit unit: conditional_branch_units)
		{
			unit.setReady(index);
		}
	}
	if(instructions.get(index).instruction_type.equals("UNCONDITIONAL_BRANCH"))
	{
		for(InstructionUnit unit: unconditional_branch_units)
		{
			unit.setReady(index);
		}
	}
}
public void free_held_register(Integer integer) {
	// TODO Auto-generated method stub
	if(instructions.get(integer).getDest_register().size()>0)
	{
		if(registers.containsKey(instructions.get(integer).getDest_register().get(0)))
		{
			registers.remove(instructions.get(integer).dest_register.get(0));
		}
	}
	
}

private boolean DCacheExecute(Integer index) {
	boolean is_data_cache_continue = false;
	
	int[] address = get_memory_address(index);
	
	if(DataCache.isResourceHeld(index))
	{
		if(DataCache.isPenaltyCompleted())
			is_data_cache_continue = true;
	}
	else
	{
		if(DataCache.cacheMiss(address[0]))
		{
			if(ICache.isResourceBusy)
				return false;
			if(DataCache.isDCacheAvailable(index))
				DataCache.addToCache(index, address[0]);
		}
		else
		is_data_cache_continue = true;
	}
	if(is_data_cache_continue==false)
		return false;
	if(instructions.get(index).instruction_current_count==0)
		instructions.get(index).instruction_current_count = 1;
	
	if(address[1]==0)
	{
		instructions.get(index).instruction_current_count = 0;
	   return true;
	}
	
	if(instructions.get(index).instruction_current_count!=instructions.get(index).cycle_count)
	{
		instructions.get(index).instruction_current_count = instructions.get(index).instruction_current_count + 1;
		return false;
	}
	is_data_cache_continue=false;
	if(DataCache.isResourceHeld(index))
	{
		if(DataCache.isPenaltyCompleted())
			is_data_cache_continue = true;
	}
	else
	{
	
		if(DataCache.cacheMiss(address[1]))
		{
			if(ICache.isResourceBusy)
				return false;
			if(DataCache.isDCacheAvailable(index))
				DataCache.addToCache(index, address[1]);
		}
		else
			is_data_cache_continue=true;
	}
	if(is_data_cache_continue)
	{
		data_cache_hit_count += 1;
		instructions.get(index).instruction_current_count = 0 ;
	}
	return is_data_cache_continue;
}

private int[] get_memory_address(Integer index) {
	
	
	int[] temp_array = new int[2];
	temp_array[0] = temp_array[1] = 0;

	// check if instruction ld
	if(instructions.get(index).opcode.equals("L.D"))
	{
		temp_array[0] = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		temp_array[0] = temp_array[0] + instructions.get(index).offset;
		temp_array[1] = temp_array[0] + 4;
		return temp_array;
	}
	// check if instruction lw
	else if(instructions.get(index).opcode.equals("LW"))
	{
		temp_array[0] = Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		temp_array[0] = temp_array[0] + instructions.get(index).offset;
		return temp_array;
	}
	// check if instruction sD
	else if(instructions.get(index).opcode.equals("S.D"))
	{
		temp_array[0] = Register.value.get(instructions.get(index).getDest_register().get(0)).value;
		temp_array[0] = temp_array[0] + instructions.get(index).offset;
		temp_array[1] = temp_array[0] + 4;
		return temp_array;
	}
	// check if instruction sw
	else if(instructions.get(index).opcode.equals("SW"))
	{
		temp_array[0] = Register.value.get(instructions.get(index).getDest_register().get(0)).value;
		temp_array[0] = temp_array[0] + instructions.get(index).offset;
		return temp_array;
	}
	// return temp array
	return temp_array;
		
}

private boolean check_branch_condition(Integer index) {
	
	// check if equals condition
	if(instructions.get(index).opcode.equals("BEQ"))
	{
		if(Register.value.get(instructions.get(index).getDest_register().get(0)).value == Register.value.get(instructions.get(index).getSrc_register().get(0)).value)
			return true;
	}
	// check if not equals condition
	else if(instructions.get(index).opcode.equals("BNE"))
	{
		//if(Register.value.get(instructions.get(index).getSrc_register().get(0)).value != Register.value.get(instructions.get(index).getSrc_register().get(1)).value)
		int r1val=Register.value.get(instructions.get(index).getDest_register().get(0)).value;
		int r2val =Register.value.get(instructions.get(index).getSrc_register().get(0)).value;
		System.out.println("r1val:"+r1val+"r2val:"+r2val);
			if(r1val!=r2val)
			return true;
	}
	return false;
}

private Boolean isStructuralFlag(Integer index) {
	// TODO Auto-generated method stub
	
	if(instructions.get(index).instruction_type.equals("ARITHMETIC"))
	{
		for(InstructionUnit unit: arithmetic_units)
		{
			if(unit.isFree(index))
					return false;
		}
	}
	
	if(instructions.get(index).instruction_type.equals("DATA"))
	{
		for(InstructionUnit unit: data_units)
		{
			if(unit.isFree(index))
				return false;
		}
	}
	
	
	
	if(instructions.get(index).instruction_type.equals("MULTIPLIER"))
	{
		for(InstructionUnit unit: multiplier_units)
		{
			if(unit.isFree(index))
				return false;
		}
	}
	if(instructions.get(index).instruction_type.equals("ADDER"))
	{
		for(InstructionUnit unit: adder_units)
		{
			if(unit.isFree(index))
				return false;
		}
	}
	if(instructions.get(index).instruction_type.equals("DIVIDER"))
	{
		for(InstructionUnit unit: divider_units)
		{
			if(unit.isFree(index))
					return false;
		}
	}
	if(instructions.get(index).instruction_type.equals("CONDITIONAL_BRANCH"))
	{
		for(InstructionUnit unit: conditional_branch_units)
		{
			if(unit.isFree(index))
				return false;
		}
	}
	if(instructions.get(index).instruction_type.equals("UNCONDITIONAL_BRANCH"))
	{
		for(InstructionUnit unit: unconditional_branch_units)
		{
			if(unit.isFree(index))
			return false;
		}
	}
	if(instructions.get(index).instruction_type.equals("CONTROL"))
	{
		for(InstructionUnit unit: control_units)
		{
			if(unit.isFree(index))
			return false;
		}
	}
	return true;
}

private Boolean isWAWFlagSet(Integer integer) {
	
	if(instructions.get(integer).dest_register.size()>0)
	{
		if(registers.containsKey(instructions.get(integer).getDest_register().get(0)))
		{
			if(registers.get(instructions.get(integer).getDest_register().get(0)) != integer)
				return true;
		}
	}
	return false;
}

private void set_register(Integer integer) {
	if(instructions.get(integer).getDest_register().size()>0)
	{
		if(Instructions.STORE_LIST.contains(instructions.get(integer).opcode)==false)
		{
			if(Instructions.BRANCH_COND_LIST.contains(instructions.get(integer).opcode)==false)
			{
				registers.put(instructions.get(integer).getDest_register().get(0),integer);
			}
		}
	}
	
}

private boolean isConditionalBranch(Integer index) {
	// TODO Auto-generated method stub
	return Instructions.BRANCH_COND_LIST.contains(instructions.get(index).opcode);
}

public boolean isHalt(int index)
{
	return Instructions.CONTROL_LIST.contains(instructions.get(index).opcode);
}

}
