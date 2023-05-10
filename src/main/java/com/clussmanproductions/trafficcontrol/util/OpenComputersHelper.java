package com.clussmanproductions.trafficcontrol.util;

import com.clussmanproductions.trafficcontrol.oc.TrafficLightCardDriver;

import li.cil.oc.api.Driver;

public class OpenComputersHelper {

	public static void addOCDriver()
	{
		Driver.add(new TrafficLightCardDriver()	);
	}
}
