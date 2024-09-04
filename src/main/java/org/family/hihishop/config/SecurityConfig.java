package org.family.hihishop.config;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.model.User;
import org.family.hihishop.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    //create object user detail
    @Bean
    public UserDetailsService  userDetailsService(){ // use attributes unique -> use phoneNumber
        return phoneNumber -> userRepository.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new DataNotFoundException("User not found by phone number: " + phoneNumber));
    }

    // password encode -> ma hoa mk
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;  // authentication provider is used for authentication in security configuaration.
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
