package com.email.sender.BulkEmailSender;

import java.io.FileInputStream;
import java.io.IOException;
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
		
		if (args.length < 1) {
			System.out.println("proper arguments are not passed");
			System.exit(0);
		}
		Properties dbProps = new Properties();
		Properties mailProps = new Properties();
		try {
			dbProps.load(new FileInputStream(args[0]));
			//dbProps.load(new FileInputStream("app.properties"));
			//mailProps.load(new FileInputStream("app.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			//System.exit(0);
		}
		
		DataSource dataSource = new DataSource(dbProps);
		
		System.out.println("\n\n\t====Welcome to Mail Sender Application===\n\n ");
		
		
		MailingService mailService = new MailingService(dataSource,dbProps);
		ExecutorService executor = Executors.newFixedThreadPool(20);
		dataSource.setupData(100);
		System.out.println("Sending emails now");
		while(true) {
			ArrayList<Email> emails= dataSource.fetchEmails(20);
			if(null==emails || emails.size()==0)
				break;
			for(Email email : emails) {
				dataSource.updateMailStatusSending(email.getId());
				executor.submit(new MailThread(email,mailService));
			}	
			
		}
		executor.shutdown();
		
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
			dataSource.generateReport();
		} catch (InterruptedException e) {
		  System.out.println(e.getMessage());
		}
	}
}
