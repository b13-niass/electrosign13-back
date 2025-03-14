package com.codev13.electrosign13back.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    @CreatedDate
    @Column(name = "createdAt")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    protected Date createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    protected Date updatedAt;

}