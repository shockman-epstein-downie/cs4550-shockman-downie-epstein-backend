package com.shockmanepsteindownie.backend.services;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shockmanepsteindownie.backend.models.Comment;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.models.WorkRequest;
import com.shockmanepsteindownie.backend.repositories.CommentRepository;
import com.shockmanepsteindownie.backend.repositories.UserRepository;
import com.shockmanepsteindownie.backend.repositories.WorkRequestRepository;

@CrossOrigin(origins={"http://localhost:3000", "https://designs-r-us.herokuapp.com"}, allowCredentials="true")
@RestController
public class WorkRequestService {
	@Autowired
	WorkRequestRepository workRequestRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("/api/workRequest")
	public ResponseEntity<List<WorkRequest>> findAllWorkRequests() {
		return ResponseEntity.ok((List<WorkRequest>) workRequestRepository.findAll());
	}
	
	@GetMapping("/api/workRequest/{wrid}")
	public ResponseEntity<WorkRequest> findWorkRequest(@PathVariable("wrid") int wrid) {
		Optional<WorkRequest> opt = workRequestRepository.findById(wrid);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@PostMapping("/api/workRequest/{wrid}")
	public ResponseEntity<WorkRequest> updateWorkRequest(@PathVariable("wrid") int wrid, @RequestBody WorkRequest workRequest) {
		Optional<WorkRequest> opt = workRequestRepository.findById(wrid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Date now = new Date();
		WorkRequest dbWorkRequest = opt.get();
		dbWorkRequest.setTitle(workRequest.getTitle());
		dbWorkRequest.setDescription(workRequest.getDescription());
		dbWorkRequest.setCompensation(workRequest.getCompensation());
		dbWorkRequest.setImageSrcs(workRequest.getImageSrcs());
		dbWorkRequest.setModified(now);
		WorkRequest newWorkRequest = workRequestRepository.save(dbWorkRequest);
		return ResponseEntity.ok(newWorkRequest);
	}
	
	@DeleteMapping("/api/workRequest/{wrid}")
	public void deleteWorkRequest(@PathVariable("wrid") int wrid) {
		workRequestRepository.deleteById(wrid);
	}
	
	@PutMapping("/api/user/{uid}/workRequest")
	public ResponseEntity<WorkRequest> createWorkRequest(@PathVariable("uid") int uid, @RequestBody WorkRequest workRequest) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User owner = opt.get();
		if (!owner.getRole().equals("CLIENT")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Date now = new Date();
		workRequest.setCreated(now);
		workRequest.setModified(now);
		workRequest.setOwner(owner);
		workRequest.setOwnerId(owner.getId());
		WorkRequest newWorkRequest = workRequestRepository.save(workRequest);
		return ResponseEntity.ok(newWorkRequest);
	}
	
	@GetMapping("/api/user/{uid}/workRequest")
	public ResponseEntity<List<WorkRequest>> findAllWorkRequestsForUser(@PathVariable("uid") int uid) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User user = opt.get();
		if (!user.getRole().equals("CLIENT")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		List<WorkRequest> workRequests = user.getWorkRequests();
		if (workRequests == null) {
			workRequests = new ArrayList<WorkRequest>();
		}
		return ResponseEntity.ok(workRequests);
	}
	
	@PostMapping("/api/workRequest/search")
	public ResponseEntity<List<WorkRequest>> searchListingLike(@RequestBody String titleQuery) {
		List<WorkRequest> workRequests = workRequestRepository.searchTitleLike(titleQuery);
		return ResponseEntity.ok(workRequests);
	}
	
	@PostMapping("/api/workRequest/{wrid}/comment")
	public ResponseEntity<Comment> addComment(@PathVariable("wrid") int wrid, @RequestBody Comment comment, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		Optional<WorkRequest> opt = workRequestRepository.findById(wrid);
		if (user == null || !opt.isPresent()) {
			return ResponseEntity.badRequest().body(null);
		}
		comment.setWorkRequest(opt.get());
		comment.setOwner(user);
		comment.setCreated(new Date());
		Comment newComment = commentRepository.save(comment);
		return ResponseEntity.ok(newComment);
	}
}
