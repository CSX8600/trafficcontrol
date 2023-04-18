package com.clussmanproductions.trafficcontrol;

import org.apache.logging.log4j.Level;

import com.clussmanproductions.trafficcontrol.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_TRAFFIC_LIGHT = "traffic_light";
	
	public static int islandTimeout = 20;
	public static int borderTimeout = 150;
	public static int borderTick = 10; 
	public static int parallelScans = 1;
	public static int tooltipCharWrapLength = 256;
	public static String[] sensorClasses = new String[] 
			{
				"minecrafttransportsimulator.vehicles.main.EntityVehicleD_Moving",
				"com.mrcrayfish.vehicle.entity.EntityVehicle",
				"com.flansmod.common.driveables.EntityDriveable",
				"net.fexcraft.mod.fvtm.sys.legacy.LandVehicle",
				"net.fexcraft.mod.fvtm.sys.uni12.ULandVehicle",
				"net.minecraft.entity.passive.EntityPig",
				"net.minecraft.entity.passive.EntityHorse",
				"net.minecraft.entity.passive.EntityDonkey",
				"net.minecraft.entity.passive.EntityMule",
				"net.minecraft.entity.passive.EntitySkeletonHorse",
				"net.minecraft.entity.passive.EntityZombieHorse",
				"de.maxhenkel.car.entity.car.base.EntityVehicleBase"
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
		sensorClasses = cfg.getStringList("sensorClasses", CATEGORY_TRAFFIC_LIGHT, sensorClasses, "What entity classes will activate the traffic signal sensors?");
		sensorScanHeight = cfg.getInt("sensorScanHeight", CATEGORY_TRAFFIC_LIGHT, sensorScanHeight, 0, 10, "How far up (in blocks) should traffic signal sensors scan for entities? [Min = 0, Max = 10, Default = 5]");
		parallelScans = cfg.getInt("parallelScans", CATEGORY_GENERAL, parallelScans, 1, 20, "How many crossing relay boxes should be scanned per tick?  PERFORMANCE NOTE: Total blocks scanned = borderTick * parallelScans.  The higher this number, the amount of blocks scanned per tick is multiplied.");
		tooltipCharWrapLength = cfg.getInt("tooltipCharWrapLength", CATEGORY_GENERAL, tooltipCharWrapLength, 64, 5412, "How many letters should be rendered in a tooltip before it wraps down to the next line?");
	}
}
