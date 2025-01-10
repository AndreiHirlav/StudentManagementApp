package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetails implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        // Verificăm fiecare rol și îl mapăm la o autoritate cu prefixul "ROLE_"
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // roleName va trebui să fie 'ROLE_ADMIN' sau 'ROLE_USER'
                .collect(Collectors.toList());
    }
}