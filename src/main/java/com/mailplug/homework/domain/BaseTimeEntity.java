package com.mailplug.homework.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    public BaseTimeEntity() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }
}
