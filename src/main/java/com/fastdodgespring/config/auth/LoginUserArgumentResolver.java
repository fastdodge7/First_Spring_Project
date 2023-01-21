package com.fastdodgespring.config.auth;

import com.fastdodgespring.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/*
* HandlerMethodArgumentResolver는 조건에 맞는 메소드가 있으면, 이 인터페이스의 구현체가 지정한 값으로 해당 메소드에
* 파라미터를 넘길 수 있다.
* */
@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        // 파라미터에 @LoginUser 어노테이션이 붙어 있는지 여부를 반환함
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        // 파라미터의 타입이 SessionUser 타입인가?
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUserAnnotation && isUserClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer
                                , NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception
    {
        return httpSession.getAttribute("user");
    }
}
/*
* 이 코드만으로는 Spring에서 LoginUserArgumentResolver 객체를 ArgumentResolver라고 인식하지 못하고, 그냥 커스텀 빈 하나라고
* 인식하기 때문에, webConfig 클래스에서 추가로 설정을 해 줘야 비로소 Spring이 LoginUserArgumentResolver를 ArgumentResolver로
* 인식한다.
* */
