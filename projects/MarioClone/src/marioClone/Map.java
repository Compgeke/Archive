/*
 * Created by Kevin Nash
 * Project: Mario Clone
 * Class: Map
 * Description: determines the location of objects on the map
 * 		generates map based on .CSV file.
 */

package marioClone;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Map {
	//////
	private BufferedImage tiles[];
	/*
	 *  0 - (G)ground (dirt with grass)
	 *  1 - (S)stone
	 *  2 - (D)dirt
	 *  3 - (C)dirt /w snow
	 *  4 - (T)Grass no collision
	 */
	private Platform platforms[];
	private Image background;
	private BufferedImage tileSet;
	private int mapSize; //1 = 20 32x32 blocks = a single 640 pixel width map
	private String mapPath;
	private Stack<String[]> map;
	private boolean isCustomMap;
	
	//if no map file was specified use default
	public Map() throws IOException{
		map = new Stack<String[]>();
		loadImages();
		buildDefault();		
	}
	
	//else build the map given
	public Map(String path, boolean isCustomMap) throws IOException{
		this.isCustomMap = isCustomMap;
		map = new Stack<String[]>();
		loadImages();
		mapPath = path;
		getFromFile();
	}
	
	public void buildDefault(){
		//create background
		background = new ImageIcon(this.getClass().getResource("../images/back.png")).getImage();
		
		//state map size
		mapSize = 3;
		
		//hard coded csv map format
		loadDefaultMap();
		buildMap();
	}
	
	//return map size
	public int getMapSize(){
		return mapSize;
	}
	
	//return the background image
	public Image getBackground(){
		return background;
	}
	
	//return the platforms
	public Platform[] getPlatforms(){
		return platforms;
	}
	
	//load and scale tile images
	public void loadImages() throws IOException{
		Image temp = ImageIO.read(getClass().getResourceAsStream("/Images/terrain.png"));
		tileSet = Engine.toBufferedImage(temp);
		tiles = new BufferedImage[5];
		tiles[0] = tileSet.getSubimage(3 * 16, 0, 16, 16);//dirt /w grass
		tiles[1] = tileSet.getSubimage(0, 16, 16, 16);//stone
		tiles[2] = tileSet.getSubimage(2 * 16, 0, 16, 16);//dirt
		tiles[3] = tileSet.getSubimage(4 * 16, 4 * 16, 16, 16);//dirt /w snow
		tiles[4] = tileSet.getSubimage(11 * 16, 5 * 16, 16, 16);//no collision grass
		
		//scale images
		for(int i = 0; i < tiles.length; i++){
			tiles[i] = Engine.toBufferedImage(tiles[i].getScaledInstance(32, 32, Image.SCALE_FAST));
		}
	}
	
	//build map
	public void buildMap(){
		mapSize = (map.lastElement().length-1)/20;
		int row = 1;
		Stack<Platform> tempPlatformCollection = new Stack<Platform>();
		Platform tempPlatform = null;
		while(!map.isEmpty()){
			String temp2[] = map.pop();
			for(int i = 0; i < temp2.length; i++){
				if(!temp2[i].equalsIgnoreCase("0")){
					if(temp2[i].equalsIgnoreCase("G")){
						tempPlatform = new Platform(tiles[0]);
						tempPlatform.setCollisionLevel(2);
					}else if(temp2[i].equalsIgnoreCase("S")){
						tempPlatform = new Platform(tiles[1]);
						tempPlatform.setCollisionLevel(2);
					}else if(temp2[i].equalsIgnoreCase("D")){
						tempPlatform = new Platform(tiles[2]);
						tempPlatform.setCollisionLevel(2);
					}else if(temp2[i].equalsIgnoreCase("C")){
						tempPlatform = new Platform(tiles[3]);
						tempPlatform.setCollisionLevel(2);
					}else if(temp2[i].equalsIgnoreCase("T")){
						tempPlatform = new Platform(tiles[4]);
						tempPlatform.setCollisionLevel(0);
					}	
					tempPlatform.setX(i*32);
					tempPlatform.setY(WindowManager.HEIGHT - 32*row);
					tempPlatformCollection.push(tempPlatform);					
				}
			}
			row++;
		}
		//convert temporary platform stack into an array;
		platforms = new Platform[tempPlatformCollection.size()];
		for(int i = 0; i < platforms.length; i++){
			platforms[i] = tempPlatformCollection.pop();
		}
	}
	
	//read the map file
	public void getFromFile(){
		try {
			BufferedReader br;
			if(!isCustomMap){
				br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Maps/" + mapPath)));
			}else{
				br = new BufferedReader(new FileReader(mapPath));
			}
			String temp = br.readLine();
			background = ImageIO.read(getClass().getResourceAsStream("/Images/" + temp));
			while((temp = br.readLine()) != null){
				map.push(temp.split(","));
			}
			buildMap();
		} catch (FileNotFoundException ex1){
			Core.showError("Map file not found.\nBuilding Default.");
			buildDefault();
		} catch (IOException e) {
			Core.showError("Error reading map file.\nBuilding Default.");
			buildDefault();
		}
	}
	
	//out of the way hard coded default map
	public void loadDefaultMap(){
		map.push("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,S,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","));
		map.push("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,S,S,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","));
		map.push("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,S,S,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","));
		map.push("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,S,S,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","));
		map.push("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,S,S,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0".split(","));
		map.push("G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G,G".split(","));
	}
}