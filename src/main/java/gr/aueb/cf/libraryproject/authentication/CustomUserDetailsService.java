package gr.aueb.cf.libraryproject.authentication;

import gr.aueb.cf.libraryproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (userRepository.findUserEntityByUsername(username) == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        return userRepository.findUserEntityByUsername(username);

    }
}
