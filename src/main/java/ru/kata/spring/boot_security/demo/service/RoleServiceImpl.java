package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return roleDao.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleDao.save(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllById(Iterable<Long> ids) {
        return roleDao.findAllById(ids);
    }
}
