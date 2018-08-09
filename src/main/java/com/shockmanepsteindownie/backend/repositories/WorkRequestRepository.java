package com.shockmanepsteindownie.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.shockmanepsteindownie.backend.models.WorkRequest;

public interface WorkRequestRepository extends CrudRepository<WorkRequest, Integer> {
	
	@Query("SELECT wr FROM WorkRequest wr WHERE UPPER(wr.title) LIKE CONCAT('%',UPPER(:title),'%')")
	public List<WorkRequest> searchTitleLike(@Param("title") String title);
}
