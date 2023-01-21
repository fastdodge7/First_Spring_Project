package com.fastdodgespring.config.auth;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target : 이 어노테이션이 생성될 수 있는 위치를 지정함
// 현재 PARAMETER로 지정했으니, 메소드의 파라미터로 선언된 객체에서만 사용 가능
// 그 외에도 클래스 선언문에 쓸 수 있도록 하게 하는 등, 다양한 설정 가능
@Target(ElementType.PARAMETER)

// 이 어노테이션의 라이프사이클을 결정함.
// RUNTIME의 경우, 프로그램이 JVM에 올라간 뒤에도 다른 객체 등에서 Reflection API 등을 사용해
// 해당 어노테이션의 정보를 알 수 있음. (Reflection API란, java에서
// SOURCE의 경우, 대표적으로 롬복의 Getter 어노테이션처럼, 컴파일 되기 이전에만 어노테이션이 존재하다
// 컴파일 후에는 접근할 수 없게 되는 방식.(롬복 Getter는, 컴파일 시 Getter 메소드에 해당하는 바이트 코드를
// 생성하고 class 파일이 된 후에는 사라진다.)
// CLASS의 경우, 프로그램이 JVM에 올라가면서 해당 어노테이션이 사라지는 정책이다.
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
