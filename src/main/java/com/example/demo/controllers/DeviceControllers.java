package com.example.demo.controllers;

import com.example.demo.models.Device;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/manager")

public class DeviceControllers {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevices() {
        try {
            List<Device> devices = new ArrayList<>();
            devices.addAll(deviceRepository.findAll());

//            if (model == null)
//                devices.addAll(deviceRepository.findAll());
//            else
//                devices.addAll(deviceRepository.findByModelContaining(model));

            if (devices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDevicesById(@PathVariable("id") String id) {
        Optional<Device> deviceData = deviceRepository.findById(id);

        if (deviceData.isPresent()) {
            return new ResponseEntity<>(deviceData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/devices")
    public ResponseEntity<Device> createDevices(@RequestBody Device device) {
        try {
            Device _device = deviceRepository.save(new Device(device.getSerialId(), device.getModel(), device.getDeviceType(), false));
            return new ResponseEntity<>(_device, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateDevices(@PathVariable("id") String id, @RequestBody Device device) {
        Optional<Device> deviceData = deviceRepository.findById(id);
        Optional<Role> role = roleRepository.findByName(UserRole.ROLE_USER);
        List<User> user = null;


        if (deviceData.isPresent()) {
            Device _device = deviceData.get();
            _device.setSerialId(device.getSerialId());
            _device.setModel(device.getModel());
            _device.setDeviceType(device.getDeviceType());
            _device.setAvailable(device.isAvailable());
            _device.setUsername(device.getUsername());
            Device devicePersisted = deviceRepository.save(_device);
            return new ResponseEntity<>(devicePersisted, HttpStatus.OK);
        } else if (role.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } else {
            user = userRepository.findAll().stream().filter(user1 -> user1.getRoles().stream().anyMatch(role1 -> role1.getName()
                    .equals(role.get().getName()))).collect(Collectors.toList());
        }
        return null;
    }


    @DeleteMapping("/devices/{id}")
    public ResponseEntity<HttpStatus> deleteDevices(@PathVariable("id") String id) {
        try {
            deviceRepository.deleteById(String.valueOf(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/devices/available")
    public ResponseEntity<List<Device>> findByAvailable() {
        try {
            List<Device> devices = deviceRepository.findByAvailable(true);

            if (devices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getRolling());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "ROLE_MANAGER":
                        Role managerRole = roleRepository.findByName(UserRole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(managerRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }


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
    public ResponseEntity<User> updateUsers(@PathVariable("id") String id, @RequestBody User user) {
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
    public ResponseEntity<HttpStatus> deleteUsers(@PathVariable("id") String id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}