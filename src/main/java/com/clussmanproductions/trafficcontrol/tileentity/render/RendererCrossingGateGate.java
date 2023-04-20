package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase.EnumState;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.Box;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfo;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfoCollection;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RendererCrossingGateGate extends TileEntitySpecialRenderer<CrossingGateGateTileEntity> {
	@Override
	public void render(CrossingGateGateTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		
		GlStateManager.rotate(te.getFacingRotation(), 0, 1, 0);
		
		GlStateManager.scale(.0625, .0625, .0625);
		
		GlStateManager.translate(3, 2, 0);
		
		GlStateManager.rotate(te.getGateRotation(), 0, 0, 1);
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder builder = tes.getBuffer();		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderWeightVerticies(builder);
		tes.draw();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderGateVerticies(builder, te.getCrossingGateLength());
		tes.draw();
		
		if (te.getCrossingGateLength() - te.getLightStartOffset() >= 2)
		{
			renderGateLights(builder, te.getCrossingGateLength(), te.getFlashState(), te.getLightStartOffset());
		}
		
		
		GlStateManager.popMatrix();
	}
	
	private void renderWeightVerticies(BufferBuilder builder)
	{
		// Rotator Cross
		ResourceLocation generic = new ResourceLocation(ModTrafficControl.MODID + ":textures/blocks/generic.png");
		TextureInfoCollection collection = new TextureInfoCollection(
				new TextureInfo(generic, 0, 0, 1, 1),
				new TextureInfo(generic, 0, 0, 8, 1),
				new TextureInfo(generic, 0, 0, 1, 1),
				new TextureInfo(generic, 0, 0, 8, 1),
				new TextureInfo(generic, 0, 0, 8, 1),
				new TextureInfo(generic, 0, 0, 8, 1));
		Box box = new Box(-7.5, -9.5, 4, 1, 2, -8, collection);
		box.render(builder, (tex) -> bindTexture(tex));
		
		// Rotator Arm Support
		collection = new TextureInfoCollection(
				new TextureInfo(generic, 3, 4, 8, 5),
				new TextureInfo(generic, 7, 4, 8, 5),
				new TextureInfo(generic, 3, 3, 8, 4),
				new TextureInfo(generic, 4, 4, 5, 5),
				new TextureInfo(generic, 4, 4, 5, 9),
				new TextureInfo(generic, 4, 4, 5, 9));
		box = new Box(-6.5, -9.5, 4, 7, 2, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	

		box = new Box(-6.5, -9.5, -3, 7, 2, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	
		
		// Rotator Connector
		collection = new TextureInfoCollection(
				new TextureInfo(generic, 5, 9, 12, 12),
				new TextureInfo(generic, 1, 7, 8, 8),
				new TextureInfo(generic, 6, 10, 13, 13),
				new TextureInfo(generic, 2, 7, 9, 8),
				new TextureInfo(generic, 5, 6, 6, 9),
				new TextureInfo(generic, 2, 3, 3, 6));

		box = new Box(-2.5, -7.5, 4, 3, 8.5, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	

		box = new Box(-2.5, -7.5, -3, 3, 8.5, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));
		
		// Weight connector
		box = new Box(0.5, -2, 4, 3, 3, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	

		box = new Box(0.5, -2, -3, 3, 3, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	

		// Weight
		box = new Box(3.5, -3.5, 4, 10, 6, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	

		box = new Box(3.5, -3.5, -3, 10, 6, -1, collection);
		box.render(builder, (tex) -> bindTexture(tex));	
	}
	
	private void renderGateVerticies(BufferBuilder builder, float crossingGateLength)
	{
		ResourceLocation gate = new ResourceLocation(ModTrafficControl.MODID + ":textures/blocks/gate.png");
		TextureInfoCollection collection = new TextureInfoCollection(
				new TextureInfo(gate, 0, 0, 16, 0.7),
				new TextureInfo(gate, 0, 2, 16, 2.7),
				new TextureInfo(gate, 0, 0, 15, 0.7),
				new TextureInfo(gate, 0, 1, 16, 1.7),
				new TextureInfo(gate, 0, 2, 3, 4),
				new TextureInfo(gate, 0, 2, 3, 4));
		Box gateBox = new Box( -(crossingGateLength * 16) - 13,  -9.5, 0.5, 
							   (crossingGateLength * 16) + 5.5,     2,  -1, collection);
		gateBox.render(builder, (tex) -> bindTexture(tex));
	}
	
	private final ModelResourceLocation lightOffLocation = new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_light", "normal");
	private final ModelResourceLocation lightOnLocation = new ModelResourceLocation(ModTrafficControl.MODID + ":crossing_gate_light", "on=true");
	private void renderGateLights(BufferBuilder builder, float crossingGateLength, BlockLampBase.EnumState flashState, float lightStartOffset)
	{
		IBakedModel modelOff = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(lightOffLocation);
		IBakedModel modelOn = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(lightOnLocation);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		// First
		GlStateManager.disableLighting();
		GlStateManager.translate(-20.5 - (lightStartOffset * 16), -7.5, -8);
		GlStateManager.scale(1 / .0625, 1 / .0625, 1 / .0625);
		IBakedModel model = flashState == EnumState.Flash2 ? modelOn : modelOff;
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(model, 1, 1, 1, 1);
		GlStateManager.scale(0.0625, 0.0625, 0.0625);
		
		// Middle
		GlStateManager.translate(-(crossingGateLength - lightStartOffset) * 16 / 2 + 1, 0, 0);
		GlStateManager.scale(1 / .0625, 1 / .0625, 1 / .0625);
		model = flashState == EnumState.Flash1 ? modelOn : modelOff;
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(model, 1, 1, 1, 1);
		GlStateManager.scale(0.0625, 0.0625, 0.0625);
		
		// End
		GlStateManager.translate(-(crossingGateLength - lightStartOffset) * 16 / 2 + 1, 0, 0);
		GlStateManager.scale(1 / .0625, 1 / .0625, 1 / .0625);
		model = flashState == EnumState.Off ? modelOff : modelOn;
		Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(model, 1, 1, 1, 1);
		GlStateManager.scale(0.0625, 0.0625, 0.0625);

		GlStateManager.enableLighting();
	}
}
