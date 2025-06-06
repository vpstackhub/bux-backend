package com.bux.service;

import com.bux.model.User;
import com.bux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
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
        try {
            System.out.println("➡️ Attempting to register user: " + user.getEmail());

            if (userRepository.existsByUsername(user.getUsername())) {
                System.out.println("⚠️ Username already taken: " + user.getUsername());
                throw new RuntimeException("Username already taken");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saved = userRepository.save(user);
            System.out.println("✅ User registered with ID: " + saved.getId());
            return saved;

        } catch (Exception e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            e.printStackTrace(); 
            throw e;
        }
    }

  
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
            .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }
    
    public List<User> getAllUsers() {  
        return userRepository.findAll();
    }
}


