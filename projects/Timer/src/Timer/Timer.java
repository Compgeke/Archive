package Timer;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.*;

public class Timer {
	public static void main(String args[]) {
		Timer t = new Timer();
		t.init();
	}
	private JFrame jf;
	private JTextField time,min,max,avg;
	private JTextArea times;
	private double curMin, curMax, curTotal, curEntries, curAvg;
	private JFileChooser fc;
	
	public void init() {
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(140,473);		
		jf.setLocationRelativeTo(null);
		jf.setResizable(false);
		
		
		JLabel timel,minl,maxl,avgl;
		
		times = new JTextArea(20,11);
		times.setEditable(false);
		times.setFocusable(false);
		JScrollPane jsp = new JScrollPane(times,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setAutoscrolls(true);
		jsp.setFocusable(false);
		
		timel = new JLabel("Time:          ");
		timel.setFocusable(false);
		minl = new JLabel("Min:  ");
		minl.setFocusable(false);
		maxl = new JLabel("Max:  ");
		maxl.setFocusable(false);
		avgl = new JLabel("Avg:  ");
		avgl.setFocusable(false);
		
		time = new JTextField(5);
		time.setEditable(false);
		time.setFocusable(false);
		time.setHorizontalAlignment(SwingConstants.TRAILING);
		min = new JTextField(5);
		min.setEditable(false);
		min.setFocusable(false);
		min.setHorizontalAlignment(SwingConstants.TRAILING);
		max = new JTextField(5);
		max.setEditable(false);
		max.setFocusable(false);
		max.setHorizontalAlignment(SwingConstants.TRAILING);
		avg = new JTextField(5);
		avg.setEditable(false);
		avg.setFocusable(false);
		avg.setHorizontalAlignment(SwingConstants.TRAILING);
		
		JPanel info = new JPanel();
		info.setSize(20,20);
		info.setLayout(new GridLayout(4,2,0,0));
		
		info.add(timel);
		info.add(time);
		info.add(minl);
		info.add(min);
		info.add(maxl);
		info.add(max);
		info.add(avgl);
		info.add(avg);
		
		jf.setLayout(new FlowLayout());
		jf.add(info);
		jf.add(jsp);
		
		
		Listener l = new Listener();
		info.setFocusable(true);
		info.addKeyListener(l);
		
		JButton save = new JButton("Save");
		save.setFocusable(false);
		save.addActionListener(l);
		JButton load = new JButton("Load");
		load.setFocusable(false);
		load.addActionListener(l);
		
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		jf.add(save);
		jf.add(load);		
		
		jf.setFocusable(false);
		jf.setVisible(true);
	}
	private class Listener implements ActionListener,KeyListener,Runnable {
		private boolean started;
		private double totalTime;
		public Listener(){
			curMin = 100000;
			curMax = 0;
			curTotal = 0;
			curEntries = 0;
			curAvg = 0;
		}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			}else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				if(started){
					started = false;
					stop();
				}else if(!started) {
					started = true;
					start();
				}
			}
		}
		public void stop(){
			checkRecords(totalTime);
			times.append(time.getText() + "\n");
		}
		public void start(){
			totalTime = 0;
			Thread t = new Thread(this);
			t.start();
		}
		public void checkRecords(double t){
			curTotal += t;
			curEntries +=1;
			curAvg = Math.round(curTotal/curEntries);
			avg.setText("" + curAvg/100);
			if(t < curMin){
				curMin = t;
				min.setText("" + t/100);
			}
			if(t > curMax){
				curMax = t;
				max.setText("" + t/100);
			}
		}
		public void run() {
			while(started){
				totalTime+=1;
				time.setText("" + totalTime/100);
				try{
					Thread.sleep(10);
				}catch(Exception ex){
				}
			}
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Save")) {
				if(!started){
					int returnVal = fc.showSaveDialog(jf);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File f = fc.getSelectedFile();
	                	if(!f.exists())
							try {
								f.createNewFile();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
		                if(f.isFile()){
			                try {
								write(f);
							} catch (IOException e1) {
							}
		                }else{
		                	JOptionPane.showMessageDialog(jf,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
		                }
		            } else {
		            	JOptionPane.showMessageDialog(jf,"Save cancelled by user.","Save cancelled!",JOptionPane.PLAIN_MESSAGE);
		            }
				}
			}else{
				if(!started){
					int returnVal = fc.showOpenDialog(jf);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                File f = fc.getSelectedFile();
		                if(f.isFile()){
			                try {
								read(f);
							} catch (IOException e1) {
							}
		                }else{
		                	JOptionPane.showMessageDialog(jf,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
		                }
		            } else {
		            	JOptionPane.showMessageDialog(jf,"Load cancelled by user.","Load cancelled!",JOptionPane.PLAIN_MESSAGE);
		            }
				}
			}
		}
		public void write(File f) throws IOException{
			try{
				BufferedReader br = new BufferedReader(new StringReader(times.getText()));
				BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
				
				bw.write("" + curMin);
				bw.newLine();
				bw.write("" + curMax);
				bw.newLine();
				bw.write("" + curAvg);
				bw.newLine();
				String temp = br.readLine();
				while(temp != null){
					bw.write(temp);
					bw.newLine();
					temp = br.readLine();
				}
				bw.flush();
				br.close();
				bw.close();
			}finally{
				JOptionPane.showMessageDialog(jf, "Saved successfully.","Success!",JOptionPane.PLAIN_MESSAGE);
			}
		}
		public void read(File f) throws IOException{
			try{
				times.setText("");
				BufferedReader br = new BufferedReader(new FileReader(f.getAbsolutePath()));
				curMin = Double.parseDouble(br.readLine());
				curMax = Double.parseDouble(br.readLine());
				curAvg = Double.parseDouble(br.readLine());
				if(curMin != 100000.0){
					min.setText("" + curMin/100);
				}else{
					min.setText("");
				}
				if(curMax != 0.0){
					max.setText("" + curMax/100);
				}else{
					max.setText("");
				}
				if(curAvg != 0.0){
					avg.setText("" + curAvg/100);
				}else{
					avg.setText("");
				}
				String temp = br.readLine();
				while(temp != null){
					times.append(temp + "\n");
					temp = br.readLine();
				}
				br.close();
			}finally{
				JOptionPane.showMessageDialog(jf, "Loaded successfully.","Success!",JOptionPane.PLAIN_MESSAGE);
			}
		}
		public void keyReleased(KeyEvent e) {
		}
		public void keyTyped(KeyEvent arg0) {
		}
	}
}
