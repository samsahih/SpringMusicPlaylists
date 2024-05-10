package com.musicplaylists.demo.musicplaylists.entities;

import jakarta.persistence.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Username is mandatory")
    private String username;

    @Column(unique = true)
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Transient
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NormalUser normalUser;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdminUser adminUser;

    private String roles;

    public User(){}

    public User(String email, String username, String password) {
        this(email, username, password, UserType.NORMAL);
    }

    public User(String email, String username, String password, UserType userType) {
        this.email = email;
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.userType = userType;
        this.roles = String.valueOf(userType);

        // Validate email format using annotation-based validation
        Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Invalid user data", violations);
        }
    }

    public Long getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() { return password; }

    public String getRoles() {
        return roles;
    }

    public NormalUser getNormalUser() {
        return normalUser;
    }

    public void setNormalUser(NormalUser user) {
        this.normalUser = user;
    }

    public AdminUser getAdministratorUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser user) {
        this.adminUser = user;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public void setId(long l) {
        this.id = l;
    }
}
