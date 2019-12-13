package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class TrafficLightControlBoxGui extends GuiScreen {
	private ResourceLocation background = new ResourceLocation(ModTrafficControl.MODID + ":textures/gui/control_box_gui.png");
	private Modes _currentMode = Modes.ManualNorthSouth;
	
	private GuiCheckBox greenOn;
	private GuiCheckBox yellowOn;
	private GuiCheckBox redOn;
	private GuiCheckBox greenArrowLeftOn;
	private GuiCheckBox yellowArrowLeftOn;
	private GuiCheckBox redArrowLeftOn;
	private GuiCheckBox greenOff;
	private GuiCheckBox yellowOff;
	private GuiCheckBox redOff;
	private GuiCheckBox greenArrowLeftOff;
	private GuiCheckBox yellowArrowLeftOff;
	private GuiCheckBox redArrowLeftOff;
	
	private GuiCheckBox greenOnFlash;
	private GuiCheckBox yellowOnFlash;
	private GuiCheckBox redOnFlash;
	private GuiCheckBox greenArrowLeftOnFlash;
	private GuiCheckBox yellowArrowLeftOnFlash;
	private GuiCheckBox redArrowLeftOnFlash;
	private GuiCheckBox greenOffFlash;
	private GuiCheckBox yellowOffFlash;
	private GuiCheckBox redOffFlash;
	private GuiCheckBox greenArrowLeftOffFlash;
	private GuiCheckBox yellowArrowLeftOffFlash;
	private GuiCheckBox redArrowLeftOffFlash;
	
	private GuiButton manualMode;
	private GuiButton manualModeNorth;
	private GuiButton manualModeSouth;
	
	private TrafficLightControlBoxTileEntity _te;
	public TrafficLightControlBoxGui(TrafficLightControlBoxTileEntity te)
	{
		_te = te;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		
		manualMode = new GuiButton(ELEMENT_IDS.manualMode, horizontalCenter - 55, verticalCenter + 83, 45, 20, "Manual");
		manualModeNorth = new GuiButton(ELEMENT_IDS.manualModeNS, horizontalCenter - 55, verticalCenter + 105, 22, 20, "N/S");
		manualModeSouth = new GuiButton(ELEMENT_IDS.manualModeWE, horizontalCenter - 33, verticalCenter + 105, 22, 20, "W/E");
		
		buttonList.add(manualMode);
		buttonList.add(manualModeNorth);
		buttonList.add(manualModeSouth);
		
		redOn = new GuiCheckBox(ELEMENT_IDS.redOn, horizontalCenter - 27, verticalCenter - 93, "", false);
		redOnFlash = new GuiCheckBox(ELEMENT_IDS.redOnFlash, horizontalCenter - 12, verticalCenter - 93, "", false);
		redOff = new GuiCheckBox(ELEMENT_IDS.redOff, horizontalCenter + 10, verticalCenter - 93, "", false);
		redOffFlash = new GuiCheckBox(ELEMENT_IDS.redOffFlash, horizontalCenter + 25, verticalCenter - 93, "", false);
		yellowOn = new GuiCheckBox(ELEMENT_IDS.yellowOn, horizontalCenter - 27, verticalCenter - 73, "", false);
		yellowOnFlash = new GuiCheckBox(ELEMENT_IDS.yellowOnFlash, horizontalCenter - 12, verticalCenter - 73, "", false);
		yellowOff = new GuiCheckBox(ELEMENT_IDS.yellowOff, horizontalCenter + 10, verticalCenter - 73, "", false);
		yellowOffFlash = new GuiCheckBox(ELEMENT_IDS.yellowOffFlash, horizontalCenter + 25, verticalCenter - 73, "", false);
		greenOn = new GuiCheckBox(ELEMENT_IDS.greenOn, horizontalCenter - 27, verticalCenter - 53, "", false);
		greenOnFlash = new GuiCheckBox(ELEMENT_IDS.greenOnFlash, horizontalCenter - 12, verticalCenter - 53, "", false);
		greenOff = new GuiCheckBox(ELEMENT_IDS.greenOff, horizontalCenter + 10, verticalCenter - 53, "", false);
		greenOffFlash = new GuiCheckBox(ELEMENT_IDS.greenOffFlash, horizontalCenter + 25, verticalCenter - 53, "", false);
		redArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOn, horizontalCenter - 27, verticalCenter - 33, "", false);
		redArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOnFlash, horizontalCenter - 12, verticalCenter - 33, "", false);
		redArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOff, horizontalCenter + 10, verticalCenter - 33, "", false);
		redArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOffFlash, horizontalCenter + 25, verticalCenter - 33, "", false);
		yellowArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOn, horizontalCenter - 27, verticalCenter - 13, "", false);
		yellowArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOnFlash, horizontalCenter - 12, verticalCenter - 13, "", false);
		yellowArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOff, horizontalCenter + 10, verticalCenter - 13, "", false);
		yellowArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOffFlash, horizontalCenter + 25, verticalCenter - 13, "", false);
		greenArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOn, horizontalCenter - 27, verticalCenter + 7, "", false);
		greenArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOnFlash, horizontalCenter - 12, verticalCenter + 7, "", false);
		greenArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOff, horizontalCenter + 10, verticalCenter + 7, "", false);
		greenArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOffFlash, horizontalCenter + 25, verticalCenter + 7, "", false);
		
		buttonList.add(redOn);
		buttonList.add(redOnFlash);
		buttonList.add(redOff);
		buttonList.add(redOffFlash);
		buttonList.add(yellowOn);
		buttonList.add(yellowOnFlash);
		buttonList.add(yellowOff);
		buttonList.add(yellowOffFlash);
		buttonList.add(greenOn);
		buttonList.add(greenOnFlash);
		buttonList.add(greenOff);
		buttonList.add(greenOffFlash);
		buttonList.add(redArrowLeftOn);
		buttonList.add(redArrowLeftOnFlash);
		buttonList.add(redArrowLeftOff);
		buttonList.add(redArrowLeftOffFlash);
		buttonList.add(yellowArrowLeftOn);
		buttonList.add(yellowArrowLeftOnFlash);
		buttonList.add(yellowArrowLeftOff);
		buttonList.add(yellowArrowLeftOffFlash);
		buttonList.add(greenArrowLeftOn);
		buttonList.add(greenArrowLeftOnFlash);
		buttonList.add(greenArrowLeftOff);
		buttonList.add(greenArrowLeftOffFlash);
		
		setManualChecked();
	}
	
	private void setManualChecked()
	{
		greenOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, false, true));
		yellowOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, false, true));
		redOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, false, true));
		greenArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, false, true));
		yellowArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, false, true));
		redArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, false, true));
		greenOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, false, false));
		yellowOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, false, false));
		redOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, false, false));
		greenArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, false, false));
		yellowArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, false, false));
		redArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, false, false));
		
		greenOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, true, true));
		yellowOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, true, true));
		redOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, true, true));
		greenArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, true, true));
		yellowArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, true, true));
		redArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, true, true));
		greenOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, true, false));
		yellowOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, true, false));
		redOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, true, false));
		greenArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false));
		yellowArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false));
		redArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, true, false));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		super.drawScreen(mouseX, mouseY, partialTicks);
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawScaledCustomSizeModalRect(horizontalCenter - 112, verticalCenter - 128, 0, 0, 16, 16, 224, 256, 16, 16);
		
		drawString(fontRenderer, "Bulb", horizontalCenter - 54, verticalCenter - 110, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter - 11, verticalCenter - 110, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter + 26, verticalCenter - 110, 0xFFFFFF);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_on");
		drawTexturedModalRect(horizontalCenter - 30, verticalCenter - 116, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_off");
		drawTexturedModalRect(horizontalCenter + 7, verticalCenter - 116, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/red");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 95, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/yellow_solid");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 75, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/green");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 55, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/red_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 35, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/yellow_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 15, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/green_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 5, sprite, 16, 16);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id)
		{
			case ELEMENT_IDS.manualMode:
			case ELEMENT_IDS.manualModeNS:
				setCurrentMode(Modes.ManualNorthSouth);
				setManualChecked();
				break;
			case ELEMENT_IDS.manualModeWE:
				setCurrentMode(Modes.ManualWestEast);
				setManualChecked();
				break;
			case ELEMENT_IDS.greenOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, false, true);
				break;
			case ELEMENT_IDS.greenOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, true, true);
				break;
			case ELEMENT_IDS.yellowOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, false, true);
				break;
			case ELEMENT_IDS.yellowOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, true, true);
				break;
			case ELEMENT_IDS.redOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, false, true);
				break;
			case ELEMENT_IDS.redOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, true, true);
				break;
			case ELEMENT_IDS.greenArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, false, true);
				break;
			case ELEMENT_IDS.yellowArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, false, true);
				break;
			case ELEMENT_IDS.redArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, false, true);
				break;
			case ELEMENT_IDS.greenArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, true, true);
				break;
			case ELEMENT_IDS.yellowArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, true, true);
				break;
			case ELEMENT_IDS.redArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, true, true);
				break;
			case ELEMENT_IDS.greenOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, false, false);
				break;
			case ELEMENT_IDS.greenOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, true, false);
				break;
			case ELEMENT_IDS.yellowOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, false, false);
				break;
			case ELEMENT_IDS.yellowOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, true, false);
				break;
			case ELEMENT_IDS.redOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, false, false);
				break;
			case ELEMENT_IDS.redOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, true, false);
				break;
			case ELEMENT_IDS.greenArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, false, false);
				break;
			case ELEMENT_IDS.yellowArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, false, false);
				break;
			case ELEMENT_IDS.redArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, false, false);
				break;
			case ELEMENT_IDS.greenArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
				break;
			case ELEMENT_IDS.yellowArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
				break;
			case ELEMENT_IDS.redArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
				break;
		}
	}
	
	@Override
	public void onGuiClosed() {
		_te.performClientToServerSync();
	}
	
	private void handleManualClick(GuiButton button, EnumTrafficLightBulbTypes bulbType, boolean flash, boolean forActive)
	{
		GuiCheckBox box = (GuiCheckBox)button;
		if (_currentMode == Modes.ManualNorthSouth)
		{
			if (forActive)
			{
				_te.addRemoveNorthSouthActive(bulbType, flash, box.isChecked());
			}
			else
			{
				_te.addRemoveNorthSouthInactive(bulbType, flash, box.isChecked());
			}
		}
		else
		{
			if (forActive)
			{
				_te.addRemoveWestEastActive(bulbType, flash, box.isChecked());
			}
			else
			{
				_te.addRemoveWestEastInactive(bulbType, flash, box.isChecked());
			}
		}
	}
	
	private void setCurrentMode(Modes mode)
	{
		_currentMode = mode;
	}
	
	private boolean getChecked(EnumTrafficLightBulbTypes bulbType, boolean flash, boolean forActive)
	{
		if (_currentMode == Modes.ManualNorthSouth)
		{
			return _te.hasSpecificNorthSouthManualOption(bulbType, flash, forActive);
		}
		else
		{
			return _te.hasSpecificWestEastManualOption(bulbType, flash, forActive);
		}
	}
	
	public static class ELEMENT_IDS
	{
		public static final int greenOn = 0;
		public static final int yellowOn = 1;
		public static final int redOn = 2;
		public static final int greenArrowLeftOn = 3;
		public static final int yellowArrowLeftOn = 4;
		public static final int redArrowLeftOn = 5;
		public static final int greenOff = 6;
		public static final int yellowOff = 7;
		public static final int redOff = 8;
		public static final int greenArrowLeftOff = 9;
		public static final int yellowArrowLeftOff = 10;
		public static final int redArrowLeftOff = 11;
		public static final int greenOnFlash = 12;
		public static final int yellowOnFlash = 13;
		public static final int redOnFlash = 14;
		public static final int greenArrowLeftOnFlash = 15;
		public static final int yellowArrowLeftOnFlash = 16;
		public static final int redArrowLeftOnFlash = 17;
		public static final int greenOffFlash = 18;
		public static final int yellowOffFlash = 19;
		public static final int redOffFlash = 20;
		public static final int greenArrowLeftOffFlash = 21;
		public static final int yellowArrowLeftOffFlash = 22;
		public static final int redArrowLeftOffFlash = 23;
		public static final int manualMode = 24;
		public static final int manualModeNS = 25;
		public static final int manualModeWE = 26;
	}

	private enum Modes
	{
		ManualNorthSouth,
		ManualWestEast
	}
}
