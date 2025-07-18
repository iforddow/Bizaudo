package com.iforddow.bizaudo.jpa.entity.business;

import com.iforddow.bizaudo.jpa.entity.rbac.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "business")
public class Business {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "business")
    private Set<Role> roles;

}