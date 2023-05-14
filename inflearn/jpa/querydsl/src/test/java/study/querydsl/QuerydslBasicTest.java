package study.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @PersistenceContext
    EntityManager em;
    JPAQueryFactory query;

    @PostConstruct
    public void post() {
        query = new JPAQueryFactory(em);
    }

    @BeforeEach
    public void beforeEach() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() throws Exception {
        // given
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        // when
        assertNotNull(findMember);
        // then
    }

    @Test
    void stateQuerydsl() throws Exception {
        // given
        // when
        assertNotNull(member);
        Member findMember =
                query.selectFrom(member)
                        .where(member.username.eq("member1")
                                .and(member.age.goe(10)))
                        .fetchOne();
        // then
        assertEquals("member1", findMember.getUsername());
        assertEquals(10, findMember.getAge());
    }

    @Test
    void resultFetch() throws Exception {

        List<Member> fetch =
                query.selectFrom(member).fetch();

        Member member1 = query.selectFrom(member).fetchOne();

    }

    @Test
    void sortJPQL() throws Exception {
        // given
        List<Member> resultList = em.createQuery("select m from Member m order by m.age desc, m.username asc nulls last ", Member.class).getResultList();
        System.out.println("resultList = " + resultList);

        List<Member> fetch = query.selectFrom(member).orderBy(member.age.asc().nullsFirst()).fetch();
        // when

        // then
    }

    @Test
    void paging() throws Exception {
        // given
        List<Member> fetch = query.selectFrom(member)
                .where(member.age.ne(0))
                .orderBy(member.age.desc())
                .offset(0).limit(2)
                .fetch();
        System.out.println("fetch = " + fetch);
        // when

        // then
    }

    @Test
    void aggredgation() throws Exception {
        Tuple tuple = query.select(
                        member.age.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetchOne();


        System.out.println("tuple = " + tuple);
        System.out.println("tuple = " + tuple.get(member.age.count()));
        // 보통 DTO로 뽑아옴
    }

    @Test
    void group() throws Exception {
        // given
        List<Tuple> tuples = query
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .having(team.name.eq("teamA"))
                .fetch();
        // when
        System.out.println("fetch = " + tuples);
        // then
    }

    @Test
    void join() throws Exception {
        // given
        query
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
        // when

        // then
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void fetchJoin() throws Exception {
        em.flush();
        em.clear();
        // given
        Member findMember =
                query
                        .select(member)
                        .from(member)
                        .join(member.team, team).fetchJoin()
                        .where(member.username.eq("member1"))
                        .fetchOne();

        // when
        boolean initialized = Hibernate.isInitialized(findMember.getTeam());
        System.out.println("initialized = " + initialized);
//        PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();
//        boolean initialized = persistenceUnitUtil.isLoaded(findMember.getTeam());
//        Assertions.assertThat(initialized).isTrue();
//        Assertions.assertThat(Hibernate.isInitialized(findMember.getTeam())).isTrue();


        // then
    }

    @Test
    void subQuery() throws Exception {
        // given
        List<Member> fetch = query
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(member.age.max())
                                .from(member)
                )).fetch();
        System.out.println("fetch = " + fetch);
        // when

        // then
    }

    @Test
    void subQueryIn() throws Exception {

        QMember memberSub = new QMember("memberSub");
        // given
        List<Member> fetch = query
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.goe(20))
                )).fetch();

        System.out.println("fetch = " + fetch);

        // 현재 jpa가 공식적으로 지원하는 서브쿼리는 where나 having같은 조건절에서나 가능
        // select절은 hibernate구현체가 지원하는 내용
        // from절은 지금현재 5버전에선 불가능
        // 그럼 어떻게 -> ? join으로 풀거나 쿼리를 나누거나 애플리케이션단에서 해결
    }

    @Test
    void basicCase() throws Exception {
        // given
        List<Tuple> fetch = query.select(member.username, member.age
                        .when(10).then("열살")
                        .when(20).then("이십살")
                        .otherwise("몰루"), team.name)
                .from(member)
                .join(member.team, team)
                .fetch();

        System.out.println("fetch = " + fetch.get(0).get(team.name));

        // when

        // then
    }

    @Test
    void complexCase() throws Exception {
        // given
    query.select(new CaseBuilder()
                    .when(member.age.between(0, 20)).then("0~20살")
                    .otherwise("dsd///////"))
                .from(member)
                .fetch();
        // when

        // then
    }

    @Test
    void constant() throws Exception {
        // given 
        query.select(member.username, Expressions.constant("A")).from(member).fetch();
        // when

        // then
    }

    @Test
    void concat() throws Exception {
        // given
        List<String> fetch =
                query.select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();
        // when

        // then
    }

}
