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
		
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.StraightRed.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_straight_red"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.StraightYellow.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_straight_yellow"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.StraightGreen.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_straight_green"));
		
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.RedArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green_arrow_left"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.Cross.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_cross"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.DontCross.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_dont_cross"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.RedArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red_arrow_right"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.YellowArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow_arrow_right"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.GreenArrowRight.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green_arrow_right"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.NoRightTurn.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_no_right_turn"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.NoLeftTurn.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_no_left_turn"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.GreenArrowUTurn.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_green_arrow_uturn"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.YellowArrowUTurn.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_yellow_arrow_uturn"));
		ModelLoader.setCustomModelResourceLocation(this, EnumTrafficLightBulbTypes.RedArrowUTurn.getIndex(), new ModelResourceLocation(ModTrafficControl.MODID + ":traffic_light_bulb_red_arrow_uturn"));
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (tab != ModTrafficControl.CREATIVE_TAB) return;
		
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Red.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Yellow.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Green.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.StraightRed.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.StraightYellow.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.StraightGreen.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowLeft.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.Cross.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.DontCross.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowRight.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowRight.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowRight.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.NoRightTurn.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.NoLeftTurn.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.RedArrowUTurn.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.YellowArrowUTurn.getIndex()));
		items.add(new ItemStack(this, 1, EnumTrafficLightBulbTypes.GreenArrowUTurn.getIndex()));
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
		}else if (meta == EnumTrafficLightBulbTypes.StraightRed.getIndex())
		{
			unlocalizedName += "straightRed";
		}
		else if (meta == EnumTrafficLightBulbTypes.StraightYellow.getIndex())
		{
			unlocalizedName += "straightYellow";
		}
		else if (meta == EnumTrafficLightBulbTypes.StraightGreen.getIndex())
		{
			unlocalizedName += "straightGreen";
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
		else if (meta == EnumTrafficLightBulbTypes.NoRightTurn.getIndex())
		{
			unlocalizedName += "noRightTurn";
		}
		else if (meta == EnumTrafficLightBulbTypes.NoLeftTurn.getIndex())
		{
			unlocalizedName += "noLeftTurn";
		}
		else if (meta == EnumTrafficLightBulbTypes.GreenArrowUTurn.getIndex())
		{
			unlocalizedName += "greenArrowUTurn";
		}
		else if (meta == EnumTrafficLightBulbTypes.YellowArrowUTurn.getIndex())
		{
			unlocalizedName += "yellowArrowUTurn";
		}
		else if (meta == EnumTrafficLightBulbTypes.RedArrowUTurn.getIndex())
		{
			unlocalizedName += "redArrowUTurn";
		}
		else
		{
			unlocalizedName += "unknown";
		}
		
		return unlocalizedName;
	}
}
