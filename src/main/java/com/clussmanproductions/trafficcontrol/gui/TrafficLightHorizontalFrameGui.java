package com.clussmanproductions.trafficcontrol.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class TrafficLightHorizontalFrameGui extends BaseTrafficLightFrameGui
{
    public TrafficLightHorizontalFrameGui(InventoryPlayer inventory, ItemStack frameStack)
    {
        super(new TrafficLightHorizontalContainer(inventory, frameStack));

        xSize = 174;
        ySize = 200;
    }

    @Override
    protected String getGuiPngName() {
        return "traffic_light_horizontal_frame.png";
    }
}
