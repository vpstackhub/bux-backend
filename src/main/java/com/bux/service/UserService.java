package com.bux.service;

import com.bux.model.User;
import com.bux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
      }
    
    public User registerUser(User user) {
    	if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
    	
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
  
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
            .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
}

