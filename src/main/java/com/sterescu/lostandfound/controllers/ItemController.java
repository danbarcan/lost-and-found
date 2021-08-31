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

    @GetMapping("/item")
    public ResponseEntity<List<ItemResponsePayload>> getAllItems() {
        return itemService.getAllItems();
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<List<ItemResponsePayload>> deleteItem(@PathVariable Long id) {
        return itemService.deleteItem(id);
    }

    @PostMapping("/item")
    public ResponseEntity<List<ItemResponsePayload>> saveItem(@Valid @ModelAttribute ItemPayload itemPayload) throws IOException {
        return itemService.saveItem(itemPayload);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<List<ItemResponsePayload>> updateItem(@PathVariable Long id, @Valid @ModelAttribute ItemPayload itemPayload) throws IOException {
        return itemService.updateItem(id, itemPayload);
    }
}
