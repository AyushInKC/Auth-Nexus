package com.FourAM.Auth_Nexus.Service;

import com.FourAM.Auth_Nexus.DTO.UserDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    UserDto createUser(UserDto userDto);

    //get user by email
    UserDto getUserByEmail(String email);

    //update user
    UserDto updateUser(UserDto userDto, String userId);

    //delete user
    void deleteUser(String userId);

    //get user by id
    UserDto getUserById(String userId);

    //get all users
    Iterable<UserDto> getAllUsers();

}
