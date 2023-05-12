package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private Long memberId;
    private String username;

    public MemberDTO(Long memberId, String username) {
        this.memberId = memberId;
        this.username = username;
    }
}
