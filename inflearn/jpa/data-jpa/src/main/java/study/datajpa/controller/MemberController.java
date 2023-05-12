package study.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members/{id}")
    public String findMemberById(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @PostConstruct
    public void postConstruct() {
        Member test1 = Member.createMember("test1");
        memberRepository.save(test1);
    }


    @GetMapping("/members")
    public Page<MemberDTO> list(
            @PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable
            /*@Qualifier("order") Pageable orderPageable*/) {
        // Spring data Jpa가 page관련된 필드들을 자동 주입해줌
        // page : 현재페이지 기본 0
        // size : 1페이지에 노출될 데이터양 기본 20
        // 글로벌설정 - application.yml data.web
        // sort : username,desc 로 배열로들어옴 asc는 생략
        // http://localhost:8080/members?page=0&sort=id,desc&size=10
        // @Qualifier("order") Pageable orderPageable 은 페이징정보가 여러개일때 접두사로 구분
        // 쿼리스트링으로 order_page=0
        Page<Member> memberPages = memberRepository.findAll(pageable);
        Page<MemberDTO> map = memberPages.map(m -> new MemberDTO(m.getId(), m.getUsername()));
        return map;
    }
}
