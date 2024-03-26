package com.clussmanproductions.trafficcontrol.blocks;

import java.util.Arrays;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.tileentity.BaseTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockBaseTrafficLight extends Block implements IHorizontalPoleConnectable {

	public static PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);
	public static PropertyBool VALIDHORIZONTALBAR = PropertyBool.create("validhorizontalbar");
	public static PropertyBool VALIDBACKBAR = PropertyBool.create("validbackbar");
	public BlockBaseTrafficLight(String name)
	{
		super(Material.IRON);
		setRegistryName(name);
		setUnlocalizedName(ModTrafficControl.MODID + "." + name);
		setHardness(2F);
		setHarvestLevel("pickaxe", 2);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public abstract void initModel();
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return CustomAngleCalculator.rotationToMeta(state.getValue(ROTATION));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, CustomAngleCalculator.metaToRotation(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ROTATION, VALIDBACKBAR, VALIDHORIZONTALBAR);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean hasValidHorizontalBar = false;
		boolean hasValidBackBar = false;
		
		int rotation = state.getValue(ROTATION);
		boolean isCardinal = CustomAngleCalculator.isCardinal(rotation);
		
		if (isCardinal)
		{
			EnumFacing myFacing = CustomAngleCalculator.getFacingFromRotation(rotation);
			IBlockState otherState = worldIn.getBlockState(pos.offset(myFacing.rotateY()));
			if (otherState.getBlock() instanceof IHorizontalPoleConnectable)
			{
				hasValidHorizontalBar = ((IHorizontalPoleConnectable)otherState.getBlock()).canConnectHorizontalPole(otherState, myFacing.rotateYCCW());
			}
			
			if (!hasValidHorizontalBar)
			{
				otherState = worldIn.getBlockState(pos.offset(myFacing.rotateYCCW()));
				if (otherState.getBlock() instanceof IHorizontalPoleConnectable)
				{
					hasValidHorizontalBar = ((IHorizontalPoleConnectable)otherState.getBlock()).canConnectHorizontalPole(otherState, myFacing.rotateY());
				}
			}
			
			otherState = worldIn.getBlockState(pos.offset(myFacing));
			if (otherState.getBlock() instanceof IHorizontalPoleConnectable)
			{
				hasValidBackBar = ((IHorizontalPoleConnectable)otherState.getBlock()).canConnectHorizontalPole(otherState, myFacing.getOpposite());
			}
			
			if (!hasValidBackBar)
			{
				otherState = worldIn.getBlockState(pos.offset(myFacing.getOpposite()));
				if (otherState.getBlock() instanceof IHorizontalPoleConnectable)
				{
					hasValidBackBar = ((IHorizontalPoleConnectable)otherState.getBlock()).canConnectHorizontalPole(otherState, myFacing);
				}
			}
		}
		
		return state.withProperty(VALIDHORIZONTALBAR, hasValidHorizontalBar).withProperty(VALIDBACKBAR, hasValidBackBar);
	}
	
	private ItemStack getItemVersionOfBlock(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof BaseTrafficLightTileEntity))
		{
			return new ItemStack(getItemVersionOfBlock());
		}
		
		BaseTrafficLightTileEntity trafficLight = (BaseTrafficLightTileEntity)tileEntity;
		ItemStack frameStack = new ItemStack(getItemVersionOfBlock());
		IItemHandler handler = frameStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		NBTTagCompound stackCompound = frameStack.getTagCompound();
		if (stackCompound == null)
		{
			stackCompound = new NBTTagCompound();
			frameStack.setTagCompound(stackCompound);
		}
		
		for(int i = 0; i < getItemVersionOfBlock().getBulbCount(); i++)
		{
			EnumTrafficLightBulbTypes bulbTypeInSlot = trafficLight.getBulbTypeBySlot(i);
			if (bulbTypeInSlot == null)
			{
				handler.insertItem(i, ItemStack.EMPTY, false);
			}
			else
			{
				handler.insertItem(i, new ItemStack(ModItems.traffic_light_bulb, 1, bulbTypeInSlot.getIndex()), false);
			}
			
			stackCompound.setBoolean("always-flash-" + i, trafficLight.getAllowFlashBySlot(i));
		}
		
		frameStack.setTagCompound(frameStack.getItem().getNBTShareTag(frameStack));
		
		return frameStack;
	}
	
	protected abstract BaseItemTrafficLightFrame getItemVersionOfBlock();
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return getItemVersionOfBlock(world, pos);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		drops.add(getItemVersionOfBlock(world, pos));
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof BaseTrafficLightTileEntity)
		{
			if (((BaseTrafficLightTileEntity)te).anyActive())
			{
				return 15;
			}
		}
		
		return 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (!(state.getBlock() instanceof BlockBaseTrafficLight))
		{
			return FULL_BLOCK_AABB;
		}
		
		int rotation = state.getValue(ROTATION);
		
		switch(rotation)
		{
			case 0:
				return new AxisAlignedBB(0.1875, -0.3125, 0.4375, 0.8125, 1, 0.75);
			case 8:
				return new AxisAlignedBB(0.1875, -0.3125, 0.25, 0.8125, 1, 0.5625);
			case 4:
				return new AxisAlignedBB(0.25, -0.3125, 0.1875, 0.5625, 1, 0.8125);
			case 12:
				return new AxisAlignedBB(0.4375, -0.3125, 0.1875, 0.75, 1, 0.8125);
			case 1:
			case 15:
			case 7:
			case 9:
			case 3:
			case 5:
			case 11:
			case 13:
				return new AxisAlignedBB(0.375, 0, 0.375, 0.75, 1, 0.75);
			case 2:
			case 6:
			case 10:
			case 14:
				return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 1, 0.8);
		}
		
		return FULL_BLOCK_AABB;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest) return true;
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}
	
	@Override
	public boolean canConnectHorizontalPole(IBlockState state, EnumFacing fromFacing) {
		return CustomAngleCalculator.isCardinal(state.getValue(ROTATION));
	}
	
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) 
	{
        if (face == EnumFacing.UP)
        {
            return BlockFaceShape.UNDEFINED;
        }
        return super.getBlockFaceShape(worldIn, state, pos, face);
    }
}
