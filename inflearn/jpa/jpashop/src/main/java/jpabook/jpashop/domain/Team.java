package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {


    @Id
    @GeneratedValue
    @Column(name = "team")
    private Long teamId;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    // 무한루프조심
    // TEAM을 TO STRING하면 members안의 member를 호출하는데 이때 member안의 team을 또 호출하고
    // team의 또 members를 호출하고 반복 -> lombok to string쓸때 제외시키거나 사용하지 말것
    // json 생성 라이브러리에서도 문제 -> entity를 직접 컨트롤러에서 사용할떄 문제
    // 결론은 테이블 설계할때 단방향으로만 해결할 것 단방향으로 일단 설계를 끝내야함.

    public List<Member> getMembers() {
        return members;
    }

    // 양방향의 주인은 항상 N:1의 N쪽 mappedBy는 내가 주인쪽으로 매핑된다는 의미
    // 조회전용이다
    // 영속성컨텍스트는 연관관계의 주인의 변경사항을 감지한다.
    // 양방향 연관관계는 객체값을 서로 세팅해줘야 좋다 (jpa랑은 관련없다)
    public Long getTeamId() {
        return teamId;
    }
}
