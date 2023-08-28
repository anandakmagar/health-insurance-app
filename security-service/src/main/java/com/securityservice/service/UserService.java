package com.securityservice.service;

import com.securityservice.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public String addUser(UserDto userDto);
    public String updateUser(UserDto userDto, Long id);
    public String deleteUser(Long id);
    public List<UserDto> getUsers();
}
