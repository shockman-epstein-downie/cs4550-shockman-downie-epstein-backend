package com.shockmanepsteindownie.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shockmanepsteindownie.backend.models.Listing;

public interface ListingRepository extends CrudRepository<Listing, Integer>{

	@Query("SELECT l FROM Listing l WHERE UPPER(l.title) LIKE CONCAT('%',UPPER(:title),'%')")
	public List<Listing> searchTitleLike(@Param("title") String title);
	
	@Query("SELECT l FROM Listing l WHERE l._ownerId=:uid")
	public List<Listing> getUserListings(@Param("uid") int uid);
}
