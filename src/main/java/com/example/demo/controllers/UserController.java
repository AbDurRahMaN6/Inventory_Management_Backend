package com.example.demo.controllers;

import com.example.demo.models.Device;
import com.example.demo.models.User;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;



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


    @PostMapping("{username}/devices")
    public ResponseEntity<Device> createUserDevices(@RequestBody Device device ){
        try{
            Device _device = deviceRepository.save(new Device(device.getSerialId(), device.getModel(), device.getDeviceType()));
            return new ResponseEntity<>(_device, HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{username}/myDevice")
    public List<Device> getDeviceByUser() {
        String id = "user";
        return deviceRepository.findAll().stream().filter(device ->
               device.getUsername() != null && device.getUsername().equals(id)).collect(Collectors.toList());
    }
}
