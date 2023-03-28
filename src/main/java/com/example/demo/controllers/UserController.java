package com.example.demo.controllers;

import com.example.demo.models.Device;
import com.example.demo.models.UserDevices;
import com.example.demo.repository.UserDeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserDeviceRepository userDeviceRepository;
    @PostMapping("/devices")
    public ResponseEntity<UserDevices> createDevices(@RequestBody UserDevices userDevice) {
//        log.error("An ERROR Message");
        try {
            UserDevices _userDevice = userDeviceRepository.save(new UserDevices(userDevice.getModel(), userDevice.getDeviceType()));
            return new ResponseEntity<>(_userDevice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
