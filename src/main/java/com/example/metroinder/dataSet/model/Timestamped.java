package com.example.metroinder.dataSet.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Timestamped {
    @CreatedDate // 최초 생성 시점
    @Column(length = 30)
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 변경 시점
    @Column(length = 30)
    private LocalDateTime modifiedAt;

}