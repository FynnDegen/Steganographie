import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.ImagingOpException;
import java.awt.Color;

/**
 * Encrypt
 *
 * @author  Fynn Degen
 * @version 1.0
 * @since 2022-06-01
 */
public class Encrypt
{      
    
    public Encrypt()
    {
        
    }
    
    public static void hallo(Image pImage)
    {
        System.out.println(pImage.getImage().getRGB(0,0));
        System.out.println(pImage.getImage().getRGB(0,0) & 0xff);
    }

    /**
     * Inserts every RGBA value into the binary array of each Image object
     * 
     * @param pImage the Image object
     * @see Image
     */
    public static void readPixels(Image pImage)
    {
        int pixelsRead=0;
        int rowsRead=0;
        String binaryValue;
        try
        {
                for(int i=0; i<pImage.getHeight(); i++)
                {
                    for(int j=0; j<pImage.getWidth(); j++)
                    {
                        pixelsRead++;
                        
                        int b = pImage.getImage().getRGB(j,i) & 0xff;
                        int g = (pImage.getImage().getRGB(j,i) & 0xff00) >> 8;
                        int r = (pImage.getImage().getRGB(j,i) & 0xff0000) >> 16;
                        int a = (pImage.getImage().getRGB(j,i) & 0xff000000) >>> 24;
                        
                        binaryValue = String.format("%8s", Integer.toBinaryString(r)).replaceAll(" ", "0");
                        binaryValue += String.format("%8s", Integer.toBinaryString(g)).replaceAll(" ", "0");
                        binaryValue += String.format("%8s", Integer.toBinaryString(b)).replaceAll(" ", "0");
                        binaryValue += String.format("%8s", Integer.toBinaryString(a)).replaceAll(" ", "0");
                        
                        pImage.setBinaryString(j,i,binaryValue);
                    }
                    rowsRead++;
                }
                System.out.println("Pixels read: " + pixelsRead + " | Rows read: " + rowsRead);
            }
            catch(Exception e) { System.out.println("err readPixels() | Pixels read: " + pixelsRead + " | Rows read: " + rowsRead); }
    }
    
    /**
     * Creates a new altered Image object and a new altered image file in a specific folder 
     * 
     * @param pImage1 the template Image object
     * @param pImage2 the Image object you want to encrypt
     * @see Image
     */
    public static void encrypt(Image pImage1, Image pImage2)
    {
        if(correctResolution(pImage1, pImage2))
        {
            readPixels(pImage1);
            readPixels(pImage2);
        }
        else { return; }
        
        try
        {
            ImageIO.write(pImage2.getImage(), "png", new File("F:\\Steganographie\\test.png"));
        }
        catch(Exception e) {  }
        
        Image oImage = new Image("F:\\Steganographie\\test.png");
        oImage = pImage2;
        
        int pixel=0; //pImage2 digit
        int progress=0; //progress of one pixel (all digits altered?)
        int xCoord=0; //x-coordinate of pImage2
        int yCoord=0; //y-coordinate of pImage2
        int all=0; //all pixels fully converted
        int res = getResolution(pImage1); //template resolution
        try
        {
            for(int i=0; i<pImage1.getHeight(); i++)
            {
                for(int j=0; j<pImage1.getWidth(); j++)
                {
                    for(int y=7; y<23; y+=8) //last digit of an 8-bit binary string | contains 3 values thus (actually 4 but the alpha value will not be altered))
                    {
                        if(all >= res) { return; }
                        if(progress == 7)
                        {
                            progress = 0;
                            xCoord++;
                        }
                        if(xCoord > pImage2.getWidth())
                        {
                            yCoord++;
                            xCoord = 0;
                        }
                        if(pixel >= 31) { pixel = 0; }
                        if(!(getLastDigit(pImage1,j,i,y).equals(getLastDigit(pImage2,xCoord,yCoord,pixel))))
                        {
                            if(getLastDigit(pImage1,j,i,y).equals("1"))
                            {
                                pImage1.setBinaryString(j, i, pImage1.getBinaryString(j, i).substring(y,y+7) + "0");
                            }
                            else
                            {
                                pImage1.setBinaryString(j, i, pImage1.getBinaryString(j, i).substring(y,y+7) + "1");
                            }
                            //System.out.println("+");
                        }
                        pixel++;
                        progress++;
                        all++;
                    }
                }
            }
            //File outputfile = new File("saved.png");
            //ImageIO.write(img, "png", outputfile);
        }
        catch(Exception e)
        {
            System.out.println(pixel + " " + progress + " " + all + " " + e);
            //showBinaryArray(eImage);
            showBinaryArray(pImage2);
            
        }
    }
    
    /**
     * Outputs the last digit of a binary String within the array
     * 
     * @param pImage
     * @param index1 first index within the array
     * @param index2 second index within the array
     * @param digit digit at given place
     * @return String the digit at this specific place
     * @see Image
     */
    public static String getLastDigit(Image pImage, int index1, int index2, int digit)
    {
        char c = pImage.getBinaryString(index1, index2).charAt(digit);
        //System.out.println(c);
        return Character.toString(c);
    }
    
    /**
     * Calculates the resolution of an image
     * 
     * @param pImage given Image object
     * @return int resolution of the image
     * @see Image
     */
    public static int getResolution(Image pImage)
    {
        return pImage.getWidth()*pImage.getHeight();
    }
    
    /**
     * Checks if the resolution is large enough
     * 
     * @param pImage1 image you want to encrypt
     * @param pImage2 image template to hide the image in
     * @return boolean if the resolution of pImage1 multiplied by 8 is still smaller than the resolution of pImage2 return true
     * @see Image
     */
    public static boolean correctResolution(Image pImage1, Image pImage2)
    {
        if(getResolution(pImage1)*8 <= getResolution(pImage2))
        {
            return true;
        }
        return false; 
    }
    
    /**
     * Outputs the binaryString array of an Image object
     * 
     * @param pImage given Image object
     * @see Image
     */
    public static void showBinaryArray(Image pImage)
    {
        if(pImage.getBinaryString(0, 0) == null) { return; }
        
        for(int i=0; i<pImage.getHeight(); i++)
        {
            for(int j=0; j<pImage.getWidth(); j++)
            {
                System.out.print("R:"+ pImage.getBinaryString(j, i).substring(0,8) + " G:"+ pImage.getBinaryString(j, i).substring(8,16)
                                  + " B:" + pImage.getBinaryString(j, i).substring(16,24) + " A:"+ pImage.getBinaryString(j, i).substring(24) +" | ");
            }
            System.out.println();
        }
    }
}
