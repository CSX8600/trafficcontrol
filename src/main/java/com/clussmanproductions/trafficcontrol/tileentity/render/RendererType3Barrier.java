package com.clussmanproductions.trafficcontrol.tileentity.render;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.blocks.BlockType3BarrierBase;
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
		if (!te.getRenderSign() || !currentState.getValue(BlockType3BarrierBase.ISFURTHESTLEFT))
		{
			return;
		}
		
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
		
		float textureBottomY = 0.5F;
		if (te.getSignType() == SignType.LaneClosed)
		{
			textureBottomY = 1;
		}
		
		builder.pos(1, 0, 0).tex(1, textureBottomY).endVertex();
		builder.pos(1, 0.6875, 0).tex(1, textureBottomY - 0.5).endVertex();
		builder.pos(0, 0.6875, 0).tex(0, textureBottomY - 0.5).endVertex();
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
}
