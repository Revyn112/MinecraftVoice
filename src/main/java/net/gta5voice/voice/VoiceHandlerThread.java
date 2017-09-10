package net.gta5voice.voice;

public class VoiceHandlerThread implements Runnable {

	private String jsonString;
	
	public VoiceHandlerThread(String parameter) {
    	this.jsonString = parameter;
	}
	
	@Override
	public void run() {
		new VoiceHandler().handleTeamspeak(this.jsonString);
	}

}
