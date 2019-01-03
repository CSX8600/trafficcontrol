package com.clussmanproductions.roadstuffreborn.util;

import java.lang.reflect.Field;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialCustomTransparency extends Material {
	public MaterialCustomTransparency()
	{
		super(MapColor.AIR);
		setNoPushMobility();
		
		try
		{
			Field field = getClass().getSuperclass().getDeclaredField("isTranslucent");
			field.setAccessible(true);
			field.set(this, true);
		}
		catch (Exception ex)
		{
			return;
		}
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public boolean blocksLight() {
		return false;
	}
}
