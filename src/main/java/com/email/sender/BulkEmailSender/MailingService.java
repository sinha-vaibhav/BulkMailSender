package com.email.sender.BulkEmailSender;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailingService {

	private Session session;
	private Properties props;
	
	
	
	public static Session session2 = initSession();
	
	public MailingService() {
		
		final String username = "jon.got.snow@gmail.com";
		final String password = "jongotsnow";
 
		props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		this.session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		
	}
	
	public Session getNewSession(Properties props) {
		
		final String username = "jon.got.snow@gmail.com";
		final String password = "jongotsnow";
		
		Session session2 = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
		
		return session2;
	}

	public static Session initSession() {
		
		final String username = "jon.got.snow@gmail.com";
		final String password = "jongotsnow";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.user","jon.got.snow@gmail.com");
		props.put("mail.smtp.password", "jongotsnow");
 
		session2 = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
		return session2;
 
	/*	try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("vbhvsinha2010@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return session;*/
		
	}

	public void sendEmail(Email email) {

		System.out.println("Trying to send email for " + email.getFromEmailAddress() + " with ID -> " + email.getId());
		try {  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(email.getFromEmailAddress()));
			//message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getToEmailAddress())); 
			message.addRecipient(Message.RecipientType.TO,new InternetAddress("jon.got.snow@gmail.com"));  
			message.setSubject(email.getSubject());  
			message.setText(email.getBody());  
			
			Transport transport = session.getTransport("smtp");
			//transport.connect();
	        transport.connect(this.props.getProperty("mail.smtp.host"), 
	                Integer.parseInt(this.props.getProperty("mail.smtp.port")),
	                this.props.getProperty("mail.smtp.user"),
	                this.props.getProperty("mail.smtp.password"));            
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	        
			//Transport.send(message);  
			DataSource.updateSentEmail(email.getId());
			System.out.println("message sent successfully");  
			

		} catch (Exception e) {
			System.out.println("Unable to send email from " + email.getFromEmailAddress() + " to " + email.getToEmailAddress());
			e.printStackTrace();
			System.exit(0);
			//throw new RuntimeException(e);
			}  



	}

}
