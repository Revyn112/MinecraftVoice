package net.gta5voice.voice;

import org.apache.commons.io.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class MyMessage implements IMessage {
	public MyMessage(){}
	
	public String toSend;
	public MyMessage(String toSend) {
		this.toSend = toSend;
	}
	
	@Override public void toBytes(ByteBuf buf) {
		buf.writeBytes(toSend.getBytes());
	}
	
	@Override public void fromBytes(ByteBuf buf) {
		toSend = buf.toString(Charsets.UTF_8);
	}
}
