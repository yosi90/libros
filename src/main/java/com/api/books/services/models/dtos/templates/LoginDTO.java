package com.api.books.services.models.dtos.templates;

import lombok.Data;

@Data
public class LoginDTO {

    private String email;
    private String password;

}
