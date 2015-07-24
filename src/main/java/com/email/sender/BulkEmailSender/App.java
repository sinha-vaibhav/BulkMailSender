package com.email.sender.BulkEmailSender;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @author vaibhav
 *
 */
public class App 
{
	public static void main( String[] args )
	{
		//Getting the properties file from arguments
		if (args.length < 1) {
			System.out.println("proper arguments are not passed");
			System.exit(0);
		}
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(args[0]));
		} catch (Exception e) {
			System.out.println("Error in reading properties file");
			System.exit(0);
		}
		
		DataSource dataSource = new DataSource(props);
		MailingService mailService = new MailingService(dataSource,props);
		
		System.out.println("\n\n\t====Welcome to Mail Sender Application===\n\n ");
		
		
		//creating a thread pool of 20 threads to send emails
		ExecutorService executor = Executors.newFixedThreadPool(20);
		
		//setting up database by inserting 100 dummy records in the mail table if required
		dataSource.setupData(props.getProperty("mail.smtp.user"),100);
		System.out.println("Sending emails now");
		while(true) {
			
			//get 20 mails which are not sent (have SENT_STATUS=1
			ArrayList<Email> emails= dataSource.fetchEmails(20);
			if(null==emails || emails.size()==0)
				break;
			for(Email email : emails) {
				
				//First updating the mail status as in process of sending(SENT_STATUS=2 so that it is not
				//fetched again it is sent by any other previous threads
				dataSource.updateMailStatusSending(email.getId());
				executor.submit(new MailThread(email,mailService));
			}	
			
		}
		executor.shutdown();
		
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			//waiting for the executor servce to shut down to generate report afterwards
			dataSource.generateReport();
			dataSource.closeConnection();
		} catch (InterruptedException e) {
		  System.out.println(e.getMessage());
		}
	}
}
