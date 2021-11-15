package com.clussmanproductions.trafficcontrol.item;

import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter.Yellow;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTrafficLightBulb extends Item {
	public ItemTrafficLightBulb()
	{
		setRegistryName("traffic_light_bulb");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.Red.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.Yellow.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.Green.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.RedArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.Cross.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_cross"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.DontCross.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_dont_cross"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.RedArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red_arrow_right"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.YellowArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow_arrow_right"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.GreenArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green_arrow_right"));
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab != ModTrafficControl.CREATIVE_TAB) return;
		
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Red.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Yellow.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Green.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Cross.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.DontCross.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowRight.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowRight.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowRight.getIndex()));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String unlocalizedName = ModTrafficControl.MODID + ".traffic_light_bulb.";
		int meta = stack.getMetadata();
		
		if (meta == EnumTrafficLightBulbTypes.Red.getIndex())
		{
			unlocalizedName += "red";
		}
		else if (meta == EnumTrafficLightBulbTypes.Yellow.getIndex())
		{
			unlocalizedName += "yellow";
		}
		else if (meta == EnumTrafficLightBulbTypes.Green.getIndex())
		{
			unlocalizedName += "green";
		}
		else if (meta == EnumTrafficLightBulbTypes.RedArrowLeft.getIndex())
		{
			unlocalizedName += "redArrowLeft";
		}
		else if (meta == EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex())
		{
			unlocalizedName += "yellowArrowLeft";
		}
		else if (meta == EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex())
		{
			unlocalizedName += "greenArrowLeft";
		}
		else if (meta == EnumTrafficLightBulbTypes.Cross.getIndex())
		{
			unlocalizedName += "cross";
		}
		else if (meta == EnumTrafficLightBulbTypes.DontCross.getIndex())
		{
			unlocalizedName += "dont_cross";
		}
		else if (meta == EnumTrafficLightBulbTypes.RedArrowRight.getIndex())
		{
			unlocalizedName += "redArrowRight";
		}
		else if (meta == EnumTrafficLightBulbTypes.YellowArrowRight.getIndex())
		{
			unlocalizedName += "yellowArrowRight";
		}
		else if (meta == EnumTrafficLightBulbTypes.GreenArrowRight.getIndex())
		{
			unlocalizedName += "greenArrowRight";
		}
		else
		{
			unlocalizedName += "unknown";
		}
		
		return unlocalizedName;
	}
}
