package com.api.books.persistence.entities;

import com.api.books.services.models.dtos.BookDTO;
import com.api.books.services.models.dtos.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    LocalDateTime lifeSpan = LocalDateTime.now();

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
            + "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9]"
            + "(?:[a-zA-Z0-9-]*[a-zA-Z0-9])?", message = "Email no válido")
    private String email;

    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookEntity> books;

    public List<BookDTO> getBooksDTOs() {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (BookEntity book : books)
            bookDTOS.add(book.ToDTO());
        return bookDTOS;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<RoleEntity> roles;

    public UserDTO ToDTO() {
        return new UserDTO(this);
    }
}
