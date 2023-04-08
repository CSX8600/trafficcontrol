package com.clussmanproductions.trafficcontrol.item;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTrafficLightCard extends Item {
	private final int maxTrafficLights;
	public ItemTrafficLightCard(int maxTrafficLights)
	{
		this.maxTrafficLights = maxTrafficLights;
		setRegistryName("traffic_light_card");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_card");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
		
		boolean didRemoveLight = false;
		for(int i = 0; i < maxTrafficLights; i++)
		{
			if (stackTag.hasKey("light" + i) && stackTag.getLong("light" + i) == pos.toLong())
			{
				stackTag.removeTag("light" + i);
				didRemoveLight = true;
			}
		}
		
		if (didRemoveLight)
		{
			player.sendMessage(new TextComponentString(String.format("Removed traffic light at [%s, %s, %s]", pos.getX(), pos.getY(), pos.getZ())));
			return EnumActionResult.SUCCESS;
		}
		
		boolean didAddLight = false;
		int totalLightsAdded = 0;
		for(int i = 0; i < maxTrafficLights; i++)
		{
			if (stackTag.hasKey("light" + i) && stackTag.getLong("light" + i) != 0)
			{
				totalLightsAdded++;
			}
			
			if ((!stackTag.hasKey("light" + i) || stackTag.getLong("light" + i) == 0) && !didAddLight)
			{
				stackTag.setLong("light" + i, pos.toLong());
				totalLightsAdded++;
				didAddLight = true;
			}
		}
		
		if (!didAddLight)
		{
			player.sendMessage(new TextComponentString("Card is full! Remove a traffic light or upgrade this card."));
		}
		else
		{
			TextComponentString message = new TextComponentString(String.format("Added traffic light at [%s, %s, %s]. %s/%s slots remaining.", pos.getX(), pos.getY(), pos.getZ(), maxTrafficLights - totalLightsAdded, maxTrafficLights));
			// Figure out how to be able to copy block pos ID (#toLong())?
			player.sendMessage(message);
		}
		
		return EnumActionResult.SUCCESS;
	}
}
