package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Role> findByName(String name) {
        List<Role> roles = em.createQuery(
                        "select r from Role r where r.name = :name", Role.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .getResultList();

        return roles.stream().findFirst();
    }


    @Override
    public Role save(Role role) {
        em.persist(role);
        return role;
    }

    @Override
    public List<Role> findAll() {
        return em.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public List<Role> findAllById(Iterable<Long> ids) {
        return em.createQuery("select r from Role r where r.id in :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
