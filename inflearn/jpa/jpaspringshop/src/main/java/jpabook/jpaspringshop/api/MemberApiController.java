package jpabook.jpaspringshop.api;

import jpabook.jpaspringshop.domain.Member;
import jpabook.jpaspringshop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> findMembersV1() {
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result findMembersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(Member::getName)
                .map(MemberDTO::new)
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(
            @RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class Result<T extends List<?>> {
        private int count;
        private T data;

        public Result(T data) {
            this.data = data;
            if (data != null) {
                count = data.size();
            }
        }
    }
    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(
            @RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // put은 멱등하다 멱등하다란? 요청에 의해 항상 결과가 같다
    // put은 전부수정 그래서 전체데이터 필요 patch는 부분수정
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member member = memberService.findOne(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

}
