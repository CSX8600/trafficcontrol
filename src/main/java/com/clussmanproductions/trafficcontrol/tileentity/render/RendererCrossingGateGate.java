package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingGateGateTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.Box;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfo;
import com.clussmanproductions.trafficcontrol.tileentity.render.TESRHelper.TextureInfoCollection;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import scala.Tuple3;

public class RendererCrossingGateGate extends TileEntitySpecialRenderer<CrossingGateGateTileEntity> {
	@Override
	public void render(CrossingGateGateTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		
		Tuple3<Double, Double, Double> translationAmount = te.getTranslation(x, y, z);
		GlStateManager.translate(translationAmount._1(), translationAmount._2(), translationAmount._3());
		
		GlStateManager.rotate(te.getFacingRotation(), 0, 1, 0);
		
		GlStateManager.rotate(te.getGateRotation(), 0, 0, 1);
		
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder builder = tes.getBuffer();		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderWeightVerticies(builder);
		tes.draw();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		renderGateVerticies(builder, te.getCrossingGateLength());
		
		tes.draw();
		
		GlStateManager.popMatrix();
	}
	
	private void renderWeightVerticies(BufferBuilder builder)
	{
		ResourceLocation generic = new ResourceLocation(ModTrafficControl.MODID + ":textures/blocks/generic.png");
		TextureInfoCollection collection = new TextureInfoCollection(
				new TextureInfo(generic, 0, 0, 10, 4),
				new TextureInfo(generic, 0, 0, 10, 1),
				new TextureInfo(generic, 0, 0, 6, 5),
				new TextureInfo(generic, 0, 0, 10, 1),
				new TextureInfo(generic, 0, 0, 1, 4),
				new TextureInfo(generic, 0, 0, 1, 4));
		Box weightBox = new Box(-4, -2, 4, 12, 4, -1, collection);
		weightBox.render(builder, (tex) -> bindTexture(tex));
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
		Box gateBox = new Box(-(crossingGateLength * 16) - 12, -1, 4, (crossingGateLength * 16) + 8, 2, -1, collection);
		gateBox.render(builder, (tex) -> bindTexture(tex));
	}
}
