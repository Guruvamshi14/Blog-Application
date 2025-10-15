package com.mountblue.blogapplication.service;

import com.mountblue.blogapplication.model.User; // your custom User entity
import com.mountblue.blogapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
        );
    }
}

//
//
//                     +----------------------+
//                             |  User submits login  |
//        |  form (username +    |
//        |  password)           |
//        +----------+-----------+
//        |
//v
//             +------------------+-------------------+
//                     | UsernamePasswordAuthenticationFilter |
//        |  (intercepts /login request)         |
//        +------------------+-------------------+
//        |
//v
//                   +------------+-------------+
//                           | AuthenticationManager    |
//        +------------+-------------+
//        |
//v
//                   +------------+-------------+
//                           | DaoAuthenticationProvider |
//        |  (default provider)       |
//        +------------+-------------+
//        |
//v
//           +---------------------------------------+
//                   | CustomUserDetailsService.loadUserByUsername(username) |
//        |  (your @Service bean is called)                     |
//        +---------------------------------------+
//        |
//v
//              +----------------+----------------+
//                      | Fetch user from database       |
//        | userRepository.findByName(...) |
//        +----------------+----------------+
//        |
//v
//             +------------------+------------------+
//                     | Return UserDetails object           |
//        | (username, password, roles)        |
//        +------------------+------------------+
//        |
//v
//             +------------------+------------------+
//                     | PasswordEncoder.matches(raw, encoded)|
//        |  (checks password correctness)      |
//        +------------------+------------------+
//        |
//        +-------------+-------------+
//        |                           |
//v                           v
//        +------------------+         +------------------+
//                | Authentication   |         | Authentication   |
//        | SUCCESS          |         | FAILURE          |
//        +------------------+         +------------------+
//        |                           |
//v                           v
//   +--------------------------+       +----------------------+
//           | SecurityContextHolder    |       | Redirect to /login?error |
//        |  stores Authentication   |       +----------------------+
//        +--------------------------+
//        |
//v
//     +----------------------------+
//             | User can access protected  |
//        | resources based on roles   |
//        +----------------------------+
