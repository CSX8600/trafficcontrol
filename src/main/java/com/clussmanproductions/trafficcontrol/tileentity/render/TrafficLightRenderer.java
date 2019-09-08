package com.clussmanproductions.trafficcontrol.tileentity.render;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfo;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfoCollection;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TrafficLightRenderer extends TileEntitySpecialRenderer<TrafficLightTileEntity> {
	ResourceLocation black = new ResourceLocation(ModTrafficControl.MODID + ":blocks/black");
	ResourceLocation blackTextured = new new ResourceLocation(ModTrafficControl.MODID + ":blocks/black_textured");
	@Override
	public void render(TrafficLightTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		TextureInfoCollection bulbTextureInfo = new TextureInfoCollection(new TextureInfo(black, 0, 0, 5.6, 5.5), 
				new TextureInfo(black, 0, 0, 5.6, 0.1), 
				new TextureInfo(black, 0, 0, 5.6, 5.5), 
				new TextureInfo(black, 0, 0, 5.6, 0.1), 
				new TextureInfo(black, 0, 0, 0.1, 5.5), 
				new TextureInfo(black, 0, 0, 0.1, 5.5));
		
		Texture
	}
}
