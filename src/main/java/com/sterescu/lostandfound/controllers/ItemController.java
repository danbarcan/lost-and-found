package com.sterescu.lostandfound.controllers;

import com.sterescu.lostandfound.payloads.ItemPayload;
import com.sterescu.lostandfound.payloads.ItemResponsePayload;
import com.sterescu.lostandfound.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(final ItemService itemService) {
        this.itemService = itemService;
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @GetMapping("/item")
    public ResponseEntity<List<ItemResponsePayload>> getAllItems() {
        return itemService.getAllItems();
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @DeleteMapping("/item/{id}")
    public ResponseEntity<List<ItemResponsePayload>> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @PostMapping("/item")
    public ResponseEntity<List<ItemResponsePayload>> saveItem(@Valid @ModelAttribute ItemPayload itemPayload) throws IOException {
        return itemService.saveItem(itemPayload);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @PutMapping("/item/{id}")
    public ResponseEntity<List<ItemResponsePayload>> updateItem(@PathVariable Long id, @Valid @ModelAttribute ItemPayload itemPayload) throws IOException {
        return itemService.updateItem(id, itemPayload);
    }
}
