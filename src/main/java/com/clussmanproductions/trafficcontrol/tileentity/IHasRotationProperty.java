package com.clussmanproductions.trafficcontrol.tileentity;

import net.minecraft.util.EnumFacing;

public interface IHasRotationProperty {
	/*
	 * Returns an EnumFacing if such a property exists, otherwise returns null
	 */
	default EnumFacing getRotationFacing() { return null; }
	/*
	 * Does nothing if this does not use EnumFacing
	 */
	default void setRotationFacing(EnumFacing facing) { }
	
	/*
	 * Returns an int for rotation if such a property exists, otherwise returns -1
	 */
	default int getRotationInt() { return -1; }
	/*
	 * Does nothing if this does not use int for rotation
	 */
	default void setRotationInt(int rotation) {}
}
