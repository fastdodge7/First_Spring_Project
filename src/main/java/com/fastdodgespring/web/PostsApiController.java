package com.fastdodgespring.web;

import com.fastdodgespring.service.posts.PostsService;
import com.fastdodgespring.web.dto.PostsResponseDto;
import com.fastdodgespring.web.dto.PostsSaveRequestDto;
import com.fastdodgespring.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {
    private final PostsService postService;

    @PostMapping("/api/v1/posts") // api/v1은 또 뭐냐? -> RestAPI에 대해서 공부할 필요가 있을듯
    // 저 문자열을 통해 Post요청을 받은 경우, 이 메소드가 실행됨.
    public Long save(@RequestBody PostsSaveRequestDto requestDto)
    {
        return postService.save(requestDto);
    }

    /*
    * @PathVariable : GetMapping 안에 url을 보면, {}로 묶여진 id가 보일 것이다.
    * 저걸 PathVariable이라고 하는데, 이 예제에서는 id를 가지고 포스트를 가져오는 기능을
    * 구현할 것이므로, url로부터 id를 가져오기 위해서 해당 어노테이션을 사용한다.
    * */
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id)
    {
        return postService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto)
    {
        return postService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id)
    {
        postService.delete(id);
        return id;
    }

}
/*
* @RequestBody란 무엇인가? https://wildeveloperetrain.tistory.com/144
* 간단히 말해 HTTP 리퀘스트 헤더 부분을 제외한 Body 부분이 Spring의 MessageConverter를 통해서
* 자바 객체로 변환된다는 것이다.
*
* 지금 구현하는 기능은 유저가 웹 페이지에 포스트 하나를 올리는 기능인데,
* 사용자는 포스팅을 하면 HTTP Post Requst를 서버에 보낸다. 이때 Request 헤더와 더불어서
* 포스팅 내용(여기서는 제목, 본문, 저자)의 데이터가 HTTP Post Body로 딸려오게 된다.
* @RequestBody PostsSaveRequestDto는 그 Body를 PostsSaveRequestDto라는 객체로 전환시켜주는 역할을 하는것.
*
* */

