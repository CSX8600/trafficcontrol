package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity;
import com.clussmanproductions.trafficcontrol.tileentity.Type3BarrierTileEntity.SignType;
import com.clussmanproductions.trafficcontrol.util.Tuple;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiType3Barrier extends GuiScreen {
	private Type3BarrierTileEntity tileEntity;
	private GuiCheckBox renderSign;
	private GuiButton prevSignType;
	private GuiButton nextSignType;
	private GuiCheckBox renderThisSign;
	private GuiButton selectThisSign;
	private GuiImageList imageList;
	private GuiTextField imageListFilter;
	private GuiButtonExt close;
	
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
		
		selectThisSign = new GuiButtonExt(CMPT_IDs.SELECT_THIS_SIGN, (width / 2) + 4 /* margin from center */ + 32 /* sign width */ + 4 /* margin from sign */, (height / 2) + 6, 75, 20, "Select Sign");
		selectThisSign.visible = tileEntity.getRenderThisSign();
		
		imageList = new GuiImageList(((width / 2) + 4) - 100, (height / 2) - 100, 200, 200, (sign) -> 
		{
			tileEntity.setThisSignType(sign.getType());
			tileEntity.setThisSignVariant(sign.getVariant());
			imageList.setVisible(false);
		});
		imageList.setVisible(false);
		
		imageListFilter = new GuiTextField(0, fontRenderer, ((width / 2) + 4) - 100, (height / 2) + 100 + 4, 200, 20);
		
		buttonList.add(renderSign);
		buttonList.add(prevSignType);
		buttonList.add(nextSignType);
		buttonList.add(renderThisSign);
		buttonList.add(selectThisSign);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		if (tileEntity.getRenderSign())
		{
			renderSign((width / 2) + 4 /* margin from center */ + 4 /* margin from prev */ + prevSignType.width, (height / 2) - 32 - 16 - (prevSignType.height / 2));
		}
		
		if (tileEntity.getRenderThisSign())
		{
			renderThisSignVariant((width / 2) + 4 /* margin from center */, height / 2);
		}
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		imageList.draw(mouseX, mouseY, fontRenderer, text -> x -> y -> drawHoveringText(text, x, y));
		
		if (imageList.isVisible())
		{
			GlStateManager.color(1, 1, 1);
			imageListFilter.drawTextBox();
		}
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
		
		builder.pos(left + 64, top, 0).tex(0.5, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top, 0).tex(0, signTextureBottomY - heightFactor).endVertex();
		builder.pos(left, top + 32, 0).tex(0, signTextureBottomY).endVertex();
		builder.pos(left + 64, top + 32, 0).tex(0.5, signTextureBottomY).endVertex();
		
		tess.draw();
		GlStateManager.popMatrix();
	}
	
	private void renderThisSignVariant(int left, int top)
	{
		ResourceLocation signTexture = SignTileEntity.SIGNS_BY_TYPE_VARIANT.get(new Tuple<Integer, Integer>(tileEntity.getThisSignType(), tileEntity.getThisSignVariant())).getImageResourceLocation();
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(signTexture);
		
		GlStateManager.pushMatrix();
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		
		builder.pos(left + 32, top, 0).tex(1, 0).endVertex();
		builder.pos(left, top, 0).tex(0, 0).endVertex();
		builder.pos(left, top + 32, 0).tex(0, 1).endVertex();
		builder.pos(left + 32, top + 32, 0).tex(1, 1).endVertex();
		
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
				selectThisSign.visible = thisSignChecked;
				break;
			case CMPT_IDs.SELECT_THIS_SIGN:
				imageList.setVisible(true);
				imageListFilter.setVisible(true);
				break;
		}
		super.actionPerformed(button);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (imageList.isVisible())
		{
			imageList.onMouseClick(mouseX, mouseY);
			imageListFilter.mouseClicked(mouseX, mouseY, mouseButton);
		}
		else
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		imageList.onMouseRelease();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode != Keyboard.KEY_ESCAPE || !imageList.isVisible())
		{
			super.keyTyped(typedChar, keyCode);
		}
		
		if (imageList.isVisible())
		{
			imageListFilter.textboxKeyTyped(typedChar, keyCode);
			imageList.filter(imageListFilter.getText());
			
			if (keyCode == Keyboard.KEY_ESCAPE)
			{
				imageList.setVisible(false);
				imageList.filter(null);
				imageListFilter.setVisible(false);
			}
		}
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		if (imageList.isVisible())
		{
			int scroll = Integer.signum(Mouse.getEventDWheel());
			imageList.scroll(scroll);
		}
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
		public static final int SELECT_THIS_SIGN = 5;
	}
}
