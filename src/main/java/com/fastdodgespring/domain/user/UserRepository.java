package com.fastdodgespring.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    /*
    * Email 정보를 통해, 로그인 했을 때 이 사람이 처음 가입하는 사용자인지, 기존에 생성되었던 사용자인지
    * 확인하기 위해서 Email 정보를 확인한다.
    * */
}
