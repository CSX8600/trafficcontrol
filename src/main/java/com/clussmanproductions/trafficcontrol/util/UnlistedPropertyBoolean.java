package com.clussmanproductions.trafficcontrol.util;

import net.minecraft.block.properties.PropertyBool;
import net.minecraftforge.common.property.Properties.PropertyAdapter;

public class UnlistedPropertyBoolean extends PropertyAdapter<Boolean> {

	public UnlistedPropertyBoolean(String name) {
		super(PropertyBool.create(name));
	}

}
