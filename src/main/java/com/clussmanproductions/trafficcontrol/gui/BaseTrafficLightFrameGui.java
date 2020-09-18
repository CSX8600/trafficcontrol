package com.clussmanproductions.trafficcontrol.gui;

import java.io.IOException;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.BaseTrafficLightFrameContainer.FrameSlotInfo;
import com.clussmanproductions.trafficcontrol.item.BaseItemTrafficLightFrame;
import com.clussmanproductions.trafficcontrol.network.PacketHandler;
import com.clussmanproductions.trafficcontrol.network.PacketTrafficLightFrameGuiUpdate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public abstract class BaseTrafficLightFrameGui extends GuiContainer {

	ItemStack frameStack;
	BaseTrafficLightFrameContainer container;
	public BaseTrafficLightFrameGui(BaseTrafficLightFrameContainer container)
	{
		super(container);
		this.container = container;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		ItemStack frameStack = container.getFrameStack();
		BaseItemTrafficLightFrame frameItem = (BaseItemTrafficLightFrame)frameStack.getItem();
		
		for(FrameSlotInfo slotInfo : container.getItemSlots(container.frameStackHandler))
		{
			int left = (width / 2) - (xSize / 2);
			int top = (height / 2) - (ySize / 2);
			int x = left + slotInfo.getSlot().xPos;
			int y = top + slotInfo.getSlot().yPos;
			
			GuiCheckBox doNotFlash = new GuiCheckBox(slotInfo.getSlot().getSlotIndex() * 10, 0, 0, "Allow Flash", true);
			doNotFlash.visible = slotInfo.getSlot().getHasStack();
			doNotFlash.setIsChecked(frameItem.getAlwaysFlash(frameStack, slotInfo.getSlot().getSlotIndex()));
			((SlotItemHandlerListenable)container.getSlot(slotInfo.getSlot().getSlotIndex() + 36)).setOnSlotChangedListener(ind -> onSlotClick(ind));
			
			switch(slotInfo.getCheckboxOrientation())
			{
				case ABOVE:
					y -= 24;
					break;
				case BELOW:
					y += 28;
					break;
				case LEFT:
					x -= doNotFlash.getButtonWidth() + 12;
					break;
				case RIGHT:
					x += 30;
					break;
			}
			
			doNotFlash.x = x;
			doNotFlash.y = y;
			
			buttonList.add(doNotFlash);
		}
	}
	
	private void onSlotClick(int slotId)
	{
		int inventorySlot = slotId;
		
		if (inventorySlot < 0)
		{
			return;
		}
		
		inventorySlot *= 10;
		
		GuiCheckBox box = findCheckboxById(inventorySlot);
		
		if (box == null)
		{
			return;
		}
		
		box.visible = container.getSlot(slotId + 36).getHasStack();
	}
	
	private GuiCheckBox findCheckboxById(int id)
	{
		for(GuiButton button : buttonList)
		{
			if (button instanceof GuiCheckBox && button.id == id)
			{
				return (GuiCheckBox)button;
			}
		}
		
		return null;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModTrafficControl.MODID + ":textures/gui/" + getGuiPngName()));
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (!(button instanceof GuiCheckBox))
		{
			return;
		}
		
		GuiCheckBox checkbox = (GuiCheckBox)button;
		
		int type = button.id % 10;
		int slotId = button.id / 10;
		
		switch(type)
		{
			case 0: // Allow Flash
				BaseItemTrafficLightFrame baseFrameItem = (BaseItemTrafficLightFrame)container.getFrameStack().getItem();
				baseFrameItem.handleGuiAlwaysUpdate(container.getFrameStack(), slotId, checkbox.isChecked());
				
				PacketTrafficLightFrameGuiUpdate packet = new PacketTrafficLightFrameGuiUpdate();
				packet.slotId = slotId;
				packet.alwaysFlash = checkbox.isChecked();
				PacketHandler.INSTANCE.sendToServer(packet);
				break;
		}
	}
	
	protected abstract String getGuiPngName();
}
