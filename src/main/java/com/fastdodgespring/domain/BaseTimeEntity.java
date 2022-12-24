package com.fastdodgespring.domain;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
// 만약 JPA Entity 클래스가 BaseTimeEntity를 상속받으면 BaseTimeEntity의 필드들도 칼럼으로 인식하게 만듦.
// Superclass(부모 클래스)의 필드도 테이블과 매핑한다는 의미인듯.
@EntityListeners(AuditingEntityListener.class)
// 이 클래스의 Auditing 기능을 추가함
// Auditing이란? JPA에서 어떤 엔티티를 감시(Auditing)하면서 수정/생성 시간을 자동으로 갱신시켜주는 기능.
public abstract class BaseTimeEntity {
    @CreatedDate // 상속 받은 Entity의 생성 시간을 나타낼 필드임을 Spring에게 알려줌.
    private LocalDateTime createdDate;

    @LastModifiedDate // 상속 받은 Entity의 최종 수정 시간을 나타낼 필드임을 Spring에게 알려줌.
    private LocalDateTime modifiedDate;
}
