package com.email.sender.BulkEmailSender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailingService {

	public static Session session = initSession();

	public static Session initSession() {
		//Get the session object  
		Properties props = new Properties();  
		props.put("mail.smtp.host", "smtp.gmail.com");  
		props.put("mail.smtp.socketFactory.port", "465");  
		props.put("mail.smtp.socketFactory.class",  
				"javax.net.ssl.SSLSocketFactory");  
		props.put("mail.smtp.auth", "true");  
		props.put("mail.smtp.port", "465");  

		Session session = Session.getDefaultInstance(props,  
				new javax.mail.Authenticator() {  
			protected PasswordAuthentication getPasswordAuthentication() {  
				return new PasswordAuthentication("report.banane.do@gmail.com","reportbananedo");//change accordingly  
			}  
		});  
		return session;

	}

	public static void sendEmail(Email email) {

		System.out.println("Trying to send email for " + email.getFromEmailAddress() + " with ID -> " + email.getId());
		try {  
			MimeMessage message = new MimeMessage(session);  
			//message.setFrom(new InternetAddress(email.getFromEmailAddress()));//change accordingly  
			message.setFrom(new InternetAddress("report.banane.do@gmail.com"));//change accordingly  
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getToEmailAddress()));  
			message.setSubject(email.getSubject());  
			message.setText(email.getBody());  
			Transport.send(message);  
			DataSource.updateSentEmail(email.getId());
			System.out.println("message sent successfully");  
			

		} catch (MessagingException e) {
			System.out.println("Unable to send email from " + email.getFromEmailAddress() + " to " + email.getToEmailAddress());
			e.printStackTrace();
			throw new RuntimeException(e);}  



	}

}
