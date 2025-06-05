package com.example.myapp.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern; // Для валідації телефону

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

import java.util.Objects;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
        name = "com.example.myapp.core.User.findAll",
        query = "SELECT u FROM User u"
    ),
    @NamedQuery(
        name = "com.example.myapp.core.User.findByEmail",
        query = "SELECT u FROM User u WHERE u.email = :email"
    )
    // Додайте сюди інші NamedQueries, якщо потрібно (наприклад, findByPhone)
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Length(max = 100)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Email
    @Length(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Нове поле для телефону
    // @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number format is invalid") // Приклад валідації
    @Length(max = 25)
    @Column(name = "phone", nullable = true) // Може бути null, якщо телефон не обов'язковий
    private String phone;

    // Нове поле для пароля
    @NotBlank
    @Length(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Column(name = "password", nullable = false)
    // Ця анотація означає, що поле 'password' буде враховуватися тільки при записі (десеріалізації JSON в об'єкт),
    // але не буде включатися в JSON при читанні (серіалізації об'єкта в JSON)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    // Конструктор за замовчуванням
    public User() {
    }

    // Оновлений конструктор
    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password; // Пам'ятайте про хешування в реальному застосунку!
    }

    // Getters and Setters

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getPhone() {
        return phone;
    }

    @JsonProperty
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter для пароля потрібен Hibernate, але JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // не дасть йому потрапити у відповідь API
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // В реальному застосунку тут би відбувалося хешування нового пароля
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
               Objects.equals(name, user.name) &&
               Objects.equals(email, user.email) &&
               Objects.equals(phone, user.phone); // Пароль зазвичай не включають в equals/hashCode
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, phone); // Пароль зазвичай не включають
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", phone='" + phone + '\'' +
               // Не виводьте пароль в toString() в логах!
               '}';
    }
}
