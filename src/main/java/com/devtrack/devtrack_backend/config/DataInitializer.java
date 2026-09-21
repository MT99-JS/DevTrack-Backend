package com.devtrack.devtrack_backend.config;

import com.devtrack.devtrack_backend.entity.Role;
import com.devtrack.devtrack_backend.entity.User;
import com.devtrack.devtrack_backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail(
                    "qa@devtrack.com")) {

                User qa = new User();

                qa.setName("DevTrack QA");
                qa.setEmail("qa@devtrack.com");

                qa.setPassword(
                        passwordEncoder.encode("QA12345678")
                );

                qa.setRole(Role.QA);

                userRepository.save(qa);
            }

            if (!userRepository.existsByEmail(
                    "admin@devtrack.com")) {

                User admin = new User();

                admin.setName("Admin User");
                admin.setEmail("admin@devtrack.com");

                admin.setPassword(
                        passwordEncoder.encode("ADMIN12345678")
                );

                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }
        };
    }
}
