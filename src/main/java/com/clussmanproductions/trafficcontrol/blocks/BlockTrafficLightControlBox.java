package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTrafficLightControlBox extends Block implements ITileEntityProvider {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public BlockTrafficLightControlBox()
	{
		super(Material.IRON);
		setRegistryName("traffic_light_control_box");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_control_box");
		setHardness(2f);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) 
	{
        if (face == EnumFacing.UP)
        {
            return BlockFaceShape.UNDEFINED;
        }
        return super.getBlockFaceShape(worldIn, state, pos, face);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		if(GuiScreen.isShiftKeyDown())
		{
			String info = I18n.format("trafficcontrol.tooltip.trafficbox");
			tooltip.addAll(Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(info, Config.tooltipCharWrapLength));
		}
		else
		{
			tooltip.add(TextFormatting.YELLOW + I18n.format("trafficcontrol.tooltip.help"));
		}
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
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		TrafficLightControlBoxTileEntity controlBox = (TrafficLightControlBoxTileEntity)worldIn.getTileEntity(pos);
		controlBox.setPowered(worldIn.isBlockPowered(pos));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TrafficLightControlBoxTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (playerIn.getHeldItemMainhand().getItem() == ModItems.crossing_relay_tuner ||
				playerIn.getHeldItemMainhand().getItem() == ModItems.screwdriver)
		{
			return false;
		}
		
		if (!worldIn.isRemote)
		{
			return true;
		}
		
		playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.TRAFFIC_LIGHT_CONTROL_BOX, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch(state.getValue(FACING))
		{
			case EAST:
				return new AxisAlignedBB(0.25, 0, 0, 0.75, 1.5, 1);
			case NORTH:
				return new AxisAlignedBB(0, 0, 0.25, 1, 1.5, 0.75);
			case SOUTH:
				return new AxisAlignedBB(0, 0, 0.25, 1, 1.5, 0.75);
			case WEST:
				return new AxisAlignedBB(0.25, 0, 0, 0.75, 1.5, 1);
		}
		return super.getBoundingBox(state, source, pos);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TrafficLightControlBoxTileEntity te = (TrafficLightControlBoxTileEntity)worldIn.getTileEntity(pos);
		if (te != null)
		{
			te.onBreak(worldIn);
		}
		
		super.breakBlock(worldIn, pos, state);
	}
}
