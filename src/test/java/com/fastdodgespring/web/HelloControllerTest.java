package com.fastdodgespring.web;

import com.fastdodgespring.web.HelloController;
import com.fastdodgespring.web.dto.HelloResponseDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {
    @Autowired private MockMvc mvc;

    @Test
    public void returnsHello() throws Exception{
        String hello = "hello";
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void returnsHelloDto() throws Exception{
        String name = "hello";
        int amount = 1000;
        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount))) // param에는 무조건 string만 가능해서 int를 변환.
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is(name)))
        .andExpect(jsonPath("$.amount", is(amount)));

        //$.name은 무엇인가? -> 아마 Json에서 부모 자식 관계가 있는 오브젝트가 포함된다면, 부모(루트)부터 쭉쭉 내려가야 하는 경우가
        //있을 수 있는데, $란 문자는 루트노드를 나타내는 문자임. 지금은 그냥 오브젝트 하나에 name, amount 필드가 있으니, 루트에서
        //부터 필드를 적는것.
    }

}
