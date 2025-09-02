package com.essence_evoke.config;

import com.essence_evoke.repository.UserRepository;
import com.essence_evoke.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private CustomLoginFailureHandler customLoginFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/register", "/login", "/home", "/css/**", "/image/**", "/js/**", "/uploads/**", "/", "/confirm").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", false)
                        .failureHandler(customLoginFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }
}
