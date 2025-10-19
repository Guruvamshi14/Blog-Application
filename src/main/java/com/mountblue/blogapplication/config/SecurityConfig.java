package com.mountblue.blogapplication.config;

import com.mountblue.blogapplication.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    public static final String ROLE_AUTHOR = "AUTHOR";
    public static final String ROLE_ADMIN = "ADMIN";

    @Bean
    @Order(1) // runs first
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**") // only URLs starting with /api/
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register").permitAll()
                        .requestMatchers("/post", "/post/edit/**", "/post/delete/**").hasAnyRole(ROLE_AUTHOR, ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/post").hasAnyRole(ROLE_AUTHOR, ROLE_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/post/create").hasAnyRole(ROLE_AUTHOR, ROLE_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/post/**").hasAnyRole(ROLE_AUTHOR, ROLE_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/post/**").hasAnyRole(ROLE_AUTHOR, ROLE_ADMIN)
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  //        return new BCryptPasswordEncoder();
    }
}

