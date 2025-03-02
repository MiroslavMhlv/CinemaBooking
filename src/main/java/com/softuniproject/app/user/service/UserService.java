package com.softuniproject.app.user.service;

import com.softuniproject.app.exception.DomainException;
import com.softuniproject.app.security.AuthenticationMetadata;
import com.softuniproject.app.user.entity.Role;
import com.softuniproject.app.user.entity.User;
import com.softuniproject.app.user.repository.UserRepository;
import com.softuniproject.app.web.dto.UserEditRequest;
import com.softuniproject.app.web.mapper.DtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(String email, String password) {
        Optional<User> optionUser = userRepository.findByEmail(email);
        if (optionUser.isPresent()) {
            throw new DomainException("Email [%s] already exists.".formatted(email));
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(Role.USER)
                .balance(100.0)
                .build();

        userRepository.save(user);
        log.info("User with email [{}] registered successfully.", user.getEmail());
        return user;
    }


    @CacheEvict(value = "users", allEntries = true)
    public void updateUser(UUID userId, String email, double balance) {
        User user = getById(userId);
        user.setEmail(email);
        user.setBalance(balance);
        userRepository.save(user);
        log.info("User with email [{}] updated successfully.", user.getEmail());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void switchRole(UUID userId) {
        User user = getById(userId);
        user.setRole(user.getRole() == Role.USER ? Role.ADMIN : Role.USER);
        userRepository.save(user);
        log.info("User with email [{}] switched role to [{}].", user.getEmail(), user.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DomainException("User with this email does not exist."));
        return new AuthenticationMetadata(user.getId(), email, user.getPassword(), user.getRole());
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {
        User user = getById(userId);

        DtoMapper.updateUserFromEditRequest(user, userEditRequest);

        userRepository.save(user);
    }

}


