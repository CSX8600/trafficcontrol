package com.clussmanproductions.roadstuffreborn.util;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class LoopableTileEntitySound extends MovingSound implements ITickableSound {
	private ILoopableSoundTileEntity tileEntity;
	public LoopableTileEntitySound(SoundEvent sound, ILoopableSoundTileEntity tileEntity, BlockPos pos, float volume, float pitch)
	{
		super(sound, SoundCategory.BLOCKS);
		this.repeat = true;
		this.tileEntity = tileEntity;
		this.volume = volume;
		this.pitch = pitch;
		this.xPosF = pos.getX();
		this.yPosF = pos.getY();
		this.zPosF = pos.getZ();
	}

	@Override
	public void update() {
		TileEntity te = (TileEntity)tileEntity;
		
		if (te.isInvalid())
		{
			donePlaying = true;
			return;
		}
		
		donePlaying = tileEntity.isDonePlayingSound();
	}
}
