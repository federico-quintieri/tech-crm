package com.techcmr.tech_cmr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techcmr.tech_cmr.model.Manager;
import com.techcmr.tech_cmr.service.ManagerService;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    // Autowired service necessari
    @Autowired
    private ManagerService managerService;

    // Definisco i miei endpoint \\

    // Restituisce tutti i managers
    @GetMapping
    public ResponseEntity<List<Manager>> index() {

        List<Manager> managers = managerService.findAllManagers();

        if (managers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(managers);

    }

}
