package com.mmacedo.springboot2essentials.service;

import com.mmacedo.springboot2essentials.domain.DevDojoUser;
import com.mmacedo.springboot2essentials.domain.DevDojoUserDetails;
import com.mmacedo.springboot2essentials.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final DevDojoUserRepository myUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<DevDojoUser> myUserFound = myUserRepository.findByUsername(username);

        myUserFound.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

        return myUserFound.map(DevDojoUserDetails::new).get();
    }
}
