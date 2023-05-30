import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.ImagingOpException;
import java.awt.Color;

/**
 * Image
 *
 * @author  Fynn Degen
 * @version 1.0
 */
public class Image
{
    private BufferedImage image;
    private int width, height;
    private String binaryString[][];
    Color colorArray[][];
    
    /**
     * Creates an Image object
     * 
     * @param pPath path to the image file
     */
    public Image(String pPath)
    {
        image = this.readFile(pPath);
        width = this.getWidth(image);
        height = this.getHeight(image);
        binaryString = this.initalizeBinaryArray();
    }
    
    /**
     * Converts a file path to a BufferedImage
     * 
     * @param pPath image file path
     */
    public static BufferedImage readFile(String pPath)
    {
        try
        {
            return ImageIO.read(new File(pPath));
        }
        catch(Exception e) { return null; }
    }
    
    /**
     * Initalizes the binaryString array of this Image object
     * 
     * @return String[][] initalized array
     */
    public String[][] initalizeBinaryArray()
    {
        return new String[this.getWidth()][this.getHeight()];
    }
    
    /**
     * Get the width of the BufferedImage
     * 
     * @param pImage BufferedImage attribute
     * @return int BufferedImage width
     */
    public static int getWidth(BufferedImage pImage)
    {
        return pImage.getWidth();
    }
    
    /**
     * Get the height of the BufferedImage
     * 
     * @param pImage BufferedImage attribute
     * @return int BufferedImage height
     */
    public static int getHeight(BufferedImage pImage)
    {
        return pImage.getHeight();
    }
    
    /**
     * Get the BufferedImage of this Image object
     * 
     * @return BufferedImage
     */
    public BufferedImage getImage()
    {
        return this.image;
    }
    
    /**
     * Get the width of this Image object
     * 
     * @return int width
     */
    public int getWidth()
    {
        return this.width;
    }
    
    /**
     * Set the width of this Image object
     * 
     * param pWidth
     */
    public void setWidth(int pWidth)
    {
        this.width = pWidth;
    }
    
    /**
     * Get the height of this Image object
     * 
     * @return int height
     */
    public int getHeight()
    {
        return this.height;
    }
    
    /**
     * Set the height of this Image object
     * 
     * @param pHeight
     */
    public void setHeight(int pHeight)
    {
        this.height = pHeight;
    }
    
    /**
     * Get the binaryString of this Image object
     * 
     * @param index1 first index of the array
     * @param index2 second index of the array
     * @return String binaryString
     */
    public String getBinaryString(int index1, int index2)
    {
        return this.binaryString[index1][index2];
    }
    
    /**
     * Set the binaryString of this Image object
     * 
     * @param index1 first index of the array
     * @param index2 second index of the array
     * @param input input for [index1][index2]
     */
    public void setBinaryString(int index1, int index2, String input)
    {
        this.binaryString[index1][index2] = input;
    }
}
