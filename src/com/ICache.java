package com;

import java.util.HashMap;
import java.util.Map;

public class ICache {
	static boolean isResourceBusy = false;
    static int index = -1;
    static Map<Integer,Integer> block_map = null;
    static int number_of_blocks = configDao.i_cache_block_count;
    static int block_size = configDao.i_cache_block_size;
    static int current_cycle_count = 0;
    static int penalty = block_size * 3;

    public static void initialize()
    {
    	isResourceBusy = false;
        index = -1;
        if(block_map == null)
        	block_map = new HashMap<Integer,Integer>();
        number_of_blocks = configDao.i_cache_block_count;
        block_size = configDao.i_cache_block_size;
        current_cycle_count = 0;
        penalty = block_size * 3;

    }
    public static boolean isResourceHeld(int index)
	{
		if(ICache.index == index)
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
	
    public static void addToCache(int index){
    	ICache.index = index;
        isResourceBusy = true;
        addBlockAddress(index);
	}
   public static boolean cacheMiss(int index){
        
	   
       int block_number = (index/block_size);
       block_number = (block_number % number_of_blocks);
       int base_index = findNewBase(index);
       if(block_map.containsKey(block_number)==false || block_map.get(block_number) != base_index)
       return true;
       
       return false;
        
   }
    public static void addBlockAddress(int address)
    {
    	int block_number = (index / block_size);
        block_number = (block_number % number_of_blocks);
        int base_index = findNewBase(index); 
        block_map.put(block_number,base_index);
        
    }
    
    public static boolean isICacheAvailable(int ind)
    {
        if(isResourceBusy==false)
            return true;
        if(index == ind)
            return true;
        return false;
    }

    
    public static int findNewBase(int address)
    {
        int address1 = address / block_size;
        address1 = address1 * block_size;
        return address1;
    }

}
