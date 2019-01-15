package com.clussmanproductions.trafficcontrol;

import org.apache.logging.log4j.Logger;

import com.clussmanproductions.trafficcontrol.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModTrafficControl.MODID, version = ModTrafficControl.VERSION, name = "Traffic Control", useMetadata = true)
public class ModTrafficControl {
	public static final String MODID = "trafficcontrol";
	public static final String VERSION = "0.0.2";
	public static CreativeTabs CREATIVE_TAB = new CreativeTabs("Traffic Control") {
		
		@Override
		public ItemStack getTabIconItem() {
			// TODO Auto-generated method stub
			return new ItemStack(ModBlocks.cone);
		}
	};
	
	@SidedProxy(clientSide = "com.clussmanproductions.trafficcontrol.proxy.ClientProxy", serverSide = "com.clussmanproductions.trafficcontrol.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	@Mod.Instance
	public static ModTrafficControl instance;
	
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
