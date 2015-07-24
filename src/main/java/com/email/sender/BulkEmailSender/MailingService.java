package com.email.sender.BulkEmailSender;

/**
 * @author vaibhav
 *
 */

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailingService {

	private Session session;
	private Properties props;
	private DataSource dataSource;

	public MailingService(DataSource dataSource, Properties props) {

		this.props = props;
		this.dataSource = dataSource;
		final String username = props.getProperty("mail.smtp.user");
		final String password = props.getProperty("mail.smtp.password");
		
		this.session = Session.getInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}


	public void sendEmail(Email email) {
		
		System.out.println("Trying to send email T0 " + email.getToEmailAddress() + " with ID -> " + email.getId());
		try {  
			MimeMessage message = new MimeMessage(Session.getDefaultInstance(props));  
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
			dataSource.updateSentEmail(email.getId());
			System.out.println("Message sent successfully to " + email.getToEmailAddress());
		} catch (Exception e) {
			System.out.println("Unable to send email from " + email.getFromEmailAddress() + " to " + email.getToEmailAddress());
			dataSource.updateMailStatusNotSent(email.getId());
			throw new RuntimeException(e);
		}  
	}

}
