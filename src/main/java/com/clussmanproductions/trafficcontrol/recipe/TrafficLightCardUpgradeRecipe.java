package com.clussmanproductions.trafficcontrol.recipe;

import java.util.stream.Collectors;

import com.clussmanproductions.trafficcontrol.item.ItemTrafficLightCard;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class TrafficLightCardUpgradeRecipe extends ShapedRecipes {

	public TrafficLightCardUpgradeRecipe(String group, int width, int height, NonNullList<Ingredient> ingredients,
			ItemStack result) {
		super(group, width, height, ingredients, result);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack card = getRecipeOutput().copy();
		NBTTagCompound cardTag = card.getTagCompound();
		if (cardTag == null)
		{
			cardTag = new NBTTagCompound();
			card.setTagCompound(cardTag);
		}
		
		for(int i = 0; i < inv.getWidth() * inv.getHeight(); i++)
		{
			ItemStack slotStack = inv.getStackInSlot(i);
			if (slotStack == null || !(slotStack.getItem() instanceof ItemTrafficLightCard))
			{
				continue;
			}
			
			NBTTagCompound sourceTag = slotStack.getTagCompound();
			if (sourceTag == null)
			{
				continue;
			}
			
			for(String key : sourceTag.getKeySet().stream().filter(key -> key.startsWith("light")).collect(Collectors.toList()))
			{
				cardTag.setLong(key, sourceTag.getLong(key));
			}
			
			break;
		}
		
		return card;
	}
}
