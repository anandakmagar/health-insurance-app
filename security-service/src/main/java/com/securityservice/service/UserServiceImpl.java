package com.securityservice.service;

import com.securityservice.dto.UserDto;
import com.securityservice.model.User;
import com.securityservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User userDtoToUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setRoles(userDto.getRoles());

        return user;
    }

    public UserDto userDtoToUser(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setPassword(user.getPassword());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
    @Override
    public String addUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        user.setRoles(userDto.getRoles());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        return "User successfully saved";
    }

    @Override
    public String updateUser(UserDto userDto, Long id) {
        if (userRepository.findById(id).isPresent()){
            User user = userRepository.findById(id).get();
            user.setId(id);
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setPassword(userDto.getPassword());
            user.setRoles(userDto.getRoles());
            userRepository.save(user);

            return "User successfully updated";
        }
        return "User couldn't be found";
    }

    @Override
    public String deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);

            return "User successfully deleted";
        }
        return "User couldn't be found";
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = new ArrayList<>();
        users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        userDtos = users.stream().map(user -> new UserDto(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.getRoles())).collect(Collectors.toList());
        return userDtos;
    }
}
