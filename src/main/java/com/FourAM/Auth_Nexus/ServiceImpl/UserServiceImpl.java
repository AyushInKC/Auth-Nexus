package com.FourAM.Auth_Nexus.ServiceImpl;

import com.FourAM.Auth_Nexus.DTO.UserDto;
import com.FourAM.Auth_Nexus.Enums.Provider;
import com.FourAM.Auth_Nexus.Exceptions.ResourceNotFoundException;
import com.FourAM.Auth_Nexus.Helpers.UserHelper;
import com.FourAM.Auth_Nexus.Model.User;
import com.FourAM.Auth_Nexus.Repository.UserRepository;
import com.FourAM.Auth_Nexus.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        User user = modelMapper.map(userDto, User.class);

        user.setProvider(
                userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL
        );

        // 🔐 PASSWORD ENCODING (ADDED)
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given email id")
                );

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        UUID uId = UserHelper.parseUUID(userId);

        User existingUser = userRepository
                .findById(uId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id")
                );

        if (userDto.getName() != null)
            existingUser.setName(userDto.getName());

        if (userDto.getImage() != null)
            existingUser.setImage(userDto.getImage());

        if (userDto.getProvider() != null)
            existingUser.setProvider(userDto.getProvider());

        // 🔐 PASSWORD UPDATE WITH ENCODING (ADDED)
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            existingUser.setPassword(
                    passwordEncoder.encode(userDto.getPassword())
            );
        }

        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User user = userRepository
                .findById(uId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id")
                );

        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository
                .findById(UserHelper.parseUUID(userId))
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with given id")
                );

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
