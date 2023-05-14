package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDTO;
import study.querydsl.dto.QMemberDTO;
import study.querydsl.dto.UserDTO;
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

    @Test
    void findDtoByJpql() throws Exception {
        // given
        List resultList = em.createQuery("select new study.querydsl.dto.MemberDTO(m.username, m.age) " +
                        "from Member m")
                .getResultList();
        // when

        // then
    }

    @Test
    void findDtoBySetter() throws Exception {
        // given
        List<MemberDTO> fetch =
                query
                        .select(Projections.bean(MemberDTO.class, // getter setter필요
                                member.username,
                                member.age))
                        .from(member)
                        .fetch();
        System.out.println("fetch = " + fetch);
        // when

        // then
    }

    @Test
    void findDtoByField() throws Exception {
        // given
        List<MemberDTO> fetch =
                query
                        .select(Projections.fields(MemberDTO.class, // getter setter필요
                                member.username,
                                member.age))
                        .from(member)
                        .fetch();
        System.out.println("fetch = " + fetch);
        // when

        // then
    }

    @Test
    void findUserDTO() throws Exception {
        // given
        List<UserDTO> name = query
                .select(Projections.fields(UserDTO.class, // getter setter필요
                        member.username.as("name"),
                        member.age))
                .from(member)
                .fetch();

        System.out.println("name = " + name);
        // when

        // then
    }

    @Test
    void findtDtoByQueryProjection() throws Exception {
        // given
        List<MemberDTO> fetch = query.select(new QMemberDTO(member.username, member.age))
                .from(member)
                .fetch();
        System.out.println("fetch = " + fetch);
        // 대신 dto는 컨트롤러나 서비스레이어에서도 사용하는대ㅔ
        // @QueryProjection 어노테이션이 querydsl에 의존적이다
    }

    @Test
    void dynamicQuery_BolleanBuilder() throws Exception {
        // given
        String username = "member1";
        Integer age = 10;
        BooleanBuilder builder = searchCond(username, age);
        // boolean builder는 한눈에파악하기 힘듬

//        List<Member> fetch = query.selectFrom(member)
//                .where(builder)
//                .fetch();
//        List<Member> fetch = query.selectFrom(member)
//                .where(usernameEq(username), ageEq(age))
//                .fetch();
        List<Member> fetch = query.selectFrom(member)
                .where(allEq(username, age))
                .fetch();
        // where조건이 null이면 skip함

        System.out.println("fetch = " + fetch);
        // then
    }

    private BooleanExpression usernameEq(String username) {
        return username != null ? member.username.eq(username) : null;
    }

    private BooleanExpression ageEq(Integer age) {
        return age != null ? member.age.eq(age) : null;
    }

    private BooleanExpression allEq(String username, Integer age) {
        return usernameEq(username).and(ageEq(age));
    }

    private BooleanBuilder searchCond(String username, Integer age) {
        BooleanBuilder builder = new BooleanBuilder();
        if (username != null) {
            builder.and(member.username.eq(username));
        }
        if (age != null) {
            builder.and(member.age.goe(age));
        }
        return builder;
    }

    @Test
    void bulkUpdate() throws Exception {
        // jpa에서는 변경사항을 사용하면 merge 사용하게 되는데
        // merge의 경우에는 디비를 업데이트하고 새로운 엔티티를 영속성컨텐스트로 교체하는 메커니즘
        // 매번 새로운 엔티티를 반환하는데 기존의 엔티티는 준영속성상태로 전환된다. -> 멱등하지 않음
        // merge는 http메소드로치면 put과같음 싹다 교체하는 메커니즘 petch는 부분만수정

        // 그래서 변경감지를 이용하게 되는데 문제는 단건밖에 안됨.
        // 이래서 한번에 여러개의 데이터를 변경할때는 bulk 업데이트를 해줘야함

        long count = query.update(member).set(member.username, "비회원").where(member.age.lt(28)).execute();

        System.out.println("count = " + count);

        // 그런데! 이러한 update는 sql쓰기지연저장소에 저장되있다가 commit시점에 db에 반영되고
        // 영속성컨텐스트의 1차캐시의 데이터는 그대로임
        // 그래서 해당 엔티티를 조회시 변경전 엔티티가 조회됨

        em.flush(); // flush로 sql쓰기지연저장소의 저장된 sql문을 db에 모두반영한다
        em.clear(); // db에 반영했지만 영속성컨텐스트의 1차캐시는 그대로이기에 clear로 1차캐시를 비워준다
        // 이후 다시 조회시 1차캐시에 엔티티가 없기에 db를 조회한다.
        List<Member> fetch = query.selectFrom(member).fetch();
        System.out.println("fetch = " + fetch);
        // 업데이트 이후엔 항상 flush clear해줘야함
        // 스프링 데이터 jpq의 @query 에 @modifing 을 사용하면 위의 플러쉬 클리어를 저절로해줌
        // 참고로 모든 jpql실행전에 flush는 자동으로 진행됨
    }

    @Test
    void sqlFunction() throws Exception {
        // given
        List<String> fetch = query.select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member).fetch();
        // when
        System.out.println("fetch = " + fetch);

        // then
    }

    @Test
    void lowerFunction() throws Exception {
        // given
        List<String> fetch = query.select(member.username)
                .from(member)
//                .where(member.username.eq(Expressions.stringTemplate(
//                        "function('lower', {0})", member.username))).fetch();
                .where(member.username.eq(member.username.lower())).fetch();
        // 대부분의 ansi표준 sql function들은 자체내장되있음
        // h2의경우 h2dialect에 등록되있는지 확인 (dialect는 해당 db의 방언? )

        // when
        System.out.println("fetch = " + fetch);
        // then
    }
}
