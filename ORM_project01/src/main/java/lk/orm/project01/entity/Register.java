package lk.orm.project01.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE,region ="register_cache")
@Entity
@Table(name = "register")
public class Register {

    @Id
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /** Role: ADMIN or RECEPTIONIST */
    @Column(name = "role", nullable = false, length = 20)
    private String role;

    @Column(name = "email", length = 100)
    private String email;

    // ---- Constructors ----

    public Register() {}

    public Register(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    // ---- Getters & Setters ----

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Register{username='" + username + "', role='" + role + "'}";
    }
}
