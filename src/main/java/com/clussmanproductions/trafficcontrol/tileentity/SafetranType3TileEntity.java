package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModSounds;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SafetranType3TileEntity extends BellBaseTileEntity {

	@SideOnly(Side.CLIENT)
	@Override
	protected SoundEvent getSoundEvent() {
		return ModSounds.safetranType3Event;
	}
	
}
