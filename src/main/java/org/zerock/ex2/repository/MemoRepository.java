package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

import java.util.List;

// 구현내용 없이, JpaRepository 인터페이스를 상속하는 것 만으로 작업이 끝난다.
// 인터페이스끼리 상속할 때에는 extends 키워드 사용
public interface MemoRepository extends JpaRepository<Memo, Long> {

    // Query Methods
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    void deleteMemoByMnoLessThan(Long num); // 실제 개발에서 deleteBy는 잘 사용하지 않음. (왜냐면 하나씩 select해서 삭제하기 때문에..
                                            // -> @Query 이용)

    // Query Method + Pageable
    // 정렬 관련된 부분은 Pagable로 처리해서 더 간략한 메서드 생성
    Page<Memo> findByMnoBetween(Long frojm, Long to, Pageable pageable);
}
