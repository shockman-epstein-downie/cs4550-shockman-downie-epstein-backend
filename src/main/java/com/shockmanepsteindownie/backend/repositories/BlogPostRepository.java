package com.shockmanepsteindownie.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shockmanepsteindownie.backend.models.BlogPost;

public interface BlogPostRepository extends CrudRepository<BlogPost, Integer>{
	
	@Query("SELECT bp FROM BlogPost bp WHERE UPPER(bp.title) LIKE CONCAT('%',UPPER(:title),'%')")
	public List<BlogPost> searchTitleLike(@Param("title") String title);

	@Query("SELECT bp FROM BlogPost bp WHERE bp._ownerId=:uid")
	public List<BlogPost> getUserBlogPosts(@Param("uid") int uid);
}
