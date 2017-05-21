package com;


 public class configDao {

	 
		//Dao class to mention all exec cycles
	 
	 	//public configDao(){
		 /*static int FPAdderUnits=2;
		 static int FPMultUnits=1;
		 static int FPAdderCycles=2;
		 static int FPMultCycles=30;
		 static int FPDivideCycles=50;
		 static int FPDivideUnits = 0;
		 static int i_cache_block_count=4;
		 static int i_cache_block_size=4;
		 static int arithmetic_count=1;
		 static int arithmetic_cycles = 1;
		 static int data_count = 1;
		 static int data_cycles = 2;
		 static int branch_count=1;
		 static int branch_cycles=1;
		 static int penalty=3;
		 static int control_cycles=1;
		 static int control_count=1;*/
		 
	 static int FPAdderUnits=0;
	 static int FPMultUnits=0;
	 static int FPAdderCycles=0;
	 static int FPMultCycles=0;
	 static int FPDivideCycles=0;
	 static int FPDivideUnits =0;
	 static int i_cache_block_count=0;
	 static int i_cache_block_size=0;
	 static int arithmetic_count=1;
	 static int arithmetic_cycles = 1;
	 static int data_count = 1;
	 static int data_cycles = 2;
	 static int branch_count=1;
	 static int branch_cycles=1;
	 static int penalty=3;
	 static int control_cycles=1;
	 static int control_count=1;
		 
		
		 
	 	//}
	 	
		 public int getArithmetic_count() {
			return arithmetic_count;
		}
		public int getArithmetic_cycles() {
			return arithmetic_cycles;
		}
		public int getBranch_count() {
			return branch_count;
		}
		public void setBranch_count(int branch_count) {
			this.branch_count = branch_count;
		}
		public int getBranch_cycles() {
			return branch_cycles;
		}
		public void setBranch_cycles(int branch_cycles) {
			this.branch_cycles = branch_cycles;
		}
		public int getData_count() {
			return data_count;
		}
		public int getData_cycles() {
			return data_cycles;
		}
		
		
		public void setI_cache_block_count(int i_cache_block_count) {
			this.i_cache_block_count = i_cache_block_count;
		}
		public void setI_cache_block_size(int i_cache_block_size) {
			this.i_cache_block_size = i_cache_block_size;
			this.penalty = 3 * this.i_cache_block_size;
		}
		public void setArithmetic_count(int arithmetic_count) {
			this.arithmetic_count = arithmetic_count;
		}
		public void setArithmetic_cycles(int arithmetic_cycles) {
			this.arithmetic_cycles = arithmetic_cycles;
		}
		public void setData_count(int data_count) {
			this.data_count = data_count;
		}
		public void setData_cycles(int data_cycles) {
			this.data_cycles = data_cycles;
		}
		
		public int getFPDivideCycles() {
			return FPDivideCycles;
		}
		public void setFPDivideCycles(int fPDivideCycles) {
			FPDivideCycles = fPDivideCycles;
		}
		
		public static int getFPAdderCycles() {
			return FPAdderCycles;
		}
		public static void setFPAdderCycles(int fPAdderCycles) {
			FPAdderCycles = fPAdderCycles;
			//cd.setFPAdderCycles(fPAdderCycles);
			System.out.println("No. of cycles in adder units set in Dao:"+FPAdderCycles);
		}
				 
		 public int getFPAdderUnits() {
			return FPAdderUnits;
		}
		public void setFPAdderUnits(int fPAdderUnits) {
			FPAdderUnits = fPAdderUnits;
			System.out.println("No. of adder units set in Dao:"+FPAdderUnits);
		}
		
		 public int getFPMultUnits() {
			return FPMultUnits;
		}
		public void setFPMultUnits(int fPMultUnits) {
			FPMultUnits = fPMultUnits;
		}
		
		 public int getFPDivideUnits() {
			return FPDivideUnits;
		}
		public void setFPDivideUnits(int fPDivideUnits) {
			FPDivideUnits = fPDivideUnits;
		}
		public int getFPMultCycles() {
			return FPMultCycles;
		}
		public void setFPMultCycles(int fPMultCycles) {
			FPMultCycles = fPMultCycles;
		}
}
