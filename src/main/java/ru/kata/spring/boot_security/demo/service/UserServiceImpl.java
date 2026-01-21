package ru.kata.spring.boot_security.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.dto.UserFormDto;
import ru.kata.spring.boot_security.demo.dto.UserReadDto;
import ru.kata.spring.boot_security.demo.dto.UserWriteDto;
import ru.kata.spring.boot_security.demo.mappers.RoleMapper;
import ru.kata.spring.boot_security.demo.mappers.UserMapper;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public UserServiceImpl(UserDao userDao,
                           RoleService roleService,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           RoleMapper roleMapper) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserReadDto> getAllDtos() {
        return userDao.findAll().stream()
                .map(userMapper::toReadDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserReadDto getDtoById(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }
        return userMapper.toReadDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserFormDto getCreateForm() {
        List<RoleDto> roles = getAllRoleDtos();
        return new UserFormDto(null, roles);
    }

    @Override
    @Transactional(readOnly = true)
    public UserFormDto getEditForm(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }
        List<RoleDto> roles = getAllRoleDtos();
        return new UserFormDto(userMapper.toReadDto(user), roles);
    }

    @Override
    public UserReadDto create(UserWriteDto dto) {
        User user = userMapper.toEntity(dto);
        user.setRoles(resolveRoles(dto.getRoleIds()));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userDao.save(user);
        return userMapper.toReadDto(user);
    }

    @Override
    public UserReadDto update(Long id, UserWriteDto dto) {
        User old = userDao.findById(id);
        if (old == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: id=" + id);
        }

        User user = userMapper.toEntity(dto);
        user.setId(id);
        user.setRoles(resolveRoles(dto.getRoleIds()));

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(old.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userDao.update(user);
        User updated = userDao.findById(id);
        return userMapper.toReadDto(updated);
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
    public List<RoleDto> getAllRoleDtos() {
        return roleService.findAll().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toList());
    }

    private Set<Role> resolveRoles(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(roleService.findAllById(roleIds));
    }
}
