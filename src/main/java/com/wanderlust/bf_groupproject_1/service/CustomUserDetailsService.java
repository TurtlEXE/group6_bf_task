package com.wanderlust.bf_groupproject_1.service;

import com.wanderlust.bf_groupproject_1.entity.User;
import com.wanderlust.bf_groupproject_1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByUsername(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + identifier)));

        String fullName = "";
        if (user.getFirstName() != null && !user.getFirstName().trim().isEmpty()) {
            fullName += user.getFirstName().trim();
        }
        if (user.getLastName() != null && !user.getLastName().trim().isEmpty()) {
            if (!fullName.isEmpty()) fullName += " ";
            fullName += user.getLastName().trim();
        }
        if (fullName.isEmpty()) {
            fullName = user.getUsername(); // fallback to username if no names provided
        }

        return new com.wanderlust.bf_groupproject_1.security.CustomUserDetails(
                user.getEmail(), // keep email as the principal name for compatibility
                user.getPassword(),
                user.isEnabled() && user.isEmailVerified(),
                true,
                true,
                true,
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                fullName
        );
    }
}
