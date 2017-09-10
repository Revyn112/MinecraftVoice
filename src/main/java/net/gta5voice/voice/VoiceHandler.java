package net.gta5voice.voice;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class VoiceHandler {
	private Map<String, Double> getLocalPosition(String playerJson, String localName)
	{
		Map<String, Double> position = new HashMap<String, Double>();
		
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(playerJson);
			JSONArray array = (JSONArray)obj;
			
			for (int i = 0; i < array.size(); i++)
			{
				JSONObject jobj = (JSONObject)array.get(i);
				String name = (String) jobj.get("name");
				if (name.equals(localName))
				{
					Double playerX = (Double) jobj.get("x");
					Double playerY = (Double) jobj.get("y");
					Double playerZ = (Double) jobj.get("z");
					Double rotation = (Double) jobj.get("rotation");

					position.put("x", playerX);
					position.put("y", playerY);
					position.put("z", playerZ);
					position.put("rotation", rotation);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return position;
	}
	
	public void handleTeamspeak(String playerJson)
	{
		EntityPlayerSP localPlayer = Minecraft.getMinecraft().thePlayer;
		String localPlayerName = localPlayer.getDisplayName();
		Map<String, Double> position = this.getLocalPosition(playerJson, localPlayerName);
		double localX = position.get("x");
		double localY = position.get("y");
		double localZ = position.get("z");
		double localPlayerRotation = position.get("rotation");
		double rotation = Math.PI / 180  * (localPlayerRotation * -1);
		int localDimension = localPlayer.dimension;
		
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(playerJson);
			JSONArray array = (JSONArray)obj;
			List<String> playerNames = new ArrayList<String>();
			for (int i = 0; i < array.size(); i++)
			{
				JSONObject jobj = (JSONObject)array.get(i);
				String name = (String) jobj.get("name");
				if (name.equals(localPlayerName))
				{
					continue;
				}
				
				Double playerX = (Double) jobj.get("x");
				Double playerY = (Double) jobj.get("y");
				Double playerZ = (Double) jobj.get("z");
				Long dimension = (Long) jobj.get("dimension");
				Boolean isDead = (Boolean) jobj.get("isDead");
				
				double distance = Math.sqrt(Math.pow(localX - playerX, 2) + Math.pow(localY - playerY, 2) + Math.pow(localZ - playerZ, 2));
				double range = 25;
				double volumeModifier = 0;
				
				if (distance > 5)
				{
					volumeModifier = (distance * -5 / 10);
				}
				if (volumeModifier > 0)
				{
					volumeModifier = 0;
				}
				if (dimension == localDimension && distance < range)
				{
					double subX = playerX - localX;
					double subY = playerY - localY;
					
					double x = subX * Math.cos(rotation) - subY * Math.sin(rotation);
					double y = subX * Math.sin(rotation) - subY * Math.cos(rotation);
					
					x = x * 10 / range;
					y = y * 10 / range;
					
					if (!isDead)
					{
						playerNames.add(name + "~" + (Math.round(x * 1000) / 1000) + "~" + (Math.round(y * 1000) / 1000) + "~0~" + (Math.round(volumeModifier * 1000) / 1000));
					}
				}
			}
			
			this.makeWebRequest("http://localhost:15555/players/" + localPlayerName + "/" + String.join(";", playerNames) + "/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void makeWebRequest(String url) {
		System.out.println(url);
		URL connUrl;
		try {
			connUrl = new URL(url);
	 		HttpURLConnection con = (HttpURLConnection) connUrl.openConnection();
	 		con.setRequestMethod("GET");
	 		con.getResponseCode();
		} catch (Exception e) {
		}
	}
}
