package fastdodgespring.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Json 파일을 리턴하는 컨트롤러로 만들어줌
public class HelloController {
    @GetMapping("/hello") // HTTP Get 요청을 받을 수 있게 해 준다.
    // 이제 /hello로 요청이 오면 문자열 hello를 반환하는 기능이 추가된 것.
    public String hello(){
        return "hello";
    }
}
