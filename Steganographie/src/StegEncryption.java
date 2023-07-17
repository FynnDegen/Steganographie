import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class StegEncryption implements Serializable {
	
	File image = null; // Original
	File temp = null; // verschluesseltes Bild
	File directory = new File("/Users/fynn/git/Steganographie/Steganographie/src/pics");
	
	public StegEncryption() {
		
	}
	
	public File encrypt() {
		
		BufferedImage image = null;
		BufferedImage temp = null;
		
		try {
			image = ImageIO.read(this.image);
			temp = ImageIO.read(this.temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(!rightImageSize(image, temp)) {
			System.err.println("Ungenügende Größe");
			System.exit(-1);
		}

    	int tempX = 0;
    	int tempY = 0;
    	
    	String height = String.format("%32s", Integer.toBinaryString(image.getHeight())).replace(' ', '0');
    	
		for (int i = 0; i < 32; i+=4) {
			int tempColour = temp.getRGB(tempX, tempY);
	    	String tempColourString = String.format("%32s", Integer.toBinaryString(tempColour)).replace(' ', '0');
        	
        	String a = tempColourString.substring(0, 7) + height.charAt(i);
        	String r = tempColourString.substring(8, 15) + height.charAt(i+1);
        	String g = tempColourString.substring(16, 23) + height.charAt(i+2);
        	String b = tempColourString.substring(24, 31) + height.charAt(i+3);
	    	
	    	temp.setRGB(tempX, tempY, Integer.parseUnsignedInt(a+r+g+b, 2));
	    	tempX++;
        	if(tempX >= temp.getWidth()) {
        		tempX = 0;
        		tempY++;
        	}
		    
    	}
		
		String width = String.format("%32s", Integer.toBinaryString(image.getWidth())).replace(' ', '0');
    	
		for (int i = 0; i < 32; i+=4) {
			int tempColour = temp.getRGB(tempX, tempY);
	    	String tempColourString = String.format("%32s", Integer.toBinaryString(tempColour)).replace(' ', '0');
        	
        	String a = tempColourString.substring(0, 7) + width.charAt(i);
        	String r = tempColourString.substring(8, 15) + width.charAt(i+1);
        	String g = tempColourString.substring(16, 23) + width.charAt(i+2);
        	String b = tempColourString.substring(24, 31) + width.charAt(i+3);
	    	
	    	temp.setRGB(tempX, tempY, Integer.parseUnsignedInt(a+r+g+b, 2));
	    	tempX++;
        	if(tempX >= temp.getWidth()) {
        		tempX = 0;
        		tempY++;
        	}
		    
    	}
		
    	
		for (int y = 0; y < image.getHeight(); y++) {
		    for (int x = 0; x < image.getWidth(); x++) {
		    	int originalColour = image.getRGB(x, y);
		        String origialColourString = String.format("%32s", Integer.toBinaryString(originalColour)).replace(' ', '0');

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
			File newFile = createImage();
			ImageIO.write(temp, "png", newFile);
			System.out.println("Fertig");
			return newFile;
		} catch (IOException e) {
			System.out.println(e);
		}
		
		System.out.println("Fertig");
		return null;
	}
	
	public void decrypt() {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(this.image);
		} catch (IOException e) {
			System.out.println(e);
		}
		
    	int tempX = 0;
    	int tempY = 0;
    	
    	String a = "";
    	String r = "";
    	String g = "";
    	String b = "";
    	
    	int p = 0;
    	
    	int xPos = 0, yPos = 0;
    	
    	readHeight:
    	for (int y = yPos; y < image.getHeight(); y++) {
		    for (int x = xPos; x < image.getWidth(); x++) {
	    		int originalColour = image.getRGB(x, y);
		        String origialColourString = String.format("%32s", Integer.toBinaryString(originalColour)).replace(' ', '0');
	        	
		        a += origialColourString.charAt(7);
		        a += origialColourString.charAt(15);
		        a += origialColourString.charAt(23);
		        a += origialColourString.charAt(31);
		        
		        p++;
		        if(p==8) {
		        	p = 0;
		        	xPos = x+1;
		        	yPos = y;
		        	break readHeight;
		        }
		    }
    	}
    	
    	readWidth:
    	for (int y = yPos; y < image.getHeight(); y++) {
		    for (int x = xPos; x < image.getWidth(); x++) {
	    		int originalColour = image.getRGB(x, y);
		        String origialColourString = String.format("%32s", Integer.toBinaryString(originalColour)).replace(' ', '0');
	        	
		        b += origialColourString.charAt(7);
		        b += origialColourString.charAt(15);
		        b += origialColourString.charAt(23);
		        b += origialColourString.charAt(31);
		        
		        p++;
		        if(p==8) {
		        	p = 0;
		        	xPos = x+1;
		        	yPos = y;
		        	break readWidth;
		        }
		    }
    	}
    	
    	BufferedImage temp = new BufferedImage(Integer.parseUnsignedInt(b, 2),Integer.parseUnsignedInt(a, 2),BufferedImage.TYPE_INT_ARGB);
    	a = b = "";
    	
	    try {
			for (int y = yPos; y < image.getHeight(); y++) {
			    for (int x = xPos; x < image.getWidth(); x++) {
			    	int originalColour = image.getRGB(x, y);
			        String origialColourString = String.format("%32s", Integer.toBinaryString(originalColour)).replace(' ', '0');
	
			        a += origialColourString.charAt(7);
			        r += origialColourString.charAt(15);
			        g += origialColourString.charAt(23);
			        b += origialColourString.charAt(31);
			        
			        p++;
			        if(p==8) {
			        	temp.setRGB(tempX, tempY, Integer.parseUnsignedInt(a+r+g+b, 2));
			        	tempX++;
			        	if(tempX >= temp.getWidth()) {
			        		tempX = 0;
			        		tempY++;
			        	}
			        	a = r = g = b = "";
			        	p = 0;
			        }
			    }
			    xPos = 0;
			}
			try {
				ImageIO.write(temp, "png", createImage());
			} catch (IOException ex) {
				System.out.println(ex);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			try {
				ImageIO.write(temp, "png", createImage());
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
		System.out.println("Fertig");
	}
	
	public void existDir() {
		File theDir = new File("/Users/fynn/git/Steganographie/Steganographie/src/pics");
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
	}
	
	public File createImage() {
		
		existDir();
		
		File f;
		int num = 0;
		
		do {
			f = new File(this.directory + "/img(" + num + ").png");
			num++;
		} while(f.exists());
		
		try {
			f.createNewFile();
		} catch (Exception e) { }
		
		return f;
	}
	
	public boolean rightImageSize(BufferedImage image, BufferedImage temp) {
		return (temp.getWidth()*temp.getHeight()*8+16) >= image.getWidth()*image.getHeight();
	}
	
	private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
		s.defaultWriteObject();
		s.writeObject(image);
		s.writeObject(temp);
		s.writeObject(directory);
	}
	  
	private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
		s.defaultReadObject();
		this.image = (File) s.readObject();
		this.temp = (File) s.readObject();
		this.directory = (File) s.readObject();
	}
}
