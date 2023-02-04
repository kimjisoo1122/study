package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

//    @Transactional
    // jpa는 반드시 tx환경이여야 함.
    public void save(Member member) {
        log.info("멤버 저장");
        em.persist(member);
    }
    @Transactional
    public Optional<Member> find(String username) {
        // pk가 아니라 jpql사용
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultStream().findAny();



    }
}
