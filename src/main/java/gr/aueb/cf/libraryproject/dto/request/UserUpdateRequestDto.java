package gr.aueb.cf.libraryproject.dto.request;

import gr.aueb.cf.libraryproject.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigInteger;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotNull
    private BigInteger id;
    @Length(min = 2, max = 255)
    private String username;
    @Size(min = 8)
    private String password;
    @Email
    private String email;
    private Role role;
}
