package com.clussmanproductions.trafficcontrol.proxy;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity.Sign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static KeyBinding entityClassRendererKey;
	@SubscribeEvent
	public void preInit(FMLPreInitializationEvent e)
	{
		super.preInit(e);
		ModelLoaderRegistry.registerLoader(new com.clussmanproductions.trafficcontrol.blocks.model.ModelLoader());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e)
	{
		ModBlocks.initModels(e);
		ModItems.initModels(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);

		SignTileEntity.initializeSigns();
		ProgressBar progressBar = ProgressManager.push("Loading sign textures", SignTileEntity.SIGNS_BY_TYPE_VARIANT.size());
		for(Sign sign : SignTileEntity.SIGNS_BY_TYPE_VARIANT.values())
		{
			progressBar.step(sign.getImageResourceLocation().toString());
			Minecraft.getMinecraft().renderEngine.loadTexture(sign.getImageResourceLocation(), new SimpleTexture(sign.getImageResourceLocation()));
		}

		ProgressManager.pop(progressBar);

		entityClassRendererKey = new KeyBinding("key.entityclassrenderer.toggle", Keyboard.KEY_RBRACKET, "key.trafficcontrol.category");
		ClientRegistry.registerKeyBinding(entityClassRendererKey);
	}
}
