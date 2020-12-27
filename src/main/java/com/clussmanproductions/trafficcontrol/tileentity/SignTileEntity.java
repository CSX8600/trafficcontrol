package com.clussmanproductions.trafficcontrol.tileentity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SignTileEntity extends TileEntity {

	private int type = 0;
	private int variant = 0;
	
	private final int MAXTYPE = 5;
	public static ImmutableMap<Tuple<Integer, Integer>, Sign> SIGNS_BY_TYPE_VARIANT;
	public static ImmutableMap<Integer, Integer> MAX_VARIANTS_BY_TYPE;
	
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
		if (variant > MAX_VARIANTS_BY_TYPE.get(getType()))
		{
			switch(getType()) // Use new error textures because it's fun
			{
				case 0:
					variant = 114;
					break;
				case 1:
					variant = 162;
					break;
				case 2:
					variant = 119;
					break;
				case 3:
					variant = 91;
					break;
				case 4:
					variant = 168;
					break;
				case 5:
					variant = 96;
					break;
			}
		}
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
		else if ((variant >= 1 && variant <= 4) || 
				(variant >= 100 && variant <= 101) ||
				variant == 113 ||
				variant == 119)
		{
			return "misc1b";
		}
		else if ((variant >= 5 && variant <= 8) ||
				(variant >= 117 && variant <= 118))
		{
			return "misc2b";
		}
		else if ((variant >= 9 && variant <= 32) || 
				(variant >= 76 && variant <= 99) ||
				(variant >= 102 && variant <= 105) ||
				(variant >= 108 && variant <= 109) ||
				variant == 112 ||
				(variant >= 114 && variant <= 116))
		{
			return "misc3b";
		}
		else if ((variant >= 33 && variant <= 40) || 
				(variant >= 106 && variant <= 107) ||
				(variant >= 110 && variant <= 111))
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

	private static boolean signsInitialized = false;
	@SideOnly(Side.CLIENT)
	public static void initializeSigns()
	{
		if (signsInitialized)
		{
			return;
		}
		
		try
		{
			ResourceLocation signJsonRL = new ResourceLocation("trafficcontrol:misc/signs.json");
			InputStream jsonStream = Minecraft.getMinecraft().getResourceManager().getResource(signJsonRL).getInputStream();
			InputStreamReader streamReader= new InputStreamReader(jsonStream);
			
			JsonArray jsonArray = new JsonParser().parse(streamReader).getAsJsonArray();
			Iterator<JsonElement> arrayIterator = jsonArray.iterator();
			
			HashMap<Tuple<Integer, Integer>, Sign> signsbyTypeVariant = new HashMap<>();
			HashMap<Integer, Integer> maxVariantsByType = new HashMap<>();
			
			while(arrayIterator.hasNext())
			{
				JsonElement element = arrayIterator.next();
				if (!element.isJsonObject())
				{
					continue;
				}
				
				JsonObject obj = element.getAsJsonObject();
				String name = obj.get("name").getAsString();
				String type = obj.get("type").getAsString();
				int typeID = SignTileEntity.getSignTypeByName(type);
				int variant = obj.get("variant").getAsInt();
				
				int currentMaxVariant = maxVariantsByType.getOrDefault(typeID, 0);
				if (variant > currentMaxVariant)
				{
					maxVariantsByType.put(typeID, variant);
				}
				
				String tooltip = null;
				String note = null;
				
				if (obj.has("tooltip"))
				{
					tooltip = obj.get("tooltip").getAsString();
				}
				
				if (obj.has("note"))
				{
					note = obj.get("note").getAsString();
				}
				
				if ((note == null || note.equals("")) && tooltip != null && !tooltip.equals(""))
				{
					note = tooltip;
				}
				
				Sign newImage = new Sign(
						new ResourceLocation("trafficcontrol:textures/blocks/sign/" + type + "/" + type + variant + ".png"),
						name,
						typeID,
						variant,
						tooltip,
						note);
				
				signsbyTypeVariant.put(new Tuple<Integer, Integer>(typeID, variant), newImage);				
			}
			
			SIGNS_BY_TYPE_VARIANT = ImmutableMap.copyOf(signsbyTypeVariant);
			MAX_VARIANTS_BY_TYPE = ImmutableMap.copyOf(maxVariantsByType);
			
			signsInitialized = true;
		}
		catch(Exception ex)
		{
			FMLCommonHandler.instance().raiseException(ex, "Failed to load signs", true);
		}
	}
	
	public static class Sign
	{
		private ResourceLocation imageResourceLocation;
		private String name;
		private int type;
		private int variant;
		private String tooltip;
		private String note;
		
		public Sign(ResourceLocation imageRL, String name, int type, int variant, String toolTip, String note)
		{
			imageResourceLocation = imageRL;
			this.name = name;
			this.type = type;
			this.variant = variant;
			this.tooltip = toolTip;
			this.note = note;
		}
		
		public String getName() { return name; }
		
		public int getType() { return type; }
		
		public int getVariant() { return variant; }
		
		public ResourceLocation getImageResourceLocation() { return imageResourceLocation; }
		
		public String getToolTip() { return tooltip; }
		
		public String getNote() { return note; }
		
		public static class TestComparator implements Comparator<Tuple<Integer, Integer>>
		{
			@Override
			public int compare(Tuple<Integer, Integer> arg0, Tuple<Integer, Integer> arg1) {
				if (arg0 == null && arg1 == null)
				{
					return 0;
				}
				
				if (arg0 == null && arg1 != null)
				{
					return -1;
				}
				
				if (arg1 == null && arg0 != null)
				{
					return 1;
				}
				
				if (arg0.getFirst() != arg1.getFirst())
				{
					if (arg0.getFirst() > arg1.getFirst())
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else
				{
					if (arg0.getSecond() == arg1.getSecond())
					{
						return 0;
					}
					else
					{
						if (arg0.getSecond() > arg1.getSecond())
						{
							return 1;
						}
						else
						{
							return -1;
						}
					}
				}
			}
			
		}
	}
}
