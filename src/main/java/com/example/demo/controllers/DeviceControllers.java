package com.example.demo.controllers;

import com.example.demo.models.Device;
import com.example.demo.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/manager")
public class DeviceControllers {

    @Autowired
    DeviceRepository deviceRepository;

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
    public ResponseEntity<Device> getDevicesById(@PathVariable("id") long id) {
        Optional<Device> deviceData = deviceRepository.findById(String.valueOf(id));

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
            Device _device = deviceRepository.save(new Device(device.getModel(), device.getDeviceType(), false));
            return new ResponseEntity<>(_device, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable("id") long id, @RequestBody Device device) {
        Optional<Device> deviceData = deviceRepository.findById(String.valueOf(id));

        if (deviceData.isPresent()) {
            Device _device = deviceData.get();
            _device.setModel(device.getModel());
            _device.setDeviceType(device.getDeviceType());
            _device.setPublished(device.isPublished());
            return new ResponseEntity<>(deviceRepository.save(_device), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<HttpStatus> deleteDevices(@PathVariable("id") long id) {
        try {
            deviceRepository.deleteById(String.valueOf(id));
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/devices")
    public ResponseEntity<HttpStatus> deleteAllDevices() {
        try {
            deviceRepository.deleteAll();
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

}