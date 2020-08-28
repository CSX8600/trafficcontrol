package com.clussmanproductions.trafficcontrol.item;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockStreetSign;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSign;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSignTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemStreetSign extends ItemBlock {
	public ItemStreetSign(BlockStreetSign streetSignBlock)
	{
		super(streetSignBlock);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.getBlockState(pos).getBlock() == ModBlocks.street_sign)
		{
			StreetSignTileEntity streetSignTileEntity = (StreetSignTileEntity)worldIn.getTileEntity(pos);
			
			StreetSign newSign = new StreetSign();
			newSign.setFacing(player.getHorizontalFacing());
			
			if (streetSignTileEntity.addStreetSign(newSign))
			{
				player.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.STREET_SIGN, worldIn, pos.getX(), pos.getY(), pos.getZ());
				(hand == EnumHand.MAIN_HAND ? player.getHeldItemMainhand() : player.getHeldItemOffhand()).shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
