package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

//@Repository 생략가능
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    @Query("select m from Member m where m.username = :username and m.age > :age")
    List<Member> findUser(
            @Param("username") String username,
            @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserNames();

    // named쿼리가 존재하면 사용하고 없으면 직접명시한걸 쓰고 그것도 없으면 메소드쿼리 시전
    // @Query 네임드쿼리는 애플리케이션 로딩중에 sql문을 검증함 일단 jpql은 검증안함 (해당쿼리사용전까지)
    // 메소드쿼리는 파라미터가한 2개정도까지 외에복잡한 쿼리는 @Query 사용
    // 그외 진짜 복잡하거나 동적쿼리는 querydsl 사용

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

    @Query(value = "select m from Member m join m.team where m.age = :age",
            countQuery = "select count(m) from Member m where m.age = :age")
    Page<Member> findByPage(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    // modifying이 있어야 executeupdate가 실행된다
    // 벌크업데이트 중요한 주의점은 벌크업데이트,삭제는 1차캐시 sql지연저장소를 안거치고
    // 바로 디비에 날리기에 영속성컨텍스트에 동기화가 안돼있음
    // 그래서 벌크연산 이후에는 em.flush em.clear해서 해줘야 함
    // clearauto기능도 있네? 이거 필수로 켜야 겠네 왜 default가 아니지
    // jpa와 jdbctemplate혼용해서 쓸떼는 이런 벌크성을 조심해야함
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @EntityGraph(attributePaths = {"team"})
    // jpql의 join fetch기능을 스프링 data jpa에서 사용하는 방법
    // @Query느 메소드쿼리 모두 사용 가능하다
    // @EntityGraph("Member.All")  엔티티에 네임드엔티티그래프를 선언해서 사용도 가능하다
    // 그냥 jpql로 쓰는편 간단할때만 엔티티그래프 사용
    List<Member> findAllEntityGraph();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUserName(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLock();
}
