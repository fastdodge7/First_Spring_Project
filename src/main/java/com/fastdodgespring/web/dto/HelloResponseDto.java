package com.fastdodgespring.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor // 필요한(final)필드의 초기화를 하는 생성자를 자동으로 만드어준다
public class HelloResponseDto {
    private final String name;
    private final int amount;
}
