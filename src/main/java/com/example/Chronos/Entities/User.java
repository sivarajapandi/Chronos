package com.example.Chronos.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User  {
    @Id
    private UUID userId;

    private String username;


    private String email;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<Job> jobs;



}


