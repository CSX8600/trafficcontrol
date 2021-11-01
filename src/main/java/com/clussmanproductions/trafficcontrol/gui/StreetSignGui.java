package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.tileentity.StreetSign;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSign.StreetSignColors;
import com.clussmanproductions.trafficcontrol.tileentity.StreetSignTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class StreetSignGui extends GuiScreen {
	private StreetSignTileEntity te;
	public StreetSignGui(StreetSignTileEntity te)
	{
		this.te = te;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		
		ResourceLocation image = new ResourceLocation("trafficcontrol:textures/blocks/street_sign.png");
		
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			StreetSign sign = te.getStreetSign(i);
			if (sign == null)
			{
				continue;
			}
			
			int baseID = 10 * i;
			int yOffset = (1 + -i) * 80;
			
			GuiButtonExt directionButton = new GuiButtonExt(baseID, horizontalCenter + 128 + 4, verticalCenter + yOffset, 70, 20, getDirectionText(sign));
			buttonList.add(directionButton);
			
			GuiButtonExtSelectableImage greenSignButton = new GuiButtonExtSelectableImage(baseID + 1,
					horizontalCenter + 128 + 4 + 70 + 4,
					verticalCenter + yOffset, 
					40,
					20, 
					image,
					StreetSignColors.Green.getCol(),
					(StreetSignColors.Green.getRow() - 1) * 0.25, 
					1,
					0.25);
			greenSignButton.setIsSelected(sign.getColor() == StreetSignColors.Green);
			buttonList.add(greenSignButton);
			
			GuiButtonExtSelectableImage redSignButton = new GuiButtonExtSelectableImage(baseID + 2,
					horizontalCenter + 128 + 4 + 70 + 4,
					verticalCenter + yOffset + 20 + 4, 
					40,
					20, 
					image,
					StreetSignColors.Red.getCol(),
					(StreetSignColors.Red.getRow() - 1) * 0.25, 
					1,
					0.25);
			redSignButton.setIsSelected(sign.getColor() == StreetSignColors.Red);
			buttonList.add(redSignButton);
			
			GuiButtonExtSelectableImage blueSignButton = new GuiButtonExtSelectableImage(baseID + 3,
					horizontalCenter + 128 + 4 + 70 + 4 + 40 + 4,
					verticalCenter + yOffset, 
					40,
					20, 
					image,
					StreetSignColors.Blue.getCol(),
					(StreetSignColors.Blue.getRow() - 1) * 0.25, 
					1,
					0.25);
			blueSignButton.setIsSelected(sign.getColor() == StreetSignColors.Blue);
			buttonList.add(blueSignButton);
			
			GuiButtonExtSelectableImage yellowSignButton = new GuiButtonExtSelectableImage(baseID + 4,
					horizontalCenter + 128 + 4 + 70 + 4 + 40 + 4,
					verticalCenter + yOffset + 20 + 4, 
					40,
					20, 
					image,
					StreetSignColors.Yellow.getCol(),
					(StreetSignColors.Yellow.getRow() - 1) * 0.25, 
					1,
					0.25);
			yellowSignButton.setIsSelected(sign.getColor() == StreetSignColors.Yellow);
			buttonList.add(yellowSignButton);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		int horizontalCenter = width / 2;
		int verticalCenter = height / 2;
		
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			StreetSign sign = te.getStreetSign(i);
			if (sign == null)
			{
				continue;
			}
			
			int yTexOffset = (sign.getColor().getRow() - 1) * 64;
			int yOffset = (1 + -i) * 80;
			
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("trafficcontrol:textures/blocks/street_sign.png"));
			drawModalRectWithCustomSizedTexture(horizontalCenter - 128, verticalCenter + yOffset, 0, yTexOffset, 256, 64, 256, 256);
			
			GlStateManager.translate(horizontalCenter, verticalCenter + yOffset + 18, 0);
			
			int width = fontRenderer.getStringWidth(sign.getText());
			double scaleFactor = (double)240 / (double)width;
			
			if (scaleFactor > 4)
			{
				scaleFactor = 4;
			}
			
			GlStateManager.scale(scaleFactor, 4, 4);
			int afterFontX = fontRenderer.drawString(sign.getText(), -(width / 2), 0, sign.getTextColor());			
			if (sign.getIsNew())
			{
				fontRenderer.drawString("_", afterFontX, 0, sign.getTextColor());				
			}
			
			GlStateManager.scale(1 / scaleFactor, 0.25, 1 / 0.25);
			GlStateManager.translate(-horizontalCenter, -(verticalCenter + yOffset + 18), 0);
			GlStateManager.color(1, 1, 1);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		StreetSign currentSign = null;
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			StreetSign testSign = te.getStreetSign(i);
			if (testSign != null && testSign.getIsNew())
			{
				currentSign = testSign;
				break;
			}
		}
		
		if (currentSign == null)
		{
			super.keyTyped(typedChar, keyCode);
			return;
		}
		
		if (keyCode == Keyboard.KEY_BACK && currentSign.getText().length() > 0)
		{
			currentSign.setText(currentSign.getText().substring(0, currentSign.getText().length() - 1));
			return;
		}
		else if (Character.isLetterOrDigit(typedChar) || 
				Character.isSpaceChar(typedChar) ||
				Arrays.stream(new Character[] { '.', ',', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '/', '`', '~' }).anyMatch(c -> c.equals(new Character(typedChar))))
		{
			currentSign.setText(currentSign.getText() + typedChar);
			return;
		}
		
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton != 1)
		{
			super.mouseClicked(mouseX, mouseY, mouseButton);
			return;			
		}
		
        for (int i = 0; i < this.buttonList.size(); ++i)
        {
            GuiButton guibutton = this.buttonList.get(i);

            if (guibutton.mousePressed(this.mc, mouseX, mouseY))
            {
                guibutton.playPressSound(this.mc.getSoundHandler());
                onButtonRightClick(guibutton);
            }
        }
	}
	
	private void onButtonRightClick(GuiButton button)
	{
		GuiButtonExt extButton = (GuiButtonExt)button;
		
		int signIndex = 0;
		int buttonID = button.id;
		
		while(buttonID >= 10)
		{
			signIndex++;
			buttonID -= 10;
		}
		
		if (buttonID != 0)
		{
			return;
		}
		
		StreetSign sign = te.getStreetSign(signIndex);
		if (sign == null)
		{
			return;
		}
		
		decrementRotation(sign);
		extButton.displayString = getDirectionText(sign);
	}
	
	@Override
	public void onGuiClosed() {
		for(int i = 0; i < StreetSignTileEntity.MAX_STREET_SIGNS; i++)
		{
			StreetSign sign = te.getStreetSign(i);
			
			if (sign == null)
			{
				continue;
			}
			
			sign.setIsNew(false);
		}
		te.performClientToServerSync();
		super.onGuiClosed();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		GuiButtonExt extButton = (GuiButtonExt)button;
		
		GuiButtonExtSelectable sButton = null;
		if (extButton instanceof GuiButtonExtSelectable)
		{
			sButton = (GuiButtonExtSelectable)extButton;
		}
		
		int signIndex = 0;
		int buttonID = button.id;
		
		while(buttonID >= 10)
		{
			signIndex++;
			buttonID -= 10;
		}
		
		StreetSign sign = te.getStreetSign(signIndex);
		if (sign == null)
		{
			return;
		}
		
		switch(buttonID)
		{
			case 0:
				incrementRotation(sign);
				extButton.displayString = getDirectionText(sign);
				break;
			case 1:
				clearAllColorSelections(signIndex);
				sButton.setIsSelected(true);
				sign.setColor(StreetSignColors.Green);
				break;
			case 2:
				clearAllColorSelections(signIndex);
				sButton.setIsSelected(true);
				sign.setColor(StreetSignColors.Red);
				break;
			case 3:
				clearAllColorSelections(signIndex);
				sButton.setIsSelected(true);
				sign.setColor(StreetSignColors.Blue);
				break;
			case 4:
				clearAllColorSelections(signIndex);
				sButton.setIsSelected(true);
				sign.setColor(StreetSignColors.Yellow);
				break;
		}
	}
	
	private GuiButtonExtSelectable findButtonByID(int id)
	{
		Optional<GuiButton> button = buttonList.stream().filter(b -> b.id == id).findFirst();
		
		return button.isPresent() ? (GuiButtonExtSelectable)button.get() : null;
	}
	
	private void clearAllColorSelections(int signIndex)
	{
		for(int i = 2; i <= 5; i++)
		{
			findButtonByID(signIndex * 10 + i).setIsSelected(false);
		}
	}
	
	private String getDirectionText(StreetSign sign)
	{
		switch(sign.getRotation())
		{
		case 0:
		case 8:
			return "N/S";
		case 1:
		case 9:
			return "NNE/SSW";
		case 2:
		case 10:
			return "NW/SE";
		case 3:
		case 11:
			return "WNW/ESE";
		case 4:
		case 12:
			return "W/E";
		case 5:
		case 13:
			return "WSW/ENE";
		case 6:
		case 14:
			return "SE/NW";
		case 7:
		case 15:
			return "SSE/NNW";
		}
		
		return "";
	}
	
	
	private void incrementRotation(StreetSign sign)
	{
		int currentRotation = sign.getRotation();
		if (currentRotation == 15)
		{
			currentRotation = -1;
		}
		
		sign.setRotation(++currentRotation);
	}
	
	private void decrementRotation(StreetSign sign)
	{
		int currentRotation = sign.getRotation();
		if (currentRotation == 0)
		{
			currentRotation = 16;
		}
		
		sign.setRotation(--currentRotation);
	}
}
