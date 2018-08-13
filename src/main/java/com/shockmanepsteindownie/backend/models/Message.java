package com.shockmanepsteindownie.backend.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JsonIgnore
	private User sender;
	@Column(name="_sender_id")
	private int senderId;
	private boolean senderDeleted;
	
	@ManyToOne
	@JsonIgnore
	private User recipient;
	@Column(name="_recipient_id")
	private int recipientId;
	private boolean recipientDeleted;
	
	private String subject;
	
	@Column(length=2000)
	private String body;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public boolean isSenderDeleted() {
		return senderDeleted;
	}

	public void setSenderDeleted(boolean senderDeleted) {
		this.senderDeleted = senderDeleted;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	public int getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}

	public boolean isRecipientDeleted() {
		return recipientDeleted;
	}

	public void setRecipientDeleted(boolean recipientDeleted) {
		this.recipientDeleted = recipientDeleted;
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	
}
