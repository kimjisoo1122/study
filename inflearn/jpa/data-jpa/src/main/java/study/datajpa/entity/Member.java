package study.datajpa.entity;

import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findMember",
        query = "select m from Member m"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity implements Persistable<String> {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tead_id")
    private Team team;

    public static Member createMember(String username) {
        Member member = new Member();
        member.username = username;
        return member;
    }

    public static Member createMember(String username, int age, Team team) {
        Member member = new Member();
        member.username = username;
        member.age = age;
        member.changeTeam(team);
        return member;
    }

    public static Member createMember(String username, int age) {
        Member member = new Member();
        member.username = username;
        member.age = age;
        return member;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Override
    public boolean isNew() {
        /**
         * spring data jpa의 save 메커니즘은 엔티티의 식별자가 객체면 null 프리미티브면 0으로 판단
         * @generatevalue를 사용해서 인조식별자를 만드는 경우는 상관없는데
         * String 식별자를 사용해서 직접 식별자를 주입하는 경우에는
         * save할때 isNew가 식별자 String을 확인해서 null이 아니기에 merge해버림
         * 그래서 genereavalue를 사용하지 않을때는 Persistable<String>을 구현하여 isNew 오버라이딩필수
         */

        return getCreatedDate() == null;
    }
}
