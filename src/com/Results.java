package com;


public class Results {
			
			String current_stage = "";
		    int current_stage_count = 0;
		    String instruction_type = "";
		    Instructions instruction;
		    int fetch_stage = 0;
		    int issue_stage = 0;
		    int read_stage = 0;
		    int exec_stage = 0;
		    
		    public void setExec_stage(int exec_stage) {
				this.exec_stage = exec_stage;
			}

			int write_stage = 0;
		    char raw_hazard = 'N';
		    char waw_hazard = 'N';
		    char struct_hazard = 'N';
		    String instruction_text = " ";
		    
		    public int getFetch_stage() {
				return fetch_stage;
			}

			public int getExec_stage() {
				return exec_stage;
			}

			public String getInstruction_text() {
				return instruction_text;
			}

			
			public void setFetch_stage(int fetch_stage) {
				this.fetch_stage = fetch_stage;
			}

			public void setInstruction_type(String instruction_type) {
				this.instruction_type = instruction_type;
			}
			
			public void setIssue_stage(int issue_stage) {
				this.issue_stage = issue_stage;
			}
			
			public void setRead_stage(int read_stage) {
				this.read_stage = read_stage;
			}
			
			public void setWrite_stage(int write_stage) {
				this.write_stage = write_stage;
			}
			
			public void setRaw_hazard(char raw_hazard) {
				this.raw_hazard = raw_hazard;
			}
			
			public void setWaw_hazard(char waw_hazard) {
				this.waw_hazard = waw_hazard;
			}
			
			public void setStruct_hazard(char struct_hazard) {
				this.struct_hazard = struct_hazard;
			}
			
			/*public void setInstruction_text(String instruction_text) {
				this.instruction_text = instruction.text;
			}*/
			public void setInstruction(Instructions instruction2) {
				this.instruction = instruction2;
				this.instruction_text = instruction2.text;
			}
			public void setInstruction_text(String text) {
				// TODO Auto-generated method stub
				this.instruction_text = text;
			}
			final String instructionOutputFormatString = " %-25s  %-10s  %-10s  %-10s  %-10s  %-10s  %-5s  %-5s  %-5s";
			@Override
		    public String toString()
		    {
				//return (this.instruction_text +"  "+ this.fetch_stage+"       "+this.issue_stage+"       "+this.read_stage+"       "+this.exec_stage+"       "+this.write_stage+"       "+this.raw_hazard+"       "+this.waw_hazard+"       "+this.struct_hazard);
				return String.format(instructionOutputFormatString,this.instruction_text,this.fetch_stage,this.issue_stage,this.read_stage,this.exec_stage,this.write_stage,this.raw_hazard,this.waw_hazard,this.struct_hazard);
		    }

			public void printOutput() {
				// TODO Auto-generated method stub
				System.out.println(this.instruction_text+"  "+this.fetch_stage+"  "+this.issue_stage+"  "+this.read_stage+"  "+this.exec_stage+"  "+this.write_stage+"  "+this.raw_hazard+"  "+this.waw_hazard+"  "+this.struct_hazard);
			}

}
