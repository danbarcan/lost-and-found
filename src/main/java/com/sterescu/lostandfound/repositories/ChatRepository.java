package com.sterescu.lostandfound.repositories;

import com.sterescu.lostandfound.entities.ChatMessage;
import com.sterescu.lostandfound.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "chat", path = "chat")
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByItem(Item item);

    List<ChatMessage> findAllByItemIn(List<Item> jobs);
}
