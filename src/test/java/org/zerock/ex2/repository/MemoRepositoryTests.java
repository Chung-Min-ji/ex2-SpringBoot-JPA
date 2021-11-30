package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex2.entity.Memo;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepo;

    @Test
    public void testClass(){
        assertNotNull(memoRepo);

        System.out.println(memoRepo.getClass().getName());
    }

    //-- 삽입
    @Test
    public void testInsertDummies(){

        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("sample..."+i).build();
            // 수정/등록작업 모두 save()
            memoRepo.save(memo);
        });
    }

    //-- 조회
    @Test
    public void testSelect(){

        // DB내에 존재하는 mno
        Long mno = 100L;

        // 이 때 SQL실행
        Optional<Memo> result = memoRepo.findById(mno);

        System.out.println("=============================");

        if(result.isPresent()){
            // select = get()
            Memo memo = result.get();

            System.out.println(memo);
        }
    }

    //-- 조회
    @Transactional
    @Test
    public void testSelect2(){

        // DB에 존재하는 mno
        Long mno = 100L;

        Memo memo = memoRepo.getOne(mno);

        System.out.println("==============================");

        System.out.println("memo : " + memo);
    }

    //-- 수정
    @Test
    public void testUpdate(){

        // 100번의 Memo 객체 만들고, save 호출
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();

        // 수정/등록작업 모두 save()
        System.out.println(memoRepo.save(memo));
    }

    //-- 삭제
    @Test
    public void testDelete(){

        Long mno = 100L;

        memoRepo.deleteById(mno);
    }

    //-- 기본 페이징처리
    @Test
    public void testPageDefault(){

        // Pageable 인터페이스 : 페이지 처리에 필요한 정보 전달.
        // 실제 객체 생성할 때는, 구현체인 PageRequest 클래스 사용.
        // PageRequest 생성자는 protected 이므로, 객체 생성시 new가 아닌 static 메소드인 of() 이용한다.
        // ** of()의 오버라이딩 형태 확인하기

        // ****** 페이지 처리는 반드시 '0'부터 시작한다!
        // 1페이지 10개
        Pageable pageable = PageRequest.of(0,10);

        // 페이징처리에 사용하는 findAll().
        // 리턴타입을 Page<T> 타입으로 지정할 경우, 반드시 파라미터를 Pageable타입으로 이용해야 한다.
        Page<Memo> result = memoRepo.findAll(pageable);

        System.out.println(result);

        //-- Page<> 타입의 메소드 테스트
        System.out.println("Total Pages: " + result.getTotalPages()); // 총 몇 페이지?
        System.out.println("Total Count: " + result.getTotalElements()); // 전체 게시물 수
        System.out.println("Current Page Number: " + result.getNumber()); // 현재 페이지 번호 (0부터 시작)
        System.out.println("Page Size: " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("has next page?: " + result.hasNext()); // 다음 페이지 존재 여부
        System.out.println("first page?: " + result.isFirst()); // 시작 페이지(0) 여부

        System.out.println("---------------------------");

        // 실제 페이지의 데이터를 처리하는 것은 getContent()를 이용해서 List<엔티티 타입>으로 처리하거나,
        // Stream<엔티티 타입> 을 반환하는 get()을 이용할 수 있다.
        for(Memo memo: result.getContent()) {
            System.out.println(memo);
        }
    }


    //-- 정렬조건 추가하기
    @Test
    public void testSort(){

        Sort sort1 = Sort.by("mno").descending();

        // 정렬조건 여러개 지정하고 싶으면, and() 이용해서 지정할 수 있다.
//        Sort sort2 = Sort.by("memoText").ascending();
//        Sort sortAll = sort1.and(sort2);    // and 를 이용한 연결
//        Pageable pageable = PageRequest.of(0,10,sortAll); // 결합된 정렬조건 사용

        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Memo> result = memoRepo.findAll(pageable);

        // get() = select
        // result.get().forEach(memo -> {
        //   System.out.println(memo);
        // });
        result.get().forEach(System.out::println);
    }


    //-- 쿼리메소드 테스트
    @Test
    public void testQueryMethods(){

        List<Memo> list = memoRepo.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for(Memo memo:list){
            System.out.println(memo);
        }
    }

    //-- 쿼리메소드 + Pageable의 결합
    @Test
    public void testQueryMethodWithPagable(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepo.findByMnoBetween(10L, 50L, pageable);

        result.get().forEach(System.out::println);
    }


    //-- 쿼리메소드 (mno 기준으로 삭제하기)
    @Commit //이 어노테이션 없으면 테스트코드는 Rollback.
    @Transactional //deleteBy... 는 select + delete 같이 이루어지므로 트랜잭션 처리 필요함.
    @Test
    public void testDeleteQueryMethods(){

        // mno가 10보다 작은 데이터 삭제하기
        memoRepo.deleteMemoByMnoLessThan(10L);
    }
}
