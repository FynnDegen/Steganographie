import static org.junit.Assert.*;
import org.junit.Test;

/**
 * EncryptTest
 *
 * @author  Fynn Degen
 * @version 1.0
 */
public class EncryptTest
{
    Image template;
    Image eImage;
    
    public EncryptTest()
    {
        template = new Image("F:\\Steganographie\\Screenshot (971).png");
        eImage = new Image("F:\\Steganographie\\Screenshot (981).png");
    }

    @Test
    public void test()
    {
        Encrypt.readPixels(template);
        Encrypt.readPixels(eImage);
        
        Encrypt.showBinaryArray(eImage);
        
        //System.out.println(eImage.getBinaryString(0,0));
        
        
        
        //Encrypt.hallo(eImage);
        Encrypt.encrypt(template,eImage);
        
        Encrypt.showBinaryArray(template);
    }
}
