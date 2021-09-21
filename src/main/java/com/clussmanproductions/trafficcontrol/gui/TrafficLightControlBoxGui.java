package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.TrafficLightControlBoxTileEntity;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
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
	private GuiCheckBox crossOn;
	private GuiCheckBox crossOff;
	private GuiCheckBox dontCrossOn;
	private GuiCheckBox dontCrossOff;
	
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
	private GuiCheckBox crossOnFlash;
	private GuiCheckBox crossOffFlash;
	private GuiCheckBox dontCrossOnFlash;
	private GuiCheckBox dontCrossOffFlash;
	
	private GuiButtonExtSelectable manualModeNorth;
	private GuiButtonExtSelectable manualModeSouth;
	
	private GuiTextField greenMinimum;
	private GuiTextField arrowMinimum;
	private GuiTextField yellowTime;
	private GuiTextField redTime;
	
	private TrafficLightControlBoxTileEntity _te;
	public TrafficLightControlBoxGui(TrafficLightControlBoxTileEntity te)
	{
		_te = te;
		
		_currentMode = _te.getHasSensors() ? Modes.Automatic : Modes.ManualNorthSouth; 
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		manualModeNorth = new GuiButtonExtSelectable(ELEMENT_IDS.manualModeNS, horizontalCenter - 55, verticalCenter + 105, 25, 20, "N/S");
		manualModeSouth = new GuiButtonExtSelectable(ELEMENT_IDS.manualModeWE, horizontalCenter - 30, verticalCenter + 105, 25, 20, "W/E");
		
		manualModeNorth.setIsSelected(true);
		
		buttonList.add(manualModeNorth);
		buttonList.add(manualModeSouth);
		
		redOn = new GuiCheckBox(ELEMENT_IDS.redOn, horizontalCenter - 27, verticalCenter - 83, "", false);
		redOnFlash = new GuiCheckBox(ELEMENT_IDS.redOnFlash, horizontalCenter - 12, verticalCenter - 83, "", false);
		redOff = new GuiCheckBox(ELEMENT_IDS.redOff, horizontalCenter + 10, verticalCenter - 83, "", false);
		redOffFlash = new GuiCheckBox(ELEMENT_IDS.redOffFlash, horizontalCenter + 25, verticalCenter - 83, "", false);
		yellowOn = new GuiCheckBox(ELEMENT_IDS.yellowOn, horizontalCenter - 27, verticalCenter - 63, "", false);
		yellowOnFlash = new GuiCheckBox(ELEMENT_IDS.yellowOnFlash, horizontalCenter - 12, verticalCenter - 63, "", false);
		yellowOff = new GuiCheckBox(ELEMENT_IDS.yellowOff, horizontalCenter + 10, verticalCenter - 63, "", false);
		yellowOffFlash = new GuiCheckBox(ELEMENT_IDS.yellowOffFlash, horizontalCenter + 25, verticalCenter - 63, "", false);
		greenOn = new GuiCheckBox(ELEMENT_IDS.greenOn, horizontalCenter - 27, verticalCenter - 43, "", false);
		greenOnFlash = new GuiCheckBox(ELEMENT_IDS.greenOnFlash, horizontalCenter - 12, verticalCenter - 43, "", false);
		greenOff = new GuiCheckBox(ELEMENT_IDS.greenOff, horizontalCenter + 10, verticalCenter - 43, "", false);
		greenOffFlash = new GuiCheckBox(ELEMENT_IDS.greenOffFlash, horizontalCenter + 25, verticalCenter - 43, "", false);
		redArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOn, horizontalCenter - 27, verticalCenter - 23, "", false);
		redArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOnFlash, horizontalCenter - 12, verticalCenter - 23, "", false);
		redArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOff, horizontalCenter + 10, verticalCenter - 23, "", false);
		redArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.redArrowLeftOffFlash, horizontalCenter + 25, verticalCenter - 23, "", false);
		yellowArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOn, horizontalCenter - 27, verticalCenter - 3, "", false);
		yellowArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOnFlash, horizontalCenter - 12, verticalCenter - 3, "", false);
		yellowArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOff, horizontalCenter + 10, verticalCenter - 3, "", false);
		yellowArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowLeftOffFlash, horizontalCenter + 25, verticalCenter - 3, "", false);
		greenArrowLeftOn = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOn, horizontalCenter - 27, verticalCenter + 17, "", false);
		greenArrowLeftOnFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOnFlash, horizontalCenter - 12, verticalCenter + 17, "", false);
		greenArrowLeftOff = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOff, horizontalCenter + 10, verticalCenter + 17, "", false);
		greenArrowLeftOffFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowLeftOffFlash, horizontalCenter + 25, verticalCenter + 17, "", false);
		crossOn = new GuiCheckBox(ELEMENT_IDS.crossOn, horizontalCenter - 27, verticalCenter + 37, "", false);
		crossOnFlash = new GuiCheckBox(ELEMENT_IDS.crossOnFlash, horizontalCenter - 12, verticalCenter + 37, "", false);
		crossOff = new GuiCheckBox(ELEMENT_IDS.crossOff, horizontalCenter + 10, verticalCenter + 37, "", false);
		crossOffFlash = new GuiCheckBox(ELEMENT_IDS.crossOffFlash, horizontalCenter + 25, verticalCenter + 37, "", false);
		dontCrossOn = new GuiCheckBox(ELEMENT_IDS.dontCrossOn, horizontalCenter - 27, verticalCenter + 57, "", false);
		dontCrossOnFlash = new GuiCheckBox(ELEMENT_IDS.dontCrossOnFlash, horizontalCenter - 12, verticalCenter + 57, "", false);
		dontCrossOff = new GuiCheckBox(ELEMENT_IDS.dontCrossOff, horizontalCenter + 10, verticalCenter + 57, "", false);
		dontCrossOffFlash = new GuiCheckBox(ELEMENT_IDS.dontCrossOffFlash, horizontalCenter + 25, verticalCenter + 57, "", false);
		
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
		buttonList.add(crossOn);
		buttonList.add(crossOnFlash);
		buttonList.add(crossOff);
		buttonList.add(crossOffFlash);
		buttonList.add(dontCrossOn);
		buttonList.add(dontCrossOnFlash);
		buttonList.add(dontCrossOff);
		buttonList.add(dontCrossOffFlash);
		
		setManualChecked();
		
		greenMinimum = new GuiTextField(ELEMENT_IDS.greenMinimum, fontRenderer, horizontalCenter - 54, verticalCenter - 90, 105, 20);
		yellowTime = new GuiTextField(ELEMENT_IDS.yellowTime, fontRenderer, horizontalCenter - 54, verticalCenter - 55, 105, 20);
		redTime = new GuiTextField(ELEMENT_IDS.redTime, fontRenderer, horizontalCenter - 54, verticalCenter - 20, 105, 20);
		arrowMinimum = new GuiTextField(ELEMENT_IDS.arrowMinimum, fontRenderer, horizontalCenter - 54, verticalCenter + 15, 105, 20);
		
		greenMinimum.setText(Double.toString(_te.getAutomator().getGreenMinimum()));
		yellowTime.setText(Double.toString(_te.getAutomator().getYellowTime()));
		redTime.setText(Double.toString(_te.getAutomator().getRedTime()));
		arrowMinimum.setText(Double.toString(_te.getAutomator().getArrowMinimum()));
		
		setButtonVisibilityForMode();
	}
	
	private void setManualChecked()
	{
		greenOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, false, true));
		yellowOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, false, true));
		redOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, false, true));
		greenArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, false, true));
		yellowArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, false, true));
		redArrowLeftOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, false, true));
		crossOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Cross, false, true));
		dontCrossOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.DontCross, false, true));
		greenOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, false, false));
		yellowOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, false, false));
		redOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, false, false));
		greenArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, false, false));
		yellowArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, false, false));
		redArrowLeftOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, false, false));
		crossOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Cross, false, false));
		dontCrossOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.DontCross, false, false));
		
		greenOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, true, true));
		yellowOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, true, true));
		redOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, true, true));
		greenArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, true, true));
		yellowArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, true, true));
		redArrowLeftOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, true, true));
		crossOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Cross, true, true));
		dontCrossOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.DontCross, true, true));
		greenOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Green, true, false));
		yellowOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Yellow, true, false));
		redOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Red, true, false));
		greenArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowLeft, true, false));
		yellowArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowLeft, true, false));
		redArrowLeftOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowLeft, true, false));
		crossOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.Cross, true, false));
		dontCrossOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.DontCross, true, false));
	}
	
	public void setButtonVisibilityForMode()
	{
		boolean manualMode = _currentMode == Modes.ManualNorthSouth || _currentMode == Modes.ManualWestEast;
		
		buttonList
			.stream()
			.filter(b -> b instanceof GuiCheckBox)
			.forEach(b -> b.visible = manualMode);
		
		manualModeNorth.visible = manualMode;
		manualModeSouth.visible = manualMode;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// TODO Auto-generated method stub
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		super.drawScreen(mouseX, mouseY, partialTicks);
		Minecraft.getMinecraft().getTextureManager().bindTexture(background);
		drawScaledCustomSizeModalRect(horizontalCenter - 112, verticalCenter - 128, 0, 0, 16, 16, 224, 256, 16, 16);
		
		if (_currentMode == Modes.ManualNorthSouth || _currentMode == Modes.ManualWestEast)
		{
			drawManualMode(horizontalCenter, verticalCenter);
		}
		
		if (_currentMode == Modes.Automatic)
		{
			drawAutomaticMode(horizontalCenter, verticalCenter);
		}
				
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void drawManualMode(int horizontalCenter, int verticalCenter)
	{
		drawString(fontRenderer, "Manual Mode", horizontalCenter - 54, verticalCenter - 110, 0xFFFF00);
		
		drawString(fontRenderer, "Bulb", horizontalCenter - 54, verticalCenter - 100, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter - 11, verticalCenter - 100, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter + 26, verticalCenter - 100, 0xFFFFFF);
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_on");
		drawTexturedModalRect(horizontalCenter - 30, verticalCenter - 106, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_off");
		drawTexturedModalRect(horizontalCenter + 7, verticalCenter - 106, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/red");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 85, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/yellow_solid");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 65, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/green");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 45, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/red_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 25, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/yellow_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 5, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/green_arrow_left");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 15, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/cross");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 35, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/dontcross");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 55, sprite, 16, 16);
	}

	private void drawAutomaticMode(int horizontalCenter, int verticalCenter)
	{
		int leftMargin = horizontalCenter - 54;
		drawString(fontRenderer, "Automatic Mode", leftMargin, verticalCenter - 110, 0xFFFF00);
		drawString(fontRenderer, "Green Minimum", leftMargin, verticalCenter - 100, 0xFFFFFF);
		greenMinimum.drawTextBox();
		drawString(fontRenderer, "Yellow Time", leftMargin, verticalCenter - 65, 0xFFFFFF);
		yellowTime.drawTextBox();
		drawString(fontRenderer, "Red Time", leftMargin, verticalCenter - 30, 0xFFFFFF);
		redTime.drawTextBox();
		drawString(fontRenderer, "Arrow Minimum", leftMargin, verticalCenter + 5, 0xFFFFFF);
		arrowMinimum.drawTextBox();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id)
		{
			case ELEMENT_IDS.manualModeNS:
				setCurrentMode(Modes.ManualNorthSouth);
				setManualChecked();
				manualModeNorth.setIsSelected(true);
				manualModeSouth.setIsSelected(false);
				break;
			case ELEMENT_IDS.manualModeWE:
				setCurrentMode(Modes.ManualWestEast);
				setManualChecked();
				manualModeNorth.setIsSelected(false);
				manualModeSouth.setIsSelected(true);
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
			case ELEMENT_IDS.crossOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Cross, false, true);
				break;
			case ELEMENT_IDS.crossOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Cross, false, false);
				break;
			case ELEMENT_IDS.dontCrossOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.DontCross, false, true);
				break;
			case ELEMENT_IDS.dontCrossOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.DontCross, false, false);
				break;
			case ELEMENT_IDS.crossOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Cross, true, true);
				break;
			case ELEMENT_IDS.crossOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Cross, true, false);
				break;
			case ELEMENT_IDS.dontCrossOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.DontCross, true, true);
				break;
			case ELEMENT_IDS.dontCrossOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.DontCross, true, false);
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

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		greenMinimum.mouseClicked(mouseX, mouseY, mouseButton);
		arrowMinimum.mouseClicked(mouseX, mouseY, mouseButton);
		yellowTime.mouseClicked(mouseX, mouseY, mouseButton);
		redTime.mouseClicked(mouseX, mouseY, mouseButton);
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		checkedKeyTyped(greenMinimum, typedChar, keyCode, (value) -> _te.getAutomator().setGreenMinimum(value));
		checkedKeyTyped(arrowMinimum, typedChar, keyCode, (value) -> _te.getAutomator().setArrowMinimum(value));
		checkedKeyTyped(yellowTime, typedChar, keyCode, (value) -> _te.getAutomator().setYellowTime(value));
		checkedKeyTyped(redTime, typedChar, keyCode, (value) -> _te.getAutomator().setRedTime(value));
		
		super.keyTyped(typedChar, keyCode);
	}
	
	private void checkedKeyTyped(GuiTextField textBox, char typedChar, int keyCode, Consumer<Double> onTypeSuccess)
	{
		if (Character.toString(typedChar).equals(".") && textBox.getText().contains("."))
		{
			return;
		}
		
		if (Keyboard.KEY_BACK == keyCode ||
				Keyboard.KEY_DELETE == keyCode ||
				Character.isDigit(typedChar))
		{
			textBox.textboxKeyTyped(typedChar, keyCode);
			
			if (textBox.isFocused())
			{
				if (textBox.getText().isEmpty())
				{
					onTypeSuccess.accept((double)0);
				}
				else
				{
					double value = Double.parseDouble(textBox.getText());
					onTypeSuccess.accept(value);
				}
			}
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
		public static final int manualModeNS = 25;
		public static final int manualModeWE = 26;
		public static final int greenMinimum = 27;
		public static final int yellowTime = 28;
		public static final int redTime = 29;
		public static final int arrowMinimum = 30;
		public static final int crossOn = 31;
		public static final int crossOff = 32;
		public static final int dontCrossOn = 33;
		public static final int dontCrossOff = 34;
		public static final int crossOnFlash = 35;
		public static final int crossOffFlash = 36;
		public static final int dontCrossOnFlash = 37;
		public static final int dontCrossOffFlash = 38;
	}

	private enum Modes
	{
		ManualNorthSouth,
		ManualWestEast,
		Automatic
	}
}
