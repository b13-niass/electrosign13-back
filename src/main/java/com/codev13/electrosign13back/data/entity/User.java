package com.codev13.electrosign13back.data.entity;

import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET active = false WHERE id=?")
public class User extends AbstractEntity implements GenericEntity<User> {
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String photo;
    private String telephone;
    private String cni;
    private String privateKey;
    private String publicKey;
    private String mySignature;

    @ManyToOne
    @JoinColumn(name = "fonction_id")
    private Fonction fonction;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<SignatureDocument> signatures;

    @Override
    public void update(User source) {
        this.prenom = source.getPrenom();
        this.nom = source.getNom();
        this.email = source.getEmail();
        this.password = source.getPassword();
        this.telephone = source.getTelephone();
        this.cni = source.getCni();
        this.privateKey = source.getPrivateKey();
        this.publicKey = source.getPublicKey();
        this.roles = source.getRoles();
        this.mySignature = source.getMySignature();
        this.fonction = source.getFonction();
        this.notifications = source.getNotifications();
    }

    @Override
    public User createNewInstance() {
        User user = new User();
        user.update(this);
        return user;
    }
}