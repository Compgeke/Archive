package oscilloscope;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.FileInputStream;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

public class Oscilloscope extends Canvas implements PCMProcessor {
	private static final long serialVersionUID = 1L;
	
	private static final int SCALE = 100;
	private int width, height;
	private Point origin;
	private int[][][] frameData;
	private int r=0,g=0,b=0;
	
	private AudioFormat fmt;
	private DataLine.Info info;
	private SourceDataLine line;
	private Vector<LineListener> listeners = new Vector<LineListener>();
	private FLACDecoder flacDecoder;
	private int bytesPerSample,numChannels,numSamples = -1;
	
	public Oscilloscope(int WIDTH, int HEIGHT){
		width = WIDTH;
		height = HEIGHT;
		origin = new Point(width/2, height/2);
		
		try {
			FileInputStream is = new FileInputStream(getClass().getResource("/res/oscillofun.flac").getPath());
			//FileInputStream is = new FileInputStream(getClass().getResource("../res/1.flac").getPath());
			flacDecoder = new FLACDecoder(is);
			flacDecoder.addPCMProcessor(this);
			new Thread(new Runnable(){
				public void run(){
					try {
						flacDecoder.decode();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		createBufferStrategy(2);
	}
	
	public void update(int n){
		render((Graphics2D) getBufferStrategy().getDrawGraphics(),n);
		if(!getBufferStrategy().contentsLost()){
			getBufferStrategy().show();
		}
	}
	
	private void render(Graphics2D pen, int n){
		pen.setColor(Color.BLACK);
		pen.fillRect(0,0,width,height);
		pen.setColor(Color.GREEN.brighter());
		//r=0;g=0;b=0;
		int temp[][] = frameData[n];
		if(numSamples != -1)
		for(int i = 0; i < numSamples/4; i++){
			pen.setColor(new Color(r%255,g%255,b%255));
			pen.fillOval(-(int)f(temp[0][i]) + origin.x, (int)f(temp[1][i]) + origin.y, 4, 4);
			if(i % 4 == 0){
				//pen.fillOval(i+128, temp[1][i]/400 + origin.y - 300, 4, 4);
				//pen.fillOval(i+128, temp[0][i]/400 + origin.y + 300, 4, 4);
			}
			if(i%3==0)r++;
			if(i%4==0)g++;
			if(i%5==0)b++;
		}
		pen.dispose();
	}
	
	public double f(int x){
		double temp = x / SCALE;
		return temp;
	}
	
	public void processPCM(ByteData pcm) {
		byte[] data = pcm.getData();
		line.write(data, 0, pcm.getLen());
		int numBytes = pcm.getLen();
		numSamples = numBytes / (numChannels*bytesPerSample);
		int trackIndex = 0;
		frameData = new int[4][numChannels][numSamples];
		long start;
		long delta;
		for(int n = 0; n < 4; n++){
			start = System.nanoTime();
			for (int i=0; i < numSamples/4; i++) {
				for (int j=0; j < numChannels; j++) {
					frameData[n][j][i] = (data[trackIndex+1] << 8) + data[trackIndex];
					trackIndex += 2;
				}
			}
			update(n);
			delta = System.nanoTime() - start;
			if(delta < 25000000){
				try {
					Thread.sleep((25000000-delta)/1000000);
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void processStreamInfo(StreamInfo streamInfo) {
		try
	    {
	      fmt = streamInfo.getAudioFormat();
	      info = new DataLine.Info(SourceDataLine.class, fmt, -1);
	      line = ((SourceDataLine)AudioSystem.getLine(info));
	      
	      bytesPerSample = streamInfo.getBitsPerSample()/8;
	      numChannels = streamInfo.getChannels();
	      
	      System.out.println(line.getFormat());
	      
	      int size = listeners.size();
	      for (int index = 0; index < size; index++) {
	    	  line.addLineListener((LineListener)listeners.get(index));
	      }
	      line.open(fmt, -1);
	      line.start();
	    } catch (LineUnavailableException e) {
	    	e.printStackTrace();
	    }
	}
}
