package com.email.sender.BulkEmailSender;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author vaibhav
 *
 */

public class DataSource { 

	private Connection connection;

	public DataSource(Properties prop) {
		
		String user = prop.getProperty("db_user");
		String password = prop.getProperty("db_password");
		String url = prop.getProperty("db_url");
		try {
			connection = DriverManager.getConnection(url,user,password);
		} catch (Exception e) {
			System.out.println("Error in getting database connection or check properties file");
		}

	}


/*	public static Connection initDataSource() {
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
	}*/



	public Connection getConnection() {

		try {
			return connection;
		} catch (Exception e) {
			System.out.println("Failed to get database connection");
			//System.exit(0);
			e.printStackTrace();
		}
		return null;
	}


	public ArrayList<Email> fetchEmails(int limit)
	{

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


		String insertTableSQL = "INSERT INTO mail(`FROM`,`TO`,`SUBJECT`,`BODY`) VALUES (?,?,?,?)";
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

	public void updateMailStatusSending(int id) {

		String updateQuery = "UPDATE mail SET `SENT_STATUS`=? WHERE `ID`=?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setInt(1, 2);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			System.out.println("Unable to update Mail Sent Status for ID -> " + id + " due to " + e.getMessage());
		}
	}

	public void updateSentEmail(int id) {

		String updateQuery = "UPDATE mail SET `SENT_STATUS`=? WHERE `ID`=?;";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setInt(1, 1);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			System.out.println("Unable to update Mail Sent Status for ID -> " + id + " due to " + e.getMessage());
		}
	}

	public void updateMailStatusNotSent(int id) {

		String updateQuery = "UPDATE mail SET `SENT_STATUS`=? WHERE `ID`=?;";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setInt(1,3);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();
		} catch(Exception e) {
			System.out.println("Unable to update Mail Sent Status for ID -> " + id + " due to " + e.getMessage());
		} 
	}
	public void setupData(int num) {

		ResultSet rs = null;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();
			String query = "select count(*) from mail";
			pstmt = connection.prepareStatement(query);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int numberOfRows = rs.getInt(1);
				if(numberOfRows==0) {
					System.out.println("No data found in mail table, so inserting data");
					this.insertIntoDatabase(num);
				}
				else {
					System.out.println("Data Found in mail table, no need to populate data");
					query = "update mail set `SENT_STATUS`=0";
					pstmt = connection.prepareStatement(query);
					pstmt.executeUpdate();
				}

			} else {
				System.out.println("error: could not get the record counts");
				//System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Error in setting up database " + e.getLocalizedMessage());
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void closeConnection() {
		try {
			connection.close();
		} catch(Exception e) {
			System.out.println("Error in closing database connection due to " + e.getMessage());
		}
	}










	public void generateReport() {

		ResultSet rs = null;
		Connection connection = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		int sent = 0,failed = 0;
		try {
			connection = getConnection();
			String query = "select count(*) from mail where `SENT_STATUS`=?";
			pstmt1 = connection.prepareStatement(query);
			pstmt1.setInt(1, 1);
			rs = pstmt1.executeQuery();
			if (rs.next()) {
				sent = rs.getInt(1);
			}
			pstmt2 = connection.prepareStatement(query);
			pstmt2.setInt(1, 3);
			rs = pstmt2.executeQuery();
			if (rs.next()) {
				failed = rs.getInt(1);
			}

			System.out.println("\n====================Results===========================\n");
			int total = sent + failed;
			System.out.println("Total Emails ->" + total);
			System.out.println("Sent Emails -> " +  sent);
			System.out.println("Failed Emails -> " + failed);


		} catch (Exception e) {
			System.out.println("Error in setting up database " + e.getLocalizedMessage());
		} finally {
			try {
				pstmt1.close();
				pstmt2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
