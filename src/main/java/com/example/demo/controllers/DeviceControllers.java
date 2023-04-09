package com.example.demo.controllers;

import com.example.demo.models.Device;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.models.UserRole;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevices(@RequestParam(required = false) String model) {
        try {
            List<Device> devices = new ArrayList<Device>();

            if (model == null)
                deviceRepository.findAll().forEach(devices::add);
            else
                deviceRepository.findByModelContaining(model).forEach(devices::add);

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
//        log.error("An ERROR Message");
        try {
            Device _device = deviceRepository.save(new Device(device.getSerialId(),device.getModel(), device.getDeviceType(), false));
            return new ResponseEntity<>(_device, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateTutorial(@PathVariable("id") String id, @RequestBody Device device) {
        Optional<Device> deviceData = deviceRepository.findById(id);

        if (deviceData.isPresent()) {
            Device _device = deviceData.get();
            _device.setSerialId(device.getSerialId());
            _device.setModel(device.getModel());
            _device.setDeviceType(device.getDeviceType());
            _device.setPublished(device.isPublished());
            return new ResponseEntity<>(deviceRepository.save(_device), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

    @GetMapping("/devices/published")
    public ResponseEntity<List<Device>> findByPublished() {
        try {
            List<Device> devices = deviceRepository.findByPublished(true);

            if (devices.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(devices, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUsersByName(@PathVariable("username") String username) {
        Optional<User> userData = userRepository.findByUsername(username);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{username}")
    public ResponseEntity<User> updateUsersList(@PathVariable("username") String username, @RequestBody User users) {
        Optional<User> usersData = userRepository.findByUsername(username);

        if (usersData.isPresent()) {
            User _users = usersData.get();
            _users.setUsername(users.getUsername());
            _users.setEmail(users.getEmail());
            _users.setRoles(users.getRoles());
            return new ResponseEntity<>(userRepository.save(_users), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<HttpStatus> deleteUsers(@PathVariable("username") String username) {
        try {
            roleRepository.deleteById(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    User user = userRepository.findByUsername(username);
//        if (user != null) {
//        userRepository.delete(user);
//    }

}