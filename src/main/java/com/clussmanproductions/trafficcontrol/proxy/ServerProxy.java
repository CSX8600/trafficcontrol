package com.clussmanproductions.trafficcontrol.proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.tileentity.SignTileEntity;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ServerProxy extends CommonProxy {
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		String signsJson;
		try {
			signsJson = Resources.toString(ModTrafficControl.class.getResource("../../../assets/trafficcontrol/misc/signs.json"), Charsets.UTF_8);
		} catch (IOException ex) {
			FMLCommonHandler.instance().raiseException(ex, "Failed to load signs", true);
			return;
		}
		
		JsonArray jsonArray = new JsonParser().parse(signsJson).getAsJsonArray();
		Iterator<JsonElement> arrayIterator = jsonArray.iterator();
		HashMap<Integer, Integer> maxVariantsByType = new HashMap<>();
		
		while(arrayIterator.hasNext())
		{
			JsonElement signElement = arrayIterator.next();
			if (!signElement.isJsonObject())
			{
				continue;
			}
			
			JsonObject signObject = signElement.getAsJsonObject();
			String type = signObject.get("type").getAsString();
			int variant = signObject.get("variant").getAsInt();
			int typeID = SignTileEntity.getSignTypeByName(type);
			
			int maxVariant = maxVariantsByType.getOrDefault(typeID, 0);
			
			if (variant > maxVariant)
			{
				maxVariantsByType.put(typeID, variant);
			}
		}
		
		SignTileEntity.MAX_VARIANTS_BY_TYPE = ImmutableMap.copyOf(maxVariantsByType);
	}
}
