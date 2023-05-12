package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

//@Repository 생략가능
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    @Query("select m from Member m where m.username = :username and m.age > :age")
    List<Member> findUser(
            @Param("username") String username,
            @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserNames();

//    @Query(
//            "select new study.datajpa.dto.MemberDTO(m.id, m.username, t.id, t.name) " +
//                    "from Member m join m.team t " +
//                    "where m.id = :memberId")
//    MemberDTO findMemberDTO(@Param("memberId") Long memberId);

    @Query(name = "Member.findMember")
    // NamedQuery 네임 지정안하면 엔티티이름 + 메소드이름으로 조회가능
    // 실무에선 사용 x (엔티티클래스가 지저분) @Query나 쿼리메소드형태로 많이사용
    List<Member> findMember();

    @Query(
            value = "select m.username, m.team FROM Member m left join m.team t",
            countQuery = "select count(m) from Member  m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    List<Member> findByUsername(@Param("username") String username);


    // 기본적으로 spring data jpa인터페이스를 활용하고
    // 동적쿼리 복잡한 쿼리는 query dsl을 사용해야하는데 이때 customrepository필요
    @Override
    List<Member> findMemberCustom();
}
