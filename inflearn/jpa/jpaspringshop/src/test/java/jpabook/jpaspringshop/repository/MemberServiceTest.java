package jpabook.jpaspringshop.repository;

import jpabook.jpaspringshop.domain.Member;
import jpabook.jpaspringshop.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest()
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
//        assertEquals(memberRepository.findOne(savedId), member);
    }

    @Test()
    void 중복_회원_예약() throws Exception {
        //given
        Member memberA = new Member();
        memberA.setName("kim");
        Member memberB = new Member();
        memberB.setName("kim");

        //when
        memberService.join(memberA);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(memberB));

        fail("Dsdsds");
    }
}