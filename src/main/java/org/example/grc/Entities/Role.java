package org.example.grc.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.grc.Entities.RoleName;
import org.example.grc.Entities.UserEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    @JsonProperty("role_name")
    private RoleName roleName;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> user =new HashSet<>();
    private boolean eventManagement;
    private boolean userManagement;

    public void setEventManagement(boolean eventManagement) {
        this.eventManagement = eventManagement;
    }

    public boolean isEventManagement() {
        return eventManagement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public Set<UserEntity> getUser() {
        return user;
    }

    public void setUser(Set<UserEntity> user) {
        this.user = user;
    }

    public void setUserManagement(boolean userManagement) {
        this.userManagement = userManagement;
    }

    public boolean isUserManagement() {
        return userManagement;
    }
}


