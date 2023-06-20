package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberRepository memberJpaRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() throws Exception {
        //given
        Member member = Member.createMember("김지수");
        Team team = new Team("team1");
        em.persist(team);
        memberJpaRepository.save(member);
        member.changeTeam(team);
        member.setAge(20);


        List<Member> findMembers = memberJpaRepository.findUser("김지수", 10);
        assertThat(findMembers).hasSize(1);

        List<String> userNames = memberJpaRepository.findUserNames();
        assertThat(userNames).hasSize(1);

//        MemberDTO memberDTO = memberJpaRepository.findMemberDTO(member.getId());
//        System.out.println("memberDTO = " + memberDTO.getUsername());

        //when

        //then
    }

    @Test
    void paging() throws Exception {
        // given
        Member member1 = Member.createMember("member1", 10);
        Member member2 = Member.createMember("member2", 10);
        Member member3 = Member.createMember("member3", 10);
        Member member4 = Member.createMember("member4", 10);
        Member member5 = Member.createMember("member5", 10);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        // when
        int age = 10;
        PageRequest pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> page = memberJpaRepository.findByAge(age, pageable);
//        Page<MemberDTO> map = page.map(m -> new MemberDTO());

        List<Member> contents = page.getContent();
        long totalElements = page.getTotalElements();

        // then
        assertThat(totalElements).isEqualTo(5);
        assertThat(contents).hasSize(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();



    }

    @Test
    void teamTest() throws Exception {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member member = Member.createMember("member1", 10, teamA);
        em.persist(member);

        em.flush();
        em.clear();

        // when
//        List<Member> members = memberJpaRepository.findAll();
//        List<MemberTestDTO> result =
//                em.createQuery("select new study.datajpa.repository.MemberTestDTO(t.name, m.username)  from Member m join m.team t", MemberTestDTO.class).getResultList();
//        System.out.println("name = " + result.get(0).getTeamname());

        List<Member> all = memberJpaRepository.findAll();
        System.out.println("all = " + all.get(0).getTeam().getName());

        List<Member> member1 = memberJpaRepository.findByUsername("member1");
        assertThat(member1).hasSize(1);
        // then

    }

    @Test
    void customRepository() throws Exception {
        // given
        List<Member> memberCustom = memberJpaRepository.findMemberCustom();
        // when

        // then
    }

    @Test
    void findPage() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");

        Page<Member> byPage = memberJpaRepository.findByPage(10, pageRequest);
        List<Member> content = byPage.getContent();

        byPage.isFirst();
        byPage.getTotalElements();
        // 반환값을 page로 받고 인자로  pageable을 구현한 pagerequest를 넘겨준다
        // 저절로 페이징처리에 유용한 값들을 받을 수 있다
        // page로 반환하면 문제는 count query가 알아서 나가는데
        // 복잡한 조회문인경우 count쿼리도 join해서 나가는데 보통 카운트쿼리는
        // join안해도 되는 경우가 많아 이런경우 메소드에 @query에 카운트쿼리를 따로 명시해준다.
    }

    @Test
    void queryHint() throws Exception {
        // jpa 쿼리 힌트는 sql힌트가 아님 jpq 하이버네이트 임

        Member member = Member.createMember("member2");
        em.persist(member);
        em.flush();
        em.clear();

        Member findMember = memberJpaRepository.findReadOnlyByUserName("member2");
        findMember.setUsername("test2");

        em.flush();
        // jpa는 readonly를 사용하면 자체적으로 성능최적화를 하여
        // 1차캐시에 저장할때 스냅샷을 안만들어서 더티체크를 안한다
        // 고로 위의 변경감지가 안됨
        // readonly로 성능 효율 ? 진짜 트래픽 많은것 외에는 의미없음
        // db의 복잡한 쿼리 조회가 성능에 가장 큰 이슈
    }

    @Test
    void lock() throws Exception {
        List<Member> lock = memberJpaRepository.findLock();
        // 조회할때 select for update가 붙음
        // 해당 락을 설정하면 조회하는 트랜잭션안에서 다른 트랙잭션이 변경을 못하게 막는다
    }
}