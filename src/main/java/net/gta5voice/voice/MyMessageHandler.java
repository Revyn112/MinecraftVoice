package net.gta5voice.voice;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;

public class MyMessageHandler implements IMessageHandler<MyMessage, IMessage> {
	@Override public IMessage onMessage(MyMessage message, MessageContext ctx) {
		String jsonString = message.toSend;
		Runnable r = new VoiceHandlerThread(jsonString);
		new Thread(r).start();
			
		return null;
	}
}
