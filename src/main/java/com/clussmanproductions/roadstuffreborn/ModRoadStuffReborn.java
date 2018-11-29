package com.clussmanproductions.roadstuffreborn;

import org.apache.logging.log4j.Logger;

import com.clussmanproductions.roadstuffreborn.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModRoadStuffReborn.MODID, version = ModRoadStuffReborn.VERSION, name = "RoadStuff Reborn", useMetadata = true)
public class ModRoadStuffReborn {
	public static final String MODID = "roadstuffreborn";
	public static final String VERSION = "0.0.1";
	public static CreativeTabs CREATIVE_TAB = new CreativeTabs("Roadstuff Reborn") {
		
		@Override
		public ItemStack getTabIconItem() {
			// TODO Auto-generated method stub
			return new ItemStack(Blocks.STONE);
		}
	};
	
	@SidedProxy(clientSide = "com.clussmanproductions.roadstuffreborn.proxy.ClientProxy", serverSide = "com.clussmanproductions.roadstuffreborn.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		logger = e.getModLog();
		proxy.preInit(e);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.init(e);
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.postInit(e);
	}
}
