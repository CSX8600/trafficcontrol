package com.clussmanproductions.trafficcontrol.util;

import net.minecraft.util.math.BlockPos;

public interface ITrafficLightContainer {
	boolean hasBulb(EnumTrafficLightBulbTypes bulbType);
	
	void setBulbActive(EnumTrafficLightBulbTypes bulbType, boolean active);
	
	void setRelayPosition(BlockPos relayPos);
}
