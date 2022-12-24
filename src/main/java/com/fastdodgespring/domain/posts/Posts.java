package com.fastdodgespring.domain.posts;

import com.fastdodgespring.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자를 만들어준다
@Entity // 이제 이 클래스는 DB상의 한 테이블과 링크될 것이라는 것을 나타내는 어노테이션
public class Posts extends BaseTimeEntity {

    @Id // 이 필드가 해당 테이블의 Primary Key가 된다는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Primary Key의 생성 규칙을 지정하는 어노테이션. GenerationType.Identity는 auto_increment 방식임
    // 어지간해서는 auto_increment를 사용하는 것이 좋음. 91p 참고.
    private Long id;

    @Column(length = 500, nullable = false)
    // 테이블의 한 칼럼이라는 것을 나타내는 어노테이션. 기본적으로 쓰지 않아도 Entity가 붙은 클래스의 필드는 칼럼이 되지만,
    // 칼럼의 기본 설정을 변경하는 경우에 쓴다.
    // String은 기본적으로 VARCHAR(255)가 기본 값인데, 여기선 사이즈를 500으로 늘렸음.
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    // 여기선 타입을 VARCHAR(255)에서 TEXT로 바꿨음
    private String content;

    private String author;

    @Builder // 빌더 클래스를 생성해 주는 어노테이션
    /*
    * https://readystory.tistory.com/121 빌더 클래스에 대한 포스트.
    * 91p 부터 빌더 클래스에 대한 설명이 나와있음.
    * 빌더 패턴이란, 생성 패턴 중의 하나로, 인스턴스 생성 절차를 추상화한다.
    * 생성패턴은 시스템이 어떤 Concrete class(추상 클래스가 아닌 클래스)를 사용하는지 가려주고
    * 인스턴스가 만들어지는 과정을 완전히 가려준다
    *
    * 빌더 패턴은 인스턴스의 필수값은 빌더 클래스의 생성자로, 다른 Optional한 필드는 메소드를 따로 호출하며
    * 초기화 해 준다.
    * 팩토리 패턴에서는 팩토리 클래스에 파라미터를 넘길 때, 파라미터의 순서 등에 따라 어떤 Sub클래스를 사용할지
    * 구분하는게 어려웠는데, 빌더 패턴은 이런 문제를 해결한다.
    * */
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

}
