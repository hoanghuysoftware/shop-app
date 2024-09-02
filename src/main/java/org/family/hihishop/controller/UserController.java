package org.family.hihishop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.family.hihishop.dto.UserLoginDTO;
import org.family.hihishop.dto.UserRegisterDTO;
import org.family.hihishop.utils.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final ErrorMessage errorMessage;

    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@Valid @RequestBody UserRegisterDTO userDTO,
                                        BindingResult result) {
        try {
            // check error input values
            if (result.hasErrors()) {
                return ResponseEntity.badRequest().body(errorMessage.getErrorMessages(result));
            }
            // check retype password is same as password
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Retype password not match!");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully registered !\n"+userDTO.toString() );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@Valid @RequestBody UserLoginDTO userDTO) {
        // kiemr tra dang nhap
        // tra ve token
        return ResponseEntity.ok("Successfully logged in !");
    }


}
