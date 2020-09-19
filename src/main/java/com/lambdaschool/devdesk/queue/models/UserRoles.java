package com.lambdaschool.devdesk.queue.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userroles")
@IdClass(UserRolesId.class)
public class UserRoles extends Auditable implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnoreProperties(value = "roles", allowSetters = true)
    private User user;
    @Id
    @ManyToOne
    @JoinColumn(nullable = false, name = "roleid")
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Role role;

    public UserRoles(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public UserRoles() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return 14;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
        {
            return true;
        }
        if(!( obj instanceof UserRoles))
        {
            return false;
        }
        var that = (UserRoles) obj;
        return (user == null ? 0 : user.getId()) == (that.user == null ? 0 : that.user.getId()) &&
                (role == null ? 0 : role.getId()) == (that.role == null ? 0 : that.role.getId());
    }
}
