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

    // Връщане на всички потребители с кеширане
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Връщане на потребител по ID
    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DomainException("User with id [%s] does not exist.".formatted(id)));
    }

    // Регистрация на нов потребител
    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(String username, String password, String email) {
        Optional<User> optionUser = userRepository.findByUsername(username);
        if (optionUser.isPresent()) {
            throw new DomainException("Username [%s] already exists.".formatted(username));
        }

        User user = User.builder()
                .id(UUID.randomUUID())  // Автоматично генерираме ID
                .username(username)
                .password(passwordEncoder.encode(password))  // Криптиране на паролата
                .email(email)
                .role(Role.USER)
                .balance(100.0)  // Начален баланс
                .build();

        userRepository.save(user);
        log.info("User [{}] registered successfully.", user.getUsername());
        return user;
    }


    // Обновяване на потребителска информация
    @CacheEvict(value = "users", allEntries = true)
    public void updateUser(UUID userId, String email, double balance) {
        User user = getById(userId);
        user.setEmail(email);
        user.setBalance(balance);
        userRepository.save(user);
        log.info("User [{}] updated successfully.", user.getUsername());
    }

    // Промяна на роля на потребител
    @CacheEvict(value = "users", allEntries = true)
    public void switchRole(UUID userId) {
        User user = getById(userId);
        user.setRole(user.getRole() == Role.USER ? Role.ADMIN : Role.USER);
        userRepository.save(user);
        log.info("User [{}] switched role to [{}].", user.getUsername(), user.getRole());
    }

    // Зареждане на потребител за Spring Security
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User with this username does not exist."));
        return new AuthenticationMetadata(user.getId(), username, user.getPassword(), user.getRole());
    }

    public void editUserDetails(UUID userId, UserEditRequest userEditRequest) {
        User user = getById(userId); // Взимаме потребителя по ID

        // Обновяваме информацията с помощта на DtoMapper
        DtoMapper.updateUserFromEditRequest(user, userEditRequest);

        userRepository.save(user);
    }

}


