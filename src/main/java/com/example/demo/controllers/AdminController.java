package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        Optional<Role> role = roleRepository.findByName(UserRole.ROLE_USER);
        List<User> user = null;
        if (role.isPresent()) {
            user = userRepository.findAll().stream().filter(user1 -> user1.getRoles().stream().anyMatch(role1 -> role1.getId()
                    .equals(role.get().getId()))).collect(Collectors.toList());
        }
        return user;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUsersById(@PathVariable("id") String id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateAdminUsers(@PathVariable("id") String id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setUsername(user.getUsername());
            _user.setEmail(user.getEmail());
            _user.setPassword(user.getPassword());
            _user.setRoles(user.getRoles());
            _user.setRolling(user.getRolling());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteAdminUsers(@PathVariable("id") String id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/managers")
    public List<User> getAllManagers() {
        Optional<Role> role = roleRepository.findByName(UserRole.ROLE_MANAGER);
        List<User> user = null;
        if (role.isPresent()) {
            user = userRepository.findAll().stream().filter(user1 -> user1.getRoles().stream().anyMatch(role1 -> role1.getId()
                    .equals(role.get().getId()))).collect(Collectors.toList());
        }
        return user;
    }


    @GetMapping("/managers/{id}")
    public ResponseEntity<User> getManagersById(@PathVariable("id") String id) {
        Optional<User> managerData = userRepository.findById(id);

        if (managerData.isPresent()) {
            return new ResponseEntity<>(managerData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<User> updateManagers(@PathVariable("id") String id, @RequestBody User manager) {
        Optional<User> managerData = userRepository.findById(id);

        if (managerData.isPresent()) {
            User _manager = managerData.get();
            _manager.setUsername(manager.getUsername());
            _manager.setEmail(manager.getEmail());
            _manager.setPassword(manager.getPassword());
            _manager.setRoles(manager.getRoles());
            _manager.setRolling(manager.getRolling());
            return new ResponseEntity<>(userRepository.save(_manager), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<HttpStatus> deleteManagers(@PathVariable("id") String id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
