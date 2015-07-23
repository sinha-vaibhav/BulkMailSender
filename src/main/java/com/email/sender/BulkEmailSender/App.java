package com.email.sender.BulkEmailSender;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author vaibhav
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		DataSource dataSource = new DataSource();
		
		System.out.println("\n\n\t====Welcome to Mail Sender Application===\n\n ");
		
		//System.out.println("Checking your database status....");
		MailingService mailService = new MailingService();
		ExecutorService executor = Executors.newFixedThreadPool(20);
		
		while(true) {
			ArrayList<Email> emails= dataSource.fetchEmails(10);
			if(null==emails)
				break;
			for(Email email : emails) {
				executor.submit(new MailThread(email,mailService));
			}	
			
		}
		
		
	}
}
