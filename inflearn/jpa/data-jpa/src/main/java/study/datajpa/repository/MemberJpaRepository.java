package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import java.util.List;

//@Repository 생략가능
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.username = :username and m.age > :age")
    List<Member> findUser(
            @Param("username") String username,
            @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserNames();

    @Query(
            "select new study.datajpa.dto.MemberDTO(m.id, m.username, t.id, t.name) " +
                    "from Member m join m.team t " +
                    "where m.id = :memberId")
    MemberDTO findMemberDTO(@Param("memberId") Long memberId);

    @Query(name = "Member.findMember")
    // NamedQuery 네임 지정안하면 엔티티이름 + 메소드이름으로 조회가능
    // 실무에선 사용 x (엔티티클래스가 지저분) @Query나 쿼리메소드형태로 많이사용
    List<Member> findMember();
}
