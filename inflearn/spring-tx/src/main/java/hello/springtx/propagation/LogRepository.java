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
public class LogRepository {

    private final EntityManager em;

//    @Transactional
    public void save(Log logmessage) {
        log.info("로그 저장");
        em.persist(logmessage);

        if (logmessage.getMessage().contains("로그예외")) {
            log.info("로그 저장이 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    @Transactional
    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message = :message", Log.class)
                .setParameter("message", message)
                .getResultStream().findAny();
    }
}
