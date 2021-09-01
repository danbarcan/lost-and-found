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

    @PostMapping("/chat")
    public ResponseEntity<List<ChatMessage>> postMessage(@Valid @RequestBody ChatMessagePayload chatMessagePayload) {
        return chatService.postMessage(chatMessagePayload);
    }

    @GetMapping("/chat/{itemId}")
    public ResponseEntity<List<ChatMessage>> getMessagesByJobId(@PathVariable(value = "itemId") Long itemId) {
        return chatService.getMessagesByJobId(itemId);
    }

    @GetMapping("/chat/unread")
    public ResponseEntity<Map<Long, Long>> getUnreadMessagesByJob() {
        return chatService.getUnreadMessagesByJob();
    }

    @GetMapping("/chat")
    public ResponseEntity<Map<Long, List<ChatMessage>>> getMessagesGroupByJob() {
        return chatService.getMessagesGroupByJob();
    }

    @GetMapping("/chat/items")
    public ResponseEntity<List<Item>> getAllJobsWithMessages() {
        return chatService.getAllJobsWithMessages();
    }

}
