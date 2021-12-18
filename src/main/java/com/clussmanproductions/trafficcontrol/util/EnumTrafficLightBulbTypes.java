package com.clussmanproductions.trafficcontrol.util;

public enum EnumTrafficLightBulbTypes {
	Red(0),
	Yellow(1),
	Green(2),
	RedArrowLeft(3),
	YellowArrowLeft(4),
	GreenArrowLeft(5),
	Cross(6),
	DontCross(7),
	RedArrowRight(8),
	YellowArrowRight(9),
	GreenArrowRight(10),
	NoRightTurn(11),
	NoLeftTurn(12);
	
	private int index = -1;
	private EnumTrafficLightBulbTypes(int index)
	{
		this.index = index;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public static EnumTrafficLightBulbTypes get(int index)
	{
		for(EnumTrafficLightBulbTypes bulbType : EnumTrafficLightBulbTypes.values())
		{
			if (bulbType.index == index)
			{
				return bulbType;
			}
		}
		
		return null;
	}
}
