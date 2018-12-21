package com.clussmanproductions.roadstuffreborn.gui;

import java.io.IOException;

import com.clussmanproductions.roadstuffreborn.ModRoadStuffReborn;
import com.clussmanproductions.roadstuffreborn.network.PacketHandler;
import com.clussmanproductions.roadstuffreborn.network.PacketUpdateSign;
import com.clussmanproductions.roadstuffreborn.tileentity.SignTileEntity;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class SignGui extends GuiScreen {
	private SignTileEntity te;
	private BlockPos pos;
	GuiButton cmdPrevType;
	GuiButton cmdNextType;
	GuiButton cmdPrevVariant;
	GuiButton cmdNextVariant;
	GuiButton cmdSkipPrevVariant;
	GuiButton cmdSkipNextVariant;
	private final ImmutableMap<Integer, Integer> U_POSITIONS_BY_TYPE =ImmutableMap.<Integer, Integer>builder()
			.put(0, 96)
			.put(1, 160)
			.put(2, 224)
			.put(3, 192)
			.put(4, 64)
			.put(5, 128)
			.build();
	private ResourceLocation signConfigResourceLocation = new ResourceLocation(ModRoadStuffReborn.MODID, "textures/gui/signconfig.png");
	
	public SignGui(SignTileEntity te, BlockPos pos)
	{
		this.te = te;
		this.pos = pos;
	}
	
	@Override
	public void initGui() {
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		cmdPrevType = new GuiButton(0, 80, 20, 20, 20, "<");
		cmdNextType = new GuiButton(1, width - 80, 20, 20, 20,">");
		cmdPrevVariant = new GuiButton(2, 80, verticalCenter, 20, 20,"<");
		cmdNextVariant = new GuiButton(3, width - 80, verticalCenter, 20, 20,">");
		cmdSkipPrevVariant = new GuiButton(4, 55, verticalCenter, 20, 20, "<<");
		cmdSkipNextVariant = new GuiButton(5, width - 55, verticalCenter, 20, 20, ">>");
		
		buttonList.addAll(ImmutableSet.of(cmdPrevType, cmdNextType, cmdPrevVariant, cmdNextVariant, cmdSkipPrevVariant, cmdSkipNextVariant));
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		drawDefaultBackground();
		
		drawCenteredString(fontRenderer, "Type: " + te.getVariant(), horizontalCenter, verticalCenter - 84, 16777215);
		
		mc.getTextureManager().bindTexture(signConfigResourceLocation);
		drawModalRectWithCustomSizedTexture(horizontalCenter - 16, 20, U_POSITIONS_BY_TYPE.get(te.getType()), 0, 32, 32, 256, 256);
		
		mc.getTextureManager().bindTexture(te.getTexture());
		drawModalRectWithCustomSizedTexture(horizontalCenter - 64, verticalCenter - 64, 0, 0, 128, 128, 128, 128);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id)
		{
			case 0:
				te.prevType();
				break;
			case 1:
				te.nextType();
				break;
			case 2:
				te.prevVariant();
				break;
			case 3:
				te.nextVariant();
				break;
			case 4:
				for(int i = 0; i < 5; i++)
				{
					te.prevVariant();
				}
				break;
			case 5:
				for(int i = 0; i < 5; i++)
				{
					te.nextVariant();
				}
				break;
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		PacketUpdateSign updateSign = new PacketUpdateSign();
		updateSign.x = pos.getX();
		updateSign.y = pos.getY();
		updateSign.z = pos.getZ();
		updateSign.type = te.getType();
		updateSign.variant = te.getVariant();
		
		PacketHandler.INSTANCE.sendToServer(updateSign);
	}
}
