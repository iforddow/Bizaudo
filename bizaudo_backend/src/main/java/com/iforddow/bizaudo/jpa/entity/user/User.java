package com.iforddow.bizaudo.jpa.entity.user;

import com.iforddow.bizaudo.jpa.entity.rbac.Role;
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
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "password")
    private String password;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "expired", nullable = false)
    private Boolean expired = false;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "credentials_expired", nullable = false)
    private Boolean credentialsExpired = false;

    @Builder.Default
    @ColumnDefault("true")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "last_active", nullable = false)
    private Instant lastActive;

    @Builder.Default
    @ColumnDefault("false")
    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,  orphanRemoval = true)
    private UserProfile profile;

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