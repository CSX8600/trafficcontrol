package com.clussmanproductions.trafficcontrol.recipes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.clussmanproductions.trafficcontrol.ModItems;
import com.clussmanproductions.trafficcontrol.util.EnumTrafficLightBulbTypes;
import com.google.common.base.Function;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeTrafficLightBulb extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	HashMap<Item[][], Function<ItemStack[][], ItemStack>> craftingRecipes = new HashMap<>();
	public RecipeTrafficLightBulb()
	{
		setRegistryName("traffic_light_bulb");
		fillMap();
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		
		Item[][] craftingRecipePattern = getCurrentRecipe(inv).getFirst();
		return craftingRecipesContainsKey(craftingRecipePattern);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		Tuple<Item[][], ItemStack[][]> craftingInfo = getCurrentRecipe(inv);
		Function<ItemStack[][], ItemStack> recipeResolver = craftingRecipesGet(craftingInfo.getFirst());
		
		if (recipeResolver != null)
		{
			return recipeResolver.apply(craftingInfo.getSecond());
		}
		
		return null;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		ItemStack bulbStack = new ItemStack(ModItems.traffic_light_bulb);
		bulbStack.setTagInfo("type", new NBTTagFloat(EnumTrafficLightBulbTypes.Red.getIndex()));
		return bulbStack;
	}
	
	private Tuple<Item[][], ItemStack[][]> getCurrentRecipe(InventoryCrafting inv)
	{
		int maxCounter = inv.getWidth();
		if (inv.getHeight() > maxCounter)
		{
			maxCounter = inv.getHeight();
		}
		
		int startingColumn = 0;
		int startingRow = 0;
		int endingColumn = 0;
		int endingRow = 0;
		
		boolean establishedStart = false;
		for(int i = 0; i < inv.getWidth(); i++)
		{
			for(int j = 0; j < inv.getHeight(); j++)
			{
				if (inv.getStackInRowAndColumn(i, j).getItem() != ItemBlock.getItemFromBlock(Blocks.AIR))
				{
					if (!establishedStart)
					{
						startingColumn = i;
						establishedStart = true;
					}
					endingColumn = i;
				}
			}
		}
		
		establishedStart = false;
		for(int i = 0; i < inv.getHeight(); i++)
		{
			for(int j = 0; j < inv.getWidth(); j++)
			{
				if (inv.getStackInRowAndColumn(j, i).getItem() != ItemBlock.getItemFromBlock(Blocks.AIR))
				{
					if (!establishedStart)
					{
						startingRow = i;
						establishedStart = true;
					}
					endingRow = i;
				}
			}
		}
		
		Item[][] craftingRecipePattern = new Item[endingRow - startingRow + 1][(endingColumn - startingColumn + 1)];
		ItemStack[][] craftingRecipe = new ItemStack[endingRow - startingRow + 1][endingColumn - startingColumn + 1];
		
		for(int row = 0; row <= endingRow - startingRow; row++)
		{
			for(int column = 0; column <= endingColumn - startingColumn; column++)
			{
				if (inv.getStackInRowAndColumn(column + startingColumn, row + startingRow).getItem() != ItemBlock.getItemFromBlock(Blocks.AIR))
				{
					craftingRecipePattern[row][column] = inv.getStackInRowAndColumn(column + startingColumn, row + startingRow).getItem();
					craftingRecipe[row][column] = inv.getStackInRowAndColumn(column + startingColumn, row + startingRow);
				}
				else
				{
					craftingRecipePattern[row][column] = null;
				}
			}
		}
		
		return new Tuple<Item[][], ItemStack[][]>(craftingRecipePattern, craftingRecipe);
	}
	
	private void fillMap()
	{
		// Red
		craftingRecipes.put(
				new Item[][]
				{
					{ Items.DYE, Item.getItemFromBlock(Blocks.REDSTONE_LAMP) }
				},
				(recipe) ->
				{
					ItemStack dyeStack = recipe[0][0];
					if (dyeStack == null || dyeStack.getItemDamage() != EnumDyeColor.RED.getDyeDamage())
					{
						return null;
					}
					
					ItemStack bulbStack = new ItemStack(ModItems.traffic_light_bulb);
					bulbStack.setTagInfo("type", new NBTTagFloat(EnumTrafficLightBulbTypes.Red.getIndex()));
					
					return bulbStack;
				});
		
		// Yellow
		craftingRecipes.put(
				new Item[][]
				{
					{ Items.DYE, Item.getItemFromBlock(Blocks.REDSTONE_LAMP) }
				},
				(recipe) ->
				{
					ItemStack dyeStack = recipe[0][0];
					if (dyeStack == null || dyeStack.getItemDamage() != EnumDyeColor.YELLOW.getDyeDamage())
					{
						return null;
					}
					
					ItemStack bulbStack = new ItemStack(ModItems.traffic_light_bulb);
					bulbStack.setTagInfo("type", new NBTTagFloat(EnumTrafficLightBulbTypes.Yellow.getIndex()));
					
					return bulbStack;
				});
		
		// Green
		craftingRecipes.put(
				new Item[][]
				{
					{ Items.DYE, Item.getItemFromBlock(Blocks.REDSTONE_LAMP) }
				},
				(recipe) ->
				{
					ItemStack dyeStack = recipe[0][0];
					if (dyeStack == null || dyeStack.getItemDamage() != EnumDyeColor.GREEN.getDyeDamage())
					{
						return null;
					}
					
					ItemStack bulbStack = new ItemStack(ModItems.traffic_light_bulb);
					bulbStack.setTagInfo("type", new NBTTagFloat(EnumTrafficLightBulbTypes.Green.getIndex()));
					
					return bulbStack;
				});
		
	}

	private boolean craftingRecipesContainsKey(Item[][] pattern)
	{
		for(Map.Entry<Item[][], Function<ItemStack[][], ItemStack>> recipe : craftingRecipes.entrySet())
		{
			if (Arrays.deepEquals(recipe.getKey(), pattern))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private Function<ItemStack[][], ItemStack> craftingRecipesGet(Item[][] pattern)
	{
		for(Map.Entry<Item[][], Function<ItemStack[][], ItemStack>> recipe : craftingRecipes.entrySet())
		{
			if (Arrays.deepEquals(recipe.getKey(), pattern))
			{
				return recipe.getValue();
			}
		}
		
		return null;
	}
}
