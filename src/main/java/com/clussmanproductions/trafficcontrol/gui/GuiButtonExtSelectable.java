package com.clussmanproductions.trafficcontrol.gui;

import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonExtSelectable extends GuiButtonExt {

	private boolean isSelected = false;
	public GuiButtonExtSelectable(int id, int xPos, int yPos, int width, int height, String displayString) {
		super(id, xPos, yPos, width, height, displayString);
		// TODO Auto-generated constructor stub
	}
	
	public void setIsSelected(boolean value)
	{
		isSelected = value;
	}
	
	public boolean getIsSelected()
	{
		return isSelected;
	}
	
	@Override
	protected int getHoverState(boolean mouseOver) {
		if (isSelected)
		{
			return 2;
		}
		
		return super.getHoverState(mouseOver);
	}
}
