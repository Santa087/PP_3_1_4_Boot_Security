package ru.kata.spring.boot_security.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public User create(User user, List<Long> roleIds) {
        user.setRoles(resolveRoles(roleIds));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
        return user;
    }

    @Override
    public User update(Long id, User user, List<Long> roleIds) {
        User old = userDao.findById(id);
        if (old == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }


        user.setId(id);
        user.setRoles(resolveRoles(roleIds));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userDao.update(user);
        return userDao.findById(id);
    }


    @Override
    public void delete(Long id) {
        boolean deleted = userDao.deleteById(id);
        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }
    }




    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    private Set<Role> resolveRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleService.findAllById(roleIds));
    }
}
