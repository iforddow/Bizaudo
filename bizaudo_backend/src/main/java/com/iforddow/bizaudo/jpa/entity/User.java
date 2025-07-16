package com.iforddow.bizaudo.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "credentials_expired")
    private Boolean credentialsExpired;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "last_active")
    private Instant lastActive;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @OneToMany(mappedBy = "user")
    private Set<UserProfile> userProfiles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().flatMap(
                role -> {
                    Stream<GrantedAuthority> roleAuth = Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getCodeName()));
                    Stream<GrantedAuthority> permAuth = role.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getCodeName()));
                    return Stream.concat(roleAuth, permAuth);
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}