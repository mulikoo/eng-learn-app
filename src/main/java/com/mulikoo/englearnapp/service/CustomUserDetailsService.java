package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<String> permissions = user.getRole().getPermissions().stream()
                .map(permission -> permission.getCode())
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(permissions.stream().map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}
