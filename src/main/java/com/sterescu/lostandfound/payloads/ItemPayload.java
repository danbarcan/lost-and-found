package com.sterescu.lostandfound.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemPayload {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String location;

    private MultipartFile image;
}
