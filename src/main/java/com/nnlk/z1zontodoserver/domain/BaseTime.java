package com.nnlk.z1zontodoserver.domain;

import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class) //JPA에게 해당 Entity는 Auditing기능을 사용한다는 것을 알리는 어노테이션입니다.
public class BaseTime {

    @CreatedDate
    @Column(updatable = false) // 처음 생성될때만 생기면 되므로...
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;



}
