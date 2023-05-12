package study.datajpa.repository;

public class MemberTestDTO {
    private String teamname;
    private String username;

    public MemberTestDTO(String teamname, String username) {
        this.teamname = teamname;
        this.username = username;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }
}
