package com.clussmanproductions.trafficcontrol.gui;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID)
		{
			case GUI_IDs.TRAFFIC_LIGHT_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_frame)
				{
					return new TrafficLightFrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_frame)
				{
					return new YTrafficLightFrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_5_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_5_frame)
				{
					return new TrafficLight5FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_5_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_5_frame)
				{
					return new YTrafficLight5FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_DOGHOUSE_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_doghouse_frame)
				{
					return new TrafficLightDoghouseFrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_DOGHOUSE_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_doghouse_frame)
				{
					return new YTrafficLightDoghouseFrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_1_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_1_frame)
				{
					return new TrafficLight1FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_1_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_1_frame)
				{
					return new YTrafficLight1FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_2_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_2_frame)
				{
					return new TrafficLight2FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_2_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_2_frame)
				{
					return new YTrafficLight2FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_4_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_4_frame)
				{
					return new TrafficLight4FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_4_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_4_frame)
				{
					return new YTrafficLight4FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
				case GUI_IDs.TRAFFIC_LIGHT_6_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_6_frame)
				{
					return new TrafficLight6FrameContainer(player.inventory, player.getHeldItemMainhand());
				}
				break;
				case GUI_IDs.YTRAFFIC_LIGHT_6_FRAME:
					if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_6_frame)
					{
						return new YTrafficLight6FrameContainer(player.inventory, player.getHeldItemMainhand());
					}
					break;
				
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		switch(ID)
		{
			case GUI_IDs.SIGN:
				BlockPos pos = new BlockPos(x, y, z);
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof SignTileEntity)
				{
					SignTileEntity signTE = (SignTileEntity)te;
					return new SignGui(signTE);
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_frame)
				{
					return new TrafficLightFrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_frame)
				{
					return new YTrafficLightFrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_CONTROL_BOX:
				BlockPos preControlBoxPos = new BlockPos(x, y, z);
				TileEntity preControlBoxTE = world.getTileEntity(preControlBoxPos);
				if (preControlBoxTE instanceof TrafficLightControlBoxTileEntity)
				{
					TrafficLightControlBoxTileEntity controlBoxTE = (TrafficLightControlBoxTileEntity)preControlBoxTE;
					return new TrafficLightControlBoxGui(controlBoxTE);
				}
				break;
			case GUI_IDs.TYPE_3_BARRIER:
				BlockPos type3BarrierPos = new BlockPos(x, y, z);
				TileEntity type3BarrierTE = world.getTileEntity(type3BarrierPos);
				if (type3BarrierTE instanceof Type3BarrierTileEntity)
				{
					Type3BarrierTileEntity type3Barrier = (Type3BarrierTileEntity)type3BarrierTE;
					return new GuiType3Barrier(type3Barrier);
				}
				break;
			case GUI_IDs.CROSSING_GATE_GATE:
				BlockPos preCrossingGatePos = new BlockPos(x, y, z);
				TileEntity preCrossingGateTE = world.getTileEntity(preCrossingGatePos);
				if (preCrossingGateTE instanceof CrossingGateGateTileEntity)
				{
					CrossingGateGateTileEntity crossingGateTE = (CrossingGateGateTileEntity)preCrossingGateTE;
					return new CrossingGateGateGui(crossingGateTE);
				}
				break;
			case GUI_IDs.STREET_SIGN:
				BlockPos preStreetSignPos = new BlockPos(x, y, z);
				TileEntity preStreetSignTE = world.getTileEntity(preStreetSignPos);
				if (preStreetSignTE instanceof StreetSignTileEntity)
				{
					StreetSignTileEntity streetSignTileEntity = (StreetSignTileEntity)preStreetSignTE;
					return new StreetSignGui(streetSignTileEntity);
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_5_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_5_frame)
				{
					return new TrafficLight5FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_5_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_5_frame)
				{
					return new YTrafficLight5FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_DOGHOUSE_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_doghouse_frame)
				{
					return new TrafficLightDoghouseFrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_DOGHOUSE_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_doghouse_frame)
				{
					return new YTrafficLightDoghouseFrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_1_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_1_frame)
				{
					return new TrafficLight1FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_1_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_1_frame)
				{
					return new YTrafficLight1FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_2_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_2_frame)
				{
					return new TrafficLight2FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_2_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_2_frame)
				{
					return new YTrafficLight2FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.TRAFFIC_LIGHT_4_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_4_frame)
				{
					return new TrafficLight4FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
			case GUI_IDs.YTRAFFIC_LIGHT_4_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_4_frame)
				{
					return new YTrafficLight4FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
				case GUI_IDs.TRAFFIC_LIGHT_6_FRAME:
				if (player.getHeldItemMainhand().getItem() == ModItems.traffic_light_6_frame)
				{
					return new TrafficLight6FrameGui(player.inventory, player.getHeldItemMainhand());
				}
				break;
				case GUI_IDs.YTRAFFIC_LIGHT_6_FRAME:
					if (player.getHeldItemMainhand().getItem() == ModItems.ytraffic_light_6_frame)
					{
						return new YTrafficLight6FrameGui(player.inventory, player.getHeldItemMainhand());
					}
					break;
				
		}

		return null;
	}

	public static class GUI_IDs
	{
		public static final int SIGN = 1;
		public static final int TRAFFIC_LIGHT_FRAME = 2;
		public static final int TRAFFIC_LIGHT_CONTROL_BOX = 3;
		public static final int TYPE_3_BARRIER = 4;
		public static final int CROSSING_GATE_GATE = 5;
		public static final int STREET_SIGN = 6;
		public static final int TRAFFIC_LIGHT_5_FRAME = 7;
		public static final int TRAFFIC_LIGHT_DOGHOUSE_FRAME = 8;
		public static final int TRAFFIC_LIGHT_1_FRAME = 9;
		public static final int TRAFFIC_LIGHT_2_FRAME = 10;
		public static final int TRAFFIC_LIGHT_4_FRAME = 11;
		public static final int TRAFFIC_LIGHT_6_FRAME = 12;
		public static final int YTRAFFIC_LIGHT_FRAME = 13;
		public static final int YTRAFFIC_LIGHT_1_FRAME = 14;
		public static final int YTRAFFIC_LIGHT_2_FRAME = 15;
		public static final int YTRAFFIC_LIGHT_4_FRAME = 16;
		public static final int YTRAFFIC_LIGHT_5_FRAME = 17;
		public static final int YTRAFFIC_LIGHT_DOGHOUSE_FRAME = 18;
		public static final int YTRAFFIC_LIGHT_6_FRAME = 19;
	}
}
