package com.clussmanproductions.trafficcontrol.event;

import org.lwjgl.input.Keyboard;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class SignPackReloaderEventHandler {
	@SubscribeEvent
	public static void onKeyPress(RenderTickEvent e)
	{
		if (e.phase == Phase.END && Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_RBRACKET))
		{
			ModTrafficControl.instance.signRepo.reload();
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Signpacks reloaded!"));
		}
	}
}
