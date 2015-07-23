package com.email.sender.BulkEmailSender;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class DataSource { 

	private static Connection connection = initDataSource();

	public static Connection initDataSource() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("app.properties");
			prop.load(input);
			String user = prop.getProperty("db_user");
			String password = prop.getProperty("db_password");
			String url = prop.getProperty("db_url");
			connection = DriverManager.getConnection(url,user,password);
			return connection;

		} catch (Exception e) {
			System.out.println("Error in reading properties file. Check app.properties file");
			System.exit(0);
			e.printStackTrace();
		}
		return null;
	}

	public Connection getConnection() {

		try {
			return connection;
			//return DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			System.out.println("Failed to get database connection");
			System.exit(0);
			e.printStackTrace();
		}
		return null;
	}


	public ArrayList<Email> fetchEmails(int limit)
	{

		Connection connection = this.getConnection();
		try {
			String selectQuery = "SELECT `ID`,`FROM`,`TO`,`SUBJECT`,`BODY` from mail WHERE `SENT_STATUS`=? LIMIT ?";
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
			preparedStatement.setInt(1, 0);
			preparedStatement.setInt(2, limit);
			ResultSet result = preparedStatement.executeQuery();
			ArrayList<Email> emails = new ArrayList<Email>();
			while (result.next()) {
				int id = result.getInt("ID");
				String from = result.getString("FROM");
				String to = result.getString("TO");
				String subject = result.getString("SUBJECT");
				String body = result.getString("BODY");
				Email email = new Email(id,from,to,subject,body);
				emails.add(email);
			}
			return emails;		
		} catch(Exception e) {
			System.out.println("Error in fetching records from the database");
			e.printStackTrace();
		}
		return null;
	}

	public void insertIntoDatabase(int number) {

		Connection connection = this.getConnection();
		String insertTableSQL = "INSERT INTO MAIL(`FROM`,`TO`,`SUBJECT`,`BODY`) VALUES (?,?,?,?)";
		for(int i=0;i<number;i++) {

			try {
				System.out.println("Inserting record number " + i + " into database");
				String from = "from_" + String.valueOf(i) + "@mailinator.com";
				String to = "to_" + String.valueOf(i) + "@mailinator.com";
				String subject = "Test Mail from " + from + " to " + to;
				String body = "This is a test email body for " + subject;

				PreparedStatement preparedStatement = connection.prepareStatement(insertTableSQL);
				preparedStatement.setString(1, from);
				preparedStatement.setString(2, to);
				preparedStatement.setString(3, subject);
				preparedStatement.setString(4, body);
				preparedStatement .executeUpdate();

			} catch(Exception e) {
				System.out.println("Error in inserting into database, check database connection");
				e.printStackTrace();
			}
		}
	}
	
	public static void updateSentEmail(int id) {
		
		String updateQuery = "UPDATE MAIL SET `SENT_STATUS`=? WHERE `ID`=?;";
		try {
		PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
		preparedStatement.setInt(1, 1);
		preparedStatement.setInt(2, id);
		} catch(Exception e) {
			System.out.println("Unable to update Mail Sent Status for ID -> " + id);
		}
		
		
	}









}
