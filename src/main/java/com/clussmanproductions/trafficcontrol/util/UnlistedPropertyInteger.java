package com.clussmanproductions.trafficcontrol.util;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyInteger implements IUnlistedProperty<Integer> {

	private String name;
	public UnlistedPropertyInteger(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Integer value) {
		return value != null;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public String valueToString(Integer value) {
		return value.toString();
	}
}
