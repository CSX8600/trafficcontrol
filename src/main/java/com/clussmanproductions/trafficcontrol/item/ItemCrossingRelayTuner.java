package com.clussmanproductions.trafficcontrol.item;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockPedestrianButton;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntBorder;
import com.clussmanproductions.trafficcontrol.blocks.BlockShuntIsland;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorLeft;
import com.clussmanproductions.trafficcontrol.blocks.BlockTrafficSensorStraight;
import com.clussmanproductions.trafficcontrol.tileentity.BaseTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.BellBaseTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.PedestrianButtonTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.RelayTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.ShuntBorderTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.ShuntIslandTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.WigWagTileEntity;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

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
		setUnlocalizedName(ModTrafficControl.MODID + ".crossing_relay_tuner");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
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
		TileEntity pairedTE = worldIn.getTileEntity(relayPos);

		if (selectedTE != null)
		{
			checkUseOnTileEntity(worldIn, selectedTE, pairedTE, player);
		}
		else
		{
			checkUseOnBlock(worldIn, pos, pairedTE, player);
		}

		return EnumActionResult.SUCCESS;
	}

	private void checkUseOnBlock(World world, BlockPos pos, TileEntity te, EntityPlayer player)
	{
		IBlockState state = world.getBlockState(pos);
		if (te instanceof RelayTileEntity)
		{
			RelayTileEntity relay = (RelayTileEntity)te;
			// There are currently no blocks that pair to relay
		}

		if (te instanceof TrafficLightControlBoxTileEntity)
		{
			TrafficLightControlBoxTileEntity tlBox = (TrafficLightControlBoxTileEntity)te;

			if (state.getBlock() instanceof BlockTrafficSensorLeft || state.getBlock() instanceof BlockTrafficSensorStraight)
			{
				if (tlBox.addOrRemoveSensor(pos))
				{
					player.sendMessage(new TextComponentString("Paired sensor to Traffic Light Control Box"));
				}
				else
				{
					player.sendMessage(new TextComponentString("Unpaired sensor from Traffic Light Control Box"));
				}
			}
		}
	}

	private boolean performPairCheck(EntityPlayer player, World world, TileEntity te)
	{
		NBTTagCompound nbt = player.inventory.getCurrentItem().getTagCompound();

		if (nbt == null || !nbt.hasKey("pairingpos"))
		{
			if (te == null || (!(te instanceof RelayTileEntity) && !(te instanceof TrafficLightControlBoxTileEntity)))
			{
				return false;
			}
			BlockPos relayPos = null;

			if (nbt == null)
			{
				nbt = new NBTTagCompound();
			}

			String typeOfPairing = "";
			if (te instanceof RelayTileEntity)
			{
				RelayTileEntity relay = (RelayTileEntity)te;
				relay = relay.getMaster(world);
				relayPos = relay.getPos();
				addTileEntityPosToNBT(nbt, "pairingpos", relay);

				typeOfPairing = "Relay Box";
			}

			if (te instanceof TrafficLightControlBoxTileEntity)
			{
				TrafficLightControlBoxTileEntity controlBox = (TrafficLightControlBoxTileEntity)te;
				relayPos = controlBox.getPos();
				addTileEntityPosToNBT(nbt, "pairingpos", controlBox);

				typeOfPairing = "Traffic Light Control Box";
			}

			player.inventory.getCurrentItem().setTagCompound(nbt);
			player.sendMessage(new TextComponentString("Started pairing with " + typeOfPairing + " at "
					+ relayPos.getX() + ", "
					+ relayPos.getY() + ", "
					+ relayPos.getZ()));

		}
		else
		{
			int[] pairingpos = nbt.getIntArray("pairingpos");
			if (te != null && (te instanceof RelayTileEntity || te instanceof TrafficLightControlBoxTileEntity))
			{
				BlockPos relayPos = null;
				String typeOfPairing = "";
				if (te instanceof RelayTileEntity)
				{
					RelayTileEntity relayTE = (RelayTileEntity)te;
					relayTE = relayTE.getMaster(world);
					relayPos = relayTE.getPos();

					typeOfPairing = "Relay Box";
				}

				if (te instanceof TrafficLightControlBoxTileEntity)
				{
					TrafficLightControlBoxTileEntity controlBoxTE = (TrafficLightControlBoxTileEntity)te;
					relayPos = controlBoxTE.getPos();

					typeOfPairing = "Traffic Light Control Box";
				}

				nbt.removeTag("pairingpos");
				player.sendMessage(new TextComponentString("Stopped pairing with " + typeOfPairing + " at "
						+ pairingpos[0] + ", "
						+ pairingpos[1] + ", "
						+ pairingpos[2]));

				if (pairingpos[0] ==  relayPos.getX() && pairingpos[1] == relayPos.getY() && pairingpos[2] == relayPos.getZ())
				{
					player.inventory.getCurrentItem().setTagCompound(nbt);
					return false;
				}

				if (te instanceof RelayTileEntity)
				{
					addTileEntityPosToNBT(nbt, "pairingpos", ((RelayTileEntity)te).getMaster(world));
				}
				else
				{
					addTileEntityPosToNBT(nbt, "pairingpos", te);
				}

				player.inventory.getCurrentItem().setTagCompound(nbt);

				pairingpos = nbt.getIntArray("pairingpos");

				player.sendMessage(new TextComponentString("Started pairing with " + typeOfPairing + " at "
						+ pairingpos[0] + ", "
						+ pairingpos[1] + ", "
						+ pairingpos[2]));
			}
			else if (te != null)
			{
				BlockPos pos = new BlockPos(pairingpos[0], pairingpos[1], pairingpos[2]);
				TileEntity teAtPairingPos = world.getTileEntity(pos);

				if (teAtPairingPos == null || (!(teAtPairingPos instanceof RelayTileEntity) && !(teAtPairingPos instanceof TrafficLightControlBoxTileEntity)))
				{
					nbt.removeTag("pairingpos");
					player.inventory.getCurrentItem().setTagCompound(nbt);

					player.sendMessage(new TextComponentString("Could not find pair at "
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

	private void checkUseOnTileEntity(World world, TileEntity te, TileEntity pairedTE, EntityPlayer player)
	{
		if (pairedTE instanceof RelayTileEntity)
		{
			RelayTileEntity relay = (RelayTileEntity)pairedTE;
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

			if (te instanceof WigWagTileEntity)
			{
				if (relay.addOrRemoveWigWag(te.getPos()))
				{
					player.sendMessage(new TextComponentString("Paired Wig Wag to Relay Box"));
				}
				else
				{
					player.sendMessage(new TextComponentString("Unpaired Wig Wag from Relay Box"));
				}
			}

			if (te instanceof ShuntBorderTileEntity)
			{
				ShuntBorderTileEntity shuntBorderTileEntity = (ShuntBorderTileEntity)te;
				IBlockState borderBlock = world.getBlockState(te.getPos());
				EnumFacing borderFacing = borderBlock.getValue(BlockShuntBorder.FACING);
				if (relay.addOrRemoveShuntBorder(shuntBorderTileEntity.getTrackOrigin(), borderFacing))
				{
					shuntBorderTileEntity.addPairedRelayBox(relay.getPos());
					player.sendMessage(new TextComponentString("Paired Border Shunt to Relay Box"));
				}
				else
				{
					shuntBorderTileEntity.removePairedRelayBox(relay.getPos());
					player.sendMessage(new TextComponentString("Unpaired Border Shunt from Relay Box"));
				}
			}

			if (te instanceof ShuntIslandTileEntity)
			{
				ShuntIslandTileEntity shuntIslandTileEntity = (ShuntIslandTileEntity)te;
				IBlockState islandBlock = world.getBlockState(te.getPos());
				EnumFacing islandFacing = islandBlock.getValue(BlockShuntIsland.FACING);
				if (relay.addOrRemoveShuntIsland(shuntIslandTileEntity.getTrackOrigin(), islandFacing))
				{
					shuntIslandTileEntity.addPairedRelayBox(relay.getPos());
					player.sendMessage(new TextComponentString("Paired Island Shunt to Relay Box"));
				}
				else
				{
					shuntIslandTileEntity.removePairedRelayBox(relay.getPos());
					player.sendMessage(new TextComponentString("Unpaired Island Shunt from Relay Box"));
				}
			}

			if (te instanceof CrossingLampsTileEntity)
			{
				if (relay.addOrRemoveCrossingGateLamp(te.getPos()))
				{
					player.sendMessage(new TextComponentString("Paired Crossing Lamps to Relay Box"));
				}
				else
				{
					player.sendMessage(new TextComponentString("Unpaired Crossing Lamps from Relay Box"));
				}
			}
		}

		if (pairedTE instanceof TrafficLightControlBoxTileEntity)
		{
			TrafficLightControlBoxTileEntity controlBox = (TrafficLightControlBoxTileEntity)pairedTE;
			if (te instanceof BaseTrafficLightTileEntity)
			{
				IBlockState state = world.getBlockState(te.getPos());

				if (state.getBlock() instanceof BlockBaseTrafficLight)
				{
					int rotation = state.getValue(BlockBaseTrafficLight.ROTATION);

					boolean operationResult = false;
					if (CustomAngleCalculator.isEast(rotation) || CustomAngleCalculator.isWest(rotation))
					{
						operationResult = controlBox.addOrRemoveWestEastTrafficLight(te.getPos());
					}
					else
					{
						operationResult = controlBox.addOrRemoveNorthSouthTrafficLight(te.getPos());
					}

					if (operationResult)
					{
						player.sendMessage(new TextComponentString("Paired Traffic Light to Traffic Light Control Box"));
					}
					else
					{
						player.sendMessage(new TextComponentString("Unpaired Traffic Light to Traffic Light Control Box"));
					}
				}
			}

			if (te instanceof PedestrianButtonTileEntity)
			{
				IBlockState state = world.getBlockState(te.getPos());

				if (state.getBlock() instanceof BlockPedestrianButton)
				{
					EnumFacing facing = state.getValue(BlockPedestrianButton.FACING);

					boolean operationResult = false;
					if (facing == EnumFacing.EAST || facing == EnumFacing.WEST)
					{
						operationResult = controlBox.addOrRemoveNorthSouthPedButton(te.getPos());
					}
					else
					{
						operationResult = controlBox.addOrRemoveWestEastPedButton(te.getPos());
					}

					PedestrianButtonTileEntity pedTE = (PedestrianButtonTileEntity)te;
					if (operationResult)
					{
						pedTE.addPairedBox(controlBox.getPos());
						player.sendMessage(new TextComponentString("Paired Pedestrian Button to Traffic Light Control Box"));
					}
					else
					{
						pedTE.removePairedBox(controlBox.getPos());
						player.sendMessage(new TextComponentString("Unpaired Pedestrian Button to Traffic Light Control Box"));
					}
				}
			}
		}
	}
}
