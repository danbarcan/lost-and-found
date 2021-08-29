package com.sterescu.lostandfound.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePayload {
    private Long id;

    @NotNull
    private Long itemId;

    @NotBlank
    private String message;
}
