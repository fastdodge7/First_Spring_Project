package com.fastdodgespring.domain.user;


import com.fastdodgespring.domain.BaseTimeEntity;
import com.fastdodgespring.domain.user.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity { // 사용자 정보를 담을 DB 테이블!
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String picture;

    /*
    * @Enumerated : JPA가 DB에 Enum 타입을 저장할 때, 어떤 형태로 저장할지를 지정함!
    * 디폴트는 int, EnumType.STRING을 통해 String으로 저장하도록 한다.
    * */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String email, String Picture, Role role)
    {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.role = role;
    }

    public User update(String name, String picture)
    {
        this.name = name;
        this.email = email;
        return this;
    }

    public String getRoleKey()
    {
        return this.role.getKey();
    }

}
