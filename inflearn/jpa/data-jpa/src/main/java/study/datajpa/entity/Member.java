package study.datajpa.entity;

import lombok.*;

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
public class Member extends BaseEntity{

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
}
