package com.clussmanproductions.trafficcontrol.tileentity;

import java.util.ArrayList;
import java.util.UUID;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.signs.Sign;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class SignTileEntity extends TileEntity {
	
	private int typeLegacy = -1;
	private int variantLegacy = -1;
	
	private UUID id = null;
	private ArrayList<String> textLines = null;
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		typeLegacy = compound.getInteger("type");
		variantLegacy = compound.getInteger("variant");
		if (compound.hasKey("signid"))
		{
			id = NBTUtil.getUUIDFromTag(compound.getCompoundTag("signid"));
		}
		
		if (textLines != null)
		{
			textLines.clear();
		}
		
		if (compound.hasKey("text0"))
		{
			textLines = new ArrayList<>();
			int i = 0;
			while(compound.hasKey("text" + i))
			{
				textLines.add(compound.getString("text" + i));
				i++;
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {		
		compound.setInteger("type", typeLegacy);
		compound.setInteger("variant", variantLegacy);
		
		if (id != null)
		{
			compound.setTag("signid", NBTUtil.createUUIDTag(id));
		}
		
		if (textLines != null)
		{
			for(int i = 0; i < textLines.size(); i++)
			{
				compound.setString("text" + i, textLines.get(i));
			}
		}
		
		return super.writeToNBT(compound);
	}
	
	public void setTypeLegacy(int type)
	{
		this.typeLegacy = type;
	}
	
	public int getTypeLegacy()
	{
		return typeLegacy;
	}
	
	public void setVariantLegacy(int variant)
	{
		this.variantLegacy = variant;
	}
	
	public int getVariantLegacy()
	{
		return variantLegacy;
	}
	
	public void setID(UUID id)
	{
		this.id = id;
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public String getTextLine(int index)
	{
		if (textLines == null || index >= textLines.size())
		{
			return null;
		}
		
		return textLines.get(index);
	}
	
	public void setTextLine(int index, String text)
	{
		if (textLines == null)
		{
			textLines = new ArrayList<String>();
		}
		
		if (textLines.size() <= index)
		{
			for(int i = textLines.size(); i <= index; i++)
			{
				textLines.add(null);
			}
		}
		
		textLines.set(index, text);
	}
	
	public void clearTextLines()
	{
		if (textLines != null)
		{
			textLines.clear();
		}
	}
	
	public Sign getSign()
	{
		Sign sign = null;
		if (typeLegacy != -1)
		{
			sign = ModTrafficControl.instance.signRepo.getSignByTypeVariant(getSignTypeName(typeLegacy), variantLegacy);
		}
		else if (id != null)
		{
			sign = ModTrafficControl.instance.signRepo.getSignByID(id);
		}
		
		if (sign == null)
		{
			return ModTrafficControl.instance.signRepo.getSignByID(Sign.DEFAULT_ERROR_SIGN);
		}
		
		return sign;
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
	
	public static int getSignTypeNumber(String name)
	{
		switch(name)
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
		
		return -1;
	}
	
	public String getFriendlySignName()
	{
		if (typeLegacy != -1)
		{
			switch(typeLegacy)
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
		}
		else if (id != null)
		{
			Sign sign = ModTrafficControl.instance.signRepo.getSignByID(id);
			if (sign != null)
			{
				return ModTrafficControl.instance.signRepo.getFriendlyTypeName(sign.getType());
			}
		}
		return "Unknown";
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		NBTTagCompound compound = super.getUpdateTag();
		compound.setInteger("type", typeLegacy);
		compound.setInteger("variant", variantLegacy);
		if (id != null)
		{
			compound.setTag("signid", NBTUtil.createUUIDTag(id));
		}
		
		if (textLines != null)
		{
			for(int i = 0; i < textLines.size(); i++)
			{
				compound.setString("text" + i, textLines.get(i));
			}
		}
		
		return compound;
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		
		this.typeLegacy = tag.getInteger("type");
		this.variantLegacy = tag.getInteger("variant");
		if (tag.hasKey("signid"))
		{
			this.id = NBTUtil.getUUIDFromTag(tag.getCompoundTag("signid"));
		}
		
		if (textLines != null)
		{
			textLines.clear();
		}
		
		if (tag.hasKey("text0"))
		{
			textLines = new ArrayList<>();
			int i = 0;
			while(tag.hasKey("text" + i))
			{
				textLines.add(tag.getString("text" + i));
				i++;
			}
		}
		
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

	@Override
	public double getMaxRenderDistanceSquared() {
		return ModTrafficControl.MAX_RENDER_DISTANCE;
	}
}
