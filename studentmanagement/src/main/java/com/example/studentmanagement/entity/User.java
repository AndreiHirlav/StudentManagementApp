package com.example.studentmanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class User {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String nume;

    @Column(nullable=false)
    private String prenume;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String password;
    @Column
    private LocalDateTime ultimaAccesare;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns ={@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName="ID")}
    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String nume, String prenume, String email, String password, Set<Role> roles) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }
    public String getPrenume() {
        return prenume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getUltimaAccesare() {
        return ultimaAccesare;
    }

    public void setUltimaAccesare(LocalDateTime ultimaAccesare) {
        this.ultimaAccesare = ultimaAccesare;
    }
}
