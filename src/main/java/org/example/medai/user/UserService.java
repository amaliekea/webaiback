package org.example.medai.user;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String pword) {
        User user = userRepository.findByUsername(username);

        return user != null && user.getPword().equals(pword);
    }

    public void saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        userRepository.save(user);
    }


}
