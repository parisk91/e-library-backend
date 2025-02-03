package gr.aueb.cf.libraryproject.authentication;

import gr.aueb.cf.libraryproject.dto.request.AuthenticationRequestDto;
import gr.aueb.cf.libraryproject.dto.response.AuthenticationResponseDto;
import gr.aueb.cf.libraryproject.model.business.User;
import gr.aueb.cf.libraryproject.model.entity.UserEntity;
import gr.aueb.cf.libraryproject.repository.UserRepository;
import gr.aueb.cf.libraryproject.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto dto) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        if (userRepository.findUserEntityByUsername(authentication.getName()) == null) throw new Exception("User not authenticated");

        UserEntity user = userRepository.findUserEntityByUsername(authentication.getName());

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDto( token, user.getUsername(), user.getRole());
    }

}
