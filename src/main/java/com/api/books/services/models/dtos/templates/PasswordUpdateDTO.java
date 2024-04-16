package com.api.books.services.models.dtos.templates;

import lombok.Data;

@Data
public class PasswordUpdateDTO {

    private String passwordNew;
    private String passwordOld;
}
