package com.sterescu.lostandfound.services;

import com.sterescu.lostandfound.entities.Image;
import com.sterescu.lostandfound.entities.Item;
import com.sterescu.lostandfound.entities.User;
import com.sterescu.lostandfound.payloads.ItemPayload;
import com.sterescu.lostandfound.payloads.ItemResponsePayload;
import com.sterescu.lostandfound.repositories.ImageRepository;
import com.sterescu.lostandfound.repositories.ItemRepository;
import com.sterescu.lostandfound.repositories.UserRepository;
import com.sterescu.lostandfound.security.UserPrincipal;
import com.sterescu.lostandfound.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private ItemRepository itemRepository;
    private UserRepository userRepository;
    private ImageRepository imageRepository;

    @Autowired
    public ItemService(final ItemRepository itemRepository, final UserRepository userRepository, final ImageRepository imageRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public ResponseEntity<List<ItemResponsePayload>> getAllItems() {
        return ResponseEntity.ok(mapListOfItemsToResponse(itemRepository.findAll()));
    }

    public ResponseEntity<List<ItemResponsePayload>> saveItem(ItemPayload itemPayload) throws IOException {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Image image = Image.builder()
                .contentType(itemPayload.getImage().getContentType())
                .name(itemPayload.getImage().getName())
                .size(itemPayload.getImage().getSize())
                .data(itemPayload.getImage().getBytes())
                .build();
        image = imageRepository.save(image);

        Item item = Item.builder()
                .name(itemPayload.getName())
                .description(itemPayload.getDescription())
                .location(itemPayload.getLocation())
                .createdAt(Timestamp.from(Instant.now()))
                .foundBy(user.get())
                .image(image)
                .build();

        itemRepository.save(item);

        return getAllItems();
    }

    public ResponseEntity<List<ItemResponsePayload>> updateItem(Long id, ItemPayload itemPayload) throws IOException {
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Optional<Item> itemOptional = itemRepository.findById(id);
        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Item item = itemOptional.get();

        if (!item.getFoundBy().getId().equals(user.get().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        Image oldImage = null;

        if (itemPayload.getImage() != null) {
            Image image = Image.builder()
                    .contentType(itemPayload.getImage().getContentType())
                    .name(itemPayload.getImage().getName())
                    .size(itemPayload.getImage().getSize())
                    .data(itemPayload.getImage().getBytes())
                    .build();
            image = imageRepository.save(image);

            oldImage = item.getImage();
            item.setImage(image);
        }

        item.setDescription(itemPayload.getDescription());
        item.setLocation(itemPayload.getLocation());
        item.setName(itemPayload.getName());
        itemRepository.save(item);

        if (oldImage != null) {
            imageRepository.delete(oldImage);
        }

        return getAllItems();
    }

    public ResponseEntity<List<ItemResponsePayload>> deleteItem(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (!itemOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Item car = itemOptional.get();
        UserPrincipal userPrincipal = AppUtils.getCurrentUserDetails();
        if (!userPrincipal.getId().equals(car.getFoundBy().getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        itemRepository.delete(itemOptional.get());

        return getAllItems();
    }

    private List<ItemResponsePayload> mapListOfItemsToResponse(List<Item> items) {
        return items.stream().map(ItemResponsePayload::createJobResponsePayloadFromJob).collect(Collectors.toList());
    }
}
