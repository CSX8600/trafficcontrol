package com.clussmanproductions.trafficcontrol;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.clussmanproductions.trafficcontrol.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_TRAFFIC_LIGHT = "traffic_light";
	
	public static int islandTimeout = 20;
	public static int borderTimeout = 150;
	public static int borderTick = 10; 
	public static String[] sensorEntities = new String[] 
			{
				"mts:vehicleg_car",
				"vehicle:bumper_car",
				"vehicle:couch",
				"vehicle:dirt_bike",
				"vehicle:moped",
				"vehicle:tractor",
				"vehicle:smart_car",
				"vehicle:dune_buggy",
				"vehicle:go_kart",
				"vehicle:golf_cart",
				"vehicle:mini_bike",
				"vehicle:mini_bus",
				"vehicle:off_roader",
				"vehicle:atv",
				"flansmod:panzer",
				"flansmod:sasjeep",
				"flansmod:sdkfz251",
				"flansmod:tiger",
				"flansmod:hellcat",
				"flansmod:uc2pdr",
				"flansmod:m3halftrack",
				"flansmod:sdkfz2",
				"flansmod:b1",
				"flansmod:kv1",
				"flansmod:humvee",
				"flansmod:t90",
				"flansmod:leo2a6",
				"flansmod:abrams",
				"flansmod:buggy",
				"flansmod:sdkfz222",
				"flansmod:cromwell",
				"flansmod:tigerii",
				"flansmod:bmwr75",
				"flansmod:tiger131",
				"flansmod:t34",
				"flansmod:crusader",
				"flansmod:stug",
				"flansmod:churchill",
				"flansmod:greyhound",
				"flansmod:sherman",
				"flansmod:chaffee",
				"flansmod:kubel",
				"fvp:t1p",
				"fvp:c11",
				"fvp:c10",
				"fvp:c1r1",
				"fvp:ab1",
				"fvp:c4",
				"fvp:c5",
				"fvp:c8",
				"fvp:t2",
				"fvp:tr1",
				"fvp:sentinel",
				"minecraft:pig",
				"minecraft:horse",
				"minecraft:donkey",
				"minecraft:mule",
				"minecraft:skeleton_horse",
				"minecraft:zombie_horse"
			};
	public static int sensorScanHeight = 5;
	
	public static void readConfig()
	{
		Configuration cfg = CommonProxy.config;
		try
		{
			cfg.load();
			initGeneralConfig(cfg);
		}
		catch(Exception e)
		{
			ModTrafficControl.logger.log(Level.ERROR, "Problem loading config file!", e);
		}
		finally
		{
			if (cfg.hasChanged())
			{
				cfg.save();
			}
		}
	}
	
	private static void initGeneralConfig(Configuration cfg)
	{
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
		islandTimeout = cfg.getInt("islandTimeout", CATEGORY_GENERAL, islandTimeout, 1, 100, "How far (in blocks) should each island shunt scan for the next island shunt?");
		borderTimeout = cfg.getInt("borderTimeout", CATEGORY_GENERAL, borderTimeout, 1, 2000, "How far (in blocks) should border shunts scan for the next island shunt?");
		borderTick = cfg.getInt("borderTick", CATEGORY_GENERAL, borderTick, 1, 2000, "How far (in blocks) should border shunts scan for the next island shunt per tick?");
		sensorEntities = cfg.getStringList("sensorEntities", CATEGORY_TRAFFIC_LIGHT, sensorEntities, "What entities will activate the traffic signal sensors?");
		sensorScanHeight = cfg.getInt("sensorScanHeight", CATEGORY_TRAFFIC_LIGHT, sensorScanHeight, 0, 10, "How far up (in blocks) should traffic signal sensors scan for entities? [Min = 0, Max = 10, Default = 5]");
	}
}
