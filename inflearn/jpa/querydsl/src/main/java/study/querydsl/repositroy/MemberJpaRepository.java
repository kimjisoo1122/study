package study.querydsl.repositroy;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDTO;
import study.querydsl.dto.QMemberTeamDTO;
import study.querydsl.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
@Transactional
@RequiredArgsConstructor
public class MemberJpaRepository {

    @PersistenceContext
    private final EntityManager em;
    // 엔티티매니저는 스프링이 프록시로 주입해주는데
    // 실제 사용 시점에 트랜잭션단위로 할당해주기에 멀티스레드 환경에서 컨텍스트가 중복되는문제가 없다
    /**
     * 각각의 클라이언트들은 서비스를 이용할때 entitymanager를 싱글톤으로 사용하게되는데
     * 싱글톤이라면 모든 클라이언트들이 하나의 엔티티매니저를 쓰는것처럼 생각하지만
     * 엔티티매니저는 스프링에서 특별하게 관리하여
     * 실제 의존관계주입할때는 프록시객체를 주입하며
     * 클라이언트들이 실제 트랜잭션사용시점에 개별적으로 인스턴스를 생성해서 주입해준다
     * 스프링은 싱글톤 빈으로 EntityManager 인스턴스를 관리하지만, 실제로는
     * EntityManager의 프록시를 주입합니다. 이 프록시는 실제 사용 시점에 개별적으로
     * EntityManager 인스턴스를 생성하고 이를 트랜잭션과 연결합니다.
     * 이 방식은 동시성 문제를 피하면서도 EntityManager를 효과적으로 관리할 수 있게 해줍니다.
     * 이렇게 하면 각 트랜잭션은 고유한 영속성 컨텍스트를 가지게 되므로,
     * 각 클라이언트의 요청은 격리된 상태에서 처리됩니다. 이는 데이터 일관성을 유지하는 데
     * 매우 중요한 역할을 합니다.
     * 이런 방식은 JPA 뿐만 아니라, 스프링의 다른 트랜잭션 관련 구성 요소에서도
     * 일반적으로 사용됩니다. 예를 들어, JDBC DataSource 또한 비슷한 방식으로 작동하는데,
     * 싱글톤 DataSource 빈은 실제로는 커넥션 풀을 관리하며,
     * 각 트랜잭션은 이 커넥션 풀로부터 독립된 DB 커넥션을 얻게 됩니다.
     */
    private final JPAQueryFactory query;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findAll_Querydsl() {
        return query.selectFrom(member).fetch();
    }

    public List<Member> findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByUsername_Querydsl(String username) {
        return query.selectFrom(member).where(member.username.eq(username)).fetch();
    }

    public List<MemberTeamDTO> findMemberTeamDTO(MemberSearchCondition searchCondition) {

        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(searchCondition.getUserName())) {
            builder.and(member.username.eq(searchCondition.getUserName()));
        }
        if (StringUtils.hasText(searchCondition.getTeamName())) {
            builder.and(member.username.eq(searchCondition.getTeamName()));
        }
        if (searchCondition.getAgeLoe() != null) {
            builder.and(member.age.loe(searchCondition.getAgeLoe()));
        }
        if (searchCondition.getAgeGoe() != null) {
            builder.and(member.age.goe(searchCondition.getAgeGoe()));
        }

        query.select(new QMemberTeamDTO(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        member.team.id.as("teaMID"),
                        member.team.name.as("teamName")
                    ))
                .from(member)
                .join(member.team, team).fetchJoin()
                .where(builder)
                .fetch();


        return query
                .select(new QMemberTeamDTO(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(userNameEq(searchCondition.getUserName()),
                        (teamNameEq(searchCondition.getTeamName())),
                        (ageGoe(searchCondition.getAgeGoe())))
                // and 나 or 사용시 null 조심 null이면 조건에서 무시하지만
                // 반환값인 booleanexpression에다가 and를 달면 npe발생위험
                .fetch();
    }

    private BooleanExpression userNameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? member.team.name.eq(teamName) : null;
    }

    private BooleanExpression ageGoe(Integer age) {
        return age != null ? member.age.goe(age) : null;
    }

    private BooleanExpression ageLoe(Integer age) {
        return age != null ? member.age.loe(age) : null;
    }
}
