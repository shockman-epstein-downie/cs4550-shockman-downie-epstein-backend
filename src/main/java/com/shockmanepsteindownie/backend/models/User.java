package com.shockmanepsteindownie.backend.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	private String name;
	private String role;
	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;
	@OneToMany(mappedBy="owner")
	private List<WorkRequest> workRequests;
	@OneToMany(mappedBy="owner")
	private List<Listing> listings;
	@OneToMany(mappedBy="owner")
	private List<BlogPost> blogPosts;
	@OneToMany(mappedBy="owner")
	@JsonIgnore
	private List<Comment> comments;
	@OneToMany(mappedBy="sender")
	private List<Message> sentMessages;
	@OneToMany(mappedBy="recipient")
	private List<Message> receivedMessages;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public List<WorkRequest> getWorkRequests() {
		return workRequests;
	}
	public void setWorkRequests(List<WorkRequest> workRequests) {
		this.workRequests = workRequests;
	}
	public List<Listing> getListings() {
		return listings;
	}
	public void setListings(List<Listing> listings) {
		this.listings = listings;
	}
	public List<BlogPost> getBlogPosts() {
		return blogPosts;
	}
	public void setBlogPosts(List<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<Message> getSentMessages() {
		return sentMessages;
	}
	public void setSentMessages(List<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}
	public List<Message> getReceivedMessages() {
		return receivedMessages;
	}
	public void setReceivedMessages(List<Message> receivedMessages) {
		this.receivedMessages = receivedMessages;
	}
}
