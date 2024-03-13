package com.clussmanproductions.trafficcontrol.item;

import java.util.List;

import javax.annotation.Nullable;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.*;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
            
            if(!player.isSneaking())
            {
                if(block instanceof BlockCone)
                {
                	int rotation = state.getValue(BlockCone.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCone.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSign)
                {
                	int rotation = state.getValue(BlockSign.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockSign.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockChannelizer)
                {
                	int rotation = state.getValue(BlockChannelizer.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockChannelizer.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockDrum)
                {
                	int rotation = state.getValue(BlockDrum.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockDrum.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateBase)
                {
                	int rotation = state.getValue(BlockCrossingGateBase.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCrossingGateBase.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateCrossbuck)
                {
                	int rotation = state.getValue(BlockCrossingGateCrossbuck.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCrossingGateCrossbuck.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateGate)
                {
                	int rotation = state.getValue(BlockCrossingGateGate.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCrossingGateGate.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateLamps)
                {
                	int rotation = state.getValue(BlockCrossingGateLamps.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCrossingGateLamps.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGatePole)
                {
                	int rotation = state.getValue(BlockCrossingGatePole.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockCrossingGatePole.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
               
                if(block instanceof BlockPedestrianButton)
                {
                	int rotation = state.getValue(BlockPedestrianButton.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockPedestrianButton.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranMechanical)
                {
                	int rotation = state.getValue(BlockSafetranMechanical.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockSafetranMechanical.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranType3)
                {
                	int rotation = state.getValue(BlockSafetranType3.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockSafetranType3.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranType3)
                {
                	int rotation = state.getValue(BlockSafetranType3.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockSafetranType3.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStand)
                {
                	int rotation = state.getValue(BlockStand.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockStand.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStreetLightSingle)
                {
                	int rotation = state.getValue(BlockStreetLightSingle.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockStreetLightSingle.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStreetLightDouble)
                {
                	int rotation = state.getValue(BlockStreetLightDouble.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockStreetLightDouble.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight)
                {
                	int rotation = state.getValue(BlockTrafficLight.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight1)
                {
                	int rotation = state.getValue(BlockTrafficLight1.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight1.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight2)
                {
                	int rotation = state.getValue(BlockTrafficLight2.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight2.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight4)
                {
                	int rotation = state.getValue(BlockTrafficLight4.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight4.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight5)
                {
                	int rotation = state.getValue(BlockTrafficLight5.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight5.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight5Upper)
                {
                	int rotation = state.getValue(BlockTrafficLight5Upper.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight5Upper.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight6)
                {
                	int rotation = state.getValue(BlockTrafficLight6.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLight6.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLightDoghouse)
                {
                	int rotation = state.getValue(BlockTrafficLightDoghouse.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockTrafficLightDoghouse.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockVerticalWigWag)
                {
                	int rotation = state.getValue(BlockVerticalWigWag.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockVerticalWigWag.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWCHBell)
                {
                	int rotation = state.getValue(BlockWCHBell.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockWCHBell.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWCHMechanicalBell)
                {
                	int rotation = state.getValue(BlockWCHMechanicalBell.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockWCHMechanicalBell.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWigWag)
                {
                	int rotation = state.getValue(BlockWigWag.ROTATION);
                    int newRotation = (rotation + 1) % 16;
                    IBlockState newState = state.withProperty(BlockWigWag.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {	
                if(block instanceof BlockCone)
                {
                	int rotation = state.getValue(BlockCone.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                	if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCone.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSign)
                {
                	int rotation = state.getValue(BlockSign.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockSign.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockChannelizer)
                {
                	int rotation = state.getValue(BlockChannelizer.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockChannelizer.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockDrum)
                {
                	int rotation = state.getValue(BlockDrum.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockDrum.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateBase)
                {
                	int rotation = state.getValue(BlockCrossingGateBase.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCrossingGateBase.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateCrossbuck)
                {
                	int rotation = state.getValue(BlockCrossingGateCrossbuck.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCrossingGateCrossbuck.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateGate)
                {
                	int rotation = state.getValue(BlockCrossingGateGate.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCrossingGateGate.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGateLamps)
                {
                	int rotation = state.getValue(BlockCrossingGateLamps.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCrossingGateLamps.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockCrossingGatePole)
                {
                	int rotation = state.getValue(BlockCrossingGatePole.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockCrossingGatePole.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
               
                if(block instanceof BlockPedestrianButton)
                {
                	int rotation = state.getValue(BlockPedestrianButton.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockPedestrianButton.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranMechanical)
                {
                	int rotation = state.getValue(BlockSafetranMechanical.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockSafetranMechanical.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranType3)
                {
                	int rotation = state.getValue(BlockSafetranType3.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockSafetranType3.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockSafetranType3)
                {
                	int rotation = state.getValue(BlockSafetranType3.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockSafetranType3.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStand)
                {
                	int rotation = state.getValue(BlockStand.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockStand.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStreetLightSingle)
                {
                	int rotation = state.getValue(BlockStreetLightSingle.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockStreetLightSingle.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockStreetLightDouble)
                {
                	int rotation = state.getValue(BlockStreetLightDouble.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockStreetLightDouble.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight)
                {
                	int rotation = state.getValue(BlockTrafficLight.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight1)
                {
                	int rotation = state.getValue(BlockTrafficLight1.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight1.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight2)
                {
                	int rotation = state.getValue(BlockTrafficLight2.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight2.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight4)
                {
                	int rotation = state.getValue(BlockTrafficLight4.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight4.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight5)
                {
                	int rotation = state.getValue(BlockTrafficLight5.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight5.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight5Upper)
                {
                	int rotation = state.getValue(BlockTrafficLight5Upper.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight5Upper.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLight6)
                {
                	int rotation = state.getValue(BlockTrafficLight6.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLight6.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockTrafficLightDoghouse)
                {
                	int rotation = state.getValue(BlockTrafficLightDoghouse.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockTrafficLightDoghouse.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockVerticalWigWag)
                {
                	int rotation = state.getValue(BlockVerticalWigWag.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockVerticalWigWag.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWCHBell)
                {
                	int rotation = state.getValue(BlockWCHBell.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockWCHBell.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWCHMechanicalBell)
                {
                	int rotation = state.getValue(BlockWCHMechanicalBell.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockWCHMechanicalBell.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
                
                if(block instanceof BlockWigWag)
                {
                	int rotation = state.getValue(BlockWigWag.ROTATION);
                    int newRotation = (rotation - 1) % 16;
                    if (newRotation < 0) { newRotation = 15; }
                    IBlockState newState = state.withProperty(BlockWigWag.ROTATION, newRotation);

                    world.setBlockState(pos, newState);
                    world.notifyBlockUpdate(pos, state, newState, newRotation);
                    
                    if(!player.isCreative()) {player.getHeldItem(hand).damageItem(1, player);}
                    
                    return EnumActionResult.SUCCESS;
                }
            }
           
    	}
    	player.swingArm(hand);
    	return EnumActionResult.FAIL;
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
