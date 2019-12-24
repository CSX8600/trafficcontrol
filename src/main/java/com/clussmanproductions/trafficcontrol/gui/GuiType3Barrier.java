package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity.SignType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiType3Barrier extends GuiScreen {
	private Type3BarrierTileEntity tileEntity;
	private GuiCheckBox renderSign;
	private GuiButton prevSignType;
	private GuiButton nextSignType;
	
	public GuiType3Barrier(Type3BarrierTileEntity barrierTE)
	{
		tileEntity = barrierTE;
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		renderSign = new GuiCheckBox(CMPT_IDs.RENDER_SIGN, 0, 0, "Has Sign", tileEntity.getRenderSign());
		renderSign.setIsChecked(tileEntity.getRenderSign());
		renderSign.x = (width / 2) - (renderSign.width / 2);
		renderSign.y = (height / 2) + 16 /* height of sign */ + 8;
		
		prevSignType = new GuiButton(CMPT_IDs.PREV_SIGN_TYPE, 0, 0, "<");
		prevSignType.x = (width / 2) - 32 /* width of sign */ - 20 /* width of button */ - 4;
		prevSignType.y = (height / 2) - 16 /* height of sign */ + 6;
		prevSignType.width = 20;
		prevSignType.visible = tileEntity.getRenderSign();
		
		nextSignType = new GuiButton(CMPT_IDs.NEXT_SIGN_TYPE, 0, 0, ">");
		nextSignType.x = (width / 2) + 32 /* width of sign */ + 4;
		nextSignType.y = (height / 2) - 16 /* height of sign */ + 6;
		nextSignType.width = 20;
		nextSignType.visible = tileEntity.getRenderSign();
		
		buttonList.add(renderSign);
		buttonList.add(prevSignType);
		buttonList.add(nextSignType);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (tileEntity.getRenderSign())
		{
			renderSign((width / 2) - 32, (height / 2) - 16);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void renderSign(int left, int top)
	{
		ResourceLocation signRL = new ResourceLocation("trafficcontrol:textures/blocks/road_closed_sign.png");
		float signTextureBottomY = 0.5F;
		if (tileEntity.getSignType() == SignType.LaneClosed)
		{
			signTextureBottomY = 1F;
		}
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signRL);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 64, top, 1).tex(1, signTextureBottomY - 0.5).endVertex();
		builder.pos(left, top, 1).tex(0, signTextureBottomY - 0.5).endVertex();
		builder.pos(left, top + 32, 1).tex(0, signTextureBottomY).endVertex();
		builder.pos(left + 64, top + 32, 1).tex(1, signTextureBottomY).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id)
		{
			case CMPT_IDs.RENDER_SIGN:
				boolean checked = ((GuiCheckBox)button).isChecked();
				tileEntity.setRenderSign(checked);
				prevSignType.visible = checked;
				nextSignType.visible = checked;
				break;
			case CMPT_IDs.NEXT_SIGN_TYPE:
				tileEntity.nextSignType();
				break;
			case CMPT_IDs.PREV_SIGN_TYPE:
				tileEntity.prevSignType();
				break;
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void onGuiClosed() {
		tileEntity.syncConnectedBarriers(true);
		super.onGuiClosed();
	}
	
	public static class CMPT_IDs
	{
		public static final int RENDER_SIGN = 1;
		public static final int PREV_SIGN_TYPE = 2;
		public static final int NEXT_SIGN_TYPE = 3;
	}
}
