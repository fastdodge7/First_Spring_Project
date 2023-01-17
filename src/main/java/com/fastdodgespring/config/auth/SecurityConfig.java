package com.fastdodgespring.config.auth;

import com.fastdodgespring.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity//(debug = true)
@Configuration
public class SecurityConfig{

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean // 이거 빈 등록을 안하니까 무조건 로그인 하라고 나온다?
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        System.out.println("FilterChain called!");
        http
                .csrf().disable()
                .headers().frameOptions().disable()// h-2 console을 쓰기 위한 코드라고 한다.
                .and()
                    // authorizeHttpRequests 메서드는 URL별로 권한 설정을 하는 옵션의 시작점이다.
                    // SecurityFilterChain은 메서드 호출 순서가 중요하다. 각 메서드마다 종속성을 지니고 있기 때문.
                    // requestMatcher는 authorizeHttpRequests 메서드를 호출한 뒤에야 쓸 수 있다.
                    .authorizeHttpRequests()

                    // 인덱스 페이지, css, images, js와 같이 정적 컨텐츠들은 모든이에게 허용한다.
                    .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**")
                    .permitAll()

                    // api 호출(글 게시/수정/삭제 등)은 USER Role을 가진 사용자만 접근할 수 있게 한다.
                    .requestMatchers("/api/v1/**").hasRole(Role.USER.name())

                    // 위에서 설정한 URL을 제외하면(정적 리소스 / API 호출) 인증된 사용자(로그인 한 사용자)에게만 사용 가능하게 한다는 코드이다.
                    .anyRequest().authenticated()
                .and()
                    // 로그아웃 시에는 인덱스 페이지로 돌아오도록 한다.
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    //oauth2 로그인 기능을 설정하겠다는 코드
                    .oauth2Login()
                        //로그인 성공 후, 사용자 정보를 가져올 때의 설정들을 하겠다는 코드
                        .userInfoEndpoint()
                            // 로그인 성공 후, 후속조치를 어떤 구현체에서 하겠다는건지 명시함
                            .userService(customOAuth2UserService);

                return http.build();
    }


}
