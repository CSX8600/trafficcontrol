package com.clussmanproductions.trafficcontrol.gui;

import java.util.function.Consumer;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotItemHandlerListenable extends SlotItemHandler {

	private Consumer<Integer> onSlotChangedListener = null;
	public SlotItemHandlerListenable(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	public void setOnSlotChangedListener(Consumer<Integer> c)
	{
		onSlotChangedListener = c;
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		
		if (onSlotChangedListener != null)
		{
			onSlotChangedListener.accept(getSlotIndex());
		}
	}
}
