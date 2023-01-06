package com.fastdodgespring.service.posts;

import com.fastdodgespring.domain.posts.Posts;
import com.fastdodgespring.domain.posts.PostsRepository;
import com.fastdodgespring.web.dto.PostsListResponseDto;
import com.fastdodgespring.web.dto.PostsResponseDto;
import com.fastdodgespring.web.dto.PostsSaveRequestDto;
import com.fastdodgespring.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto)
    {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream() // stream 메서드를 통해 List로부터 stream 객체를 생성
                .map(PostsListResponseDto::new)       // 생성자 레퍼런스.https://tourspace.tistory.com/7에 자세한 설명이 되어있음
                .collect(Collectors.toList());        // 일단 PostsListResponseDto의 생성자에 파라미터로 List의 원소가 들어간다 이해하자.
    }

    @Transactional
    public PostsResponseDto findById(Long id)
    {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No posts matching with ID : " + id));
        return new PostsResponseDto(entity);
        /*
        * find는 내가 구현해보겠다고 쓴 코드가 밑의 코드. 생각없이 무작정 돌아가게만 만들겠다고 코드를 저렇게 늘려놨다.
        * 위는 책에 나온 코드. PostsResponseDto의 생성자를 하나 추가해서 밑의 리턴쪽의 코드를 확 줄였다.
        * findById(id)의 리턴이 자바의 Optional이라는 타입인데, orElseThrow라는 메소드가 있더라.
        * find 결과가 없으면 괄호 안의 함수를 던져서 예외를 던질 수 있다. 결과가 있으면 그냥 그 결과를 리턴.
        * 좋은 메소드 하나 알아간다.
        * */

//        Optional<Posts> findResult = postsRepository.findById(id);
//        if(findResult.isEmpty()){ throw new Exception("No record"); }
//
//        Posts post = findResult.get();
//
//        return PostsResponseDto.builder()
//                .title(post.getTitle())
//                .content(post.getContent())
//                .author(post.getAuthor())
//                .build();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto)
    {
        /*
        * 신기한점 : 아니, 포스트 내용을 업데이트 하는데, DB에 쿼리를 날리지 않음!! post.update 메소드의 코드를 보면 알겠지만
        * 그 메소드는 그냥 자기 title, content 필드를 바꾸기만 할 뿐이다!
        * 그런데도 이게 가능한 이유는 JPA의 "영속성 컨텍스트" 때문이라고 한다.
        *
        * 영속성 컨텍스트란? 엔티티를 영구히 저장하는 환경
        * 엔티티가 영속성 컨텍스트 안에 포함되어 있고, JPA의 엔티티 매니저가 활성화 상태라면
        * JPA는 해당 엔티티에 뭔가 변화가 생겼을 경우, "트랜잭션이 종료되는 시점에" 해당 엔티티의 변경사항을 DB에 반영한다.
        * 이를 더티 체킹이라고 한다. DB시간에 배웠던 버퍼 매니저의 '더티 페이지' 관련 개념들이 확 떠오르는 대목이다.
        * detach된 엔티티나, DB에 반영되기 이전의, 처음 생성된 엔티티는 이 더티 체킹의 대상이 되지 않는다.
        * */
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No matching posts with id : " + id));
        post.update(requestDto.getTitle(), requestDto.getContent());
        return id;

//        Optional<Posts> findResult = postsRepository.findById(id);
//        if(findResult.isEmpty()){ throw new Exception("No record"); }
//
//        findResult.get().update(requestDto.getTitle(), requestDto.getContent());
//        return id;
    }

    @Transactional
    public Long delete(Long id)
    {
        Posts post = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No matching posts with id : " + id));
        postsRepository.delete(post);
        return id;
        /*
        * postsRepository는 JpaRepository를 implement 하고 있으므로, Jpa에서 제공하는 delete 기능을 이용할 수 있음.
        *
        * */
    }

}
