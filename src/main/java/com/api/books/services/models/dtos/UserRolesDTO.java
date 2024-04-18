package com.api.books.services.models.dtos;

import com.api.books.persistence.entities.RoleEntity;
import com.api.books.persistence.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRolesDTO {

    private Long userId;
    private String name;
    private String email;
    private List<RoleEntity> roles;

    public UserRolesDTO(UserEntity user) {
        userId = user.getId();
        name = user.getName();
        email = user.getEmail();
        roles = user.getRoles();
    }
}
