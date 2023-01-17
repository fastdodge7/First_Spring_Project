package com.fastdodgespring.config.auth.dto;

import com.fastdodgespring.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user)
    {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
/*
* User 클래스를 직접 사용하지 않고, 세션에 저장할 클래스를 따로 만드는 이유는?
* CustomOAuth2UserService클래스의 loadUser 메서드의 httpSession.setAttribute("user", new SessionUser(user));
* 라는 코드에서 new SessionUser 대신 User를 사용한다면, User 클래스에 Serializable 인터페이스가 구현되지 않았다고 오류가 뜬다.
* 그럼 User에 Serializable 인터페이스를 구현하면 될까?
*
* 구현한다고 오류가 나지는 않는다.
* 그러나, User 클래스는 Entity이다.
* 이런 Entity 클래스는 다른 엔티티와 관계가 형성될 수 있다.
* */