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

import com.shockmanepsteindownie.backend.models.BlogPost;
import com.shockmanepsteindownie.backend.models.Comment;
import com.shockmanepsteindownie.backend.models.User;
import com.shockmanepsteindownie.backend.repositories.BlogPostRepository;
import com.shockmanepsteindownie.backend.repositories.CommentRepository;
import com.shockmanepsteindownie.backend.repositories.UserRepository;

@CrossOrigin(origins={"http://localhost:3000", "https://designs-r-us.herokuapp.com"}, allowCredentials="true")
@RestController
public class BlogPostService {
	@Autowired
	BlogPostRepository blogPostRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("/api/blogPost")
	public ResponseEntity<List<BlogPost>> findAllBlogPosts() {
		return ResponseEntity.ok((List<BlogPost>) blogPostRepository.findAll());
	}
	
	@GetMapping("/api/blogPost/{bpid}")
	public ResponseEntity<BlogPost> findBlogPost(@PathVariable("bpid") int bpid) {
		Optional<BlogPost> opt = blogPostRepository.findById(bpid);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@PostMapping("/api/blogPost/{bpid}")
	public ResponseEntity<BlogPost> updateBlogPost(@PathVariable("bpid") int bpid, @RequestBody BlogPost blogPost) {
		Optional<BlogPost> opt = blogPostRepository.findById(bpid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		Date now = new Date();
		BlogPost dbBlogPost = opt.get();
		dbBlogPost.setTitle(blogPost.getTitle());
		dbBlogPost.setDescription(blogPost.getDescription());
		dbBlogPost.setModified(now);
		BlogPost newBlogPost = blogPostRepository.save(dbBlogPost);
		return ResponseEntity.ok(newBlogPost);
	}
	
	@DeleteMapping("/api/blogPost/{bpid}")
	public void deleteBlogPost(@PathVariable("bpid") int bpid) {
		blogPostRepository.deleteById(bpid);
	}
	
	@PutMapping("/api/user/{uid}/blogPost")
	public ResponseEntity<BlogPost> createBlogPost(@PathVariable("uid") int uid, @RequestBody BlogPost blogPost) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User owner = opt.get();
		if (!owner.getRole().equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		Date now = new Date();
		blogPost.setCreated(now);
		blogPost.setModified(now);
		blogPost.setOwner(owner);
		blogPost.setOwnerId(owner.getId());
		BlogPost newBlogPost = blogPostRepository.save(blogPost);
		return ResponseEntity.ok(newBlogPost);
	}
	
	@GetMapping("/api/user/{uid}/blogPost")
	public ResponseEntity<List<BlogPost>> findAllBlogPostsForUser(@PathVariable("uid") int uid) {
		Optional<User> opt = userRepository.findById(uid);
		if (!opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		User user = opt.get();
		if (!user.getRole().equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}
		List<BlogPost> blogPosts = user.getBlogPosts();
		if (blogPosts == null) {
			blogPosts = new ArrayList<BlogPost>();
		}
		return ResponseEntity.ok(blogPosts);
	}
	
	@PostMapping("/api/blogPost/search")
	public ResponseEntity<List<BlogPost>> searchBlogPostLike(@RequestBody String titleQuery) {
		List<BlogPost> blogPosts = blogPostRepository.searchTitleLike(titleQuery);
		return ResponseEntity.ok(blogPosts);
	}
	
	@PostMapping("/api/blogPost/{bpid}/comment")
	public ResponseEntity<Comment> addComment(@PathVariable("bpid") int bpid, @RequestBody Comment comment, HttpSession session) {
		User user = (User) session.getAttribute("currentUser");
		Optional<BlogPost> opt = blogPostRepository.findById(bpid);
		if (user == null || !opt.isPresent()) {
			return ResponseEntity.badRequest().body(null);
		}
		comment.setBlogPost(opt.get());
		comment.setOwner(user);
		comment.setCreated(new Date());
		Comment newComment = commentRepository.save(comment);
		return ResponseEntity.ok(newComment);
	}
}
