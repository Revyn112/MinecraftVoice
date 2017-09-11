package net.gta5voice.voice;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = VoiceMod.MOD_ID)
public class VoiceMod {
	public static final String MOD_ID = "voicemod";
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("voicemod");
	public static String Url = "";
	public static Map<UUID, Double> playersRange = new HashMap<UUID, Double>();
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event){
		
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event){
    	if (FMLCommonHandler.instance().getSide() == Side.SERVER)
    	{
    		INSTANCE.registerMessage(MyMessageHandler.class, MyMessage.class, 0, Side.SERVER);
    		
    		Thread thread = new Thread(){
    		    public void run(){
    		    	while (true)
    		    	{
    		    		try {
	    		    		JSONArray arr = new JSONArray();
	    			    	List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
	    			    	for (EntityPlayerMP player : players)
	    			    	{
	    			    		JSONObject obj = new JSONObject();
	    			    		obj.put("name", player.getDisplayName());
	    			    		obj.put("x", Math.round(player.posX * 1000) / 1000.0);
	    			    		obj.put("y", Math.round(player.posY * 1000) / 1000.0);
	    			    		obj.put("z", Math.round(player.posZ * 1000) / 1000.0);
	    			    		obj.put("rotation", Math.round(player.rotationYawHead * 1000) / 1000.0);
	    			    		obj.put("dimension", player.dimension);
	    			    		obj.put("isDead", player.isDead);
	    			    		UUID id = player.getPersistentID();
	    			    		if (!playersRange.containsKey(id))
	    			    		{
	    			    			playersRange.put(id, 5.0);
	    			    		}
	    			    		obj.put("range", playersRange.get(id));
	    			    		arr.add(obj);
	    			    	}
	    			    	StringWriter out = new StringWriter();
	    					arr.writeJSONString(out);
	    			        
	    			        String jsonText = out.toString();
	    			        //System.out.println(jsonText);
	    			    	INSTANCE.sendToAll(new MyMessage(jsonText));
							Thread.sleep(500);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
    		    	}
    		    }
    		  };

    		  thread.start();
    	}
    	else
    	{
    		VoiceEvents handler = new VoiceEvents();
    		MinecraftForge.EVENT_BUS.register(handler);
    		FMLCommonHandler.instance().bus().register(handler);
    		INSTANCE.registerMessage(MyMessageHandler.class, MyMessage.class, 0, Side.CLIENT);
			 
			 Thread thread = new Thread(){
    		    public void run(){
    		    	while (true)
    		    	{
    		    		try {
    		    			if (VoiceMod.Url != "")
    				    	{
    				    		makeWebRequest(VoiceMod.Url);
    				    	}
							Thread.sleep(500);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						}
    		    	}
    		    }
    		  };

    		  thread.start();
    	}
    }
    
	public void makeWebRequest(String url) {
		URL connUrl;
		try {
			connUrl = new URL(url);
	 		HttpURLConnection con = (HttpURLConnection) connUrl.openConnection();
	 		con.setRequestMethod("GET");
	 		con.getContent();
	 		con.disconnect();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
            
    }
}
