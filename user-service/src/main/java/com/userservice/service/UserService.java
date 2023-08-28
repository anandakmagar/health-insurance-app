package com.userservice.service;

import com.userservice.dto.UserDto;
import com.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public String addUser(UserDto userDto);
    public String deleteUserById(Long id);
    public String updateUserById(Long id, UserDto userDto);
    public UserDto findUserById(Long id);
    public List<UserDto> getUsers();

    boolean authenticateUser(String username, String password);
}
