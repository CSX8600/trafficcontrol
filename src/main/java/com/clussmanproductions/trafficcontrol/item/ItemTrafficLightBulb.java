package com.clussmanproductions.trafficcontrol.item;

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
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_bulb");
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
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Red.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Yellow.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Green.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex()));
	}
}
