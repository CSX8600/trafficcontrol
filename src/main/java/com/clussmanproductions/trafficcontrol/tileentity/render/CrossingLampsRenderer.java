package com.clussmanproductions.trafficcontrol.tileentity.render;

import java.util.ArrayList;

import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.blocks.BlockLampBase;
import com.clussmanproductions.trafficcontrol.blocks.BlockOverheadLamps;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class CrossingLampsRenderer extends TileEntitySpecialRenderer<CrossingLampsTileEntity> {

	@Override
	public void render(CrossingLampsTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);
		
		if (te == null || !(getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockLampBase))
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
		
		// === ROTATE (& fetch values from crossing gate lamps)
		GlStateManager.translate(0.5, 0.5, 0.5);
		boolean isCrossingGateLamps = false;
		boolean north = false;
		boolean west = false;
		boolean south = false;
		boolean east = false;
		boolean down = false;
		
		if (blockState.getBlock() instanceof BlockCrossingGateLamps)
		{
			GlStateManager.rotate(blockState.getValue(BlockCrossingGateLamps.ROTATION) * -22.5F + 180F, 0, 1, 0);
			isCrossingGateLamps = true;
			IExtendedBlockState exState = (IExtendedBlockState)blockState.getBlock().getExtendedState(blockState, te.getWorld(), te.getPos());
			if (exState != null)
			{
				north = exState.getValue(BlockCrossingGateLamps.NORTH);
				west = exState.getValue(BlockCrossingGateLamps.WEST);
				south = exState.getValue(BlockCrossingGateLamps.SOUTH);
				east = exState.getValue(BlockCrossingGateLamps.EAST);
				down = exState.getValue(BlockCrossingGateLamps.DOWN);				
			}
		}
		else
		{
			GlStateManager.rotate((4 - blockState.getValue(BlockOverheadLamps.FACING).getHorizontalIndex()) * 90 + 180, 0, 1, 0);
//			GlStateManager.rotate(180, 0, 1, 0);
		}
		GlStateManager.translate(-0.5, -0.5, -0.5);
		// ===
		
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		
		final String modelPrefix = ((BlockLampBase)blockState.getBlock()).getLampRegistryName();
		if (te.getNeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:" + modelPrefix + "_ne_lamp", "rotation=" + te.getNeBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getNwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:" + modelPrefix + "_nw_lamp", "rotation=" + te.getNwBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getSeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:" + modelPrefix + "_se_lamp", "rotation=" + te.getSeBulbRotation() + ",state=" + te.getState().getName());
		}
		if (te.getSwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:" + modelPrefix + "_sw_lamp", "rotation=" + te.getSwBulbRotation() + ",state=" + te.getState().getName());
		}
		if (isCrossingGateLamps && down)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_pole_ext", "rotation=down");
		}
		
		tess.draw();
		GlStateManager.popMatrix();
		
		if (isCrossingGateLamps)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			builder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
			if (north)
			{
				addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_pole_ext", "rotation=north");
			}
			if (west)
			{
				addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_pole_ext", "rotation=west");
			}
			if (south)
			{
				addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_pole_ext", "rotation=south");
			}
			if (east)
			{
				addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_pole_ext", "rotation=east");
			}
			tess.draw();
			GlStateManager.popMatrix();
		}
		
		
		// === POST RENDER
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
