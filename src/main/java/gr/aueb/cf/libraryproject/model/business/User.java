package gr.aueb.cf.libraryproject.model.business;

import gr.aueb.cf.libraryproject.core.enums.Role;
import lombok.*;

import java.math.BigInteger;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private BigInteger id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Boolean isActive;
    private List<Book> books;
}
