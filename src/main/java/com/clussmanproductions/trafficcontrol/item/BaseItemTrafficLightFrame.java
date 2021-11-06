package com.clussmanproductions.trafficcontrol.item;

import java.util.HashMap;
import java.util.List;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.blocks.BlockBaseTrafficLight;
import com.clussmanproductions.trafficcontrol.tileentity.BaseTrafficLightTileEntity;
import com.clussmanproductions.trafficcontrol.util.CustomAngleCalculator;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class BaseItemTrafficLightFrame extends Item {
	public BaseItemTrafficLightFrame(String name)
	{
		super();
		setRegistryName(name);
		setUnlocalizedName(ModTrafficControl.MODID + "." + name);
		setMaxStackSize(1);
		setCreativeTab(ModTrafficControl.CREATIVE_TAB);
	}
	
	public void initModel()
	{
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
	
	protected abstract int getGuiID();
	
	public abstract int getBulbCount();
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(ModTrafficControl.instance, getGuiID(), worldIn, 0, 0, 0);
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new ItemTrafficLightFrameCapabilityProvider(stack, getBulbCount());
	}
	
	@Override
	public abstract void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn);
	
	protected abstract BlockBaseTrafficLight getBaseBlockTrafficLight();

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		
		if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
		{
			pos = pos.offset(facing);
		}
		
		worldIn.setBlockState(pos, getBaseBlockTrafficLight().getDefaultState().withProperty(BlockBaseTrafficLight.ROTATION, CustomAngleCalculator.getRotationForYaw(player.rotationYaw)));
		BaseTrafficLightTileEntity trafficLight = (BaseTrafficLightTileEntity)worldIn.getTileEntity(pos);
		
		int bulbCount = getBulbCount();
		
		ItemStack heldItem = player.getHeldItem(hand);
		HashMap<Integer, EnumTrafficLightBulbTypes> bulbsBySlot = new HashMap<Integer, EnumTrafficLightBulbTypes>(bulbCount);
		IItemHandler handler = heldItem.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		HashMap<Integer, Boolean> allowFlashBySlot = new HashMap<>();
		
		for(int i = 0; i < bulbCount; i++)
		{
			ItemStack bulbTypeInSlot = handler.getStackInSlot(i);
			if (bulbTypeInSlot == ItemStack.EMPTY)
			{
				bulbsBySlot.put(i, null);
			}
			else
			{
				bulbsBySlot.put(i, EnumTrafficLightBulbTypes.get(bulbTypeInSlot.getMetadata()));
			}
			
			allowFlashBySlot.put(i, getAlwaysFlash(heldItem, i));
		}
		
		trafficLight.setBulbsBySlot(bulbsBySlot);
		trafficLight.setAllowFlashBySlot(allowFlashBySlot);
		
		player.getHeldItemMainhand().shrink(1);
		
		return EnumActionResult.SUCCESS;
	}

	@Override
	public NBTTagCompound getNBTShareTag(ItemStack stack) {
		NBTTagCompound tag = super.getNBTShareTag(stack);
		
		if (tag == null)
		{
			tag = new NBTTagCompound();
		}
		
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		NBTBase capTag = CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null);
		tag.setTag("ClientInventory", capTag);
		
		return tag;
	}
	
	@Override
	public void readNBTShareTag(ItemStack stack, NBTTagCompound nbt) {
		super.readNBTShareTag(stack, nbt);
		
		if (nbt != null && nbt.hasKey("ClientInventory"))
		{
			IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, handler, null, nbt.getTag("ClientInventory"));
		}
	}

	public void handleGuiAlwaysUpdate(ItemStack stack, int slotId, boolean alwaysFlash)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null)
		{
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}
		
		tag.setBoolean("always-flash-" + slotId, alwaysFlash);
	}
	
	public boolean getAlwaysFlash(ItemStack stack, int slotId)
	{
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null || !tag.hasKey("always-flash-" + slotId))
		{
			return true;
		}
		
		return tag.getBoolean("always-flash-" + slotId);
	}
}
