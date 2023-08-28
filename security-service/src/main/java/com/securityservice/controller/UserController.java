package com.securityservice.controller;

import com.securityservice.dto.AuthRequest;
import com.securityservice.dto.UserDto;
import com.securityservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService securityService;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome to User Page";
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasAuthority('ADMIN")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto){
        securityService.addUser(userDto);
        return ResponseEntity.ok("User added successfully");
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    //@PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UserDto userDto, @PathVariable Long id){
        securityService.updateUser(userDto, id);
        return ResponseEntity.ok("User updated successfully");
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        securityService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsers(){
        List<UserDto> users = securityService.getUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){

    }
}
