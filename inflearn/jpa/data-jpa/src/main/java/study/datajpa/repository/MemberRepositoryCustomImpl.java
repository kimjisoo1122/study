package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
@RequiredArgsConstructor
// Impl을 뒤에 붙히는걸 약속해야함
// 임의의 그냥 리포지토리를 만들어서 사용하는것도 좋은 방법
// 쿼리 전용 리포지토리를 만들어서 서비스에서 직접사용
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    List<Member> findList() {
        return jdbcTemplate.query("select * from Member", (rs, rowNum) -> {
            return Member.createMember(rs.getString("ddd"));
        });
    }


}
