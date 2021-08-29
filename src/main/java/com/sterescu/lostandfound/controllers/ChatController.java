package com.sterescu.lostandfound.controllers;

import com.sterescu.lostandfound.entities.ChatMessage;
import com.sterescu.lostandfound.entities.Item;
import com.sterescu.lostandfound.payloads.ChatMessagePayload;
import com.sterescu.lostandfound.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private ChatService chatService;

    @Autowired
    public ChatController(final ChatService chatService) {
        this.chatService = chatService;
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @PostMapping("/chat")
    //@PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<ChatMessage>> postMessage(@Valid @RequestBody ChatMessagePayload chatMessagePayload) {
        return chatService.postMessage(chatMessagePayload);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/chat/{itemId}")
    //@PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<ChatMessage>> getMessagesByJobId(@PathVariable(value = "itemId") Long itemId) {
        return chatService.getMessagesByJobId(itemId);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/chat/unread")
    //@PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<Map<Long, Long>> getUnreadMessagesByJob() {
        return chatService.getUnreadMessagesByJob();
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/chat")
    //@PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<Map<Long, List<ChatMessage>>> getMessagesGroupByJob() {
        return chatService.getMessagesGroupByJob();
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/chat/items")
    //@PreAuthorize("hasRole('USER') OR hasRole('ADMIN') OR hasRole('SERVICE')")
    public ResponseEntity<List<Item>> getAllJobsWithMessages() {
        return chatService.getAllJobsWithMessages();
    }

}
