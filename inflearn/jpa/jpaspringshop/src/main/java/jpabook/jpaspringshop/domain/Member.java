package jpabook.jpaspringshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
// getter는 필수 setter는 지양
public class Member {

    /**
     * 엔티티 설계시 주의점
     * setter는 엔티티를 쉽게 수정하게 설정할 수 있는 여지를 컴파일 과정에서 제한시켜주는게 좋다
     * 모든 연관관계는 지연 로딩을 사용해야 한다.
     * eager 즉시 로딩으로 사용시에는 n + 1 문제가 발생한다
     * 엔티티 조회의 경우 즉시로딩이라고 해서 바로 join문으로 가져오는게 아닌
     * 엔티티를 조회하고 해당 필드가 즉시로딩인경우 select절을 한번더 날린다 이것이 n + 1(관계필드갯수)
     * XtoOne의 기본값은 모두 eager임
     * 일대다의 mapped by 컬렉션은 필드에서 초기화를 해준다.
     * 스프링부트의 경우 카멜케이스, 대문자, .(점)들을 소문자로 변경한다
     * 양방향관계의 경우 연관관계 편의메소드로 컬렉션은 조회전용이지만 같이 업데이트해줘야 한다
     */

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member") // readonly
    private List<Order> orders = new ArrayList<>();
}
