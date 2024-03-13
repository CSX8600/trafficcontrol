package com.clussmanproductions.trafficcontrol.proxy;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
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

//		SignTileEntity.initializeSigns();
//		ProgressBar progressBar = ProgressManager.push("Loading sign textures", SignTileEntity.SIGNS_BY_TYPE_VARIANT.size());
//		for(Sign sign : SignTileEntity.SIGNS_BY_TYPE_VARIANT.values())
//		{
//			progressBar.step(sign.getImageResourceLocation().toString());
//			Minecraft.getMinecraft().renderEngine.loadTexture(sign.getImageResourceLocation(), new SimpleTexture(sign.getImageResourceLocation()));
//		}
//
//		ProgressManager.pop(progressBar);

		entityClassRendererKey = new KeyBinding("key.entityclassrenderer.toggle", Keyboard.KEY_RBRACKET, "key.trafficcontrol.category");
		ClientRegistry.registerKeyBinding(entityClassRendererKey);
	}

	@SubscribeEvent
	public static void bakeModels(ModelBakeEvent e)
	{
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_light", "normal"));
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_light", "on=true"));
		
		for(String direction : new String[] { "ne", "nw", "se", "sw" })
		{
			for(EnumState flashState : EnumState.values())
			{
				for(int i = 0; i <= 16; i++)
				{
					bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_lamps_" + direction + "_lamp", "rotation=" + i + ",state=" + flashState.getName()));
					bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":overhead_lamps_" + direction + "_lamp", "rotation=" + i + ",state=" + flashState.getName()));
				}
			}
		}
		
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_pole_ext", "rotation=" + facing.getName()));
		}
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_pole_ext", "rotation=down"));
		
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":vertical_wig_wag_arm", "normal"));
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":vertical_wig_wag_arm", "lamp=off"));
		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":vertical_wig_wag_arm", "lamp=on"));
		
//		bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_lamps_sw_support", "normal"));
//		for(EnumState flashState : EnumState.values())
//		{
//			bakeModel(e, new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_lamps_sw_lamp", "state=" + flashState.getName()));
//		}
	}

	private static void bakeModel(ModelBakeEvent e, ModelResourceLocation location)
	{
		try {
			IModel model = ModelLoaderRegistry.getModel(location);
			IBakedModel bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, ModelLoader.defaultTextureGetter());
			e.getModelRegistry().putObject(location, bakedModel);
		} catch (Exception e1) {
			ModTrafficControl.logger.error("An error occurred while baking a custom model", e1);
		}
	}
}
