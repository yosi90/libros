package com.api.books.services.models.dtos.askers;

import lombok.Data;

@Data
public class PasswordUpdateDTO {

    private String passwordNew;
    private String passwordOld;
}
