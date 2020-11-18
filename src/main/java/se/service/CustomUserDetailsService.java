package se.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import se.domain.User;
import se.repository.UserRepositoryImpl;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepositoryImpl userService = new UserRepositoryImpl();

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userService.getUserByUsername(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
}