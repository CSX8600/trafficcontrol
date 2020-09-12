package com.clussmanproductions.trafficcontrol.tileentity;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class SignTileEntity extends TileEntity {

	private int type = 0;
	private int variant = 0;
	
	private final int MAXTYPE = 5;
	public static final ImmutableMap<Integer, Integer> MAXVARIANTS = ImmutableMap.<Integer, Integer>builder()
			.put(0, 113)
			.put(1, 144)
			.put(2, 97)
			.put(3, 88)
			.put(4, 166)
			.put(5, 95)
			.build();
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		type = compound.getInteger("type");
		variant = compound.getInteger("variant");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {		
		compound.setInteger("type", type);
		compound.setInteger("variant", variant);
		
		return super.writeToNBT(compound);
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setVariant(int variant)
	{
		this.variant = variant;
	}
	
	public int getVariant()
	{
		return variant;
	}
	
	public ResourceLocation getTexture()
	{
		String signName = getSignTypeName(type);
		String resourceName = ModTrafficControl.MODID + ":textures/blocks/sign/" + signName + "/" + signName + variant + ".png";
		return new ResourceLocation(resourceName);
	}
	
	public static String getSignTypeName(int type)
	{
		switch(type)
		{
			case 0:
				return "circle";
			case 1:
				return "diamond";
			case 2:
				return "misc";
			case 3:
				return "rectangle";
			case 4:
				return "square";
			case 5:
				return "triangle";
		}
		
		return null;
	}
	
	public static String getBackSignName(int type, int variant)
	{
		if (type != 2)
		{
			return getSignTypeName(type) + "0";
		}
		
		if (variant == 0)
		{
			return "misc0b";
		}
		else if (variant >= 1 && variant <= 4)
		{
			return "misc1b";
		}
		else if (variant >= 5 && variant <= 8)
		{
			return "misc2b";
		}
		else if ((variant >= 9 && variant <= 32) || (variant >= 76 && variant <= 98))
		{
			return "misc3b";
		}
		else if (variant >= 33 && variant <= 40)
		{
			return "misc4b";
		}
		else if (variant >= 41 && variant <= 45)
		{
			return "misc5b";
		}
		else if (variant >= 46 && variant <= 47)
		{
			return "misc6b";
		}
		else if (variant >= 48 && variant <= 50)
		{
			return "misc7b";
		}
		else
		{
			return "misc8b";
		}
	}

	public static int getSignTypeByName(String type)
	{
		switch(type)
		{
			case "circle":
				return 0;
			case "diamond":
				return 1;
			case "misc":
				return 2;
			case "rectangle":
				return 3;
			case "square":
				return 4;
			case "triangle":
				return 5;
		}
		
		return 0;
	}
	
	public String getFriendlySignName()
	{
		switch(type)
		{
			case 0:
				return "Circle";
			case 1:
				return "Diamond";
			case 2:
				return "Miscellaneous";
			case 3:
				return "Rectangle";
			case 4:
				return "Square";
			case 5:
				return "Triangle";
		}
		
		return "Unknown";
	}

	public void prevType()
	{
		if (type == 0)
		{
			type = 5;
		}
		else
		{
			type--;
		}
		
		variant = 0;
	}
	
	public void nextType()
	{
		if (type == MAXTYPE)
		{
			type = 0;
		}
		else
		{
			type++;
		}
		
		variant = 0;
	}
	
	public void prevVariant()
	{
		if (variant == 0)
		{
			int maxVariant = MAXVARIANTS.get(type);
			variant = maxVariant;
		}
		else
		{
			variant--;
		}
	}
	
	public void nextVariant()
	{
		int maxVariant = MAXVARIANTS.get(type);
		
		if (variant == maxVariant)
		{
			variant = 0;
		}
		else
		{
			variant++;
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger("type", type);
		compound.setInteger("variant", variant);
		
		return compound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		this.type = tag.getInteger("type");
		this.variant = tag.getInteger("variant");
		IBlockState state = world.getBlockState(getPos());
			
		world.notifyBlockUpdate(getPos(), state, state, 3);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		
		NBTTagCompound nbt = pkt.getNbtCompound();
		handleUpdateTag(nbt);
	}
}
