package com.clussmanproductions.trafficcontrol.worldrender;

import com.clussmanproductions.trafficcontrol.ModBlocks;
import com.clussmanproductions.trafficcontrol.ModItems;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class TrafficLightPreview {
	@SubscribeEvent
	public static void render(RenderWorldLastEvent e)
	{
		if (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() != ModItems.traffic_light_frame)
		{
			return;
		}
		
		RayTraceResult res = Minecraft.getMinecraft().player.rayTrace(5, e.getPartialTicks());
		if (res.typeOfHit != Type.BLOCK)
		{
			return;
		}
		
		BlockPos pos = new BlockPos(res.hitVec.x, res.hitVec.y, res.hitVec.z);
		IBlockState state = Minecraft.getMinecraft().player.world.getBlockState(pos);
		if (state.getBlock() != ModBlocks.crossing_gate_pole)
		{
			return;
		}
		
		
	}
}
