package com.clussmanproductions.trafficcontrol.recipe;

import com.google.gson.JsonObject;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

public class RecipeFactory implements IRecipeFactory {

	public RecipeFactory() {}
	@Override
	public IRecipe parse(JsonContext context, JsonObject json) {
		ShapedRecipes shapedRecipe = ShapedRecipes.deserialize(json);
		return new TrafficLightCardUpgradeRecipe(shapedRecipe.getGroup(), shapedRecipe.getRecipeWidth(), shapedRecipe.getRecipeHeight(), shapedRecipe.getIngredients(), shapedRecipe.getRecipeOutput());
	}

}
