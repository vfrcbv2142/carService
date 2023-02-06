package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Role;
import com.blankerdog.carService.repository.RoleRepository;
import com.blankerdog.carService.services.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role create(Role role) {
        if (role != null) {
            return roleRepository.save(role);
        }
        throw new NullPointerException("Role cannot be 'null'");
    }

    @Override
    public Role readById(long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Role with id " + id + " not found"));
    }

    @Override
    public Role readByName(String name){
        return roleRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundException("Role with name " + name + " not found"));
    }

    @Override
    public Role update(Role role) {
        if (role != null) {
            readById(role.getId());
            return roleRepository.save(role);
        }
        throw new NullPointerException("Role cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        Role role = readById(id);
        roleRepository.delete(role);
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
