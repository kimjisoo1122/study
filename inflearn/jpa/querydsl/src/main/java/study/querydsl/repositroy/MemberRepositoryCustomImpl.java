package study.querydsl.repositroy;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDTO;
import study.querydsl.dto.QMemberTeamDTO;

import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory query;
    @Override
    public List<MemberTeamDTO> search(MemberSearchCondition searchCondition) {
        return query
                .select(new QMemberTeamDTO(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(userNameEq(searchCondition.getUserName()),
                        (teamNameEq(searchCondition.getTeamName())),
                        (ageGoe(searchCondition.getAgeGoe())),
                        ageLoe(searchCondition.getAgeLoe()))
                // and 나 or 사용시 null 조심 null이면 조건에서 무시하지만
                // 반환값인 booleanexpression에다가 and를 달면 npe발생위험
                .fetch();
    }

    @Override
    public Page<MemberTeamDTO> searchPageSimple(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberTeamDTO> result = query
                .select(new QMemberTeamDTO(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(userNameEq(condition.getUserName()),
                        (teamNameEq(condition.getTeamName())),
                        (ageGoe(condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDTO> content = result.getResults();
        long totalCnt = result.getTotal();
        return new PageImpl<>(content, pageable, totalCnt);
    }

    @Override
    public Page<MemberTeamDTO> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {
        List<MemberTeamDTO> content = query
                .select(new QMemberTeamDTO(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(userNameEq(condition.getUserName()),
                        (teamNameEq(condition.getTeamName())),
                        (ageGoe(condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(member.count())
                .from(member)
                .leftJoin(member.team, team)
                .where(userNameEq(condition.getUserName()),
                        (teamNameEq(condition.getTeamName())),
                        (ageGoe(condition.getAgeGoe())),
                        ageLoe(condition.getAgeLoe()));

        // 카운트쿼리가 필요없는경우 안날림
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression userNameEq(String username) {
        return StringUtils.hasText(username) ? member.username.eq(username) : null;
    }
    private BooleanExpression teamNameEq(String teamName) {
        return StringUtils.hasText(teamName) ? member.team.name.eq(teamName) : null;
    }
    private BooleanExpression ageGoe(Integer age) {
        return age != null ? member.age.goe(age) : null;
    }
    private BooleanExpression ageLoe(Integer age) {
        return age != null ? member.age.loe(age): null;
    }


}
