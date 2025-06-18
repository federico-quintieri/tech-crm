package com.techcmr.tech_cmr.repository;

import com.techcmr.tech_cmr.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByNameContaining(String name);

}
