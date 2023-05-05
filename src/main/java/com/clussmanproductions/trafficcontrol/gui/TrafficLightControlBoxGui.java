package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import java.util.function.Consumer;
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
	private GuiCheckBox greenArrowRightOn;
	private GuiCheckBox yellowArrowRightOn;
	private GuiCheckBox redArrowRightOn;
	private GuiCheckBox greenArrowRightOff;
	private GuiCheckBox yellowArrowRightOff;
	private GuiCheckBox redArrowRightOff;
	
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
	private GuiCheckBox greenArrowRightOnFlash;
	private GuiCheckBox yellowArrowRightOnFlash;
	private GuiCheckBox redArrowRightOnFlash;
	private GuiCheckBox greenArrowRightOffFlash;
	private GuiCheckBox yellowArrowRightOffFlash;
	private GuiCheckBox redArrowRightOffFlash;
	
	private GuiButtonExtSelectable manualModeNorth;
	private GuiButtonExtSelectable manualModeSouth;
	
	private GuiTextField greenMinimum;
	private GuiTextField arrowMinimum;
	private GuiTextField rightArrowMinimum;
	private GuiTextField yellowTime;
	private GuiTextField redTime;
	private GuiTextField crossTime;
	private GuiTextField crossWarningTime;
	
	
	
	
	private TrafficLightControlBoxTileEntity _te;
	public TrafficLightControlBoxGui(TrafficLightControlBoxTileEntity te)
	{
		_te = te;
		
		_currentMode = _te.isAutoMode() ? Modes.Automatic : Modes.ManualNorthSouth; 
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		manualModeNorth = new GuiButtonExtSelectable(ELEMENT_IDS.manualModeNS, horizontalCenter - 107, verticalCenter - 100, 25, 20, "N/S");
		manualModeSouth = new GuiButtonExtSelectable(ELEMENT_IDS.manualModeWE, horizontalCenter - 107, verticalCenter - 78, 25, 20, "W/E");
		
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
		redArrowRightOn = new GuiCheckBox(ELEMENT_IDS.redArrowRightOn, horizontalCenter - 27, verticalCenter + 77, "", false);
		redArrowRightOnFlash = new GuiCheckBox(ELEMENT_IDS.redArrowRightOnFlash, horizontalCenter - 12, verticalCenter + 77, "", false);
		redArrowRightOff = new GuiCheckBox(ELEMENT_IDS.redArrowRightOff, horizontalCenter + 10, verticalCenter + 77, "", false);
		redArrowRightOffFlash = new GuiCheckBox(ELEMENT_IDS.redArrowRightOffFlash, horizontalCenter + 25, verticalCenter + 77, "", false);
		yellowArrowRightOn = new GuiCheckBox(ELEMENT_IDS.yellowArrowRightOn, horizontalCenter - 27, verticalCenter + 97, "", false);
		yellowArrowRightOnFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowRightOnFlash, horizontalCenter - 12, verticalCenter + 97, "", false);
		yellowArrowRightOff = new GuiCheckBox(ELEMENT_IDS.yellowArrowRightOff, horizontalCenter + 10, verticalCenter + 97, "", false);
		yellowArrowRightOffFlash = new GuiCheckBox(ELEMENT_IDS.yellowArrowRightOffFlash, horizontalCenter + 25, verticalCenter + 97, "", false);
		greenArrowRightOn = new GuiCheckBox(ELEMENT_IDS.greenArrowRightOn, horizontalCenter - 27, verticalCenter + 117, "", false);
		greenArrowRightOnFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowRightOnFlash, horizontalCenter - 12, verticalCenter + 117, "", false);
		greenArrowRightOff = new GuiCheckBox(ELEMENT_IDS.greenArrowRightOff, horizontalCenter + 10, verticalCenter + 117, "", false);
		greenArrowRightOffFlash = new GuiCheckBox(ELEMENT_IDS.greenArrowRightOffFlash, horizontalCenter + 25, verticalCenter + 117, "", false);
		
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
		buttonList.add(redArrowRightOn);
		buttonList.add(redArrowRightOnFlash);
		buttonList.add(redArrowRightOff);
		buttonList.add(redArrowRightOffFlash);
		buttonList.add(yellowArrowRightOn);
		buttonList.add(yellowArrowRightOnFlash);
		buttonList.add(yellowArrowRightOff);
		buttonList.add(yellowArrowRightOffFlash);
		buttonList.add(greenArrowRightOn);
		buttonList.add(greenArrowRightOnFlash);
		buttonList.add(greenArrowRightOff);
		buttonList.add(greenArrowRightOffFlash);
		
		setManualChecked();
		
		greenMinimum = new GuiTextField(ELEMENT_IDS.greenMinimum, fontRenderer, horizontalCenter - 54, verticalCenter - 90, 105, 20);
		yellowTime = new GuiTextField(ELEMENT_IDS.yellowTime, fontRenderer, horizontalCenter - 54, verticalCenter - 55, 105, 20);
		redTime = new GuiTextField(ELEMENT_IDS.redTime, fontRenderer, horizontalCenter - 54, verticalCenter - 20, 105, 20);
		arrowMinimum = new GuiTextField(ELEMENT_IDS.arrowMinimum, fontRenderer, horizontalCenter - 54, verticalCenter + 15, 105, 20);
		crossTime = new GuiTextField(ELEMENT_IDS.crossTime, fontRenderer, horizontalCenter - 54, verticalCenter + 50, 105, 20);
		crossWarningTime = new GuiTextField(ELEMENT_IDS.crossWarningTime, fontRenderer, horizontalCenter - 54, verticalCenter + 85, 105, 20);
		rightArrowMinimum = new GuiTextField(ELEMENT_IDS.rightArrowMinimum, fontRenderer, horizontalCenter - 54, verticalCenter + 120, 105, 20);
		
		greenMinimum.setText(Double.toString(_te.getAutomator().getGreenMinimum()));
		yellowTime.setText(Double.toString(_te.getAutomator().getYellowTime()));
		redTime.setText(Double.toString(_te.getAutomator().getRedTime()));
		arrowMinimum.setText(Double.toString(_te.getAutomator().getArrowMinimum()));
		crossTime.setText(Double.toString(_te.getAutomator().getCrossTime()));
		crossWarningTime.setText(Double.toString(_te.getAutomator().getCrossWarningTime()));
		rightArrowMinimum.setText(Double.toString(_te.getAutomator().getRightArrowTime()));
		
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
		greenArrowRightOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowRight, false, true));
		yellowArrowRightOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowRight, false, true));
		redArrowRightOn.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowRight, false, true));
		greenArrowRightOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowRight, false, false));
		yellowArrowRightOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowRight, false, false));
		redArrowRightOff.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowRight, false, false));
		// Flashing Bulbs
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
		greenArrowRightOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowRight, true, true));
		yellowArrowRightOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowRight, true, true));
		redArrowRightOnFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowRight, true, true));
		greenArrowRightOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.GreenArrowRight, true, false));
		yellowArrowRightOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.YellowArrowRight, true, false));
		redArrowRightOffFlash.setIsChecked(getChecked(EnumTrafficLightBulbTypes.RedArrowRight, true, false));
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
		drawScaledCustomSizeModalRect(horizontalCenter - 115, verticalCenter - 138, 0, 0, 16, 16, 230, 280, 16, 16);
		
		if (_currentMode == Modes.ManualNorthSouth || _currentMode == Modes.ManualWestEast)
		{
			drawManualMode(horizontalCenter, verticalCenter);
			 
			
			
			
		}
		
		if (_currentMode == Modes.Automatic)
		{
			drawAutomaticMode(horizontalCenter, verticalCenter);
		}
		
		
		
				
		super.drawScreen(mouseX, mouseY, partialTicks);
		int hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    int hoverY = verticalCenter + 75; // Y coordinate of the designated point
	    int hoverWidth = 18; // Width of the hovering text area
	    int hoverHeight = 16; // Height of the hovering text area
	    if (_currentMode == Modes.ManualNorthSouth || _currentMode == Modes.ManualWestEast)
		{
	    	
		if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "right");
	    }
		hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter - 25; // Y coordinate of the designated point
	    
		
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "leftred");
	    }
		hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter - 5; // Y coordinate of the designated point
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "leftyellow");
	    }
	    hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter - 85; // Y coordinate of the designated point
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "red");
	    }
	    hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter - 65; // Y coordinate of the designated point
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "yellow");
	    }
	    hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter - 45; // Y coordinate of the designated point
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "green");
	    }
	    hoverX = horizontalCenter - 54; // X coordinate of the designated point
	    hoverY = verticalCenter + 15; // Y coordinate of the designated point
	    if (mouseX >= hoverX && mouseX <= hoverX + hoverWidth && mouseY >= hoverY && mouseY <= hoverY + hoverHeight) {
			drawHoveredText(mouseX, mouseY, "leftgreen");
	    }
		}
	}
	
	private void drawHoveredText(int mouseX, int mouseY, String type) {
		String text = "";
		
		switch(type) {
		case "right":
			text = "Right Arrow Red And No Right Turn";
				drawHoveringText(text, mouseX, mouseY);
			break;
		case "leftred": 
			text = "Left Arrow Red, No Left Turn, And U-Turn Arrow Red";
			drawHoveringText(text, mouseX, mouseY);
			break;
		case "leftyellow":
			text = "Left Arrow Yellow and U-Turn Arrow Yellow";
			drawHoveringText(text, mouseX, mouseY);
			break;
		case "leftgreen":
			text = "Left Arrow Green and U-Turn Arrow Green";
			drawHoveringText(text, mouseX, mouseY);
			break;
		case "red":
			text = "Solid Red and Striaght Arrow Red";
			drawHoveringText(text, mouseX, mouseY);
			break;
		case "yellow":
			text = "Solid Yellow and Striaght Arrow Yellow";
			drawHoveringText(text, mouseX, mouseY);
			break;
		case "green":
			text = "Solid Green and Striaght Arrow Green";
			drawHoveringText(text, mouseX, mouseY);
			break;
				
}			
	}
