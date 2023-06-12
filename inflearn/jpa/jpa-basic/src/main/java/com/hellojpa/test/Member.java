package com.hellojpa.test;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 1)
// allocation size는 시퀀스값을 미리 사이즈만큼 조회하여 메모리에 올려서 db를 조회하지않고
// 엔티티생성시 pk값을 메모리에서 얻어오는 구조
// 단 사용안한 pk들은 db에서 사라지게됨 숫자의 순차성이 보장안됨
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "MEMBER_SEQ_GENERATOR") // 개별 SEQ 사용할시 제네레이터 생성
    // MYSQL은 IDENTITY ORACLE은 SEQUENCE AUTO로 설정시 각각의 IDENTITY로 설정됨.
    // TABLE은 자체 채번테이블을 생성함 @TableGenerator -> 운영에서 성능이슈 부담됨
    @Column(name = "MEMBER_ID")
    private Long id;
    // 권장하는 식별자 전략 Long + 대체키(UUID) + 키(랜덤키) 생성전략 인조식별자 사용
    // 전략을 identity로 설정할시 엔티티를 영속성컨텐스트에 올린순간 쿼리가 날라감
    // pk값을 조회해야 하기 때문 // eager 로딩
    // 시퀀스전략을 사용하면 시퀀스에서 값을 미리 조회해와서 pk값을 넣어줌
    // lazy 로딩 가능
    // seq의 allocationsize는 미리 설정된 값만큼 시퀀스를 땡겨온다 매 생성마다 조회하는 문제 보완

}
