package org.ftf.koifishveterinaryservicecenter.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role_key", nullable = false, length = 4, unique = true)
    private String roleKey;  // MAN, CUS, STA, VET

    @Column(name = "role_name", nullable = false, length = 20, unique = true)
    private String roleName;


    // Bidirectional, identifying relationship
    // owning side: User
    // inverse side: Role
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<User> users = new LinkedHashSet<>();



}

