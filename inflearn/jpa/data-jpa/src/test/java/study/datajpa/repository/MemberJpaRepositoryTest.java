package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;
@SpringBootTest
@Transactional
//@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
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
        Assertions.assertThat(findMembers).hasSize(1);

        List<String> userNames = memberJpaRepository.findUserNames();
        Assertions.assertThat(userNames).hasSize(1);

        MemberDTO memberDTO = memberJpaRepository.findMemberDTO(member.getId());
        System.out.println("memberDTO = " + memberDTO.getUsername());

        //when

        //then
    }
}