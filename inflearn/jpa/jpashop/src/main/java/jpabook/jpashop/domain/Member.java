package jpabook.jpashop.domain;

import javax.naming.Name;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;

@Entity
//@Table(indexes = @Index(name = "idx_name", columnList = "name"))
// 인덱스 또는 유니크 제약조건의 이름을 정할수 있다. -> ddl을 엔티티에 명시해야 비즈니스로직 짤떄 좋음
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    // 멤버 조회시 멤버는 실제 엔티티  team은 프록시 객체
    // member.getTeam().getName() -> 강제 초기화

    // 실무에선 지연 로딩만 사용해야 하는데
    // 연관 관계가 10개면?? 한번에 하나의 엔티티 조회하는데 조인을 10번을 함...
    // jpql을 사용하면 N + 1 문제가 발생하는데
    // em.find는 jpa자체적으로 join해서 엔티티를 조회하는데
    // jpql은 sql그자체로 나감 멤버를 조회했는데 team도 채워야해서 team조회 쿼리가 날라감
    // 멤버를 조회하면 N  + 관계엔티티가 추가로 + 1 조회되는 문제
    // -> join fetch로 해결함
    // ~ One시리즈들은 default가 eager라 lazy로반드시 변경 ManyToOne OntToOne
    @JoinColumn(name = "team_id")
    // 양방향의 주인은 항상 fk를 가지고 있는 쪽 N:1의 N쪽
    private Team team;

    // 양방향 전용 메소드가 필요
    private void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    // 일대일 관계는 fk키에 유니크제약 조건을 걸어 완성
    // 일대일 관계는 어느 엔티티에 FK를 넣어도 관계가 성립되기에 주인을 아무거나 해도 상관없으나
    // 주로 자주쓰는 테이블을 주인으로 하는게 좋음
    // 멤버를 라커보다 조회를 많이함
    // 대상테이블(라커)에 외래키 존재시 지연 로딩으로 설정해도 항상 즉시 로딩됨
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id")
    @Column(unique = true)
    private Locker locker;

    // 임베디드타입을 여러 엔티티에서 사용할 경우 매 엔티티마다 새로운 임베디드값을 넣어줘야함
    // 같은 객체를 넣어주면 수정할떄 전부 수정됨
    // 원래 값은 primitive 특성을 지녀야 하는데 임베디드타입은 객체라 사이드이펙트 주의
    // 그걸 막으려면 임베디드 타입은 생성자로만 생성하게 끔 해서 변경못하게 유도
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "work_city"))})
    private Address address;

    @ElementCollection
    @CollectionTable(
            name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "MEMBE 0R_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();
    // 값 타입 컬렉션은 치킨,피자같은 단순한 경우에만 사용
    // 엔티티 생성해서 OneToMany로 설정하고 cascade와 orphanremoal주면 됨





    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
