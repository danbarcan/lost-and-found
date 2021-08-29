package com.sterescu.lostandfound.repositories;

import com.sterescu.lostandfound.entities.Item;
import com.sterescu.lostandfound.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByFoundByOrderByCreatedAtDesc(User user);

}