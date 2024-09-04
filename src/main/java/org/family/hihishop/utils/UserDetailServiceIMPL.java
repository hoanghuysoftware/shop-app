//package org.family.hihishop.utils;
//
//import lombok.RequiredArgsConstructor;
//import org.family.hihishop.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailServiceIMPL implements UserDetailsService {
//    private final UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.getUserByPhoneNumber(username)
//                .orElseThrow(() -> new UsernameNotFoundException(null));
//    }
//}
