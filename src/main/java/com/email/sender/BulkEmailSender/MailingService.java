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

	public MailingService() {

		final String username = ".com";
		final String password = "";

		props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.user", username);
		props.put("mail.smtp.password", password);

		this.session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}


	public void sendEmail(Email email) {
		
		System.out.println("Trying to send email TO " + email.getToEmailAddress() + " with ID -> " + email.getId());
		try {  
			MimeMessage message = new MimeMessage(session);  
			message.setFrom(new InternetAddress(email.getFromEmailAddress()));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(email.getToEmailAddress())); 
			message.setSubject(email.getSubject());  
			message.setText(email.getBody());  

			Transport transport = session.getTransport("smtp");
			transport.connect(this.props.getProperty("mail.smtp.host"), 
					Integer.parseInt(this.props.getProperty("mail.smtp.port")),
					this.props.getProperty("mail.smtp.user"),
					this.props.getProperty("mail.smtp.password"));            
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			DataSource.updateSentEmail(email.getId());
			System.out.println("message sent successfully to " + email.getToEmailAddress());  
		} catch (Exception e) {
			System.out.println("Unable to send email from " + email.getFromEmailAddress() + " to " + email.getToEmailAddress());
			e.printStackTrace();
			throw new RuntimeException(e);
		}  



	}

}
