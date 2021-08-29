package com.sterescu.lostandfound.payloads;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileResponsePayload {

    private String id;
    private String name;
    private Long size;
    private String url;
    private String contentType;
}