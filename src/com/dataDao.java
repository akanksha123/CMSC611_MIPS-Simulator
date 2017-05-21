package com;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class dataDao {

	//private Map<Integer, Integer> memoryDataMap = new TreeMap<Integer, Integer>();
	static public Map<Integer, String> address_string_value  = new HashMap<Integer, String>();
	static public Map<Integer, Integer> address_actual_value  = new HashMap<Integer, Integer>();
	static int baseAddress = 256;
    
	
	
    public static void setValueToAddress(int address, int data) throws Exception
    {
        address_actual_value.put(address, data);
    }

   
    public static int getValueFromAddress(int address) throws Exception
    {
        
    	return address_actual_value.get(address);
    }


	public static void setStringAddress(int address, String str) {
		address_string_value.put(address, str);
		
	}
}
