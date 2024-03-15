package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import javax.annotation.Nullable;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockChannelizer;
import com.clussmanproductions.trafficcontrol.blocks.BlockCone;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateCrossbuck;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateGate;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGatePole;
import com.clussmanproductions.trafficcontrol.blocks.BlockDrum;
import com.clussmanproductions.trafficcontrol.blocks.BlockPedestrianButton;
import com.clussmanproductions.trafficcontrol.blocks.BlockSafetranMechanical;
import com.clussmanproductions.trafficcontrol.blocks.BlockSafetranType3;
import com.clussmanproductions.trafficcontrol.blocks.BlockSign;
import com.clussmanproductions.trafficcontrol.blocks.BlockStand;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightDouble;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetLightSingle;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight1;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight2;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight4;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight5;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight5Upper;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLight6;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficLightDoghouse;
import com.clussmanproductions.trafficcontrol.blocks.BlockVerticalWigWag;
import com.clussmanproductions.trafficcontrol.blocks.BlockWCHBell;
import com.clussmanproductions.trafficcontrol.blocks.BlockWCHMechanicalBell;
import com.clussmanproductions.trafficcontrol.blocks.BlockWigWag;
import com.clussmanproductions.trafficcontrol.tileentity.IHasRotationProperty;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScrewdriver extends Item
{
	public ItemScrewdriver()
	{
		setRegistryName("screwdriver");
		setUnlocalizedName(ModTrafficControl.MODID + ".screwdriver");
		setMaxStackSize(1);
		setMaxDamage(128);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
    	if(!world.isRemote)
    	{
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(ModTrafficControl.MODID))
            {
	            if (block.hasTileEntity(state) && world.getTileEntity(pos) instanceof IHasRotationProperty)
	            {
            		IHasRotationProperty hasRotationProperty = (IHasRotationProperty)world.getTileEntity(pos);
            		if (hasRotationProperty.getRotationFacing() != null)
            		{
            			hasRotationProperty.setRotationFacing(player.isSneaking() ? hasRotationProperty.getRotationFacing().rotateYCCW() : hasRotationProperty.getRotationFacing().rotateY());
            			return EnumActionResult.SUCCESS;
            		}
            		else if (hasRotationProperty.getRotationInt() != -1)
            		{
            			int currentRotation = hasRotationProperty.getRotationInt();
            			int newRotation = player.isSneaking() ? currentRotation - 1 : currentRotation + 1;
            			if (newRotation < 0)
            			{
            				newRotation = 15;
            			}
            			
            			if (newRotation >= 16)
            			{
            				newRotation = 0;
            			}
            			
            			hasRotationProperty.setRotationInt(newRotation);
            			return EnumActionResult.SUCCESS;
            		}
	            }
	            else if (state.getPropertyKeys().stream().anyMatch(p -> p.getName().equalsIgnoreCase("rotation") || p.getName().equalsIgnoreCase("facing")))
	        	{
	        		IBlockState newState = state;
	        		IProperty<?> property = state.getPropertyKeys().stream().filter(p -> p.getName().equalsIgnoreCase("rotation") || p.getName().equalsIgnoreCase("facing")).findFirst().get();
	        		if (property.getValueClass() == EnumFacing.class)
	        		{
	        			PropertyDirection facingProp = (PropertyDirection)property;
	        			
	        			EnumFacing currentFacing = state.getValue(facingProp);
	        			EnumFacing newFacing = player.isSneaking() ? currentFacing.rotateYCCW() : currentFacing.rotateY();
	        			
	        			newState = state.withProperty(facingProp, newFacing);
	        		}
	        		else if (property.getValueClass() == Integer.class)
	        		{
	        			PropertyInteger intProp = (PropertyInteger)property;
	        			
	        			int currentRotation = state.getValue(intProp);
	        			int newRotation = player.isSneaking() ? currentRotation - 1 : currentRotation + 1;
	        			if (newRotation < 0)
	        			{
	        				newRotation = 15;
	        			}
	        			
	        			if (newRotation >= 16)
	        			{
	        				newRotation = 0;
	        			}
	        			
	        			newState = state.withProperty(intProp, newRotation);
	        		}
	        		
	        		world.setBlockState(pos, newState);
	                world.notifyBlockUpdate(pos, state, newState, 3);
	                
	                if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
	                
	                return EnumActionResult.SUCCESS;
	        	}
            }
    	}
    	player.swingArm(hand);
    	return EnumActionResult.PASS;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if(GuiScreen.isShiftKeyDown())
        {
            String info = I18n.format("trafficcontrol.tooltip.screwdriver");
            tooltip.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(info, Config.tooltipCharWrapLength));
        }
        else
        {
            tooltip.add(TextFormatting.YELLOW + I18n.format("trafficcontrol.tooltip.help"));
        }
    }
}
