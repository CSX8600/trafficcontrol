package com.clussmanproductions.trafficcontrol.item;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrafficLightCard extends Item {
	public ItemTrafficLightCard()
	{
		setRegistryName(ModTrafficControl.MODID, "traffic_light_card");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_card");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
		setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String unlocalizedName = getUnlocalizedName() + ".";
		switch(stack.getMetadata())
		{
			case 1:
				unlocalizedName += "tier2";
				break;
			case 2:
				unlocalizedName += "tier3";
				break;
			case 3:
				unlocalizedName += "creative";
				break;
			default:
				unlocalizedName += "tier1";
		}
		
		return unlocalizedName;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab == CreativeTabs.SEARCH || tab == ModTrafficControl.CREATIVE_TAB)
		{
			for(int i = 0; i < 4; i++)
			{
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		for (int i = 0; i < 4; i++)
		{
			String suffix = "_tier" + (i + 1);
			if (i == 3)
			{
				suffix = "_creative";
			}
			
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(new ResourceLocation(ModTrafficControl.MODID, getRegistryName().getResourcePath() + suffix), "inventory"));
		}
	}
	
	public static int getMaxTrafficLights(int metadata)
	{
		switch(metadata)
		{
			case 1:
				return Config.trafficLightCardT2Capacity;
			case 2:
				return Config.trafficLightCardT3Capacity;
			case 3:
				return Integer.MAX_VALUE;
			default:
				return Config.trafficLightCardT1Capacity;
		}
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {		
		if (worldIn.isRemote || !(worldIn.getBlockState(pos).getBlock() instanceof BlockBaseTrafficLight))
		{
			return EnumActionResult.PASS;
		}
		
		ItemStack heldStack;
		if (hand == EnumHand.MAIN_HAND)
		{
			heldStack = player.getHeldItemMainhand();
		}
		else
		{
			heldStack = player.getHeldItemOffhand();
		}
		
		NBTTagCompound stackTag = heldStack.getTagCompound();
		if (stackTag == null)
		{
			stackTag = new NBTTagCompound();
			heldStack.setTagCompound(stackTag);
		}
		int maxTrafficLights = getMaxTrafficLights(heldStack.getMetadata());
		
		boolean didRemoveLight = false;
		
		HashSet<String> keysToRemove = new HashSet<>();
		for(String key : stackTag.getKeySet())
		{
			if (!key.startsWith("light"))
			{
				continue;
			}
			
			if (stackTag.getLong(key) == pos.toLong())
			{
				keysToRemove.add(key);
				didRemoveLight = true;
			}
		}
		
		if (didRemoveLight)
		{
			for(String keyToRemove : keysToRemove)
			{
				stackTag.removeTag(keyToRemove);
			}
			
			player.sendMessage(new TextComponentString(String.format("Removed traffic light at [%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())));
			return EnumActionResult.SUCCESS;
		}
		
		int totalLightsAdded = 0;
		HashSet<Integer> addedNumbers = new HashSet<>();
		for(String key : stackTag.getKeySet())
		{
			if (key.startsWith("light") && stackTag.getLong(key) != 0)
			{
				totalLightsAdded++;
				try
				{
					addedNumbers.add(Integer.parseInt(key.substring(5)));
				}
				catch(Exception ex) {}
			}
		}
		
		if (totalLightsAdded >= maxTrafficLights)
		{
			player.sendMessage(new TextComponentString("Card is full! Remove a traffic light or upgrade this card."));
		}
		else
		{
			int nextEmptyNumber = 0;
			while(addedNumbers.contains(nextEmptyNumber))
			{
				nextEmptyNumber++;
			}
			stackTag.setLong("light" + nextEmptyNumber, pos.toLong());
			totalLightsAdded++;
			
			String format = "Added traffic light at [%s, %s, %s].";
			if (maxTrafficLights != Integer.MAX_VALUE)
			{
				format += " %s/%s slots remaining.";
			}
			TextComponentString message = new TextComponentString(String.format(format, pos.getX(), pos.getY(), pos.getZ(), maxTrafficLights - totalLightsAdded, maxTrafficLights));
			// Figure out how to be able to copy block pos ID (#toLong())?
			player.sendMessage(message);
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (stack.getMetadata() == 3)
		{
			super.addInformation(stack, worldIn, tooltip, flagIn);
			return;
		}
		
		NBTTagCompound tag = stack.getTagCompound();
		int totalUsed = 0;
		if (tag != null)
		{
			for(String item : tag.getKeySet().stream().filter(k -> k.startsWith("light")).collect(Collectors.toList()))
			{
				totalUsed++;
			}
		}
		int maxAvailable = getMaxTrafficLights(stack.getMetadata());
		tooltip.add("" + TextFormatting.DARK_PURPLE + TextFormatting.ITALIC + totalUsed + "/" + maxAvailable + " slots filled");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
