package gr.aueb.cf.libraryproject.dto.response;

import gr.aueb.cf.libraryproject.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    private String access_token;
    private String username;
    private Role role;
}
