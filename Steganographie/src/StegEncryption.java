import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class StegEncryption {
	
	static File original, template;
	static BufferedImage image, temp;
	static File f;
	static String alpha, rot, gruen, blau;
	
	public static void main(String[] args) {
		existDir();
    	encrypt();
    	//System.out.print(alpha + "\n" + rot + "\n" + gruen + "\n" + blau);
    }
	
	public static void encrypt() {
		
		original = new File("/Users/fynn/git/Steganographie/Steganographie/src/Screenshot (981).png");
		template = new File("/Users/fynn/git/Steganographie/Steganographie/src/testa.png");
    	try {
			image = ImageIO.read(original);
			temp = ImageIO.read(template);
			existImage();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		    	int argb = image.getRGB(x, y);
		        alpha = Integer.toBinaryString(argb).substring(0, 7) + "0";
		        rot = Integer.toBinaryString(argb).substring(8, 15) + "0";
		        gruen = Integer.toBinaryString(argb).substring(16, 23) + "0";
		        blau = Integer.toBinaryString(argb).substring(24, 31) + "0";
		        
		        //temp.setRGB(x, y, argb);
		        
		        for(int y2 = 0; y2 < 8; y2++) {
		        	for(int x2 = 0; x2 < 8; x2++) {
		        		temp.setRGB(x+x2, y+y2, argb);
		        		//System.out.println(temp.getRGB(x+x2, y+y2));
		        	}
		        }
		    }
		}
		
		try {
			ImageIO.write(temp, "png", f);
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
	
	public static void existImage() {
		
		int num = 0;
		
		do {
			f = new File("/Users/fynn/git/Steganographie/Steganographie/src/pics/img(" + num + ").png");
			num++;
		} while(f.exists());
		
		try {
			f.createNewFile();
		} catch (Exception e) { }
		
	}

}
