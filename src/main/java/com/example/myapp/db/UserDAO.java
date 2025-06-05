package com.example.myapp.db;

import com.example.myapp.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query; // Важливо: org.hibernate.query.Query

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
        // Використовуємо named query для пошуку за email
        Query<User> query = namedTypedQuery("com.example.myapp.core.User.findByEmail");
        query.setParameter("email", email);
        return Optional.ofNullable(uniqueResult(query)); // uniqueResult може повернути null
    }

    public User update(User user) {
        // persist також оновлює, якщо сутність вже існує в контексті персистентності
        // або якщо вона detached, але має ідентифікатор та версію (якщо використовується @Version)
        return persist(user);
    }

    public void delete(User user) {
        currentSession().remove(user); // Використовуйте remove для JPA 2.1+ сумісності
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }
}
