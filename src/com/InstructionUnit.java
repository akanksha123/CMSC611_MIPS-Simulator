package com;


public class InstructionUnit {
	boolean isUnitBusy=false;
	int index =-1;
	
	public void setReady(int index)
	{
		this.index=index;
		this.isUnitBusy=false;
		this.index=-1;
	}
	
	public boolean isFree(int index)
	{
		if(isUnitBusy==false)
		{
			this.index = index;
			this.isUnitBusy=true;
			return true;
		}
		else
		{
			if(this.index==index)
				return true;
		}
		return false;
	}
	

}
