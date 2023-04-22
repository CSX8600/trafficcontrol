package com.clussmanproductions.trafficcontrol.oc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.Config;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightCard;
import com.clussmanproductions.trafficcontrol.tileentity.BaseTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import li.cil.oc.api.Network;
import li.cil.oc.api.driver.item.Slot;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.machine.Value;
import li.cil.oc.api.network.ComponentConnector;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;
import li.cil.oc.api.prefab.DriverItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TrafficLightCardDriver extends DriverItem {

	public TrafficLightCardDriver()
	{
		super(new ItemStack(ModItems.traffic_light_card, 1, 0),
			  new ItemStack(ModItems.traffic_light_card, 1, 1),
			  new ItemStack(ModItems.traffic_light_card, 1, 2),
			  new ItemStack(ModItems.traffic_light_card, 1, 3));
	}
	
	@Override
	public ManagedEnvironment createEnvironment(ItemStack stack, EnvironmentHost host) {
		return new CardEnvironment(stack, host);
	}

	@Override
	public String slot(ItemStack stack) {
		return Slot.Card;
	}
	
	@Override
	public int tier(ItemStack stack) {
		switch(stack.getMetadata())
		{
			case 0:
				return 0;
			case 1:
				return 1;
			default:
				return 2;
		}
	}

	public class CardEnvironment extends AbstractManagedEnvironment
	{
		private final ItemStack card;
		private final EnvironmentHost host;
		public CardEnvironment(ItemStack card, EnvironmentHost host)
		{
			this.card = card;
			this.host = host;
			setNode(Network.newNode(this, Visibility.Neighbors).withComponent("traffic_light_card").withConnector(300).create());
		}
		
		@Callback(direct = true, doc = "listBlockPos():array -- Retrieves a list of block positions currently in use by the card")
		public Object[] listBlockPos(Context c, Arguments args)
		{
			NBTTagCompound cardTag = card.getTagCompound();
			if (cardTag == null)
			{
				return new Object[] { new ArrayList<Integer[]>() };
			}
			
			int maxTrafficLights = ItemTrafficLightCard.getMaxTrafficLights(card.getMetadata());
			ArrayList<Integer[]> blockPositions = new ArrayList<>();
			
			for(int i = 0; i < maxTrafficLights; i++)
			{
				if (cardTag.hasKey("light" + i) && cardTag.getLong("light" + i) != 0)
				{
					BlockPos position = BlockPos.fromLong(cardTag.getLong("light" + i));
					blockPositions.add(new Integer[] { position.getX(), position.getY(), position.getZ() });
				}
			}
			
			return new Object[] { blockPositions };
		}
		
		@Callback(direct = true, doc = "listBlockIDs():array -- Retrieves a list of block ids currently in use by the card")
		public Object[] listBlockIDs(Context c, Arguments args)
		{
			NBTTagCompound cardTag = card.getTagCompound();
			if (cardTag == null)
			{
				return new Object[] { new ArrayList<Long>() };
			}
			
			int maxTrafficLights = ItemTrafficLightCard.getMaxTrafficLights(card.getMetadata());
			ArrayList<Long> blockPositions = new ArrayList<>();
			
			for(int i = 0; i < maxTrafficLights; i++)
			{
				if (cardTag.hasKey("light" + i) && cardTag.getLong("light" + i) != 0)
				{
					blockPositions.add(cardTag.getLong("light" + i));
				}
			}
			
			return new Object[] { blockPositions };
		}
		
		private final Map<String, EnumTrafficLightBulbTypes> bulbTypesByString = Arrays.stream(EnumTrafficLightBulbTypes.values()).collect(Collectors.toMap(EnumTrafficLightBulbTypes::toString, Function.identity()));
		private final Map<String, String> bulbTypeStringsByString = Arrays.stream(EnumTrafficLightBulbTypes.values()).collect(Collectors.toMap(EnumTrafficLightBulbTypes::toString, EnumTrafficLightBulbTypes::toString));
		@Callback(direct = true, getter = true, doc = "states():array -- Retrieves a list of all possible states")
		public Object[] states(Context c, Arguments args)
		{
			return new Object[] { bulbTypeStringsByString };
		}
		
		@Callback(doc = "setState(x:int, y:int, z:int, state:string, active:boolean, flash:boolean):boolean, string OR setState(id:long, state:string, active:boolean, flash:boolean) -- Sets the state of the specified traffic light")
		public Object[] setState(Context c, Arguments args) throws Exception
		{
			BlockPos pos = getBlockPosFromArgs(args);
			if (!(args.isString(3) && args.isBoolean(4) && args.isBoolean(5)) &&
				!(args.isString(1) && args.isBoolean(2) && args.isBoolean(3)))
			{
				throw new IllegalArgumentException("Invalid argument format");
			}
			
			if (!cardContainsPos(pos))
			{
				return new Object[] { false, "Card does not contain this block position" };
			}
			
			String state;
			boolean active;
			boolean flash;
			if (args.isInteger(1))
			{
				state = args.checkString(3);
				active = args.checkBoolean(4);
				flash = args.checkBoolean(5);
			}
			else
			{
				state = args.checkString(1);
				active = args.checkBoolean(2);
				flash = args.checkBoolean(3);
			}
			
			if (!bulbTypesByString.containsKey(state))
			{
				return new Object[] { false, "Invalid state specified" };
			}
			
			TileEntity posTE = host.world().getTileEntity(pos);
			if (!(posTE instanceof BaseTrafficLightTileEntity))
			{
				return new Object[] { false, "A traffic light no longer exists at this block position" };
			}
			
			BaseTrafficLightTileEntity trafficLight = (BaseTrafficLightTileEntity)posTE;
			if (!trafficLight.hasBulb(bulbTypesByString.get(state)))
			{
				return new Object[] { false, "Traffic light does not contain the specified bulb" };
			}
			
			BlockPos hostPos = new BlockPos(host.xPosition(), host.yPosition(), host.zPosition());
			
			double draw = (Config.trafficLightCardDrawPerBlock * hostPos.distanceSq(pos));
			if (!((ComponentConnector)node()).tryChangeBuffer(-draw))
			{
				return new Object[] { false, "Not enough energy" };
			}
			
			trafficLight.setActive(bulbTypesByString.get(state), active, flash);
			return new Object[] { true };
		}
		
		@Callback(doc = "clearStates(x:int, y:int, z:int):boolean, string OR clearStates(id:long):boolean, string -- Sets all states to inactive for the specified traffic light")
		public Object[] clearStates(Context c, Arguments args) throws Exception
		{
			BlockPos pos = getBlockPosFromArgs(args);
			
			if (!cardContainsPos(pos))
			{
				return new Object[] { false, "Card does not contain this block position" };
			}
			
			TileEntity posTE = host.world().getTileEntity(pos);
			if (!(posTE instanceof BaseTrafficLightTileEntity))
			{
				return new Object[] { false, "A traffic light no longer exists at this block position" };
			}
			
			BlockPos hostPos = new BlockPos(host.xPosition(), host.yPosition(), host.zPosition());
			double draw = (Config.trafficLightCardDrawPerBlock * hostPos.distanceSq(pos));
			if (!((ComponentConnector)node()).tryChangeBuffer(-draw))
			{
				return new Object[] { false, "Not enough energy" };
			}
			
			BaseTrafficLightTileEntity trafficLight = (BaseTrafficLightTileEntity)posTE;
			trafficLight.powerOff();
			trafficLight.setActive(EnumTrafficLightBulbTypes.DontCross, false, false); // Power off automatically turns on Dont Cross, which is wrong in this context
			return new Object[] { true };
		}
		
		@Callback(doc = "getStates(x:int, y:int, z:int):boolean, string/table OR getStates(id:long):boolean, string/table -- Returns a table where the key is the state and value is a table that has an active and flash key")
		public Object[] getStates(Context c, Arguments args) throws Exception
		{
			BlockPos pos = getBlockPosFromArgs(args);
			
			if (!cardContainsPos(pos))
			{
				return new Object[] { false, "Card does not contain this block position" };
			}
			
			TileEntity posTE = host.world().getTileEntity(pos);
			if (!(posTE instanceof BaseTrafficLightTileEntity))
			{
				return new Object[] { false, "A traffic light no longer exists at this block position" };
			}
			
			BlockPos hostPos = new BlockPos(host.xPosition(), host.yPosition(), host.zPosition());
			double draw = (Config.trafficLightCardDrawPerBlock * hostPos.distanceSq(pos));
			if (!((ComponentConnector)node()).tryChangeBuffer(-draw))
			{
				return new Object[] { false, "Not enough energy" };
			}
			
			BaseTrafficLightTileEntity trafficLight = (BaseTrafficLightTileEntity)posTE;
			HashMap<String, StateInfo> stateInfos = new HashMap<>();
			for(String bulbTypeString : bulbTypeStringsByString.keySet())
			{
				stateInfos.put(bulbTypeString, new StateInfo());
			}
			
			HashSet<String> discoveredStates = new HashSet<>();
			
			for(int i = 0; i < trafficLight.getBulbCount(); i++)
			{
				EnumTrafficLightBulbTypes bulbType = trafficLight.getBulbTypeBySlot(i); 
				if (bulbType == null)
				{
					continue;
				}
				
				if (discoveredStates.add(bulbType.toString()))
				{
					stateInfos.get(bulbType.toString()).active = trafficLight.getActiveBySlot(i);
					stateInfos.get(bulbType.toString()).flash = trafficLight.getFlashBySlot(i);
				}
			}
			
			return new Object[] { true, stateInfos };
		}
		
		private class StateInfo implements Value
		{
			public boolean active;
			public boolean flash;
			
			@Override
			public void load(NBTTagCompound nbt) {
				active = nbt.getBoolean("active");
				flash = nbt.getBoolean("flash");
			}
			@Override
			public void save(NBTTagCompound nbt) {
				nbt.setBoolean("active", active);
				nbt.setBoolean("flash", flash);
			}
			@Override
			public Object apply(Context context, Arguments arguments) {
				switch(arguments.checkString(0))
				{
					case "active":
						return active;
					case "flash":
						return flash;
				}
				
				return null;
			}
			@Override
			public void unapply(Context context, Arguments arguments) { }
			@Override
			public Object[] call(Context context, Arguments arguments) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void dispose(Context context) {
				// TODO Auto-generated method stub
				
			}
		}
		
		private BlockPos getBlockPosFromArgs(Arguments args) throws Exception
		{
			// Check valid args
			if (!(args.isInteger(0) && args.isInteger(1) && args.isInteger(2)) ||
				!args.isDouble(0))
			{
				throw new IllegalArgumentException("Invalid argument format");
			}
			
			BlockPos pos;
			if (args.isInteger(0) && args.isInteger(1) && args.isInteger(2))
			{
				pos = new BlockPos(args.checkInteger(0), args.checkInteger(1), args.checkInteger(2));
			}
			else if (args.isDouble(0) && args.isString(1))
			{
				double posIDDouble = args.checkDouble(0);
				pos = BlockPos.fromLong((long)posIDDouble);
			}
			else
			{
				throw new IllegalArgumentException("Could not determine block position");
			}
			
			return pos;
		}
		
		private boolean cardContainsPos(BlockPos pos)
		{
			long id = pos.toLong();
			for(String lightKey : card.getTagCompound().getKeySet().stream().filter(key -> key.startsWith("light")).collect(Collectors.toList()))
			{
				long tagKey = card.getTagCompound().getLong(lightKey);
				if (tagKey == id)
				{
					return true;
				}
			}
			
			return false;
		}
	}
}
