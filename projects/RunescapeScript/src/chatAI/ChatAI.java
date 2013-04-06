package chatAI;

import java.awt.Graphics;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;

@Manifest(authors = "Kieve", name = "ChatAI", keywords={"AI", "Chat", "Tools", "Unreleased"}, description = "Responds to chat.")
public class ChatAI extends ActiveScript implements MessageListener, PaintListener{	
	public boolean onStart(){
		return true; 
	}
	@Override
	public int loop(){		
		return 0; 
	}
	@Override
	public void messageReceived(MessageEvent e){		
	}
	@Override
	public void onRepaint(Graphics g){		
	}
}