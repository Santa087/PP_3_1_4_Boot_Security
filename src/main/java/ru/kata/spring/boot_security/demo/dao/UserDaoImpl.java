package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        return entityManager.createQuery(
                "select distinct u from User u left join fetch u.roles", User.class
        ).getResultList();
    }


    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) entityManager.remove(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return entityManager.createQuery(
                        "select distinct u from User u left join fetch u.roles where u.username = :username",
                        User.class
                )
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
    }



}
