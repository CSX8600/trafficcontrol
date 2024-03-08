package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsPoleBasedTileEntity;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CrossingGateLampsRenderer extends TileEntitySpecialRenderer<CrossingLampsPoleBasedTileEntity> {

	@Override
	public void render(CrossingLampsPoleBasedTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		if (te == null || !(getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockCrossingGateLamps))
		{
			return;
		}
		
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		ArrayList<EnumFacing> facings = new ArrayList<>();
		facings.add(null);
		for(EnumFacing facing : EnumFacing.values())
		{
			facings.add(facing);
		}
		
		IBlockState blockState = getWorld().getBlockState(te.getPos());
		
		ModelManager manager = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
		
		// === PRE RENDER;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
        // ===
		
		// === ROTATE
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.rotate(blockState.getValue(BlockCrossingGateLamps.ROTATION) * -22.5F + 180F, 0, 1, 0);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		// ===
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		
		if (te.getNeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_ne_lamp", "rotation=" + te.getNeBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getNwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_nw_lamp", "rotation=" + te.getNwBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getSeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_se_lamp", "rotation=" + te.getSeBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getSwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_sw_lamp", "rotation=" + te.getSwBulbRotation() + ",state=" + te.getState().getName());
		}
		
		tess.draw();
		
		// === POST RENDER
		GlStateManager.popMatrix();
		// ===
	}

	private void addModelToBuffer(ArrayList<EnumFacing> facings, ModelManager manager,
			BufferBuilder builder, String modelName, String variant) {
		IBakedModel model = manager.getModel(new ModelResourceLocation(modelName, variant));
		for(EnumFacing facing : facings)
		{
			for(BakedQuad quad : model.getQuads(null, facing, 0))
			{
				builder.addVertexData(quad.getVertexData());				
			}
		}
	}
	
	
}
