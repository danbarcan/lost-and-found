package com.sterescu.lostandfound.payloads;

import com.sterescu.lostandfound.entities.Image;
import com.sterescu.lostandfound.entities.Item;
import com.sterescu.lostandfound.entities.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemResponsePayload implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String location;
    private Timestamp createdAt;
    private Long foundBy;
    private Image image;

    public static ItemResponsePayload createJobResponsePayloadFromJob(Item item) {
        return ItemResponsePayload.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .location(item.getLocation())
                .createdAt(item.getCreatedAt())
                .foundBy(item.getFoundBy().getId())
                .image(item.getImage())
                .build();
    }
}
