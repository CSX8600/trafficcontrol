package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import com.clussmanproductions.trafficcontrol.blocks.BlockCrossingGateLamps;
import com.clussmanproductions.trafficcontrol.tileentity.CrossingLampsPoleBasedTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class CrossingGateLampsGui extends GuiScreen {

	private final ArrayList<EnumFacing> facings;
	private final int rotation;
	private final CrossingLampsPoleBasedTileEntity te;
	public CrossingGateLampsGui(CrossingLampsPoleBasedTileEntity te)
	{
		this.te = te;
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		rotation = state.getValue(BlockCrossingGateLamps.ROTATION);
		facings = new ArrayList<>();
		facings.add(null);
		for(EnumFacing facing : EnumFacing.values())
		{
			facings.add(facing);
		}
	}
	
	private int nwX;
	private int nwY;
	private int neX;
	private int neY;
	private int seX;
	private int seY;
	private int swX;
	private int swY;
	
	private static final String SHOWN = "Bulb attached"; 
	
	@Override
	public void initGui() {
		super.initGui();
		
		nwX = -(int)(Math.cos(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 45)) * 110) + (width / 2);
		nwY = -(int)(Math.sin(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 45)) * 110) + (height / 2);
		
		neX = -(int)(Math.cos(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 135)) * 110) + (width / 2);
		neY = -(int)(Math.sin(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 135)) * 110) + (height / 2);
		
		seX = -(int)(Math.cos(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 225)) * 110) + (width / 2);
		seY = -(int)(Math.sin(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 225)) * 110) + (height / 2);
		
		swX = -(int)(Math.cos(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 315)) * 110) + (width / 2);
		swY = -(int)(Math.sin(Math.toRadians((360 - (mc.player.rotationYawHead % 360 + rotation * -22.5F)) + 315)) * 110) + (height / 2);
		
		final int shownWidth = fontRenderer.getStringWidth(SHOWN);
		
		for(int i = 0; i < 40; i += 10)
		{
			final int thisX = i == 0 ? nwX :
								i == 10 ? neX :
								i == 20 ? swX : seX;
			final int thisY = i == 0 ? nwY :
								i == 10 ? neY :
								i == 20 ? swY : seY;
			final boolean isRightAligned = thisX < (width / 2);
			final boolean isTopAligned = thisY > (height / 2);
			final int currentAngle = i == 0 ? te.getNwBulbRotation() :
										i == 10 ? te.getNeBulbRotation() :
										i == 20 ? te.getSwBulbRotation() : te.getSeBulbRotation();
			
			GuiCheckBox box = new GuiCheckBox(i, isRightAligned ? thisX - 11 - shownWidth : thisX, isTopAligned ? thisY : thisY - 11, SHOWN, currentAngle >= 0);
			buttonList.add(box);
			GuiButtonExt decrease = new GuiButtonExt(i + 1, isRightAligned ? thisX - 80 : thisX, isTopAligned ? thisY + 14 : thisY - 34, 20, 20, "<");
			GuiButtonExt increase = new GuiButtonExt(i + 2, isRightAligned ? thisX - 20 : thisX + 60, isTopAligned ? thisY + 15 : thisY - 34, 20, 20, ">");
			buttonList.add(increase);
			buttonList.add(decrease);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		fontRenderer.drawStringWithShadow(TextFormatting.BOLD + "Crossing Lamp Configuration", (width / 2) - fontRenderer.getStringWidth(TextFormatting.BOLD + "Crossing Lamp Configuration") / 2, 30, 0xFFFFFF);
		
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)(width / 2) - 64, (float)(height / 2) - 64, 100.0F + this.zLevel);
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.scale(128.0F, 128.0F, 128.0F);
        GlStateManager.rotate(90, 1, 0, 0);
        
        // Rotate to player
        GlStateManager.translate(0.5, 0.5, 0.5);
