package com.clussmanproductions.trafficcontrol.tileentity.render;

import com.clussmanproductions.trafficcontrol.tileentity.ICustomAngle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CustomAngleRenderer<T extends TileEntity & ICustomAngle> extends TileEntitySpecialRenderer<T> {

	@Override
	public final void render(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate(te.getAngle(), 0, 1, 0);
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		World world = te.getWorld();
		BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBakedModel model = dispatcher.getModelForState(world.getBlockState(te.getPos()));
		dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), model, world.getBlockState(te.getPos()), te.getPos(), builder, true);
		
		
		GlStateManager.popMatrix();
	}
	
	protected void doRender(T te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) { }
}
