package com.clussmanproductions.trafficcontrol;

import org.apache.logging.log4j.Level;

import com.clussmanproductions.trafficcontrol.proxy.CommonProxy;

import net.minecraftforge.common.config.Configuration;

public class Config {
	private static final String CATEGORY_GENERAL = "general";
	
	public static int islandTimeout = 20;
	public static int borderTimeout = 150;
	public static int borderTick = 10;
	
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
	}
	
	private static void initGeneralConfig(Configuration cfg)
	{
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
		islandTimeout = cfg.getInt("islandTimeout", CATEGORY_GENERAL, islandTimeout, 1, 100, "How far (in blocks) should each island shunt scan for the next island shunt?");
		borderTimeout = cfg.getInt("borderTimeout", CATEGORY_GENERAL, borderTimeout, 1, 2000, "How far (in blocks) should border shunts scan for the next island shunt?");
		borderTick = cfg.getInt("borderTick", CATEGORY_GENERAL, borderTick, 1, 2000, "How far (in blocks) should border shunts scan for the next island shunt per tick?");
	}
}
