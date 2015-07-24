package com.email.sender.BulkEmailSender;

/**
 * @author vaibhav
 *
 */

public class MailThread implements Runnable {
	
	private Email email;
	private MailingService mailingService;
	
	public MailThread(Email email,MailingService mailingService) {
		this.email = email;
		this.mailingService = mailingService;
	}

	public void run() {
		mailingService.sendEmail(email);
	}

}
