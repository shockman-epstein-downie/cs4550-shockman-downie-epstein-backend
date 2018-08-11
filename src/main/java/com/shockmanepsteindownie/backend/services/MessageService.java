package com.shockmanepsteindownie.backend.services;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shockmanepsteindownie.backend.models.Message;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.MessageRepository;
import com.shockmanepsteindownie.backend.repositories.UserRepository;

@CrossOrigin(origins={"http://localhost:3000", "https://designs-r-us.herokuapp.com"}, allowCredentials="true")
@RestController
public class MessageService {
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired 
	UserRepository userRepository;
	
	@DeleteMapping("/api/message/{mid}")
	public ResponseEntity<Boolean> deleteMessage(@PathVariable("mid") int mid, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Message> opt = messageRepository.findById(mid);
		if (currentUser == null || !opt.isPresent()) {
			return ResponseEntity.badRequest().body(false);
		}
		Message message = opt.get();
		if (currentUser.getId() == message.getSenderId()) {
			message.setSenderDeleted(true);
		} else if (currentUser.getId() == message.getRecipientId()) {
			message.setRecipientDeleted(true);
		} else {
			return ResponseEntity.badRequest().body(false);
		}
		
		if (message.isRecipientDeleted() && message.isSenderDeleted()) {
			messageRepository.deleteById(mid);
		} else {
			messageRepository.save(message);
		}
		return ResponseEntity.ok(true);
	}
	
	@PostMapping("/api/profile/message/{username}")
	public ResponseEntity<Message> sendMessage(@PathVariable("username") String recipientUsername, @RequestBody Message message, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		User recipient = userRepository.findUserByCredentials(recipientUsername);
		if (currentUser == null || recipient == null) {
			return ResponseEntity.badRequest().body(null);
		}
		message.setCreated(new Date());
		message.setRecipient(recipient);
		message.setRecipientId(recipient.getId());
		message.setRecipientDeleted(false);
		message.setSender(currentUser);
		message.setSenderId(currentUser.getId());
		message.setSenderDeleted(false);
		Message newMessage = messageRepository.save(message);
		return ResponseEntity.ok(newMessage);
	}
	
}