//	These are var's for what lets the code know what texture to should for special bulbs 
	// Must be anywhere above the next //comment() 
	private int tickCounter = 0;
	private Boolean isRightRed = true;
	private Boolean isLeftYellow = true;
	private String LeftTurn = "Red";
	private Boolean isLeftGreen = true;
	private Boolean isRed = true;
	private Boolean isYellow = true;
	private Boolean isGreen = true;
	private ResourceLocation textureRight = null;
	private ResourceLocation textureLeftRed = null;
	private ResourceLocation textureLeftYellow = null;
	private ResourceLocation textureLeftGreen = null;
	private ResourceLocation textureRed = null;
	private ResourceLocation textureYellow = null;
	private ResourceLocation textureGreen = null;
	
	// the above must stay above this comment if will break otherwise
	private void drawGreenBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if(isGreen) {
			 textureGreen = new ResourceLocation("trafficcontrol:blocks/green");
		 } else {
			 textureGreen = new ResourceLocation("trafficcontrol:blocks/straight_green");
		 }
		 
		 sprite = textureMap.getAtlasSprite(textureGreen.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 45, sprite, 16, 16);
	}
	
	private void drawYellowBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if(isYellow) {
			 textureYellow = new ResourceLocation("trafficcontrol:blocks/yellow_solid");
		 } else {
			 textureYellow = new ResourceLocation("trafficcontrol:blocks/straight_yellow");
		 }
		 
		 sprite = textureMap.getAtlasSprite(textureYellow.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 65, sprite, 16, 16);
	}
	
	private void drawRedBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if(isRed) {
			 textureRed = new ResourceLocation("trafficcontrol:blocks/red");
		 } else {
			 textureRed = new ResourceLocation("trafficcontrol:blocks/straight_red");
		 }
		 
		 sprite = textureMap.getAtlasSprite(textureRed.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 85, sprite, 16, 16);
	}
	
	private void drawLeftGreenBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if(isLeftGreen) {
			 textureLeftGreen = new ResourceLocation("trafficcontrol:blocks/green_arrow_left");
		 } else {
			 textureLeftGreen = new ResourceLocation("trafficcontrol:blocks/green_arrow_uturn");
		 }
		 
		 sprite = textureMap.getAtlasSprite(textureLeftGreen.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 15, sprite, 16, 16);
	}
	
	private void drawLeftYellowBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if(isLeftYellow) {
			 textureLeftYellow = new ResourceLocation("trafficcontrol:blocks/yellow_arrow_left");
		 } else {
			 textureLeftYellow = new ResourceLocation("trafficcontrol:blocks/yellow_arrow_uturn");
		 }
		 
		 sprite = textureMap.getAtlasSprite(textureLeftYellow.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 5, sprite, 16, 16);
	}
	
	private void drawLeftRedBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		
		 if (LeftTurn == "Red") {
		        textureLeftRed = new ResourceLocation("trafficcontrol:blocks/red_arrow_left");
		    } else if(LeftTurn == "No") {
		        textureLeftRed = new ResourceLocation("trafficcontrol:blocks/no_left_turn");
		    } else if(LeftTurn == "UTurn"){
		    	textureLeftRed = new ResourceLocation("trafficcontrol:blocks/red_arrow_uturn");
		    }
		    sprite = textureMap.getAtlasSprite(textureLeftRed.toString());
			drawTexturedModalRect(horizontalCenter - 54, verticalCenter - 25, sprite, 16, 16);
	}
		
		   
	
	
	
	private void drawRightRedBulb(int horizontalCenter, int verticalCenter) {
		Minecraft mc = Minecraft.getMinecraft();
		TextureMap textureMap = mc.getTextureMapBlocks();
		 TextureAtlasSprite sprite;
		 
		 if (isRightRed) {
		        textureRight = new ResourceLocation("trafficcontrol:blocks/red_arrow_right");
		    } else {
		        textureRight = new ResourceLocation("trafficcontrol:blocks/no_right_turn");
		    }
	    
	    sprite = textureMap.getAtlasSprite(textureRight.toString());
	    drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 75, sprite, 16, 16);
	}

	
	private void switchSpecialBulbs(int horizontalCenter, int verticalCenter) {
	    
	    
	   
	    
	    tickCounter++;
	    if (tickCounter >= 80) { // 5 seconds (assuming 20 ticks per second)
	        tickCounter = 0;
	        isRightRed = !isRightRed;
	        isLeftYellow = !isLeftYellow;
	        isLeftGreen = !isLeftGreen;
	        isRed = !isRed;
	        isYellow = !isYellow;
	        isGreen = !isGreen;
	        if (LeftTurn == "Red") {
		        LeftTurn = "No";
		    } else if(LeftTurn == "No") {
		    	LeftTurn = "UTurn";
		    } else if(LeftTurn == "UTurn"){
		    	LeftTurn = "Red";
		    }
	       
	       
	    }
	    drawRightRedBulb(horizontalCenter, verticalCenter);
	    drawLeftRedBulb(horizontalCenter, verticalCenter);
        drawLeftYellowBulb(horizontalCenter, verticalCenter);
        drawLeftGreenBulb(horizontalCenter, verticalCenter);
        drawRedBulb(horizontalCenter, verticalCenter);
        drawYellowBulb(horizontalCenter, verticalCenter);
        drawGreenBulb(horizontalCenter, verticalCenter);
	}
	    
	    
	
    
	
	private void drawManualMode(int horizontalCenter, int verticalCenter)
	{
	   
		drawString(fontRenderer, "Manual Mode", horizontalCenter - 54, verticalCenter - 110, 0xFFFF00);
		drawString(fontRenderer, "Direction", horizontalCenter - 115, verticalCenter - 110, 0xFFFFFF);
		
		drawString(fontRenderer, "Bulb", horizontalCenter - 54, verticalCenter - 100, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter - 11, verticalCenter - 100, 0xFFFFFF);
		drawString(fontRenderer, "F", horizontalCenter + 26, verticalCenter - 100, 0xFFFFFF);
		
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_on");
		drawTexturedModalRect(horizontalCenter - 30, verticalCenter - 106, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_torch_off");
		drawTexturedModalRect(horizontalCenter + 7, verticalCenter - 106, sprite, 16, 16);
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/cross");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 35, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/dontcross");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 55, sprite, 16, 16);
		switchSpecialBulbs(horizontalCenter, verticalCenter);
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/yellow_arrow_right");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 95, sprite, 16, 16);
		
		sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("trafficcontrol:blocks/green_arrow_right");
		drawTexturedModalRect(horizontalCenter - 54, verticalCenter + 115, sprite, 16, 16);
		
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
		drawString(fontRenderer, "Left Arrow", leftMargin, verticalCenter + 5, 0xFFFFFF);
		arrowMinimum.drawTextBox();
		drawString(fontRenderer, "Cross Time", leftMargin, verticalCenter + 40, 0xFFFFFF);
		crossTime.drawTextBox();
		drawString(fontRenderer, "Cross Warning Time", leftMargin, verticalCenter + 75, 0xFFFFFF);
		crossWarningTime.drawTextBox();
		drawString(fontRenderer, "Right Arrow", leftMargin, verticalCenter + 110, 0xFFFFFF);
		rightArrowMinimum.drawTextBox();
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
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightGreen, false, true);
				break;
			case ELEMENT_IDS.greenOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightGreen, true, true);
				break;
			case ELEMENT_IDS.yellowOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightYellow, false, true);
				break;
			case ELEMENT_IDS.yellowOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightYellow, true, true);
				break;
			case ELEMENT_IDS.redOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightRed, false, true);
				break;
			case ELEMENT_IDS.redOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightRed, true, true);
				break;
			case ELEMENT_IDS.greenArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowUTurn, false, true);
				break;
			case ELEMENT_IDS.yellowArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowUTurn, false, true);
				break;
			case ELEMENT_IDS.redArrowLeftOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoLeftTurn, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowUTurn, false, true);
				break;
			case ELEMENT_IDS.greenArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowUTurn, true, true);
				break;
			case ELEMENT_IDS.yellowArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowUTurn, true, true);
				break;
			case ELEMENT_IDS.redArrowLeftOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoLeftTurn, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowUTurn, true, true);
				break;
			case ELEMENT_IDS.greenOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightGreen, false, false);
				break;
			case ELEMENT_IDS.greenOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Green, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightGreen, true, false);
				break;
			case ELEMENT_IDS.yellowOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightYellow, false, false);
				break;
			case ELEMENT_IDS.yellowOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Yellow, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightYellow, true, false);
				break;
			case ELEMENT_IDS.redOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightRed, false, false);
				break;
			case ELEMENT_IDS.redOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.Red, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.StraightRed, true, false);
				break;
			case ELEMENT_IDS.greenArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowUTurn, false, false);
				break;
			case ELEMENT_IDS.yellowArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowUTurn, false, false);
				break;
			case ELEMENT_IDS.redArrowLeftOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowUTurn, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoLeftTurn, false, false);
				break;
			case ELEMENT_IDS.greenArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowLeft, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowUTurn, true, false);
				break;
			case ELEMENT_IDS.yellowArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowLeft, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowUTurn, true, false);
				break;
			case ELEMENT_IDS.redArrowLeftOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowLeft, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowUTurn, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoLeftTurn, true, false);
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
			case ELEMENT_IDS.greenArrowRightOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowRight, false, true);
				break;
			case ELEMENT_IDS.yellowArrowRightOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowRight, false, true);
				break;
			case ELEMENT_IDS.redArrowRightOn:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowRight, false, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoRightTurn, false, true);
				break;
			case ELEMENT_IDS.greenArrowRightOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowRight, true, true);
				
				break;
			case ELEMENT_IDS.yellowArrowRightOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowRight, true, true);
				break;
			case ELEMENT_IDS.redArrowRightOnFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowRight, true, true);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoRightTurn, true, true);
				break;
			case ELEMENT_IDS.greenArrowRightOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowRight, false, false);
				break;
			case ELEMENT_IDS.yellowArrowRightOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowRight, false, false);
				break;
			case ELEMENT_IDS.redArrowRightOff:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowRight, false, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoRightTurn, false, false);
				break;
			case ELEMENT_IDS.greenArrowRightOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.GreenArrowRight, true, false);
				break;
			case ELEMENT_IDS.yellowArrowRightOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.YellowArrowRight, true, false);
				break;
			case ELEMENT_IDS.redArrowRightOffFlash:
				handleManualClick(button, EnumTrafficLightBulbTypes.RedArrowRight, true, false);
				handleManualClick(button, EnumTrafficLightBulbTypes.NoRightTurn, true, false);
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
		crossTime.mouseClicked(mouseX, mouseY, mouseButton);
		crossWarningTime.mouseClicked(mouseX, mouseY, mouseButton);
		rightArrowMinimum.mouseClicked(mouseX, mouseY, mouseButton);
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		checkedKeyTyped(greenMinimum, typedChar, keyCode, (value) -> _te.getAutomator().setGreenMinimum(value));
		checkedKeyTyped(arrowMinimum, typedChar, keyCode, (value) -> _te.getAutomator().setArrowMinimum(value));
		checkedKeyTyped(yellowTime, typedChar, keyCode, (value) -> _te.getAutomator().setYellowTime(value));
		checkedKeyTyped(redTime, typedChar, keyCode, (value) -> _te.getAutomator().setRedTime(value));
		checkedKeyTyped(crossTime, typedChar, keyCode, (value) -> _te.getAutomator().setCrossTime(value));
		checkedKeyTyped(crossWarningTime, typedChar, keyCode, (value) -> _te.getAutomator().setCrossWarningTime(value));
		checkedKeyTyped(rightArrowMinimum, typedChar, keyCode, (value) -> _te.getAutomator().setRightArrowTime(value));
		
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
				Character.isDigit(typedChar) ||
				typedChar == '.')
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
					try
					{
						double value = Double.parseDouble(textBox.getText());
						onTypeSuccess.accept(value);
					}
					catch(NumberFormatException | NullPointerException ex) {}
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
		public static final int crossTime = 39;
		public static final int crossWarningTime = 40;
		public static final int greenArrowRightOn = 41;
		public static final int yellowArrowRightOn = 42;
		public static final int redArrowRightOn = 43;
		public static final int greenArrowRightOff = 44;
		public static final int yellowArrowRightOff = 45;
		public static final int redArrowRightOff = 46;
		public static final int greenArrowRightOnFlash = 47;
		public static final int yellowArrowRightOnFlash = 48;
		public static final int redArrowRightOnFlash = 49;
		public static final int greenArrowRightOffFlash = 50;
		public static final int yellowArrowRightOffFlash = 51;
		public static final int redArrowRightOffFlash = 52;
		public static final int rightArrowMinimum = 53;
		
	}

	private enum Modes
	{
		ManualNorthSouth,
		ManualWestEast,
		Automatic
	}
}
