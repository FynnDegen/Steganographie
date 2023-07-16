import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StegaKryptView extends JPanel {
	
	//private StegEncryption model;
	
	private final StegaKryptView instance = this;
	
	private StegEncryption model;
	
	JMenuBar menu;
	JMenu m1, m2, m3;
	JMenuItem mi1, mi2, mj1;
	
	CardLayout cardLayout;
	JPanel recent, cards;
	
	JPanel encryptPanel1, encryptPanel2;
	JLabel fileText1 = new JLabel("k.A."), fileText2 = new JLabel("k.A.");
	JLabel img1Label = new JLabel("k.A."), img2Label = new JLabel("k.A.");

	public StegaKryptView(StegEncryption model) {
		
		this.model = model;
		
		this.setLayout(new BorderLayout(10,0));
		
		createMenu();
		createCenter();
		createFooter();
		
	}
	
	public void createCenter() {
		
		cardLayout = new CardLayout();
		
		recent = new JPanel(new GridLayout(50,1));
		recent.setSize(100,100);
		recent.add(new JLabel("n"));
		recent.add(new JLabel("naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
		
		add(recent, BorderLayout.WEST);
		
		cards = new JPanel(cardLayout);

		add(cards, BorderLayout.CENTER);
		
		// -------------- c1
		
		JPanel c1 = new JPanel(new GridLayout(2,1));
		
		JButton b3 = new JButton("Bild entschlüsseln");
		b3.addActionListener(new StegaKryptController(this) {
			public void actionPerformed(ActionEvent ev) {
		    	model.image = createFileChooser(new JFrame());
		    	model.decrypt();
		    }
		});
		
		c1.add(b3);
		
		// --------------- c2
		
		JPanel c2 = new JPanel(new GridLayout(2,2));
		
		JButton b1 = new JButton("Bild verschlüsseln");
		b1.addActionListener(new StegaKryptController(this) {
			public void actionPerformed(ActionEvent ev) {
				model.image = createFileChooser(new JFrame());
				BufferedImage img = null;
				try {
					img = ImageIO.read(model.image);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Image dimg = img.getScaledInstance(img1Label.getWidth(), img1Label.getHeight(), Image.SCALE_SMOOTH);
				fileText1.setText(model.image.toString());
				img1Label.setIcon(new ImageIcon(dimg));
				c2.validate();
		    }
		});
		JButton b2 = new JButton("Speichermedium");
		b2.addActionListener(new StegaKryptController(this) {
			public void actionPerformed(ActionEvent ev) {
				model.temp = createFileChooser(new JFrame());
				BufferedImage img = null;
				try {
					img = ImageIO.read(model.temp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Image dimg = img.getScaledInstance(img2Label.getWidth(), img2Label.getHeight(), Image.SCALE_SMOOTH);
				fileText2.setText(model.temp.toString());
				img2Label.setIcon(new ImageIcon(dimg));
				c2.validate();
		    }
		});
		
		encryptPanel1 = new JPanel(new BorderLayout());
		encryptPanel1.add(fileText1, BorderLayout.SOUTH);
		encryptPanel1.add(img1Label, BorderLayout.CENTER);
		
		encryptPanel2 = new JPanel(new BorderLayout());
		encryptPanel2.add(fileText2, BorderLayout.SOUTH);
		encryptPanel2.add(img2Label, BorderLayout.CENTER);
		
		c2.add(encryptPanel1);
		c2.add(encryptPanel2);
		c2.add(b1);
		c2.add(b2);
		
		cards.add(c1, "c1");
		cards.add(c2, "c2");
		
		cardLayout.show(cards, "c2");
		
	}
	
	public void createMenu() {
		
		menu = new JMenuBar();
		m1 = new JMenu("Datei");
		m2 = new JMenu("Einstellungen");
		m3 = new JMenu("Hilfe");
		
		mi1 = new JMenuItem("Neues Bild verschlüsseln");
		mi1.addActionListener(new StegaKryptController(this) {
			@Override
		    public void actionPerformed(ActionEvent ev) {
				cardLayout.show(cards, "c1");
		    }
		});
		
		mi2 = new JMenuItem("Neues Bild entschlüsseln");
		mi2.addActionListener(new StegaKryptController(this) {
			@Override
		    public void actionPerformed(ActionEvent ev) {
		    	cardLayout.show(cards, "c2");
		    }
		});
		
		m1.add(mi1);
		m1.add(mi2);
		
		mj1 = new JMenuItem("Einstellungen");
		
		mj1.addActionListener(new StegaKryptController(this) {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFrame f = new JFrame();
		    	f.setVisible(true);
		    	f.setSize(400,600);
		    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		    	
		    	JPanel p = new JPanel(new BorderLayout());
		    	p.setVisible(true);
		    	
		    	JPanel p2 = new JPanel(new GridLayout(10,1));
		    	 
		    	JButton b1 = new JButton("Speicherort");
		    	b1.addActionListener(new StegaKryptController(instance) {
		    		@Override
					public void actionPerformed(ActionEvent ev) {
		    			model.directory = createDirectoryChooser(f);
				    }
				});
		    	
		    	p2.add(b1);
		    	p2.add(new JButton("BabaBoi"));
		    	p2.add(new JButton("BabaBoi"));
		    	p2.add(new JButton("BabaBoi"));
		    	
		    	p.add(new JLabel("Einstellungen"), BorderLayout.NORTH);
		    	p.add(p2, BorderLayout.WEST);
		    	p.add(new JLabel("Sachen"), BorderLayout.CENTER);
		    	
		    	f.add(p);
			}
		});
		
		m2.add(mj1);
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		
		add(menu, BorderLayout.NORTH);
		
	}
	
	public void createFooter() {
		JPanel footer = new JPanel(new BorderLayout());
		footer.add(new JLabel("StegaKrypt 2023"), BorderLayout.WEST);
		footer.add(new JLabel(model.directory.toString()), BorderLayout.EAST);
		add(footer, BorderLayout.SOUTH);
	}
	
	public File createFileChooser(JFrame f) {
		
    	f.setSize(300,300);
    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	
    	f.add(fileChooser);
    	f.setVisible(true);
    	
    	try {
    		int result = fileChooser.showSaveDialog(f);
        	if(result == JFileChooser.APPROVE_OPTION) {
        		System.out.println("Select was selected");
        		return fileChooser.getSelectedFile().getAbsoluteFile();
        	} else {
        		return null;
        	}
    	} finally {
    		f.dispose();
    	}
	}
	
//	public void createFileChooser(JPanel p) {
//		try {
//			StegEncryption.image = ImageIO.read(fileChooser.getSelectedFile().getAbsoluteFile());
//			JFrame f2 = new JFrame();
//	    	f.setVisible(true);
//	    	f.setSize(300,300);
//	    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//	    	int result2 = fileChooser.showSaveDialog(f);
//	    	if (result2 == JFileChooser.APPROVE_OPTION) {
//	    		StegEncryption.temp = ImageIO.read(
//	    	}
//		} catch(Exception e) {
//			JOptionPane.showMessageDialog(null,	"Fehler");
//		}
//	}
	
	public File createDirectoryChooser(JFrame f) {
		
    	f.setSize(300,300);
    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	
    	f.add(fileChooser);
    	f.setVisible(true);
    	
    	try {
    		int result = fileChooser.showSaveDialog(f);
        	if(result == JFileChooser.APPROVE_OPTION) {
        		System.out.println("Select was selected");
        		return fileChooser.getSelectedFile().getAbsoluteFile();
        	} else {
        		return null;
        	}
    	} finally {
    		f.dispose();
    	}
	}
}