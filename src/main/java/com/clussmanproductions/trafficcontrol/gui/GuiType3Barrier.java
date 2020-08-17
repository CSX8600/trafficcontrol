package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
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
	private GuiCheckBox renderThisSign;
	private GuiButton prevThisSignType;
	private GuiButton nextThisSignType;
	private GuiButton prevThisSignVariant;
	private GuiButton nextThisSignVariant;
	
	public GuiType3Barrier(Type3BarrierTileEntity barrierTE)
	{
		tileEntity = barrierTE;
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
		renderSign = new GuiCheckBox(CMPT_IDs.RENDER_SIGN, 0, 0, "Sign across barriers", tileEntity.getRenderSign());
		renderSign.setIsChecked(tileEntity.getRenderSign());
		renderSign.x = (width / 2) - renderSign.width - 4;
		renderSign.y = (height / 2) - 16 /* height of sign */ - 32;
		
		prevSignType = new GuiButton(CMPT_IDs.PREV_SIGN_TYPE, 0, 0, "<");
		prevSignType.width = 20;
		prevSignType.x = (width / 2) + 4;
		prevSignType.y = (height / 2) - 16 /* 1/2 height of sign */ - 32 - ((prevSignType.height - renderSign.height) / 2);
		prevSignType.visible = tileEntity.getRenderSign();
		
		nextSignType = new GuiButton(CMPT_IDs.NEXT_SIGN_TYPE, 0, 0, ">");
		nextSignType.width = 20;
		nextSignType.x = (width / 2) + 64 /* width of sign */ + prevSignType.width + 4 /* margin from center */ + 4 /* sign margin from prev */
				+ 4 /* margin from sign */;
		nextSignType.y = (height / 2) - 16 /* 1/2 height of sign */ - 32 - ((nextSignType.height - renderSign.height) / 2);
		nextSignType.visible = tileEntity.getRenderSign();
		
		renderThisSign = new GuiCheckBox(CMPT_IDs.RENDER_THIS_SIGN, 0, 0, "Sign on this barrier", tileEntity.getRenderThisSign());
		renderThisSign.x = (width / 2) - renderSign.width - 4;
		renderThisSign.y = (height / 2) + 8 /* 1/2 height of sign */;
		
		prevThisSignType = new GuiButton(CMPT_IDs.PREV_THIS_SIGN_TYPE, 0, 0, "<");
		prevThisSignType.width = 20;
		prevThisSignType.x = (width / 2) + 4;
		prevThisSignType.y = (height / 2) + 8 /* 1/2 height of sign */ - ((prevThisSignType.height - renderThisSign.height)/2);
		prevThisSignType.visible = tileEntity.getRenderThisSign();
		
		nextThisSignType = new GuiButton(CMPT_IDs.NEXT_THIS_SIGN_TYPE, 0, 0, ">");
		nextThisSignType.width = 20;
		nextThisSignType.x = (width / 2) + 4 /* Margin from center */ + prevThisSignType.width + 4 /* Sign margin from prev */ 
				+ 32 /* Sign width */ + 4 /* Margin from sign */;
		nextThisSignType.y = (height / 2) + 8 /* 1/2 height of sign */ - ((nextThisSignType.height - renderThisSign.height)/2);
		nextThisSignType.visible = tileEntity.getRenderThisSign();
		
		prevThisSignVariant = new GuiButton(CMPT_IDs.PREV_THIS_SIGN_VARIANT, 0, 0, "<");
		prevThisSignVariant.width = 20;
		prevThisSignVariant.x = (width / 2) + 4;
		prevThisSignVariant.y = (height / 2) + 8 /* 1/2 height of sign */ + 32 /* height of type sign */ + 4 /* margin from type sign */;
		prevThisSignVariant.visible = tileEntity.getRenderThisSign();
		
		nextThisSignVariant = new GuiButton(CMPT_IDs.NEXT_THIS_SIGN_VARIANT, 0, 0, ">");
		nextThisSignVariant.width = 20;
		nextThisSignVariant.x = (width / 2) + 4 /* Margin from center */ + prevThisSignVariant.width + 4 /* Sign margin from prev */ 
				+ 32 /* Sign width */ + 4 /* Margin from sign */;
		nextThisSignVariant.y = (height / 2) + 8 /* 1/2 height of sign */ + 32 /* height of type sign */ + 4 /* margin from type sign */;
		nextThisSignVariant.visible = tileEntity.getRenderThisSign();
		
		buttonList.add(renderSign);
		buttonList.add(prevSignType);
		buttonList.add(nextSignType);
		buttonList.add(renderThisSign);
		buttonList.add(prevThisSignType);
		buttonList.add(nextThisSignType);
		buttonList.add(prevThisSignVariant);
		buttonList.add(nextThisSignVariant);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (tileEntity.getRenderSign())
		{
			renderSign((width / 2) + 4 /* margin from center */ + 4 /* margin from prev */ + prevSignType.width, (height / 2) - 32 - 16 - (prevSignType.height / 2));
		}
		
		if (tileEntity.getRenderThisSign())
		{
			renderThisSignType((width / 2) + 4 /* margin from center */ + prevThisSignType.width + 4 /* margin from prevThisSignType */,
					(height / 2));
			renderThisSignVariant((width / 2) + 4 /* margin from center */ + prevThisSignVariant.width + 4 /* margin from prevThisSignType */,
					(height / 2) + 32 + 4);
		}
		
		drawDefaultBackground();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	private void renderSign(int left, int top)
	{
		ResourceLocation signRL = new ResourceLocation("trafficcontrol:textures/blocks/road_closed_sign.png");
		float signTextureBottomY = 0.25F;
		float heightFactor = 0.25F;
		if (tileEntity.getSignType() == SignType.LaneClosed)
		{
			signTextureBottomY = 0.5F;
		}
		else if (tileEntity.getSignType() == SignType.RoadClosedThruTraffic)
		{
			signTextureBottomY = 1F;
			heightFactor = 0.5F;
		}
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signRL);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 64, top, 1).tex(0.5, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top, 1).tex(0, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top + 32, 1).tex(0, signTextureBottomY).endVertex();
		builder.pos(left + 64, top + 32, 1).tex(0.5, signTextureBottomY).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
	
	private void renderThisSignType(int left, int top)
	{
		ResourceLocation typeLocation = new ResourceLocation("trafficcontrol:textures/gui/signconfig.png");
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(typeLocation);
		
		float xFactor = 0;
		switch(tileEntity.getThisSignType())
		{
			case 0:
				xFactor = 1;
				break;
			case 1:
				xFactor = 3;
				break;
			case 2:
				xFactor = 5;
				break;
			case 3:
				xFactor = 4;
				break;
			case 4:
				xFactor = 0;
				break;
			case 5:
				xFactor = 2;
				break;
		}
		
		float x = 0.25F + (0.125F * xFactor);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 32, top, 1).tex(x + 0.125, 0).endVertex();
		builder.pos(left, top, 1).tex(x, 0).endVertex();
		builder.pos(left, top + 32, 1).tex(x, 0.125).endVertex();
		builder.pos(left + 32, top + 32, 1).tex(x + 0.125, 0.125).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
	
	private void renderThisSignVariant(int left, int top)
	{
		String signTypeName = SignTileEntity.getSignTypeName(tileEntity.getThisSignType());
		String signName = signTypeName + tileEntity.getThisSignVariant();
		
		ResourceLocation signTexture = new ResourceLocation("trafficcontrol:textures/blocks/sign/" + signTypeName + "/" + signName + ".png");
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signTexture);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 32, top, 1).tex(1, 0).endVertex();
		builder.pos(left, top, 1).tex(0, 0).endVertex();
		builder.pos(left, top + 32, 1).tex(0, 1).endVertex();
		builder.pos(left + 32, top + 32, 1).tex(1, 1).endVertex();
		
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
			case CMPT_IDs.RENDER_THIS_SIGN:
				boolean thisSignChecked = ((GuiCheckBox)button).isChecked();
				tileEntity.setRenderThisSign(thisSignChecked);
				prevThisSignType.visible = thisSignChecked;
				nextThisSignType.visible = thisSignChecked;
				prevThisSignVariant.visible = thisSignChecked;
				nextThisSignVariant.visible = thisSignChecked;
				break;
			case CMPT_IDs.NEXT_THIS_SIGN_TYPE:
				tileEntity.nextThisSignType();
				break;
			case CMPT_IDs.PREV_THIS_SIGN_TYPE:
				tileEntity.prevThisSignType();
				break;
			case CMPT_IDs.NEXT_THIS_SIGN_VARIANT:
				tileEntity.nextThisSignVariant();
				break;
			case CMPT_IDs.PREV_THIS_SIGN_VARIANT:
				tileEntity.prevThisSignVariant();
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
		public static final int RENDER_THIS_SIGN = 4;
		public static final int NEXT_THIS_SIGN_TYPE = 5;
		public static final int PREV_THIS_SIGN_TYPE = 6;
		public static final int NEXT_THIS_SIGN_VARIANT = 7;
		public static final int PREV_THIS_SIGN_VARIANT = 8;
	}
}
