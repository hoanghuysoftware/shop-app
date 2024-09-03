package org.family.hihishop.services;

import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.UserLoginDTO;
import org.family.hihishop.dto.UserRegisterDTO;
import org.family.hihishop.exception.DataNotFoundException;
import org.family.hihishop.model.Role;
import org.family.hihishop.model.User;
import org.family.hihishop.repository.RoleRepository;
import org.family.hihishop.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserSrevice{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public User createNewUser(UserRegisterDTO userRegisterDTO) {
        String phoneNumber = userRegisterDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("Phone number already exists");
        }
        // convert userRegisterDTO to User
        User user = User.builder()
                .fullName(userRegisterDTO.getFullName())
                .phoneNumber(phoneNumber)
                .address(userRegisterDTO.getAddress())
                .password(userRegisterDTO.getPassword())
                .dateOfBirth(userRegisterDTO.getDateOfBirth())
                .facebookAccountId(userRegisterDTO.getFacebookAccountId())
                .googleAccountId(userRegisterDTO.getGoogleAccountId())
                .build();
        Role role = roleRepository.findById(userRegisterDTO.getRoleId())
                .orElseThrow(() -> new DataNotFoundException("Could not find role"));
        user.setRole(role);
        // if has accountID, then cannot need password
//        if(userRegisterDTO.getFacebookAccountId() == 0 || userRegisterDTO.getGoogleAccountId() == 0){
//            user.setPassword(userRegisterDTO.getPassword());
//        }

        return userRepository.save(user);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        // This is related with spring security
        return null;
    }
}
