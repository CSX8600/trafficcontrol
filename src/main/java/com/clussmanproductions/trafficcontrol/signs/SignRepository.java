package com.clussmanproductions.trafficcontrol.signs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.clussmanproductions.trafficcontrol.ModTrafficControl;
import com.clussmanproductions.trafficcontrol.signs.Sign.TextLine;
import com.clussmanproductions.trafficcontrol.util.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SignRepository {
	
	private HashMap<Tuple<String, Integer>, Sign> signsByTypeVariant = new HashMap<>();
	private HashMap<UUID, Sign> signsByID = new HashMap<>();
	private HashMap<String, Sign> signsByType = new HashMap<>();
	private ArrayList<Sign> allSigns = new ArrayList<>();
	private HashMap<String, String> friendlyTypesByName = new HashMap<>();
	private HashMap<UUID, String> packNamesByID = new HashMap<>();
	
	private boolean signsInitialized = false;
	
	public void reload()
	{
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			for(Sign sign : allSigns)
			{
				deleteTexture(sign.getFrontImageResourceLocation());
				deleteTexture(sign.getBackImageResourceLocation());
			}
		}
		
		signsByTypeVariant.clear();
		signsByID.clear();
		signsByType.clear();
		allSigns.clear();
		friendlyTypesByName.clear();
		packNamesByID.clear();
		
		signsInitialized = false;
		init(str -> {}, steps -> {});
	}
	
	public void init(Consumer<String> splashUpdate, IntConsumer maximumUpdate)
	{
		if (signsInitialized)
		{
			return;
		}
		
		signsByTypeVariant = new HashMap<>();
		signsByID = new HashMap<>();
		signsByType = new HashMap<>();
		allSigns = new ArrayList<>();
		friendlyTypesByName = new HashMap<>();
		
		try
		{
			InputStream jsonStream;
			if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
			{
				jsonStream = getBaseJsonClient();
			}
			else
			{
				jsonStream = getBaseJsonServer();
			}
			
			InputStreamReader streamReader= new InputStreamReader(jsonStream);
			
			JsonElement parser = new JsonParser().parse(streamReader);
			jsonStream.close();
			
			processSignFile(parser.getAsJsonObject(), null, splashUpdate, maximumUpdate);
		}
		catch(Exception ex)
		{
			ModTrafficControl.logger.error("Could not process signpack base mod signpack." , ex);
		}
		
		File signpackDir = new File(Loader.instance().getConfigDir(), "..\\tc_signpacks");
		if (!signpackDir.exists())
		{
			try
			{
				signpackDir.mkdir();
			}
			catch(Exception ex)
			{
				ModTrafficControl.logger.error("Could not create signpack folder." , ex);
				signsInitialized = true;
				return;
			}
		}
		
		if (!signpackDir.isDirectory())
		{
			signsInitialized = true;
			return;
		}
		
		for(File file : signpackDir.listFiles())
		{
			if (!file.getName().endsWith(".zip"))
			{
				continue;
			}
			
			JsonObject signsFile = null;
			ZipFile zipFile = null;
			try
			{
				zipFile = new ZipFile(file);
			
				Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
				while(enumeration.hasMoreElements())
				{
					ZipEntry entry = enumeration.nextElement();
					if (entry.getName().toLowerCase().equals("signs.json"))
					{
						try
						{
							InputStream inputStream = zipFile.getInputStream(entry);
							JsonParser parser = new JsonParser();
							signsFile = parser.parse(new InputStreamReader(inputStream)).getAsJsonObject();
							inputStream.close();
							break;
						}
						catch(Exception ex)
						{
							ModTrafficControl.logger.error("Could not read or parse signs.json in signpack " + file.getName() , ex);
							continue;
						}
					}
				}
			
				if (signsFile == null)
				{
					continue;
				}
				
				processSignFile(signsFile, zipFile, splashUpdate, maximumUpdate);
			}
			catch(Exception ex)
			{
				ModTrafficControl.logger.error("Could not process signpack " + file.getName() + "." , ex);
				continue;
			}
			finally
			{
				if (zipFile != null)
				{
					try { zipFile.close(); } catch (Exception ex) {}
				}
			}
		}
		
		signsInitialized = true;
	}
	
	private void processSignFile(JsonObject signsFile, ZipFile zipFile, Consumer<String> splashUpdate, IntConsumer stepsUpdate) throws Exception
	{
		UUID packID;
		try
		{
			String packIDString = signsFile.get("pack_id").getAsString();
			packID = UUID.fromString(packIDString);
		}
		catch(Exception ex)
		{
			ModTrafficControl.logger.error("Could not parse pack_id string to a uuid. This pack will not be loaded.", ex);
			return;
		}
		
		String name;
		try
		{
			name = signsFile.get("name").getAsString();
		}
		catch(Exception ex)
		{
			ModTrafficControl.logger.error("Could not retrieve name for pack id " + packID.toString() + ". This pack will not be loaded.", ex);
			return;
		}
		
		if (packNamesByID.containsKey(packID))
		{
			ModTrafficControl.logger.error("Pack " + name + " has already been loaded! Do you have a duplicate pack installed?");
			return;
		}
		
		packNamesByID.put(packID, name);
		
		if (signsFile.has("types") && signsFile.get("types").isJsonObject())
		{
			JsonObject typesObject = signsFile.get("types").getAsJsonObject();
			for(Entry<String, JsonElement> typeEntry : typesObject.entrySet())
			{
				String key = typeEntry.getKey();
				String value = typeEntry.getValue().getAsString();
				
				friendlyTypesByName.put(key, value);
			}
		}
		
		JsonElement signsArrayObject = signsFile.get("signs");
		if (signsArrayObject == null || !signsArrayObject.isJsonArray())
		{
			return;
		}
		
		processSignsArray(signsArrayObject.getAsJsonArray(), packID, splashUpdate, stepsUpdate);
		
		HashSet<UUID> signsToRemove = new HashSet<>();
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			stepsUpdate.accept(signsByID.size());
			for(Sign sign : signsByID.values())
			{
				splashUpdate.accept("Loading texture for " + sign.getName());
				loadSignTexture(sign, zipFile, signsToRemove);
			}
		}
		
		
		for(UUID signToRemove : signsToRemove)
		{
			Sign sign = signsByID.get(signToRemove);
			signsByID.remove(signToRemove);
			signsByType.values().remove(sign);
			signsByTypeVariant.values().remove(sign);
			allSigns.remove(sign);
		}
	}
	
	private void processSignsArray(JsonArray signs, UUID packID, Consumer<String> splashUpdate, IntConsumer stepsUpdate)
	{
		stepsUpdate.accept(signs.size());
		
		for(JsonElement signElement : signs)
		{
			if (!signElement.isJsonObject())
			{
				continue;
			}
			
			JsonObject signObject = signElement.getAsJsonObject();
			Sign sign;
			try
			{
				sign = getSignFromObject(signObject, packID, splashUpdate);
			}
			catch(Exception ex)
			{
				ModTrafficControl.logger.error("A sign failed to load.", ex);
				continue;
			}
			
			if (sign.getVariant() >= 0)
			{
				signsByTypeVariant.put(new Tuple<String, Integer>(sign.getType(), sign.getVariant()), sign);
			}
			
			signsByID.put(sign.getID(), sign);
			signsByType.put(sign.getType(), sign);
			allSigns.add(sign);
		}
	}
	
	private Sign getSignFromObject(JsonObject signObject, UUID packID, Consumer<String> splashUpdate) throws Exception
	{
		if (!signObject.has("id") ||
			!signObject.has("name") ||
			!signObject.has("type") ||
			!signObject.has("front"))
		{
			splashUpdate.accept("");
			throw new IllegalArgumentException("Ensure every sign in the pack has an id, name, type, and front.");
		}
		
		String idString = signObject.get("id").getAsString();
		UUID id;
		try
		{
			id = UUID.fromString(idString);
		}
		catch(Exception ex)
		{
			splashUpdate.accept("");
			throw new IllegalArgumentException("A sign failed to load. Could not convert id '" + idString + "' to a uuid", ex);
		}
		
		String name = signObject.get("name").getAsString();
		splashUpdate.accept("Reading " + name);
		String type = signObject.get("type").getAsString();
		String frontTextureName = signObject.get("front").getAsString();
		ResourceLocation frontRL = new ResourceLocation("trafficcontrol", "textures/blocks/signs/" + packID.toString() + "/" + type + "/" + frontTextureName);
		
		String backTextureName = "back.png";
		if(signObject.has("back"))
		{
			backTextureName = signObject.get("back").getAsString();
		}
		ResourceLocation backRL = new ResourceLocation("trafficcontrol", "textures/blocks/signs/" + packID.toString() + "/" + type + "/" + backTextureName);
		
		int variant = -1;
		if (signObject.has("variant"))
		{
			String variantString = signObject.get("variant").getAsString();
			variant = Integer.parseInt(variantString);
		}
		
		String tooltip = null;
		if (signObject.has("tooltip"))
		{
			tooltip = signObject.get("tooltip").getAsString();
		}
		
		String note = null;
		if (signObject.has("note"))
		{
			note = signObject.get("note").getAsString();
		}
		
		boolean halfHeight = false;
		if (signObject.has("halfheight"))
		{
			halfHeight = signObject.get("halfheight").getAsBoolean();
		}
		
		ArrayList<TextLine> textLines = new ArrayList<>();
		if (signObject.has("textlines"))
		{
			JsonArray textlineArray = signObject.get("textlines").getAsJsonArray();
			for(JsonElement element : textlineArray)
			{
				JsonObject textLineObject = element.getAsJsonObject();
				
				try
				{
					textLines.add(getTextLine(textLineObject));
				}
				catch(Exception ex)
				{
					throw new Exception("Failed to parse text line for sign '" + id.toString() + "'", ex);
				}
			}
		}
		
		return new Sign(id, frontRL, backRL, name, variant, type, tooltip, note, halfHeight, textLines);
	}
	
	private TextLine getTextLine(JsonObject textLineObject) throws Exception
	{
		if (!textLineObject.has("label") ||
			!textLineObject.has("x") ||
			!textLineObject.has("y") ||
			!textLineObject.has("width") ||
			!textLineObject.has("color"))
		{
			throw new IllegalArgumentException("The following properties must be present in a text line: label, x, y, width, scale, maxlength, color");
		}
		
		String label = textLineObject.get("label").getAsString();
		double x = textLineObject.get("x").getAsDouble();
		double y = textLineObject.get("y").getAsDouble();
		double width = textLineObject.get("width").getAsDouble();
		int color = textLineObject.get("color").getAsInt();
		
		
		int maxLength = -1;
		if (textLineObject.has("maxlength"))
		{
			maxLength = textLineObject.get("maxlength").getAsInt();
		}
		
		double xScale = 1;
		if (textLineObject.has("xscale"))
		{
			xScale = textLineObject.get("xscale").getAsDouble();
		}
		
		double yScale = 1;
		if (textLineObject.has("yscale"))
		{
			yScale = textLineObject.get("yscale").getAsDouble();
		}
		
		SignHorizontalAlignment hAlign = SignHorizontalAlignment.Left;
		if (textLineObject.has("halign"))
		{
			String strHalign = textLineObject.get("halign").getAsString().toLowerCase();
			for(SignHorizontalAlignment align : SignHorizontalAlignment.values())
			{
				if (align.toString().toLowerCase().equals(strHalign))
				{
					hAlign = align;
					break;
				}
			}
		}
		
		SignVerticalAlignment vAlign = SignVerticalAlignment.Top;
		if (textLineObject.has("valign"))
		{
			String strValign = textLineObject.get("valign").getAsString().toLowerCase();
			for(SignVerticalAlignment align : SignVerticalAlignment.values())
			{
				if (align.toString().toLowerCase().equals(strValign))
				{
					vAlign = align;
					break;
				}
			}
		}
		
		return new TextLine(label, x, y, width, xScale, yScale, maxLength, color, hAlign, vAlign);
	}
	
	@SideOnly(Side.CLIENT)
	private void deleteTexture(ResourceLocation location)
	{
		Minecraft.getMinecraft().getTextureManager().deleteTexture(location);
	}
	
	@SideOnly(Side.CLIENT)
	private InputStream getBaseJsonClient() throws IOException
	{
		ResourceLocation signJsonRL = new ResourceLocation("trafficcontrol:misc/signs.json");
		return Minecraft.getMinecraft().getResourceManager().getResource(signJsonRL).getInputStream();
	}
	
	@SideOnly(Side.SERVER)
	private InputStream getBaseJsonServer()
	{
		return ModTrafficControl.class.getClassLoader().getResourceAsStream("assets/trafficcontrol/misc/signs.json");
	}
	
	@SideOnly(Side.CLIENT)
	private void loadSignTexture(Sign sign, ZipFile zipFile, HashSet<UUID> signsToRemove) throws Exception
	{
		ResourceLocation frontTexture = sign.getFrontImageResourceLocation();
		if (Minecraft.getMinecraft().getTextureManager().getTexture(frontTexture) == null)
		{
			String resourcePath = frontTexture.getResourcePath();
			int subStrIndex = StringUtils.ordinalIndexOf(resourcePath, "/", 4) + 1;
			String entryPath = resourcePath.substring(subStrIndex);
			if (zipFile != null) // load from zip file
			{
				ZipEntry entry = zipFile.getEntry(entryPath);
				if (entry == null)
				{
					ModTrafficControl.logger.error("Could not find image '" + entryPath + "' for sign '" + sign.getID().toString() + "'. It will not be available.");
					signsToRemove.add(sign.getID());
					return;
				}
				
				InputStream imageStream = zipFile.getInputStream(entry);
				BufferedImage bufferedImage = ImageIO.read(imageStream);
				DynamicTexture texture = new DynamicTexture(bufferedImage);
				Minecraft.getMinecraft().getTextureManager().loadTexture(frontTexture, texture);
			}
			else // tell minecraft to try to load from mod file directly. will log automatically
			{
				SimpleTexture simpleTexture = new SimpleTexture(frontTexture);
				Minecraft.getMinecraft().getTextureManager().loadTexture(frontTexture, simpleTexture);
			}
		}
		
		ResourceLocation backTexture = sign.getBackImageResourceLocation();
		if (Minecraft.getMinecraft().getTextureManager().getTexture(backTexture) == null)
		{
			String resourcePath = backTexture.getResourcePath();
			int subStrIndex = StringUtils.ordinalIndexOf(resourcePath, "/", 4) + 1;
			String entryPath = resourcePath.substring(subStrIndex);
			if (zipFile != null)
			{
				ZipEntry entry = zipFile.getEntry(entryPath);
				if (entry == null)
				{
					ModTrafficControl.logger.error("Could not find image '" + entryPath + "' for sign '" + sign.getID().toString() + "'. It will not be available.");
					signsToRemove.add(sign.getID());
					return;
				}
				
				InputStream imageStream = zipFile.getInputStream(entry);
				BufferedImage bufferedImage = ImageIO.read(imageStream);
				DynamicTexture texture = new DynamicTexture(bufferedImage);
				Minecraft.getMinecraft().getTextureManager().loadTexture(backTexture, texture);
			}
			else
			{
				SimpleTexture simpleTexture = new SimpleTexture(backTexture);
				Minecraft.getMinecraft().getTextureManager().loadTexture(backTexture, simpleTexture);
			}
		}
	}

	public Sign getSignByTypeVariant(String type, int variant)
	{
		return signsByTypeVariant.get(new Tuple<String, Integer>(type, variant));
	}
	
	public Sign getSignByID(UUID id)
	{
		return signsByID.get(id);
	}

	public String getFriendlyTypeName(String unlocalizedName)
	{
		return friendlyTypesByName.get(unlocalizedName);
	}
	
	public ImmutableMap<UUID, String> getPacksByID() { return ImmutableMap.copyOf(packNamesByID); } 
	
	public ImmutableList<Sign> getAllSigns()
	{
		return ImmutableList.copyOf(allSigns);
	}
}
