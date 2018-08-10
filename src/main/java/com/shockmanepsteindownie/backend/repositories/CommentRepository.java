package com.shockmanepsteindownie.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.shockmanepsteindownie.backend.models.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>{

}
