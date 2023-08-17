import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.security.CodeSource;

import javax.imageio.ImageIO;
import javax.swing.*;

public class StegaKryptView extends JPanel {
	
	private final StegaKryptView instance = this;
	
	private StegEncryption model;
	
	JMenuBar menu;
	JMenu m1, m2, m3;
	JMenuItem mi1, mi2, mj1, mk1;
	
	CardLayout cardLayout;
	JPanel recent, cards;
	
	JPanel encryptPanel1, encryptPanel2;
	JLabel fileText1 = new JLabel("k.A."), fileText2 = new JLabel("k.A.");
	JLabel img1Label = new JLabel("k.A."), img2Label = new JLabel("k.A.");
	
	JLabel fileText3 = new JLabel("k.A.");
	JLabel img3Label = new JLabel("k.A.");
	
	JLabel footerDirectory;

	public StegaKryptView(StegEncryption model) {
		
		this.model = model;
		
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(model.getJarPath() + "/stegakrypt.ser"))) {
			this.model = (StegEncryption) ois.readObject();
		} catch (FileNotFoundException e) {
			serializeAll();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		this.setLayout(new BorderLayout(10,0));
		
		createMenu();
		createCenter();
		createFooter();
	}
	
	public void createCenter() {
		
		cardLayout = new CardLayout();
		
//		recent = new JPanel(new GridLayout(50,1));
//		recent.setSize(100,100);
//		recent.add(new JLabel("n"));
//		recent.add(new JLabel("naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
//		
//		add(recent, BorderLayout.WEST);
		
		cards = new JPanel(cardLayout);

		add(cards, BorderLayout.CENTER);
		
		// -------------- c0
		
		createMenuBarMainCard();
		
		// -------------- c1
		
		createMenuBarEncryptCard();
		
		// --------------- c2
		
		createMenuBarDecryptCard();
		
		
		cardLayout.show(cards, "c0");
		
	}
	
	public void createMenuBarMainCard() {
		JPanel c0 = new JPanel(new GridLayout(2,1));
		
		JButton encryptButton = new JButton("Verschlüsseln");
		encryptButton.addActionListener(new StegaKryptController(this) {
			@Override
			public void actionPerformed(ActionEvent ev) {
				cardLayout.show(cards, "c1");
		    }
		});
		
		JButton decryptButton = new JButton("Entschlüsseln");
		decryptButton.addActionListener(new StegaKryptController(this) {
			@Override
			public void actionPerformed(ActionEvent ev) {
				cardLayout.show(cards, "c2");
		    }
		});
		
		c0.add(encryptButton);
		c0.add(decryptButton);
		
		cards.add(c0, "c0");
	}
	
	public void createMenuBarEncryptCard() {
		
		JPanel p1 = new JPanel(new GridLayout(2,1));
		JPanel p2 = new JPanel(new GridLayout(1,2));
		
		p2.add(img1Label);
		p2.add(img2Label);
		
		p1.add(p2);
		
		JPanel p3 = new JPanel(new BorderLayout());
		JPanel p4 = new JPanel(new GridLayout(1,2));
		
		p3.add(p4, BorderLayout.CENTER);
		
		p1.add(p2);
		p1.add(p3);
		
		JPanel c1 = p1;
		
		JButton b1 = new JButton("Bild auswählen");
		b1.addActionListener(new StegaKryptController(this) {
			@Override
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
		    }
		});
		JButton b2 = new JButton("Speichermedium auswählen");
		b2.addActionListener(new StegaKryptController(this) {
			@Override
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
		    }
		});
		JButton encryptButton = new JButton("Verschlüsseln");
		encryptButton.addActionListener(new StegaKryptController(this) {
			@Override
			public void actionPerformed(ActionEvent ev) {
				try {
					model.encrypt();
				} catch(IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null,	"Das Speichermedium muss 16+8x Pixel größer sein, als das Bild!");
				} catch(NullPointerException e) {
					JOptionPane.showMessageDialog(null,	"Es werden zwei Bilder benötigt!");
				}
		    }
		});
		
		encryptPanel1 = new JPanel(new BorderLayout());
		encryptPanel1.add(fileText1, BorderLayout.NORTH);
		encryptPanel1.add(b1, BorderLayout.CENTER);
		
		encryptPanel2 = new JPanel(new BorderLayout());
		encryptPanel2.add(fileText2, BorderLayout.NORTH);
		encryptPanel2.add(b2, BorderLayout.CENTER);
		
		
		p4.add(encryptPanel1);
		p4.add(encryptPanel2);
		p3.add(encryptButton, BorderLayout.SOUTH);
		
		cards.add(c1, "c1");
	}
	
	public void createMenuBarDecryptCard() {
		JPanel c2 = new JPanel(new GridLayout(2,1));
		
		JButton b1 = new JButton("Bild auswählen");
		b1.addActionListener(new StegaKryptController(this) {
			public void actionPerformed(ActionEvent ev) {
		    	model.image = createFileChooser(new JFrame());
				BufferedImage img = null;
				try {
					img = ImageIO.read(model.image);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Image dimg = img.getScaledInstance(img3Label.getWidth(), img3Label.getHeight(), Image.SCALE_SMOOTH);
				fileText3.setText(model.image.toString());
				img3Label.setIcon(new ImageIcon(dimg));
		    }
		});
		JButton decryptButton = new JButton("Entschlüsseln");
		decryptButton.addActionListener(new StegaKryptController(this) {
			public void actionPerformed(ActionEvent ev) {
		    	try {
					model.decrypt();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null,	"Bild konnte nicht entschlüsselt werden!");
				}
		    }
		});
		
		encryptPanel1 = new JPanel(new BorderLayout());
		encryptPanel2 = new JPanel(new BorderLayout());
		encryptPanel2.add(fileText3, BorderLayout.NORTH);
		encryptPanel2.add(b1, BorderLayout.CENTER);
		
		encryptPanel1.add(encryptPanel2, BorderLayout.CENTER);
		
		encryptPanel1.add(decryptButton, BorderLayout.SOUTH);
		
		c2.add(img3Label);
		c2.add(encryptPanel1);
		
		cards.add(c2, "c2");
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
		    			footerDirectory.setText(model.directory.toString());
		    			serializeAll();
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
		
		mk1 = new JMenuItem("Nutzung");
		
		mk1.addActionListener(new StegaKryptController(this) {
			@Override
			public void actionPerformed(ActionEvent ev) {
				JFrame f = new JFrame();
		    	f.setVisible(true);
		    	f.setSize(600,400);
		    	f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		    	
		    	JPanel p = new JPanel();
		    	p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		    	
		    	p.add(new JLabel("<html><body>"
		    			+ "1. Speichermedien muessen mindestens 16 + 8x Pixel groesser sein,<br>als das zu verschluesselnde Bild.<br>"
		    			+ "2. Auf MacOS-Geraeten fehlen JAR-Dateien unter Umstaenden die benoetigten Berechtigungen, um fehlerfrei zu funktionieren<br>"
		    			+ "3. Nicht alle Bildformate werden funktionieren (Bildformate die einen Pixel als 32bit speichern funktionieren)<br>"
		    			+ "</body></html>"));
		    	
		    	f.add(p);
			}
		});
		
		m3.add(mk1);
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		
		add(menu, BorderLayout.NORTH);
		
	}
	
	public void createFooter() {
		JPanel footer = new JPanel(new BorderLayout());
		footerDirectory = new JLabel("Speicherort: " + model.directory.toString());
		footer.add(new JLabel("StegaKrypt 2023"), BorderLayout.WEST);
		footer.add(footerDirectory, BorderLayout.EAST);
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
	
	public void serializeAll() {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(model.getJarPath() + "/stegakrypt.ser"))) {
			oos.writeObject(model);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,	"StegaKrypt konnte nicht serialisiert werden!");
			//e.printStackTrace();
		}
	}
}