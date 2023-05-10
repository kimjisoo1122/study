package jpabook.jpaspringshop.service;

import jpabook.jpaspringshop.domain.Member;
import jpabook.jpaspringshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // jpa는 반드시 트랜잭션안에서 이뤄져야함
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional()
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // db에 유니크제약조건으로 최후방어
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원 입니다");
        }
    }

    /**
     * 회원전체조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원단건조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional(readOnly = false)
    public void update(Long id, String name) {
        Member findMember = this.findOne(id);
        findMember.setName(name);
    }
}
