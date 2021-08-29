package com.sterescu.lostandfound.services;

import com.sterescu.lostandfound.entities.ChatMessage;
import com.sterescu.lostandfound.entities.Item;
import com.sterescu.lostandfound.entities.User;
import com.sterescu.lostandfound.payloads.ChatMessagePayload;
import com.sterescu.lostandfound.repositories.ChatRepository;
import com.sterescu.lostandfound.repositories.ItemRepository;
import com.sterescu.lostandfound.repositories.UserRepository;
import com.sterescu.lostandfound.security.UserPrincipal;
import com.sterescu.lostandfound.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private ChatRepository chatRepository;

    @Autowired
    public ChatService(final UserRepository userRepository, final ChatRepository chatRepository, final ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.itemRepository = itemRepository;
    }

    public ResponseEntity<Map<Long, Long>> getUnreadMessagesByJob() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<Long, List<ChatMessage>> messagesByJob = getMessagesByJob(userOptional.get());

        Map<Long, Long> unreadMessagesByJob = messagesByJob.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().stream()
                        .filter(chatMessage -> !chatMessage.getFromUser().equals(userOptional.get()) && chatMessage.getUnread()).count()));

        return ResponseEntity.status(HttpStatus.OK).body(unreadMessagesByJob);
    }

    public ResponseEntity<Map<Long, List<ChatMessage>>> getMessagesGroupByJob() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(getMessagesByJob(userOptional.get()));
    }

    public ResponseEntity<List<Item>> getAllJobsWithMessages() {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();
        List<Item> jobs = itemRepository.findAllByFoundByOrderByCreatedAtDesc(fromUser);

        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    public ResponseEntity<List<ChatMessage>> getMessagesByJobId(Long itemId) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Item item = itemOptional.get();
        if (!item.getFoundBy().getId().equals(fromUser.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ChatMessage> chatMessages = chatRepository.findAllByItem(item);
        chatMessages.stream().filter(chatMessage -> !chatMessage.getFromUser().equals(fromUser)).forEach(chatMessage -> chatMessage.setUnread(false));
        chatRepository.saveAll(chatMessages);

        return ResponseEntity.status(HttpStatus.OK).body(chatMessages);
    }

    public ResponseEntity<List<ChatMessage>> postMessage(ChatMessagePayload chatMessagePayload) {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<User> userOptional = userRepository.findByUsername(userPrincipal.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User fromUser = userOptional.get();

        Optional<Item> itemOptional = itemRepository.findById(chatMessagePayload.getItemId());
        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Item item = itemOptional.get();

        ChatMessage message = ChatMessage.builder()
                .message(chatMessagePayload.getMessage())
                .fromUser(fromUser)
                .item(item)
                .timestamp(Timestamp.from(Instant.now()))
                .unread(true)
                .build();

        chatRepository.save(message);

        List<ChatMessage> chatMessages = chatRepository.findAllByItem(item);
        chatMessages.stream().filter(chatMessage -> !chatMessage.getFromUser().equals(fromUser)).forEach(chatMessage -> chatMessage.setUnread(false));
        chatRepository.saveAll(chatMessages);

        return ResponseEntity.status(HttpStatus.OK).body(chatMessages);
    }

    private Map<Long, List<ChatMessage>> getMessagesByJob(User user) {
        List<Item> items = itemRepository.findAllByFoundByOrderByCreatedAtDesc(user);

        List<ChatMessage> chatMessages = chatRepository.findAllByItemIn(items);
        return chatMessages.stream().collect(Collectors.groupingBy(message -> message.getItem().getId()));
    }
}
