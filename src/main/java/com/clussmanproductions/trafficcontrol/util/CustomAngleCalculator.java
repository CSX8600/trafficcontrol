package com.clussmanproductions.trafficcontrol.util;

import java.util.Arrays;

import net.minecraft.util.math.MathHelper;

public class CustomAngleCalculator {
	private static final int[] metas = new int[] { 2, 4, 5, 6, 3, 7, 8, 9, 0, 10, 11, 12, 1, 13, 14, 15 };
	private static final int[] rotations = new int[] { 8, 12, 0, 4, 1, 2, 3, 5, 6, 7, 9, 10, 11, 13, 14, 15 };
	private static final int[] cardinals = new int[] { 0, 4, 8, 12 };
	
	public static int rotationToMeta(int rotation)
	{
		return metas[rotation];
	}
	
	public static int metaToRotation(int meta)
	{
		return rotations[meta];
	}
	
	public static int getRotationForYaw(float yaw)
	{
		return MathHelper.floor((double)((yaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
	}
	
	public static boolean isCardinal(int rotation)
	{
		return Arrays.stream(cardinals).anyMatch(num -> num == rotation);
	}
	
	public static boolean isNorthSouth(int rotation)
	{
		return isNorth(rotation) || isSouth(rotation);
	}
	
	public static boolean isNorth(int rotation)
	{
		return rotation >= 14 || rotation < 2;
	}
	
	public static boolean isEast(int rotation)
	{
		return rotation >= 2 && rotation < 6; 
	}
	
	public static boolean isSouth(int rotation)
	{
		return rotation >= 6 && rotation < 10; 
	}
	
	public static boolean isWest(int rotation)
	{
		return rotation >= 10 && rotation < 14; 
	}
}
