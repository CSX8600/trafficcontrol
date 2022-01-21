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
	
	public static void initSounds()
	{
		gateEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":gate")).setRegistryName("trafficcontrol:gate");
		safetranType3Event = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":safetran_type_3")).setRegistryName("trafficcontrol:safetran_type_3");
		safetranMechanicalEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":safetran_mechanical")).setRegistryName("trafficcontrol:safetran_mechanical");
		wchEvent = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":wch")).setRegistryName("trafficcontrol:wch");
		pedButton = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":ped_button")).setRegistryName("trafficcontrol:ped_button");
		tunerLinkStart = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":tuner_link_start")).setRegistryName("trafficcontrol:tuner_link_start");
		tunerLinkStop = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":tuner_link_stop")).setRegistryName("trafficcontrol:tuner_link_stop");
		tunerLinkAdd = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":tuner_link_add")).setRegistryName("trafficcontrol:tuner_link_add");
		tunerLinkRemove = new SoundEvent(new ResourceLocation(ModTrafficControl.MODID + ":tuner_link_remove")).setRegistryName("trafficcontrol:tuner_link_remove");
	}
}
