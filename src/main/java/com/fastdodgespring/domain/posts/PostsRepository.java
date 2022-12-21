package com.fastdodgespring.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
* 개발자들은 같은 단어도 전혀 다른 의미로 사용하는 동음이의어가 꽤 되는 것 같다...
* 여기서 Repository라는 단어가 나오는데, 내가 아는 리포지토리는 코드 저장소로 이용되는 깃허브 리포지토리인데..
* 여기서 나오는 Repository는 구현한 Entity(DB 테이블 객체)에 값 수정, 조회 메서드를 정의하기 위한 인터페이스이다.
* DB는 보통 접근하기 위해 Persistence layer를 거치는데, 이 Persistence layer와 매칭되는 개념이다.
* DAO라고도 한다! (Data Access Object) 그냥 이렇게 부르지...
* */

// 아니, JpaRepository는 결국 인터페이스인데, 이걸 extends하는 것 만으로도 실제 메소드를 쓸 수 있다고?
// -> Java Spring에서 자동으로 해당 인터페이스에 맞는 코드를 생성한다고 함. 또한 자동으로 '빈'으로 등록됨.

// 참고로 Repository와 Entity는 반드시 같은 위치에 있어야 한다.(같은 패키지 안에 같은 위치)
@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
}
