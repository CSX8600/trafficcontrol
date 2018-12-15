package com.clussmanproductions.roadstuffreborn.item;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.blocks.BlockLampBase;
import com.clussmanproductions.roadstuffreborn.tileentity.BellBaseTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.roadstuffreborn.tileentity.RelayTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemCrossingRelayTuner extends Item {
	public ItemCrossingRelayTuner()
	{
		setRegistryName("crossing_relay_tuner");
		setUnlocalizedName(ModRoadStuffReborn.MODID + ".crossing_relay_tuner");
		setMaxStackSize(1);
		setCreativeTab(ModRoadStuffReborn.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		
		TileEntity selectedTE = worldIn.getTileEntity(pos);
		if (!performPairCheck(player, worldIn, selectedTE))
		{
			return EnumActionResult.SUCCESS;
		}
		
		NBTTagCompound nbt = player.inventory.getCurrentItem().getTagCompound();
		int[] relayPosArray = nbt.getIntArray("pairingpos");
		BlockPos relayPos = new BlockPos(relayPosArray[0], relayPosArray[1], relayPosArray[2]);
		RelayTileEntity relay = (RelayTileEntity)worldIn.getTileEntity(relayPos);
		
		if (selectedTE != null)
		{
			checkUseOnTileEntity(worldIn, selectedTE, relay, player);
		}
		else
		{
			checkUseOnBlock(worldIn, pos, relay, player);
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	private void checkUseOnBlock(World world, BlockPos pos, RelayTileEntity te, EntityPlayer player)
	{
		IBlockState state = world.getBlockState(pos);
		
		if (state.getBlock() instanceof BlockLampBase)
		{
			if (te.addOrRemoveCrossingGateLamp(pos))
			{
				player.sendMessage(new TextComponentString("Paired Crossing Lamps to Relay Box"));
			}
			else
			{
				player.sendMessage(new TextComponentString("Unpaired Crossing Lamps from Relay Box"));
			}
		}
	}
	
	private boolean performPairCheck(EntityPlayer player, World world, TileEntity te)
	{
		NBTTagCompound nbt = player.inventory.getCurrentItem().getTagCompound();
		
		if (nbt == null || !nbt.hasKey("pairingpos"))
		{
			if (te == null || !(te instanceof RelayTileEntity))
			{
				return false;
			}
			
			RelayTileEntity relay = (RelayTileEntity)te;
			relay = relay.getMaster(world);
			
			if (nbt == null)
			{
				nbt = new NBTTagCompound();
			}
			BlockPos relayPos = relay.getPos();
			addTileEntityPosToNBT(nbt, "pairingpos", relay);

			player.inventory.getCurrentItem().setTagCompound(nbt);
			player.sendMessage(new TextComponentString("Started pairing with Relay Box at "
					+ relayPos.getX() + ", "
					+ relayPos.getY() + ", "
					+ relayPos.getZ()));
			
		}
		else
		{
			int[] pairingpos = nbt.getIntArray("pairingpos");
			if (te != null && te instanceof RelayTileEntity)
			{
				RelayTileEntity relayTE = (RelayTileEntity)te;
				relayTE = relayTE.getMaster(world);
				BlockPos relayPos = relayTE.getPos();
				
				nbt.removeTag("pairingpos");
				player.sendMessage(new TextComponentString("Stopped pairing with Relay Box at "
						+ pairingpos[0] + ", "
						+ pairingpos[1] + ", "
						+ pairingpos[2]));
				
				if (pairingpos[0] ==  relayPos.getX() && pairingpos[1] == relayPos.getY() && pairingpos[2] == relayPos.getZ())
				{
					player.inventory.getCurrentItem().setTagCompound(nbt);
					return false;
				}
				
				addTileEntityPosToNBT(nbt, "pairingpos", relayTE);
				
				player.inventory.getCurrentItem().setTagCompound(nbt);
				
				pairingpos = nbt.getIntArray("pairingpos");
				
				player.sendMessage(new TextComponentString("Started pairing with Relay Box at "
						+ pairingpos[0] + ", "
						+ pairingpos[1] + ", "
						+ pairingpos[2]));
			}
			else if (te != null)
			{
				BlockPos pos = new BlockPos(pairingpos[0], pairingpos[1], pairingpos[2]);
				TileEntity teAtPairingPos = world.getTileEntity(pos);
				
				if (teAtPairingPos == null || !(teAtPairingPos instanceof RelayTileEntity))
				{
					nbt.removeTag("pairingpos");
					player.inventory.getCurrentItem().setTagCompound(nbt);
					
					player.sendMessage(new TextComponentString("Could not find Relay Box at "
							+ pairingpos[0] + ", "
							+ pairingpos[1] + ", "
							+ pairingpos[2] + ".  Unpaired."));
					
					return false;
				}
			}
		}
		
		return true;
	}
	
	private void addTileEntityPosToNBT(NBTTagCompound nbt, String key, TileEntity te)
	{
		BlockPos tePos = te.getPos();
		int[] pos = new int[] { tePos.getX(), tePos.getY(), tePos.getZ() };
		nbt.setIntArray(key, pos);
	}
	
	private void checkUseOnTileEntity(World world, TileEntity te, RelayTileEntity relay, EntityPlayer player)
	{		
		if (te instanceof CrossingGateGateTileEntity)
		{
			if (relay.addOrRemoveCrossingGateGate((CrossingGateGateTileEntity)te))
			{
				player.sendMessage(new TextComponentString("Paired Crossing Gate to Relay Box"));
			}
			else
			{
				player.sendMessage(new TextComponentString("Unpaired Crossing Gate from Relay Box"));
			}
		}
		
		if (te instanceof BellBaseTileEntity)
		{
			if (relay.addOrRemoveBell((BellBaseTileEntity)te))
			{
				player.sendMessage(new TextComponentString("Paired Bell to Relay Box"));
			}
			else
			{
				player.sendMessage(new TextComponentString("Unpaired Bell from Relay Box"));
			}
		}
	}
}
