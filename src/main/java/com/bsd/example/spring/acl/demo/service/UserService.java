package com.bsd.example.spring.acl.demo.service;

import com.bsd.example.spring.acl.demo.domain.User;
import com.bsd.example.spring.acl.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MutableAclService aclService;

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found!");
        }
        return user.get();
    }
}
