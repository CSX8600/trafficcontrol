package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.RendererType3Barrier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
public abstract class BlockType3BarrierBase extends Block {
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static PropertyBool ISFURTHESTLEFT = PropertyBool.create("isfurthestleft");
	public static PropertyBool ISFURTHESTRIGHT = PropertyBool.create("isfurthestright");
	public BlockType3BarrierBase()
	{
		super(Material.IRON);
		setRegistryName(getName());
		setUnlocalizedName(ModTrafficControl.MODID + "." + getName());
		setHardness(1f);
		setHarvestLevel("axe", 1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	protected abstract String getName();
	public abstract Block getBlockInstance();
	
	@SideOnly(Side.CLIENT)
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ClientRegistry.bindTileEntitySpecialRenderer(Type3BarrierTileEntity.class, new RendererType3Barrier());
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(FACING).add(ISFURTHESTLEFT).add(ISFURTHESTRIGHT).build();
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
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		boolean isFurthestLeft = true;
		boolean isFurthestRight = true;
		EnumFacing currentFacing = state.getValue(FACING);
		EnumFacing directionOfTravel = currentFacing.rotateY();
		IBlockState borderState = worldIn.getBlockState(pos.offset(directionOfTravel));
		if (borderState.getBlock() == getBlockInstance())
		{
			isFurthestRight = borderState.getValue(FACING) != currentFacing;
		}
		
		directionOfTravel = currentFacing.rotateYCCW();
		borderState = worldIn.getBlockState(pos.offset(directionOfTravel));
		if (borderState.getBlock() == getBlockInstance())
		{
			isFurthestLeft = borderState.getValue(FACING) != currentFacing;
		}
		
		return state.withProperty(ISFURTHESTLEFT, isFurthestLeft).withProperty(ISFURTHESTRIGHT, isFurthestRight);
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
		return 0;
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing currentFacing = state.getValue(FACING);
		
		switch(currentFacing)
		{
			case NORTH:
			case SOUTH:
				return new AxisAlignedBB(0, 0, 0.5625, 1, 1.4375, 0.4375);
			case WEST:
			case EAST:
				return new AxisAlignedBB(0.4375, 0, 0, 0.5625, 1.4375, 1);
		}
		return super.getBoundingBox(state, source, pos);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity teInPos = worldIn.getTileEntity(pos);
		if (!(teInPos instanceof Type3BarrierTileEntity) || playerIn.getHeldItemMainhand().getItem() == ModItems.screwdriver)
		{
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		}
		
		Type3BarrierTileEntity barrierTE = (Type3BarrierTileEntity)teInPos;
		
		playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.TYPE_3_BARRIER, worldIn, barrierTE.getPos().getX(), barrierTE.getPos().getY(), barrierTE.getPos().getZ());
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new Type3BarrierTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (worldIn.isRemote)
		{
			return;
		}
		
		IBlockState actualState = state.getActualState(worldIn, pos);
		if (!actualState.getValue(ISFURTHESTLEFT) || !actualState.getValue(ISFURTHESTRIGHT))
		{
			Type3BarrierTileEntity myTE = (Type3BarrierTileEntity)worldIn.getTileEntity(pos);
			EnumFacing facing = actualState.getValue(FACING);
			
			BlockPos leftPos = pos.offset(facing.rotateYCCW());
			Type3BarrierTileEntity leftTE = (Type3BarrierTileEntity)worldIn.getTileEntity(leftPos);
			
			BlockPos rightPos = pos.offset(facing.rotateY());
			Type3BarrierTileEntity rightTE = (Type3BarrierTileEntity)worldIn.getTileEntity(rightPos);
			
			if (leftTE == null)
			{
				myTE.setRenderSign(rightTE.getRenderSign());
				myTE.setSignType(rightTE.getSignType());
			}
			else if (rightTE == null)
			{
				myTE.setRenderSign(leftTE.getRenderSign());
				myTE.setSignType(leftTE.getSignType());
			}
			else
			{
				boolean renderSign = leftTE.getRenderSign() || rightTE.getRenderSign();
				leftTE = myTE.findFurthestLeft();
				leftTE.setRenderSign(renderSign);
				leftTE.setSignType(leftTE.getSignType());
				leftTE.syncConnectedBarriers(false);
			}
		}
		
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}
