import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

public class StegaKryptMain {
	
	public static void main(String args[]) {
		
//		try {
//			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//		} catch(Exception e) { } 
		
		JFrame frame = new JFrame("StegaKrypt");
		
//		java.net.URL url = ClassLoader.getSystemResource("Screenshot (981).png");
//		Image img = Toolkit.getDefaultToolkit().createImage(url);
//		frame.setIconImage(img);
		
		frame.setContentPane(new StegaKryptView(new StegEncryption()));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		frame.setSize(width,height);
		frame.setMinimumSize(new Dimension(width/2, height/2));
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
