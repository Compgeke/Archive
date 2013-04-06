import java.awt.Graphics;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;

@Manifest(authors = "Kieve", version = 1.0, name = "", keywords={}, description = "")
public class Template extends ActiveScript implements MessageListener, PaintListener{	
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