package com.clussmanproductions.trafficcontrol.blocks;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSign;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.StreetSignRenderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockStreetSign extends Block {
	public BlockStreetSign()
	{
		super(Material.IRON);
		setRegistryName("street_sign");
		setUnlocalizedName(ModTrafficControl.MODID + ".street_sign");
		setLightOpacity(1);
		setHardness(1f);
		setHarvestLevel("pickaxe", 0);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(StreetSignTileEntity.class, new StreetSignRenderer());
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
		return false;
	}
	
	@Override
	public float getAmbientOcclusionLightValue(IBlockState state) {
		return 1;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new StreetSignTileEntity();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		StreetSignTileEntity te = (StreetSignTileEntity)worldIn.getTileEntity(pos);
		StreetSign newSign = new StreetSign();
		newSign.setFacing(placer.getHorizontalFacing());
		te.addStreetSign(newSign);
		
		if (!worldIn.isRemote)
		{
			return;
		}
		
		if (placer != Minecraft.getMinecraft().player)
		{
			return;
		}
		
		Minecraft.getMinecraft().player.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.STREET_SIGN, worldIn, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		TileEntity tileEntity = source.getTileEntity(pos);
		
		if (tileEntity == null || !(tileEntity instanceof StreetSignTileEntity))
		{
			return FULL_BLOCK_AABB;
		}
		
		StreetSignTileEntity streetSignTileEntity = (StreetSignTileEntity)tileEntity;
		
		double pixelsHigh = 0;
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			if (streetSignTileEntity.getStreetSign(i) != null)
			{
				pixelsHigh += 4;
			}
		}
		
		return new AxisAlignedBB(0, 0, 0, 1, (pixelsHigh / 16), 1);	
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote && !(hand == EnumHand.MAIN_HAND ? playerIn.getHeldItemMainhand().getItem() == ModItems.street_sign : playerIn.getHeldItemOffhand().getItem() == ModItems.street_sign))
		{
			playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.STREET_SIGN, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		StreetSignTileEntity streetSignTileEntity = (StreetSignTileEntity)world.getTileEntity(pos);
		if (streetSignTileEntity == null)
		{
			return;
		}
		
		int count = 0;
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			if (streetSignTileEntity.getStreetSign(i) != null)
			{
				count++;
			}
		}
		
		drops.add(new ItemStack(ModItems.street_sign, count));
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
