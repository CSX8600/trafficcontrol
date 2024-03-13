package com.clussmanproductions.trafficcontrol;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

public class ModSounds {
	public static SoundEvent gateEvent;
	public static SoundEvent safetranType3Event;
	public static SoundEvent safetranMechanicalEvent;
	public static SoundEvent wchEvent;
	public static SoundEvent pedButton;
	public static SoundEvent wigWag;
	public static SoundEvent wch_mechanical_bell;
	
	public static void initSounds()
	{
		gateEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":gate")).setRegistryName("trafficcontrol:gate");
		safetranType3Event = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":safetran_type_3")).setRegistryName("trafficcontrol:safetran_type_3");
		safetranMechanicalEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":safetran_mechanical")).setRegistryName("trafficcontrol:safetran_mechanical");
		wchEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":wch")).setRegistryName("trafficcontrol:wch");
		pedButton = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":ped_button")).setRegistryName("trafficcontrol:ped_button");
		wigWag = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":wigwag")).setRegistryName("trafficcontrol:wigwag");
		wch_mechanical_bell = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":wch_mechanical_bell")).setRegistryName("trafficcontrol:wch_mechanical_bell");
	}
}
