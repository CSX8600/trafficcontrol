package com.clussmanproductions.roadstuffreborn.tileentity;

import com.clussmanproductions.roadstuffreborn.ModSounds;

import net.minecraft.util.SoundEvent;

public class SafetranMechanicalTileEntity extends BellBaseTileEntity {

	@Override
	protected SoundEvent getSoundEvent() {
		return ModSounds.safetranMechanicalEvent;
	}

}
