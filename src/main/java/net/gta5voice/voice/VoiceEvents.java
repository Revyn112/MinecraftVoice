package net.gta5voice.voice;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

public class VoiceEvents {
	@SubscribeEvent
	public void disconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        VoiceMod.Url = "";
    }
}