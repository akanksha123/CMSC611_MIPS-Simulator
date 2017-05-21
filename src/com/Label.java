package com;


import java.util.HashMap;
import java.util.Map;

public class Label {
	
	 static Map<String,Integer> label_mapping = new HashMap<String,Integer>();

	public static void addNewLabel(String substring, int instcount) {
		label_mapping.put(substring, instcount);
		// TODO Auto-generated method stub	
	}
	
	public static Integer getLabelByName(String label)
	{
		return label_mapping.get(label);
	}
}
