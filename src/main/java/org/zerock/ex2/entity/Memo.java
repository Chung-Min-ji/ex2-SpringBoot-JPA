package org.zerock.ex2.entity;

import lombok.*;

import javax.persistence.*;

// @Entity : 엔티티 클래스에 반드시 붙여줘야 함.
//           엔티티 클래스는 옵션에 따라 자동으로 테이블 생성할 수 있고, 이 경우 엔티티 클래스 멤버 변수대로 컬럼 자동 생성됨.
@Entity

// @Table : @Entity 어노테이션과 함께 사용.
//          해당 엔티티 클래스를 DB 상에 어떠한 테이블로 생성할 것인지 정보 제공.
// 테이블명에 에러뜨는 것은, DB설정과 DB내 테이블을 같이 확인하기 때문.
// 에러 없애려면 File -> Setting -> Editor -> Inspections 에서 JPA항목의
// 'Unresolved database refernces in annotations' 항목 체크 해제
@Table(name="tbl_memo")

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {

    // @Entity 클래스에서 PK에 해당하는 필드는 @Id 지정
    @Id
    // @Id가 사용자가 입력한 값 사용하는 경우가 아니라면
    // 자동으로 생성되는 번호 사용하기 위해 활용하는 어노테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
} //end class
