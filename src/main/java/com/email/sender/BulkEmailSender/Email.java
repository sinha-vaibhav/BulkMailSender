package com.email.sender.BulkEmailSender;

public class Email {
	
	private int id;
	private String fromEmailAddress;
	private String toEmailAddress;
	private String subject;
	private String body;
	
	
	public Email(int id, String fromEmailAddress, String toEmailAddress,
			String subject, String body) {
		this.id = id;
		this.fromEmailAddress = fromEmailAddress;
		this.toEmailAddress = toEmailAddress;
		this.subject = subject;
		this.body = body;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFromEmailAddress() {
		return fromEmailAddress;
	}
	public void setFromEmailAddress(String fromEmailAddress) {
		this.fromEmailAddress = fromEmailAddress;
	}
	public String getToEmailAddress() {
		return toEmailAddress;
	}
	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Email [id=" + id + ", fromEmailAddress=" + fromEmailAddress
				+ ", toEmailAddress=" + toEmailAddress + ", subject=" + subject
				+ ", body=" + body + "]";
	}
}
