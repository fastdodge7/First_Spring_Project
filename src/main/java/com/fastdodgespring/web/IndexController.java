package com.fastdodgespring.web;

import com.fastdodgespring.config.auth.LoginUser;
import com.fastdodgespring.config.auth.dto.SessionUser;
import com.fastdodgespring.service.posts.PostsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class IndexController { // 페이지와 관련된 모든 일을 담당하는 컨트롤러
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping(value = "/") // 이 URL로 요청이 들어오면,
    public String index(Model model, @LoginUser SessionUser user){
        /*
        * user 파라미터는 @LoginUser 어노테이션의 처리 로직을 거친 후에 파라미터로 들어오게 된다.
        * */
        model.addAttribute("posts", postsService.findAllDesc());
        /*
        * 위 코드는 우리가 사용하는 템플릿 엔진(본 프로젝트에서는 mustache, 그 외에는 대표적으로 JSP 등등)에서
        * 선언해 놓은 변수에 findAllDesc 메서드 실행 결과로 나오는 리스트를 넘겨주는 역할을 한다.
        *
        * Model 클래스는 Spring에서 꽤 특수한 클래스이다.
        * 일종의 DTO처럼, 컨트롤러 클래스의 메서드에는 Model을 파라미터로 넣을 수 있는데, 얘는 뷰(view)단으로 전달할 데이터를
        * 담는 역할을 한다. 별도의 작업 없이, Spring에서 자동으로 메서드에 사용할 Model 객체를 주입해 준다.
        * */

        if(user != null)
            model.addAttribute("userName", user.getName());
        return "index"; // 이 문자열이 View Resolver에게 들어간다. 자세한 내용은 https://yenbook.tistory.com 을 참고하자.
    }

    @GetMapping("/posts/save") // 이 URL로 요청이 들어오면
    public String postsSave(){
        return "posts-save"; // 자동으로 posts-save.mustache를 불러온다.
    }
    // posts-save.mustache를 보면, 글 작성 화면에서는 DB쪽의 데이터를 보여줄 필요가 없으므로, Model 객체가 파라미터로 들어가지 않는다.
    // View 단으로 보내야 할 데이터가 따로 없다는 뜻.

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        model.addAttribute("post", postsService.findById(id));
        return "posts-update"; // 자동으로 posts-save.mustache를 불러온다.
    }

    @GetMapping("/posts/delete/{id}")
    public String postsDelete(@PathVariable Long id, Model model){
        model.addAttribute("post", postsService.findById(id));
        return "posts-update"; // 자동으로 posts-save.mustache를 불러온다.
    }
}
/*
* mustache플러그인이 저 index라는 문자열이 리턴될 때, 자동으로 index.mustache 파일이 있는 경로를 생성해서 view resolver에게
* 해당 경로를 문자열로 반환한다. 그러면 view resolver는 해당 파일을 찾아서 view 객체에 넘겨준다
* */