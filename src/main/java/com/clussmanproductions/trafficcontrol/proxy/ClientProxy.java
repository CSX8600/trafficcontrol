package com.clussmanproductions.trafficcontrol.proxy;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.network.ServerSideSoundPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
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
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

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
	
	public static void playSoundHandler(ServerSideSoundPacket message, MessageContext ctx)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		WorldClient world = Minecraft.getMinecraft().world;
		
		ResourceLocation soundLocation = new ResourceLocation(message.modID, message.soundName);
		IForgeRegistry<SoundEvent> soundRegistry = GameRegistry.findRegistry(SoundEvent.class);
		SoundEvent sound = soundRegistry.getValue(soundLocation);
		
		if (sound == null)
		{
			ModTrafficControl.logger.warn(String.format("Tried to play sound %s but it does not exist!", message.modID + ":" + message.soundName));
			return;
		}
		
		PositionedSoundRecord record = new PositionedSoundRecord(sound, SoundCategory.BLOCKS, message.volume, message.pitch, message.pos);
		Minecraft.getMinecraft().getSoundHandler().playSound(record);
	}
}
