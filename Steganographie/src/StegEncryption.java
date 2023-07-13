import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StegEncryption {
	
	static BufferedImage image; // Original
	static BufferedImage temp; // verschluesseltes Bild
	
	public static void main(String[] args) {
		
//		try {
//			image = ImageIO.read(new File("/Users/fynn/git/Steganographie/Steganographie/src/Screenshot (981).png"));
//			temp = ImageIO.read(new File("/Users/fynn/git/Steganographie/Steganographie/src/testa.png"));
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//		
//    	encrypt();
		
    	try {
			image = ImageIO.read(new File("/Users/fynn/git/Steganographie/Steganographie/src/pics/img(7).png"));
			temp = new BufferedImage(79,83,BufferedImage.TYPE_INT_ARGB);
		} catch (IOException e) {
			System.out.println(e);
		}
    	
    	decrypt();
    }
	
	public static void badEncrypt() {

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
	
	public static void encrypt() {

    	int tempX = 0;
    	int tempY = 0;
    	
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		    	int originalColour = image.getRGB(x, y);
		        String origialColourString = Integer.toBinaryString(originalColour).substring(0, 32);

		        for(int p = 0; p < 8; p++) {
		        	int tempColour = temp.getRGB(tempX, tempY);
		        	String tempColourString = String.format("%32s", Integer.toBinaryString(tempColour)).replace(' ', '0');
			        	
		        	String a = tempColourString.substring(0, 7) + origialColourString.charAt(p);
		        	String r = tempColourString.substring(8, 15) + origialColourString.charAt(p+8);
		        	String g = tempColourString.substring(16, 23) + origialColourString.charAt(p+16);
		        	String b = tempColourString.substring(24, 31) + origialColourString.charAt(p+24);

	        		temp.setRGB(tempX, tempY, Integer.parseUnsignedInt(a+r+g+b, 2));
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
	
	public static void decrypt() {
		try {
	    	int tempX = 0;
	    	int tempY = 0;
	    	
	    	String a = "";
	    	String r = "";
	    	String g = "";
	    	String b = "";
	    	
	    	int p = 0;
    	
			for (int y = 0; y < image.getHeight(); y++) {
			    for (int x = 0; x < image.getWidth(); x++) {
			    	int originalColour = image.getRGB(x, y);
			        String origialColourString = String.format("%32s", Integer.toBinaryString(originalColour)).replace(' ', '0');
	
			        a += origialColourString.charAt(7);
			        r += origialColourString.charAt(15);
			        g += origialColourString.charAt(23);
			        b += origialColourString.charAt(31);
			        
			        p++;
			        if(p==8) {
			        	temp.setRGB(tempY, tempX, Integer.parseUnsignedInt(a+r+g+b, 2));
			        	tempY++;
			        	if(tempY >= temp.getWidth()) {
			        		tempY = 0;
			        		tempX++;
			        	}
			        	a = r = g = b = "";
			        	p = 0;
			        }
			    }
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			try {
				ImageIO.write(temp, "png", createImage());
			} catch (IOException ex) {
				System.out.println(ex);
			}
			
			System.out.println("Fertig");
		}
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
}
