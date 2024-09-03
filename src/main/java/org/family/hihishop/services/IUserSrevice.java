package org.family.hihishop.services;

import org.family.hihishop.dto.UserLoginDTO;
import org.family.hihishop.dto.UserRegisterDTO;
import org.family.hihishop.model.User;

public interface IUserSrevice {
    User createNewUser(UserRegisterDTO userRegisterDTO);
    String login(UserLoginDTO userLoginDTO);
}
