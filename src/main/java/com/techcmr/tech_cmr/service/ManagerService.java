package com.techcmr.tech_cmr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techcmr.tech_cmr.model.Manager;
import com.techcmr.tech_cmr.repository.ManagerRepository;

@Service
public class ManagerService {

    // Autowired necessari
    @Autowired
    private ManagerRepository managerRepository;

    // Metodi che accedono al database
    public List<Manager> findAllManagers(){
        return managerRepository.findAll();
    }

}
