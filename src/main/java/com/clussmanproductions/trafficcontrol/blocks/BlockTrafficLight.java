package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TrafficLightRenderer;

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
		
		ItemStack upperStack = new ItemStack(ModItems.traffic_light_bulb, 1, trafficLight.getBulbTypeBySlot(0).getIndex());
		ItemStack middleStack = new ItemStack(ModItems.traffic_light_bulb, 1, trafficLight.getBulbTypeBySlot(1).getIndex());
		ItemStack lowerStack = new ItemStack(ModItems.traffic_light_bulb, 1, trafficLight.getBulbTypeBySlot(2).getIndex());
		
		handler.insertItem(0, upperStack, false);
		handler.insertItem(1, middleStack, false);
		handler.insertItem(2, lowerStack, false);
		
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
}
