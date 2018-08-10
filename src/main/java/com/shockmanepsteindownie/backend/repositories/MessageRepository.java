package com.shockmanepsteindownie.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.shockmanepsteindownie.backend.models.Message;

public interface MessageRepository extends CrudRepository<Message, Integer>{

}
