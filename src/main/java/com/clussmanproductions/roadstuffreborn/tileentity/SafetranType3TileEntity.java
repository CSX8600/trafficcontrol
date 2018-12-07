package com.clussmanproductions.roadstuffreborn.tileentity;

import com.clussmanproductions.roadstuffreborn.ModSounds;

import net.minecraft.util.SoundEvent;

public class SafetranType3TileEntity extends BellBaseTileEntity {

	@Override
	protected SoundEvent getSoundEvent() {
		return ModSounds.safetranType3Event;
	}
	
}
