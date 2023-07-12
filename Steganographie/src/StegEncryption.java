import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StegEncryption {
	
	static BufferedImage image; // Original
	static BufferedImage temp; // verschluesseltes Bild
	static String alpha, rot, gruen, blau;
	
	public static void main(String[] args) {
		
		try {
			image = ImageIO.read(new File("/Users/fynn/git/Steganographie/Steganographie/src/Screenshot (981).png"));
			temp = ImageIO.read(new File("/Users/fynn/git/Steganographie/Steganographie/src/11729.jpeg"));
		} catch (IOException e) {
			System.out.println(e);
		}
		
		System.out.println(image.getWidth() + " " + image.getWidth());
		System.out.println(temp.getWidth() + " " + temp.getWidth());
		
    	encrypt();
		
		readPixel();
    }
	
	public static void encrypt() {

    	int tempX = 0;
    	int tempY = 0;
    	
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		    	int originalColour = image.getRGB(x, y);
		        String origialColourString = Integer.toBinaryString(originalColour).substring(0, 32);
		        
		        for(int p = 0; p < 32; p++) {
		        	int tempColour = temp.getRGB(tempX, tempY);
		        	String tempColourString = Integer.toBinaryString(tempColour).substring(0, 31) + origialColourString.charAt(p);
		        	
	        		temp.setRGB(tempX, tempY, Integer.parseUnsignedInt(tempColourString, 2));
		        	tempX++;
		        	if(tempX >= temp.getWidth()) {
		        		tempX = 0;
		        		tempY++;
		        	}
		        }
		    }
		}

		try {
			ImageIO.write(temp, "png", createImage());
		} catch (IOException e) {
			System.out.println(e);
		}
		
		System.out.println("Fertig");
	}
	
	public static void existDir() {
		File theDir = new File("/Users/fynn/git/Steganographie/Steganographie/src/pics");
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
	}
	
	public static File createImage() {
		
		existDir();
		
		File f;
		int num = 0;
		
		do {
			f = new File("/Users/fynn/git/Steganographie/Steganographie/src/pics/img(" + num + ").png");
			num++;
		} while(f.exists());
		
		try {
			f.createNewFile();
		} catch (Exception e) { }
		
		return f;
	}
	
	public static void readPixel() {
		for (int x = 0; x < 32; x++) {
	    	System.out.println(Integer.toBinaryString(temp.getRGB(x,0)) + " " + Integer.toBinaryString(image.getRGB(0,0)).charAt(x));
	    }
	}
}
