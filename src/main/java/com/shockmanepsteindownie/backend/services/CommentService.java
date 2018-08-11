package com.shockmanepsteindownie.backend.services;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shockmanepsteindownie.backend.models.Comment;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.CommentRepository;

@CrossOrigin(origins = { "http://localhost:3000", "https://designs-r-us.herokuapp.com" }, allowCredentials = "true")
@RestController
public class CommentService {
	@Autowired
	CommentRepository commentRepository;

	@DeleteMapping("/api/comment/{cid}")
	public ResponseEntity<Boolean> deleteComment(@PathVariable("cid") int cid, HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		Optional<Comment> opt = commentRepository.findById(cid);
		if (currentUser == null || !opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
		}
		Comment comment = opt.get();
		if (comment.getOwner().getId() != currentUser.getId() || !currentUser.getRole().equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(false);
		}
		commentRepository.deleteById(cid);
		return ResponseEntity.ok(true);
	}
}
