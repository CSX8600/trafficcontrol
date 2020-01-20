package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockTrafficLight extends Block implements ITileEntityProvider {

	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockTrafficLight()
	{
		super(Material.IRON);
		setRegistryName("traffic_light");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light");
		setHardness(2F);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TrafficLightTileEntity.class, new TrafficLightRenderer());
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
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
	
	private ItemStack getItemVersionOfBlock(IBlockAccess world, BlockPos pos)
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof TrafficLightTileEntity))
		{
			return new ItemStack(ModItems.traffic_light_frame);
		}
		
		TrafficLightTileEntity trafficLight = (TrafficLightTileEntity)tileEntity;
		ItemStack frameStack = new ItemStack(ModItems.traffic_light_frame);
		IItemHandler handler = frameStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		for(int i = 0; i < 3; i++)
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
		}
		
		frameStack.setTagCompound(frameStack.getItem().getNBTShareTag(frameStack));
		
		return frameStack;
	}
	
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TrafficLightTileEntity();
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TrafficLightTileEntity)
		{
			if (((TrafficLightTileEntity)te).anyActive())
			{
				return 15;
			}
		}
		
		return 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch(state.getValue(FACING))
		{
			case EAST:
				return new AxisAlignedBB(0.25, -0.3125, 0.1875, 0.5625, 1, 0.8125);
			case NORTH:
				return new AxisAlignedBB(0.1875, -0.3125, 0.4375, 0.8125, 1, 0.75);
			case SOUTH:
				return new AxisAlignedBB(0.1875, -0.3125, 0.25, 0.8125, 1, 0.5625);
			case WEST:
				return new AxisAlignedBB(0.4375, -0.3125, 0.1875, 0.75, 1, 0.8125);
		}
		return super.getBoundingBox(state, source, pos);
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
}
