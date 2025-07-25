package com.iforddow.bizaudo.jpa.entity.rbac;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class UserRoleId implements Serializable {
    private static final long serialVersionUID = 2688604142490346452L;
    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @NotNull
    @Column(name = "business_id", nullable = false)
    private UUID businessId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRoleId entity = (UserRoleId) o;
        return Objects.equals(this.roleId, entity.roleId) &&
                Objects.equals(this.businessId, entity.businessId) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, businessId, userId);
    }

}