package com.fastdodgespring.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String name;
    /*
    * 열거형에 다른 필드를 집어 넣어서 각 원소마다 필드를 가질 수 있게 만들 수 있다.
    * 지금 코드에서는 GUEST, USER 각각 key와 title 필드를 갖도록 만든 것.
    * */
}
