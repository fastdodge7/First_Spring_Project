package com.fastdodgespring.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController { // 페이지와 관련된 모든 일을 담당하는 컨트롤러
    @GetMapping(value = "/") // 이 URL로 요청이 들어오면,
    public String index(){
        return "index"; // 이 문자열이 View Resolver에게 들어간다. 자세한 내용은 https://yenbook.tistory.com 을 참고하자.
    }

    @GetMapping("/posts/save") // 이 URL로 요청이 들어오면
    public String postsSave(){
        return "posts-save"; // 자동으로 posts-save.mustache를 불러온다.
    }
}
/*
* mustache플러그인이 저 index라는 문자열이 리턴될 때, 자동으로 index.mustache 파일이 있는 경로를 생성해서 view resolver에게
* 해당 경로를 문자열로 반환한다. 그러면 view resolver는 해당 파일을 찾아서 view 객체에 넘겨준다
* */