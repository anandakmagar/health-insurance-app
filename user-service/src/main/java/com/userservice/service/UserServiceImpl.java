package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.model.User;
import com.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserDto userToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    public User userDtoToUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setRoles(userDto.getRoles());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getUsername());
        return user;
    }
    @Override
    public String addUser(UserDto userDto) {
        User user = userDtoToUser(userDto);
        userRepository.save(user);
        return "User added successfully";
    }

    @Override
    public String deleteUserById(Long id) {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return "User deleted successfully";
        }
        return null;
    }

    @Override
    public String updateUserById(Long id, UserDto userDto) {
        if (userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setRoles(userDto.getRoles());
            userRepository.save(user);
            return "User has been successfully updated";
        }
        return null;
    }

    @Override
    public UserDto findUserById(Long id) {
        if (userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            return userToDto(user);
        }
        return null;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        userDtos = users.stream().map(user -> new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getRoles())).collect(Collectors.toList());
        return userDtos;
    }
    @Override
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);

        // Check if the user exists
        if (user == null) {
            return false;
        }

        // Check if the provided password matches the user's actual password
        return passwordEncoder.matches(password, user.getPassword());
    }
}
