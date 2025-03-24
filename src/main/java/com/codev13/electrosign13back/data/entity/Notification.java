package com.codev13.electrosign13back.data.entity;
import com.codev13.electrosign13back.enums.StatusNotification;
import com.codev13.electrosign13back.enums.TypeNotification;
import com.core.communs.core.GenericEntity;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "notifications")
public class Notification extends AbstractEntity implements GenericEntity<Notification> {
    private String message;

    @Enumerated(EnumType.STRING)
    private TypeNotification type;

    private String link;

    @Enumerated(EnumType.STRING)
    private StatusNotification status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public void update(Notification source) {
        this.message = source.getMessage();
        this.type = source.getType();
        this.status = source.getStatus();
    }

    @Override
    public Notification createNewInstance() {
        Notification notification = new Notification();
        notification.update(this);
        return notification;
    }
}
