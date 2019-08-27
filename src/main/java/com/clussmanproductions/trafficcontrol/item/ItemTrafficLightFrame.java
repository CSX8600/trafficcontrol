package com.clussmanproductions.trafficcontrol.item;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.gui.GuiProxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemTrafficLightFrame extends Item {
	public ItemTrafficLightFrame()
	{
		super();
		setRegistryName("traffic_light_frame");
		setUnlocalizedName(ModTrafficControl.MODID + ".traffic_light_frame");
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(ModTrafficControl.instance, GuiProxy.GUI_IDs.TRAFFIC_LIGHT_FRAME, worldIn, 0, 0, 0);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ICapabilityProvider() {
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				{
					return true;
				}
				
				return false;
			}
			
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				{
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new ItemStackHandler(3)
							{
								@Override
								public boolean isItemValid(int slot, ItemStack stack) {
									return stack.getItem() instanceof ItemTrafficLightBulb;
								}
								
								@Override
								protected int getStackLimit(int slot, ItemStack stack) {
									return 1;
								}
								
								
							});
				}
				return null;
			}
		};
	}
}
