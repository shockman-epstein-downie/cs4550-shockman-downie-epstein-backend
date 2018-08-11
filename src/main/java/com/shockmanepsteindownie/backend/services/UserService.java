package com.shockmanepsteindownie.backend.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shockmanepsteindownie.backend.models.BlogPost;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.BlogPostRepository;
import com.shockmanepsteindownie.backend.repositories.UserRepository;

@CrossOrigin(origins={"http://localhost:3000", "https://designs-r-us.herokuapp.com"}, allowCredentials="true")
@RestController
public class UserService {

	@Autowired
	UserRepository repository;
	
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@PostMapping("/api/user")
	public User createUser(@RequestBody User user, HttpSession session) {
		User currentUser = repository.save(user);
		session.setAttribute("currentUser", currentUser);
		return currentUser;
	}
	
	@PostMapping("/api/login")
	public ResponseEntity<User> login(@RequestBody User user, HttpSession session) {
		user = repository.findUserByCredentials(user.getUsername(), user.getPassword());
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	
		session.setAttribute("currentUser", user);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("/api/user")
	public ResponseEntity<List<User>> findAllUsers() {
		return ResponseEntity.ok((List<User>) repository.findAll());
	}
	
	@PostMapping("/api/logout")
	public void logout(HttpSession session) {
		session.setAttribute("currentUser", null);
	}
	
	@GetMapping("/api/user/{userId}")
	public ResponseEntity<User> findUserById(@PathVariable("userId") int id) {
		Optional<User> user = repository.findById(id);
		if (user.isPresent()) {
			return ResponseEntity.ok(user.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@DeleteMapping("/api/user/{userId}")
	public void deleteUser(@PathVariable("userId") int id) {
		repository.deleteById(id);
	}
	
	@GetMapping("/api/profile")
	public ResponseEntity<User> getProfile(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		Optional<User> opt = repository.findById(currentUser.getId());
		if (opt.isPresent()) {
			User user = opt.get();
			session.setAttribute("currentUser", user);
			return ResponseEntity.ok(user);
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@DeleteMapping("/api/profile")
	public void deleteProfile(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser != null) {
			repository.deleteById(currentUser.getId());
			session.setAttribute("currentUser", null);
		}
	}
	
	@GetMapping("/api/profile/blogPost")
	public ResponseEntity<List<BlogPost>> getProfileBlogPosts(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser == null) {
			return ResponseEntity.badRequest().body(null);
		}
		List<BlogPost> blogPosts = blogPostRepository.getUserBlogPosts(currentUser.getId());
		return ResponseEntity.ok(blogPosts);
	}
}
