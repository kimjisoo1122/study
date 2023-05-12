package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
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
        Page<MemberDTO> map = page.map(m -> new MemberDTO());

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
}