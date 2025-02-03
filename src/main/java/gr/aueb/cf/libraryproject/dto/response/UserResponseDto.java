package gr.aueb.cf.libraryproject.dto.response;

import gr.aueb.cf.libraryproject.core.enums.Role;
import gr.aueb.cf.libraryproject.model.business.Book;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private BigInteger id;
    private String username;
    private String email;
    private Role role;
    private List<Book> books;
}
