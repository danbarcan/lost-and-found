package com.sterescu.lostandfound.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginPayload {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
