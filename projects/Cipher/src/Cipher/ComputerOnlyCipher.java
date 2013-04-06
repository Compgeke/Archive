package Cipher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class ComputerOnlyCipher extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JPasswordField pwf;
	private JTextArea leftA,rightA;
	public static void main(String args[]){
		ComputerOnlyCipher g = new ComputerOnlyCipher();
		g.init();
	}
	public ComputerOnlyCipher(){
		super("Cipher!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(exit);
		
		JMenu info = new JMenu("Info");
		JMenuItem about = new JMenuItem("About");
		JMenuItem help = new JMenuItem("Help");
		info.add(about);
		info.add(help);
		
		menuBar.add(file);
		menuBar.add(info);		
		setJMenuBar(menuBar);
		

		JPanel top = new JPanel(new FlowLayout(0,2,FlowLayout.LEFT));
		JLabel pw = new JLabel("Password:");
		pwf = new JPasswordField(8);
		pwf.setDocument(new FixedSizeDocument(12));
		pwf.setToolTipText("Not case Sensitive. Max 12 chars.");
		JButton encode = new JButton("Encode");
		JButton decode = new JButton("Decode");
		top.add(pw);
		top.add(pwf);
		top.add(encode);
		top.add(decode);
		
		JPanel center = new JPanel(new BorderLayout());
		
		JPanel left = new JPanel(new BorderLayout());
		JLabel encodeL = new JLabel("Encode:");
		leftA = new JTextArea(10,26);
		leftA.setLineWrap(true);
		leftA.setWrapStyleWord(true);
		JScrollPane jspL = new JScrollPane(leftA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspL.setAutoscrolls(true);
		left.add(encodeL, BorderLayout.NORTH);
		left.add(jspL, BorderLayout.CENTER);
		
		JPanel right = new JPanel(new BorderLayout());
		JLabel encodeR = new JLabel("Decode:");
		rightA = new JTextArea(10,26);
		rightA.setLineWrap(true);
		rightA.setWrapStyleWord(true);
		JScrollPane jspR = new JScrollPane(rightA, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jspR.setAutoscrolls(true);
		right.add(encodeR, BorderLayout.NORTH);
		right.add(jspR, BorderLayout.CENTER);
		
		
		center.add(left, BorderLayout.WEST);
		center.add(right, BorderLayout.EAST);
		
		add(center, BorderLayout.CENTER);
		add(top, BorderLayout.PAGE_START);
		
		engine c = new engine();
		exit.addActionListener(c);
		about.addActionListener(c);
		help.addActionListener(c);
		encode.addActionListener(c);
		decode.addActionListener(c);
	}
	public void init(){
		setVisible(true);
	}
	private class engine implements ActionListener{
		private char[] aPW;
		private char[] aMessage;
		private char[] eMessage;
		private int[] nPW;
		private int[] nMessage;
		public void actionPerformed(ActionEvent e){
			String temp = e.getActionCommand();
			if(temp.equals("Exit")){
				System.exit(0);
			}else if(temp.equals("Encode")){
				if(!leftA.getText().equals("")){
					rightA.setText(encode());
					leftA.setText("");
				}
			}else if(temp.equals("Decode")){
				if(!rightA.getText().equals("")){
					leftA.setText(decode());
					rightA.setText("");
				}
			}else if(temp.equals("About")){
				JOptionPane.showMessageDialog(ComputerOnlyCipher.this,
						"Created by Kevin Nash\nFor more information email me at kieve10@hotmail.com",
						"hows it going?", JOptionPane.PLAIN_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(ComputerOnlyCipher.this,
						"Enter a message on the left to have it encoded.\nEnter an ecoded message on the right to have it decoded.",
						"/FACEPALM", JOptionPane.PLAIN_MESSAGE);
			}
		}
		public void parse(int z){
			String temp = new String(pwf.getPassword());
			aPW = temp.toCharArray();
			nPW = new int[aPW.length];
			if(nPW.length == 0){
				aPW = new char[]{'d','e','f','a','u','l','t'};
				nPW = new int[aPW.length];
			}
			for(int i = 0; i < nPW.length; i++){
				nPW[i] = (int)aPW[i];
			}
			if(z == 0){
				aMessage = leftA.getText().toCharArray();
			}else{
				aMessage = rightA.getText().toCharArray();
			}
			nMessage = new int[aMessage.length];
			for(int i = 0; i < nMessage.length; i++){
				nMessage[i] = (int)aMessage[i];
			}
		}
		public String encode() {
			parse(0);
			eMessage = new char[nMessage.length];
			int mTally = 0;
			while(mTally < nMessage.length){
				for(int i = 0; i < nPW.length; i++){
					if(mTally == nMessage.length){
						break;
					}
					int temp = (nMessage[mTally] + nPW[i]);
					eMessage[mTally] = (char)(temp);
					mTally++;
				}
			}
			String convert = new String(eMessage);
			return convert;
		}
		public String decode(){
			parse(1);
			aMessage = new char[nMessage.length];
			int mTally = 0;
			while(mTally < nMessage.length){
				for(int i = 0; i < nPW.length; i++){
					if(mTally == nMessage.length){
						break;
					}
					int temp = (nMessage[mTally] - nPW[i]);
					aMessage[mTally] = (char)(temp);
					mTally++;
				}
			}
			String convert = new String(aMessage);
			return convert;
		}
	}
	private class FixedSizeDocument extends PlainDocument{
		private static final long serialVersionUID = 1L;
		private int max = 10;
		public FixedSizeDocument(int max){
			this.max = max;
		}
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
			// check string being inserted does not exceed max length
			if (getLength()+str.length()>max){
				// If it does, then truncate it
				str = str.substring(0, max - getLength());
			}
			super.insertString(offs, str, a);
		}
	}
}
