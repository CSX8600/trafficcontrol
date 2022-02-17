package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.blocks.BlockType3Barrier;
import com.clussmanproductions.trafficcontrol.blocks.BlockType3BarrierBase;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity.SignType;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RendererType3Barrier extends TileEntitySpecialRenderer<Type3BarrierTileEntity> {
	
	@Override
	public void render(Type3BarrierTileEntity te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		IBlockState currentState = getWorld().getBlockState(te.getPos()).getActualState(getWorld(), te.getPos());
		if (!(currentState.getBlock() instanceof BlockType3BarrierBase))
		{
			return;
		}
		if (te.getRenderSign() && currentState.getValue(BlockType3BarrierBase.ISFURTHESTLEFT))
		{
			renderAllSign(te, x, y, z);
		}
		
		

		// Render secondary sign
		if (te.getRenderThisSign())
		{
			renderSecondarySign(te, x, y, z);
		}
	}

	private void renderAllSign(Type3BarrierTileEntity te, double x, double y, double z)
	{
		BlockPos farthestLeft = te.getPos().toImmutable();
		BlockPos farthestRight = te.getPos().toImmutable();
		
		IBlockState workingBlockState = getWorld().getBlockState(te.getPos()).getActualState(getWorld(), te.getPos());
		EnumFacing facing = workingBlockState.getValue(BlockType3BarrierBase.FACING);
		
		while(workingBlockState.getBlock() instanceof BlockType3BarrierBase && !workingBlockState.getValue(BlockType3BarrierBase.ISFURTHESTRIGHT))
		{
			farthestRight = farthestRight.offset(facing.rotateY());
			workingBlockState = getWorld().getBlockState(farthestRight).getActualState(getWorld(), farthestRight);
		}
		
		float renderX = 0;
		float renderZ = 0;
		
		switch(facing)
		{
			case NORTH:
			case SOUTH:
				renderX = Math.abs(farthestRight.getX() - farthestLeft.getX());
				renderZ = 1.126F;
				break;
			case WEST:
			case EAST:
				renderX = Math.abs(farthestRight.getZ() - farthestLeft.getZ());
				renderZ = 1.126F;
				break;
		}		
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate((4 - (facing.getHorizontalIndex() + 2)) * 90, 0, 1, 0);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		GlStateManager.translate(renderX / 2, 0.75, renderZ / 2);
		
		bindTexture(new ResourceLocation("trafficcontrol", "textures/blocks/road_closed_sign.png"));
		
		Tessellator tess = Tessellator.getInstance();
		
		// Render sign front
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		float textureBottomY = 0.25F;
		float heightFactor = 0.25F;
		if (te.getSignType() == SignType.LaneClosed)
		{
			textureBottomY = 0.5F;
		}
		else if (te.getSignType() == SignType.RoadClosedThruTraffic)
		{
			textureBottomY = 1F;
			heightFactor = 0.5F;
		}
		
		builder.pos(1, 0, 0).tex(0.5, textureBottomY).endVertex();
		builder.pos(1, 0.6875, 0).tex(0.5, textureBottomY - heightFactor).endVertex();
		builder.pos(0, 0.6875, 0).tex(0, textureBottomY - heightFactor).endVertex();
		builder.pos(0, 0, 0).tex(0, textureBottomY).endVertex();
		
		tess.draw();
		
		// Render sign back
		bindTexture(new ResourceLocation("trafficcontrol", "textures/blocks/generic.png"));
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(1, 0, 0).tex(1, 0).endVertex();
		builder.pos(0, 0.0, 0).tex(1, 0.6875).endVertex();
		builder.pos(0, 0.6875, 0).tex(0, 0.6875).endVertex();
		builder.pos(1, 0.6875, 0).tex(0, 0).endVertex();
		
		tess.draw();
		
		GlStateManager.popMatrix();
	}

	private void renderSecondarySign(Type3BarrierTileEntity te, double x, double y, double z)
	{
		EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockType3Barrier.FACING);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.rotate((4 - (facing.getHorizontalIndex() + 2)) * 90, 0, 1, 0);
		GlStateManager.translate(-0.375, 0, 0.063);
		
		String typeName = SignTileEntity.getSignTypeName(te.getThisSignType());
		String backName = SignTileEntity.getBackSignName(te.getThisSignType(), te.getThisSignVariant()) + ".png";
		String frontName = typeName + te.getThisSignVariant() + ".png";
		
		bindTexture(new ResourceLocation("trafficcontrol", "textures/blocks/sign/" + typeName + "/" + frontName));
		
		// Draw front
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(0.75, 0, 0).tex(1, 1).endVertex();
		builder.pos(0.75, 0.75, 0).tex(1, 0).endVertex();
		builder.pos(0, 0.75, 0).tex(0, 0).endVertex();
		builder.pos(0, 0, 0).tex(0, 1).endVertex();
		
		tess.draw();
		
		// Draw back		
		bindTexture(new ResourceLocation("trafficcontrol", "textures/blocks/sign/" + typeName + "/" + backName));
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		builder.pos(0.75, 0, 0).tex(1, 1).endVertex();
		builder.pos(0, 0, 0).tex(1, 0).endVertex();
		builder.pos(0, 0.75, 0).tex(0, 0).endVertex();
		builder.pos(0.75, 0.75, 0).tex(0, 1).endVertex();
		
		tess.draw();
		
		GlStateManager.popMatrix();
	}
}
