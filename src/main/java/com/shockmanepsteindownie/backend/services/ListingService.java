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
import com.shockmanepsteindownie.backend.models.Listing;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.CommentRepository;
import com.shockmanepsteindownie.backend.repositories.ListingRepository;
import com.shockmanepsteindownie.backend.repositories.UserRepository;

@CrossOrigin(origins={"http://localhost:3000", "https://designs-r-us.herokuapp.com"}, allowCredentials="true")
@RestController
public class ListingService {
	@Autowired
	ListingRepository listingRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("/api/listing")
	public ResponseEntity<List<Listing>> findAllListings() {
		return ResponseEntity.ok((List<Listing>) listingRepository.findAll());
	}
	
	@GetMapping("/api/listing/{lid}")
	public ResponseEntity<Listing> findListing(@PathVariable("lid") int lid) {
		Optional<Listing> opt = listingRepository.findById(lid);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@PostMapping("/api/listing/{lid}")
	public ResponseEntity<Listing> updateListing(@PathVariable("lid") int lid, @RequestBody Listing listing) {
		Optional<Listing> opt = listingRepository.findById(lid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Date now = new Date();
		Listing dbListing = opt.get();
		dbListing.setTitle(listing.getTitle());
		dbListing.setDescription(listing.getDescription());
		dbListing.setRate(listing.getRate());
		dbListing.setImageSrcs(listing.getImageSrcs());
		dbListing.setModified(now);
		Listing newListing = listingRepository.save(dbListing);
		return ResponseEntity.ok(newListing);
	}
	
	@DeleteMapping("/api/listing/{lid}")
	public void deleteListing(@PathVariable("lid") int lid) {
		listingRepository.deleteById(lid);
	}
	
	@PutMapping("/api/user/{uid}/listing")
	public ResponseEntity<Listing> createListing(@PathVariable("uid") int uid, @RequestBody Listing listing) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User owner = opt.get();
		if (!owner.getRole().equals("DESIGNER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Date now = new Date();
		listing.setCreated(now);
		listing.setModified(now);
		listing.setOwner(owner);
		listing.setOwnerId(owner.getId());
		Listing newListing = listingRepository.save(listing);
		return ResponseEntity.ok(newListing);
	}
	
	@GetMapping("/api/user/{uid}/listing")
	public ResponseEntity<List<Listing>> findAllListingsForUser(@PathVariable("uid") int uid) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User user = opt.get();
		if (!user.getRole().equals("DESIGNER")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		List<Listing> listings = user.getListings();
		if (listings == null) {
			listings = new ArrayList<Listing>();
		}
		return ResponseEntity.ok(listings);
	}
	
	@PostMapping("/api/listing/search")
	public ResponseEntity<List<Listing>> searchListingLike(@RequestBody String titleQuery) {
		List<Listing> listings = listingRepository.searchTitleLike(titleQuery);
		return ResponseEntity.ok(listings);
	}
	
	@PostMapping("/api/listing/{lid}/comment")
	public ResponseEntity<Comment> addComment(@PathVariable("lid") int lid, @RequestBody Comment comment, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		Optional<Listing> opt = listingRepository.findById(lid);
		if (user == null || !opt.isPresent()) {
			return ResponseEntity.badRequest().body(null);
		}
		comment.setListing(opt.get());
		comment.setOwner(user);
		comment.setCreated(new Date());
		Comment newComment = commentRepository.save(comment);
		return ResponseEntity.ok(newComment);
	}
}
