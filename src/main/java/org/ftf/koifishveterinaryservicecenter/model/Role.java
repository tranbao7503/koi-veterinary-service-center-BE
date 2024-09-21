package org.ftf.koifishveterinaryservicecenter.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role_key", nullable = false, length = 4)
    private String roleKey;  // MAN, CUS, STA, VET

    @Column(name = "role_name", nullable = false, length = 20)
    private String roleName;


    // Bidirectional, identifying relationship
    // owning side: User
    // inverse side: Role
    @OneToMany(mappedBy = "role")
    private Set<User> users = new LinkedHashSet<>();



}

