package com.codev13.electrosign13back.data.entity;

import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "roles")
public class Role extends AbstractEntity implements GenericEntity<Role>{
    @Column(unique = true, nullable = false)
    private String libelle;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public void update(Role source) {
        this.libelle = source.getLibelle();
        this.users = source.getUsers();
    }

    @Override
    public Role createNewInstance() {
        Role role = new Role();
        role.update(this);
        return role;
    }
}