//        GlStateManager.rotate(mc.player.rotationPitch, 1, 0, 0);
        GlStateManager.rotate(mc.player.rotationYawHead % 360 + (rotation * -22.5F), 0, 1, 0);
        GlStateManager.translate(-0.5, -0.5, -0.5);
        
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBuffer();
		builder.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
		
		ModelManager manager = mc.getBlockRendererDispatcher().getBlockModelShapes().getModelManager();
		addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps", "rotation=0,state=off");
		if (te.getNwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_nw_lamp", "rotation=" + te.getNwBulbRotation() + ",state=off");
		}

		if (te.getNeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_ne_lamp", "rotation=" + te.getNeBulbRotation() + ",state=off");
		}

		if (te.getSwBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_sw_lamp", "rotation=" + te.getSwBulbRotation() + ",state=off");
		}

		if (te.getSeBulbRotation() >= 0)
		{
			addModelToBuffer(facings, manager, builder, "trafficcontrol:crossing_gate_lamps_se_lamp", "rotation=" + te.getSeBulbRotation() + ",state=off");
		}
		
		tess.draw();		
		
		GlStateManager.popMatrix();
		
		GlStateManager.disableDepth();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		for(int i = 0; i < 40; i += 10)
		{
			final int thisX = i == 0 ? nwX :
								i == 10 ? neX :
								i == 20 ? swX : seX;
			final int thisY = i == 0 ? nwY :
								i == 10 ? neY :
								i == 20 ? swY : seY;
			final boolean isRightAligned = thisX < (width / 2);
			final boolean isTopAligned = thisY > (height / 2);
			final int rotationConsidered = i == 0 ? te.getNwBulbRotation() :
											i == 10 ? te.getNeBulbRotation() :
											i == 20 ? te.getSwBulbRotation() : te.getSeBulbRotation();
			if (rotationConsidered == -1)
			{
				continue;
			}
			
			String displayValue = Integer.toString(360 - (int)(rotationConsidered * 22.5));
			if (displayValue.equalsIgnoreCase("360"))
			{
				displayValue = "0";
			}
			displayValue += "°";
			
			final int displayValueWidth = fontRenderer.getStringWidth(displayValue);
			fontRenderer.drawString(displayValue, isRightAligned ? thisX - 40 - (displayValueWidth / 2) : thisX + 40 - (displayValueWidth / 2), isTopAligned ? thisY + 24 - (fontRenderer.FONT_HEIGHT / 2) : thisY - 24 - (fontRenderer.FONT_HEIGHT / 2), 0xFFFFFF);
		}
		
		GlStateManager.enableDepth();
		
//		GlStateManager.pushMatrix();
//		GlStateManager.translate(width / 2, height / 2, 0);
//		GlStateManager.rotate(-mc.player.rotationYawHead, 0, 0, 1);
//		GlStateManager.translate(-(width / 2), -(height / 2), 0);
//		fontRenderer.drawString("test", width / 2 - 64, height / 2 - 64, 0xFFFFFF);
//		GlStateManager.popMatrix();
		
//		fontRenderer.drawString("Test NW", nwX, nwY, 0xFFFFFF);
//		fontRenderer.drawString("Test NE", neX, neY, 0xFFFFFF);
//		fontRenderer.drawString("Test SE", seX, seY, 0xFFFFFF);
//		fontRenderer.drawString("Test SW", swX, swY, 0xFFFFFF);
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
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		
		int i = button.id / 10;
		int buttonType = button.id % 10;
		
		Supplier<Integer> getMethod = null;
		Consumer<Integer> setMethod = null;
		switch(i)
		{
			case 0:
				getMethod = te::getNwBulbRotation;
				setMethod = te::setNwBulbRotation;
				break;
			case 1:
				getMethod = te::getNeBulbRotation;
				setMethod = te::setNeBulbRotation;
				break;
			case 2:
				getMethod = te::getSwBulbRotation;
				setMethod = te::setSwBulbRotation;
				break;
			case 3:
				getMethod = te::getSeBulbRotation;
				setMethod = te::setSeBulbRotation;
				break;
		}
		
		if (setMethod == null)
		{
			return;
		}
		
		switch(buttonType)
		{
			case 0: //checkbox
				setMethod.accept(((GuiCheckBox)button).isChecked() ? 0 : -1);
				break;
			case 1: // decrease
				int currentI = getMethod.get();
				if (currentI >= 15)
				{
					currentI = -1;
				}
				currentI++;
				setMethod.accept(currentI);
				break;
			case 2: // increase
				int currentD = getMethod.get();
				if (currentD <= 0)
				{
					currentD = 16;
				}
				currentD--;
				setMethod.accept(currentD);
				break;
		}
		
		te.syncClientToServer();
	}

	
}
