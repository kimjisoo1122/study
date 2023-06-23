package study.querydsl.repositroy;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDTO;
import study.querydsl.dto.QMemberTeamDTO;

import javax.persistence.EntityManager;
import java.util.List;

import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@Repository
public class MemberRepositortCustom {

    private final JPAQueryFactory query;

    public MemberRepositortCustom(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    public Page<MemberTeamDTO> search(MemberSearchCondition searchCondition, Pageable pageable) {

        List<MemberTeamDTO> contents =
                query
                        .select(new QMemberTeamDTO(
                                member.id.as("memberId"),
                                member.username,
                                member.age,
                                member.team.id.as("teamId"),
                                member.team.name.as("teatName")
                        ))
                        .from(member)
                        .leftJoin(member.team, team)
                        .where()
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        JPAQuery<Long> countQuery = query.select(member.count())
                .from(member)
                .where();

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    /*
    스프링 data jpa의 sort기능을 querydsl에 적용하기가 어려움
    조건을 직접받아서 order로 직접적어주는게 좋음
     */
}
