package com.fastdodgespring.web.domain.posts;

import com.fastdodgespring.domain.posts.Posts;
import com.fastdodgespring.domain.posts.PostsRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/*
* 이걸 빼고 테스트 하니까 -> AUTOWIRED MEMBERS MUST BE DEFINED IN VALID SPRING BEAN
* 경고가 떴고, postsRepository가 null이라서 save메서드를 부를 수 없다고 오류가 떴음.
* Spring의 자동 주입을 받기 위해서는 해당 필드는 스프링 빈에 등록되어야 하고, @Autowired를 붙여야 한다.
* 또한, 주입 받는 자신도 Spring에서 빈으로 등록되어 있어야 주입을 받을 수 있다.
* */
@ExtendWith(SpringExtension.class)
@SpringBootTest
/*
* @SpringBootTest가 빠지면 -> 이 PostTest라는 클래스는 Spring에서 빈으로서 등록되지 않음
* -> 따라서 PostsRepository를 Spring으로부터 주입 받을 수 없는 것!
* 또한, 이 어노테이션은 테스트에 필요한 각종 Application Context를 로드한다고 한다. 이게 뭔지는 더 알아봐야 할듯.
*
* 그것 외에도, @SpringBootTest가 빠지면 왜 동작이 멈추는지 좀 더 알아보면 Spring 구동 원리에 대해 더 깊게 이해할 수 있을듯.
* https://reflectoring.io/spring-boot-test/에 @SpringBootTest가 무엇을 하는지 꽤 설명이 잘 되어 있다.
* */

public class PostsTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    // 단위 테스트가 끝날 때마다 테이블을 싹 비우기 위해서 AfterEach 어노테이션을 붙임.
    // 테스트 여러개가 동시에 돌아가면 DB에 남아있는 데이터가 문제를 일으킬 수 있기 때문
    public void cleanup(){
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        String title = "테스트 게시글";
        String content = "테스트 본문";

        // save 메소드 : 테이블에 insert/update 쿼리를 실행함
        // id가 있으면 update, 없으면 insert 쿼리가 실행됨.
        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("fastdodge7@gmail.com")
                .build());

        // findAll 메소드 : SELECT * FROM table과 같음.
        // 각 row는 리스트의 하나의 객체로서 저장된다.
        List<Posts> postList = postsRepository.findAll();

        Posts posts = postList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }

    @Test
    public void BaseTimeEntity_등록()
    {
        LocalDateTime now = LocalDateTime.of(2022,12,22,19,19,0);
        postsRepository.save(Posts.builder()
            .title("title")
            .content("content")
            .author("author")
            .build());

        List<Posts> all = postsRepository.findAll();

        Posts post = all.get(0);

        System.out.println(">>>>>>>>> Created Date = " + now +
                ", Modified Date = " + now);
        System.out.println(">>>>>>>>> Created Date = " + post.getCreatedDate() +
                            ", Modified Date = " + post.getModifiedDate());

        assertThat(post.getCreatedDate()).isAfter(now);
        assertThat(post.getModifiedDate()).isAfter(now);
    }

}
