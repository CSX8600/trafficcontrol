package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import javax.annotation.Nullable;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.network.ServerSideSoundPacket;
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
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
                		playScrewdriverSound(player, pos);
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
                		playScrewdriverSound(player, pos);
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
	                playScrewdriverSound(player, pos);

	                return EnumActionResult.SUCCESS;
	        	}
            }
    	}
    	player.swingArm(hand);
    	return EnumActionResult.PASS;
    }

	private void playScrewdriverSound(EntityPlayer player, BlockPos pos) {
		ServerSideSoundPacket packet = new ServerSideSoundPacket();
		packet.modID = ModTrafficControl.MODID;
		packet.soundName = "screwdriver";
		packet.pos = pos;
		packet.volume = 0.25F;
		PacketHandler.INSTANCE.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 25));
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
