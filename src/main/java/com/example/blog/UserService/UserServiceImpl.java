package com.example.blog.UserService;

import com.example.blog.Entity.Provider;
import com.example.blog.Entity.Role;
import com.example.blog.Entity.User;
import com.example.blog.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public void createUser(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        Role userRole = new Role("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        user.setProvider(Provider.LOCAL);
        user.setVisit(0);
        user.setEnabled(false);
        userRepository.save(user);
        System.out.println("work");
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public User getMemberByEmail(String login) {
        return userRepository.getUserByEmail(login);
    }

    public void processOAuthPostLogin(String email, String firstname, String lastname) {
        User existUser = userRepository.getUserByEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (existUser == null) {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstname(firstname);
            newUser.setLastname(lastname);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEnabled(true);
            Role userRole = new Role("USER");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            newUser.setRoles(roles);
            newUser.setEnabled(false);
            newUser.setPassword(encoder.encode("1"));
            userRepository.save(newUser);

            System.out.println("Created new user: " + email);
        }

    }

    public List<User> getByKeyword(String keyword) {
        return userRepository.findByKeyword(keyword);
    }
}
