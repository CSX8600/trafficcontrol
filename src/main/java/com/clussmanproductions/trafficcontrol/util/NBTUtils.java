package com.clussmanproductions.trafficcontrol.util;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils {
	public static float getFloatOrDefault(NBTTagCompound tag, String key, float defaultValue)
	{
		if (tag.hasKey(key))
		{
			return tag.getFloat(key);
		}
		
		return defaultValue;
	}
	
	public static int getIntOrDefault(NBTTagCompound tag, String key, int defaultValue)
	{
		if (tag.hasKey(key))
		{
			return tag.getInteger(key);
		}
		
		return defaultValue;
	}
}
