package com.ll.medium.global.jpa;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
@ToString(callSuper = true)
public class BaseEntity extends IdEntity {

    private static final DateTimeFormatter FORMATTER;

    static {
        FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    @PrePersist
    public void prePersist() {
        if (createDate != null) {
            createDate = LocalDateTime.parse(createDate.format(FORMATTER), FORMATTER);
        }
        if (modifyDate != null) {
            modifyDate = LocalDateTime.parse(modifyDate.format(FORMATTER), FORMATTER);
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (modifyDate != null) {
            modifyDate = LocalDateTime.parse(modifyDate.format(FORMATTER), FORMATTER);
        }
    }
}