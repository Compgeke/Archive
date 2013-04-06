package engine;

import java.io.File;

public class Rename {
	public static void main(String[] args){
		new Rename().start();
	}
	
	public void start(){
		File f = new File("C:\\Users\\Amber\\Desktop\\Upload Photos\\RenameThis\\");
		int i = 0;
		File[] array = f.listFiles();
		System.out.println(array.length);
		for(File ff: array){
			i++;
			System.out.println(ff.renameTo(getName(i)));
		}
	}
	
	public File getName(int i){
		String temp = Integer.toString(i);
		String finalString = "C:\\Users\\Amber\\Desktop\\Upload Photos\\Renamed\\";
		if(temp.length() == 3){
			finalString += temp + ".JPG";
			System.out.println(finalString);
		} else if(temp.length() == 2){
			finalString += "0" + temp + ".JPG";
			System.out.println(finalString);
		} else{
			finalString += "00" + temp + ".JPG";
			System.out.println(finalString);
		}
		return new File(finalString);
	}
}
