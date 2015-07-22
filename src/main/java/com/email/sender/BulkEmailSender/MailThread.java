package com.email.sender.BulkEmailSender;

public class MailThread implements Runnable {
	
	private Email email;
	
	public MailThread(Email email) {
		this.email = email;
	}

	public void run() {
		MailingService.sendEmail(email);
	}

}
