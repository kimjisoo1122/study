package study.datajpa.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private Long memberId;
    private String username;
    private Long teamId;
    private String teamName;

    public MemberDTO(Long memberId, String username,Long teamId, String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
