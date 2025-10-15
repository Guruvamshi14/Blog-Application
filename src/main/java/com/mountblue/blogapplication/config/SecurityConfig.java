package com.mountblue.blogapplication.config;

import com.mountblue.blogapplication.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers("/post", "/post/edit/**", "/post/delete/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/post").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/post/create").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/post/**").hasAnyRole("AUTHOR", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/post/**").hasAnyRole("AUTHOR", "ADMIN")

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) // user always goes to "/" after login
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }
}
