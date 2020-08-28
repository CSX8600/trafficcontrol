package com.clussmanproductions.trafficcontrol.tileentity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public class StreetSign implements INBTSerializable<NBTBase> {
	private EnumFacing facing = EnumFacing.NORTH;
	private StreetSignColors color = StreetSignColors.Green;
	private String text = "";
	private boolean isNew = true;
	
	public EnumFacing getFacing() {
		return facing;
	}

	public void setFacing(EnumFacing facing) {
		this.facing = facing;
	}

	public StreetSignColors getColor() {
		return color;
	}

	public void setColor(StreetSignColors color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public boolean getIsNew()
	{
		return isNew;
	}
	
	public void setIsNew(boolean isNew)
	{
		this.isNew = isNew;
	}

	public int getTextColor()
	{
		switch(getColor())
		{
			case Yellow:
				return 0x000000;
			case Blue:
			case Green:
			case Red:
				return 0xFFFFFF;
		}
		
		return 0xFFFFFF;
	}
	
	public enum StreetSignColors
	{
		Green(0, 1, 1),
		Red(1, 1, 2),
		Blue(2, 1, 3),
		Yellow(3, 1, 4);
		
		private int index;
		private int col;
		private int row;
		private StreetSignColors(int index, int col, int row)
		{
			this.index = index;
			this.col = col;
			this.row = row;
		}
		
		public int getIndex() { return index; }
		public int getCol() { return col; }
		public int getRow() { return row; }
		
		public static StreetSignColors getByIndex(int index)
		{
			for(StreetSignColors color : StreetSignColors.values())
			{
				if (color.getIndex() == index)
				{
					return color;
				}
			}
			
			return null;
		}
	}

	@Override
	public NBTBase serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("facing", getFacing().getHorizontalIndex());
		tag.setInteger("color", getColor().getIndex());
		tag.setString("text", getText());
		tag.setBoolean("isNew", getIsNew());
		
		return tag;
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		NBTTagCompound compound = (NBTTagCompound)nbt;
		
		setFacing(EnumFacing.getHorizontal(compound.getInteger("facing")));
		setColor(StreetSignColors.getByIndex(compound.getInteger("color")));
		setText(compound.getString("text"));
		setIsNew(compound.getBoolean("isNew"));
	}
}
