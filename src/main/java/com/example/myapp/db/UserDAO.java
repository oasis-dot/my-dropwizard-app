package com.example.myapp.db;

import com.example.myapp.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public User create(User user) {
        return persist(user);
    }

    public List<User> findAll() {
        return list(namedTypedQuery("com.example.myapp.core.User.findAll"));
    }

    public Optional<User> findByEmail(String email) {
        Query<User> query = namedTypedQuery("com.example.myapp.core.User.findByEmail");
        query.setParameter("email", email);
        return Optional.ofNullable(uniqueResult(query)); 
    }

    public Optional<User> findByPhone(String phone) {
        Query<User> query = namedTypedQuery("com.example.myapp.core.User.findByPhone");
        query.setParameter("phone", phone);
        return Optional.ofNullable(uniqueResult(query));
    }

    public User update(User user) {
        return persist(user);
    }

    public void delete(User user) {
        currentSession().remove(user);
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }
}
