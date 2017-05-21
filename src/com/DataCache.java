package com;


import java.util.HashMap;
import java.util.Map;

public class DataCache {
	
		static boolean isResourceBusy = false;
		    static int index = -1;
		    static Map<Integer,Integer> block_map = null;
		    static int number_of_blocks = 4;
		    static int block_size = 4;
		    static int current_cycle_count = 0;
		    static int penalty = block_size * 3;
		

		public static void initialize()
		{
			isResourceBusy = false;
		     index = -1;
		     if (block_map == null)
		    block_map = new HashMap<Integer,Integer>();
		     number_of_blocks = 4;
		     block_size = 4;
		     current_cycle_count = 0;
		     penalty = block_size * 3;
		
		}
		public static boolean isResourceHeld(int index)
		{
			if(DataCache.index == index)
				return true;
			else
			return false;
		}
		public static boolean isPenaltyCompleted(){
	        current_cycle_count += 1;
	        if (current_cycle_count == penalty){
	            current_cycle_count = 0;
	            index = -1;
	            isResourceBusy = false;
	            return true;
	        }
	        return false;
		}
		
	    public static void addToCache(int index, int address){
	        DataCache.index = index;
	        DataCache.isResourceBusy = true;
	        addBlockAddress(address);
		}
	   public static boolean cacheMiss(int address){
	        int base_address = findNewBase(address);
	        int block_number = ((base_address - 256) / 16);
	        block_number = (block_number % 4);
	        if(block_map.containsKey(block_number)==false || block_map.get(block_number) != base_address)
	        	return true;
	        
	        return false;
	   }
	    public static void addBlockAddress(int address)
	    {
	        int base_address = findNewBase(address);
	        int block_number = ((base_address - 256) / 16);
	        block_number = (block_number % 4);
	        block_map.put(block_number,base_address);
	    }
	    
	    public static boolean isDCacheAvailable(int index)
	    {
	        if(isResourceBusy==false)
	            return true;
	        if(DataCache.index == index)
	            return true;
	        return false;
	    }

	    
	    public static int findNewBase(int address)
	    {
	        int address1 = address-((address - 256) % 16);
	        return address1;
	    }
}
