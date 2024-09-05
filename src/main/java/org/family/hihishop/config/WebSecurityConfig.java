package org.family.hihishop.config;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.model.RoleName;
import org.family.hihishop.utils.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(HttpMethod.GET,"/api/v1/orders/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/v1/orders/**").hasRole(RoleName.USER.name())
                            .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT, "/api/v1/orders/**").hasRole(RoleName.USER.name())
                            .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/orders/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/api/v1/categories/**").hasRole(RoleName.ADMIN.name())
                            .requestMatchers("/api/v1/order-details**").permitAll()
                            .requestMatchers("/api/v1/users/**").permitAll()
                            .anyRequest().authenticated();
                });
        httpSecurity
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
