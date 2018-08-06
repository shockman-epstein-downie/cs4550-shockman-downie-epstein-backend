package com.shockmanepsteindownie.backend.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.UserRepository;

@CrossOrigin(origins="*")
@RestController
public class UserService {

	@Autowired
	UserRepository repository;
	
	@PostMapping("/api/user")
	public User createUser(@RequestBody User user, HttpSession session) {
		User currentUser = repository.save(user);
		session.setAttribute("currentUser", currentUser);
		return currentUser;
	}
	
	@PostMapping("/api/login")
	public User login(@RequestBody User user, HttpSession session) {
		user = repository.findUserByCredentials(user.getUsername(), user.getPassword());
		session.setAttribute("currentUser", user);
		return user;
	}
	
	@GetMapping("/api/user")
	public List<User> findAllUsers() {
		return (List<User>) repository.findAll();
	}
	
	@PostMapping("/api/logout")
	public void logout(HttpSession session) {
		session.setAttribute("currentUser", null);
	}
	
	@GetMapping("/api/user/{userId}")
	public Optional<User> findUserById(@PathVariable("userId") int id) {
		return repository.findById(id);
	}
	
	@DeleteMapping("/api/user/{userId}")
	public void deleteUser(@PathVariable("userId") int id) {
		repository.deleteById(id);
	}
	
	@GetMapping("/api/profile")
	public User getProfile(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		return currentUser;
	}
}
