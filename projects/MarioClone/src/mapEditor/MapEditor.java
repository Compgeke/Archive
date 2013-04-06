package mapEditor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import marioClone.Engine;

public class MapEditor extends JFrame{
	/**TODO
	 * Sure the whole writing objects to files was a great learning experience
	 * but it is slow, saving maps as the format usable with the game (CSV) will work
	 * just fine. Therefore: Convert this save and load shit to CSV format.
	 */
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 700;//640
	private static final int HEIGHT = 390;//360
	private Stack<WorkPanel> wp;
	private PalletPanel pp;
	private JMenuBar menu;
	private EditorListener l;
	private int currentPage,totalPages;
	private JPanel panels;
	private JLabel page;
	private JProgressBar progress;
	private JButton next;
	private boolean dragging,leftPressed;
	private JFileChooser mapChooser;
	private File directory;
	
	public static void main(String args[]){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			 public void run() {
	        		MapEditor me = new MapEditor();
	        		me.start();
	            }
	         });
	}

	public MapEditor(){
		//create the frame and set its properties
		super("WanaB Mario Map Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH,HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new FlowLayout(0,0,0));
		
		menu = new JMenuBar();
		setJMenuBar(menu);
		
		mapChooser = new JFileChooser();
		mapChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		directory = new File(System.getProperty("user.home") + "/Desktop/MarioMaps/map.csv");
		mapChooser.setSelectedFile(directory);
	}
	
	public void start(){
		l = new EditorListener();
		
		JMenu file = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem exit = new JMenuItem("Exit");
		file.add(save);
		file.add(load);
		file.add(exit);
		
		JMenu edit = new JMenu("Edit");
		JMenuItem addPage = new JMenuItem("Add New Page");
		JMenuItem export = new JMenuItem("Export");
		edit.add(addPage);
		edit.add(export);
		
		next = new JButton("Next");
		next.addActionListener(l);
		
		JButton prev = new JButton("Prev");
		prev.addActionListener(l);
		
		page = new JLabel();
		
		progress = new JProgressBar(0, 100);
		progress.setVisible(false);
		
		menu.add(file);
		menu.add(edit);
		menu.add(next);
		menu.add(prev);
		menu.add(page);
		menu.add(progress);
		
		save.addActionListener(l);
		load.addActionListener(l);
		export.addActionListener(l);
		exit.addActionListener(l);
		addPage.addActionListener(l);
		
		panels = new JPanel();
		panels.setPreferredSize(new Dimension(640,360));
		
		wp = new Stack<WorkPanel>();
		wp.push(new WorkPanel(l));		
		pp = new PalletPanel(l);
		
		wp.firstElement().setVisible(true);
		
		addMouseWheelListener(l);
		panels.add(wp.firstElement());
		add(panels);
		add(pp);
		pack();
		setVisible(true);
		
		currentPage = 0;
		totalPages = 1;
		
		l.updatePage();
	}
	
	///////LISTENER///////////
	protected class EditorListener extends MouseAdapter implements ActionListener, PropertyChangeListener{
		private static final long serialVersionUID = 1L;
		private BufferedImage tiles[];
		private int wheelTurned = 0;
		private SaveTask st;
		private boolean saving;
		
		public EditorListener(){
			tiles = loadImages();
		}
		
		public BufferedImage[] getTiles(){
			return tiles;
		}
		
		public void mouseWheelMoved(MouseWheelEvent ev){
			if((pp.getCurrentChoice() != 0 || ev.getWheelRotation() > 0)
					&& (pp.getCurrentChoice() != 4 || ev.getWheelRotation() < 0)
					&& wheelTurned % 2 == 0){
				pp.setCurrentChoice(pp.getCurrentChoice() + ev.getWheelRotation());
			}
			wheelTurned ++;
		}
		
		public void mouseClicked(MouseEvent ev){
			if(ev.getButton() == MouseEvent.BUTTON3){
				MapSlot temp = (MapSlot) ev.getSource();
				temp.setIcon(null);
				temp.setState(-1);
			}
		}
		
		public void mouseReleased(MouseEvent ev){
			dragging = false;
			leftPressed = false;
		}
		
		public void mousePressed(MouseEvent ev){
			if(ev.getButton() == MouseEvent.BUTTON1){
				leftPressed = true;
			}else{
				leftPressed = false;
			}
		}
		
		public void mouseDragged(MouseEvent ev){
			if(leftPressed){
				MapSlot temp = (MapSlot) ev.getSource();
				temp.doClick();
				dragging = true;
			}
		}
		
		public void mouseEntered(MouseEvent ev){
			if(dragging && leftPressed){
				MapSlot temp = (MapSlot) ev.getSource();
				temp.doClick();
			}
		}
		
		public void actionPerformed(ActionEvent ev) {
			if(!saving){
				if(ev.getActionCommand().equals("changeIcon")){
					MapSlot temp = (MapSlot) ev.getSource();
					pp.setCurrentChoice(temp.getState());
				}else if(ev.getActionCommand().equals("updateIcon")){
					MapSlot temp = (MapSlot) ev.getSource();
					temp.setIcon(new ImageIcon(tiles[temp.getState()]));
				}else if(ev.getActionCommand().equals("Save")){
					saving = true;
					st = new SaveTask();
					st.addPropertyChangeListener(this);
					st.execute();
				}else if(ev.getActionCommand().equals("Load")){
					try {
						load();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(ev.getActionCommand().equals("Exit")){
					System.exit(0);
				}else if(ev.getActionCommand().equals("Export")){
					try {
						exportMap();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else if(ev.getActionCommand().equals("Next")){
					if(currentPage != wp.size()-1){
						wp.get(currentPage).setVisible(false);
						currentPage++;
						wp.get(currentPage).setVisible(true);
						updatePage();
					}
				}else if(ev.getActionCommand().equals("Prev")){
					if(currentPage != 0){
						wp.get(currentPage).setVisible(false);
						currentPage--;
						wp.get(currentPage).setVisible(true);
						updatePage();
					}
				}else if(ev.getActionCommand().equals("Add New Page")){
					wp.push(new WorkPanel(l));
					panels.add(wp.peek());
					totalPages++;
					updatePage();
				}else{
					MapSlot temp = (MapSlot) ev.getSource();
					temp.setIcon(new ImageIcon(tiles[pp.getCurrentChoice()]));
					temp.setState(pp.getCurrentChoice());
				}
			}
		}
		
		public void updatePage(){
			totalPages = wp.size();
			page.setText(" Page " + (currentPage + 1) + "/" + totalPages);
			page.invalidate();
		}
		
		public void updateIcons(){
			for(int i = 0; i < wp.size(); i++){
				MapSlot temp[][] = wp.get(i).placementTiles;
				for(int row = 0; row < temp.length; row++){
					for(int col = 0; col < temp[row].length; col++){
						if(temp[row][col].getState() != -1){
							temp[row][col].setIcon(new ImageIcon(tiles[temp[row][col].getState()]));
						}
					}
				}
			}
		}
		
		public BufferedImage[] loadImages(){
			Image temp = new ImageIcon(getClass().getResource("/Images/terrain.png")).getImage();
			BufferedImage tileSet = Engine.toBufferedImage(temp);
			BufferedImage[] tiles = new BufferedImage[5];
			tiles[0] = tileSet.getSubimage(3 * 16, 0, 16, 16);//dirt /w grass
			tiles[1] = tileSet.getSubimage(0, 16, 16, 16);//stone
			tiles[2] = tileSet.getSubimage(2 * 16, 0, 16, 16);//dirt
			tiles[3] = tileSet.getSubimage(4 * 16, 4 * 16, 16, 16);//dirt /w snow
			tiles[4] = tileSet.getSubimage(11 * 16, 5 * 16, 16, 16);//no collision grass
			
			//scale images
			for(int i = 0; i < tiles.length; i++){
				tiles[i] = Engine.toBufferedImage(tiles[i].getScaledInstance(32, 32, Image.SCALE_FAST));
			}			
			return tiles;
		}		
		
		@SuppressWarnings("unchecked")
		public void load() throws IOException, ClassNotFoundException{
			int returnValue = mapChooser.showOpenDialog(null);
			File f;
			if (returnValue == JFileChooser.APPROVE_OPTION) {
                f = mapChooser.getSelectedFile();
                if(f.isFile()){
                	FileInputStream fIn = new FileInputStream(f.getAbsolutePath());
        			ObjectInputStream oIn = new ObjectInputStream(fIn);
        			
        			for(int i = 0; i < wp.size(); i++){
        				panels.remove(wp.get(i));
        			}
        			
        			wp = (Stack<WorkPanel>) oIn.readObject();
        			
        			for(int i = 0; i < wp.size(); i++){
        				if(i == 0){
        					wp.get(i).setVisible(true);
        					currentPage = 0;
        				}else{
        					wp.get(i).setVisible(false);
        				}
        				wp.get(i).enable();
        				wp.get(i).addListener(l);
        				panels.add(wp.get(i));
        			}
        			updatePage();
        			updateIcons();
        			JOptionPane.showMessageDialog(null, "Loaded", "Loaded!", JOptionPane.PLAIN_MESSAGE);
                }else{
                	JOptionPane.showMessageDialog(null,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
                }
            } else {
            	JOptionPane.showMessageDialog(null,"Load cancelled by user.","Load cancelled!",JOptionPane.PLAIN_MESSAGE);
            }			
		}
		
		public void exportMap() throws IOException{
			String[] outputRows = new String[11];
			int rows = 11;
			int cols = 20;
			for(int i = 0; i < 11; i++){
				outputRows[i] = "";
			}
			
			for(int q = 0; q < wp.size(); q ++){
				int[][] mapIntStates = wp.get(q).getStates();
				
				Queue<Character> queueCharStates;
				
				for(int i = 0; i < rows; i ++){
					queueCharStates = new LinkedList<Character>();
					for(int x = 0; x < cols; x++){
						int tempInt = mapIntStates[i][x];
						if(tempInt == 0){
							queueCharStates.offer('G');
						}else if(tempInt == 1){
							queueCharStates.offer('S');
						}else if(tempInt == 2){
							queueCharStates.offer('D');
						}else if(tempInt == 3){
							queueCharStates.offer('C');
						}else if(tempInt == 4){
							queueCharStates.offer('T');
						}else{
							queueCharStates.offer('0');
						}
						if(q != wp.size()-1 || x != cols-1){
							queueCharStates.offer(',');
						}
					}
					
					while(!queueCharStates.isEmpty()){
						outputRows[i] += queueCharStates.poll();
					}
				}
			}
			int returnValue = mapChooser.showSaveDialog(null);
			File f;
			if (returnValue == JFileChooser.APPROVE_OPTION) {
                f = mapChooser.getSelectedFile();
                if(!f.exists()){
                	f.createNewFile();
                }
                if(f.isFile()){
                	BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        			bw.write("back.png");
        			bw.newLine();
        			bw.flush();
        			for(int i = 0; i < rows; i++){
        				bw.write(outputRows[i]);
        				if(i != rows-1){
        					bw.newLine();
        				}
        				bw.flush();
        			}
        			
        			bw.close();
        			f = null;
        			
        			JOptionPane.showMessageDialog(null, "Your map has been saved to your desktop", "Saved", JOptionPane.PLAIN_MESSAGE);
                }else{
                	JOptionPane.showMessageDialog(null,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
                }
            } else {
            	JOptionPane.showMessageDialog(null,"Save cancelled by user.","Load cancelled!",JOptionPane.PLAIN_MESSAGE);
            }			
			
		}
		
		public void propertyChange(PropertyChangeEvent evt) {
	        if ("progress" == evt.getPropertyName()) {
	            int progressValue = (Integer) evt.getNewValue();
	            progress.setValue(progressValue);
	        } 
	    }
		
		class SaveTask  extends SwingWorker<Void, Void> {
	        @Override
	        public Void doInBackground() {
	        	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	        	progress.setVisible(true);
	        	progress.setIndeterminate(true);
	        	try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	return null;
	        }
	        
	        public void save() throws IOException{
	        	int returnValue = mapChooser.showSaveDialog(null);
				File f;
				if (returnValue == JFileChooser.APPROVE_OPTION) {
	                f = mapChooser.getSelectedFile();
	                if(!f.exists()){
	                	f.createNewFile();
	                }
	                if(f.isFile()){
	    				FileOutputStream fOut = new FileOutputStream(f.getAbsolutePath());
	    				ObjectOutputStream oOut = new ObjectOutputStream(fOut);
	    				for(int i = 0; i < wp.size(); i++){
	    					wp.get(i).removeListener(l);
	    					wp.get(i).disable();
	    				}
	    				oOut.writeObject(wp);
	    				for(int i = 0; i < wp.size(); i++){
	    					wp.get(i).enable();
	    				}
	                }else{
	                	JOptionPane.showMessageDialog(null,"Please select a file, not a directory.","OOPS!",JOptionPane.PLAIN_MESSAGE);
	                }
	            } else {
	            	JOptionPane.showMessageDialog(null,"Save cancelled by user.","Load cancelled!",JOptionPane.PLAIN_MESSAGE);
	            }			
			}
	        
	        @Override
	        public void done() {
	            setCursor(null); //turn off the wait cursor
	            progress.setIndeterminate(false);
	            progress.setValue(100);
	            JOptionPane.showMessageDialog(null, "Saved to Desktop", "Saved!", JOptionPane.PLAIN_MESSAGE);
	            progress.setVisible(false);
	            saving = false;
	        }
	    }
		
	}
}
