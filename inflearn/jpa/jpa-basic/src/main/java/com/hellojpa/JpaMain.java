package com.hellojpa;

import com.hellojpa.inheritance.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        // 개발자가 설정한 persistence unit(xml)을 바탕으로 엔티티매니저팩토리 생성
        // 엔티티매니저팩토리로부터 엔티티매니저생성 -> 엔티티트랜잭션가져오기
        // JPA의 모든 변경은 트랜젝션 안에서만 가능하다
        // 엔티티매니저는 객체를 저장해주는 컬렉션같은 느낌 업데이트의 경우 찾아온 엔티티에 값만 변경하면됨
        // LIST에서 객체를 가져온뒤 수정하면 LIST값이 바뀌듯이

        tx.begin();

        try {

            // 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);
            // Team객체를 1차캐시에 저장하고 sql 쓰기 지연 저장소에 sql문 저장

            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team);
            em.persist(member);
            // Member 객체를 1차캐시에 저장하고 sql 쓰기 지연 저장소에 sql문 저장

            Member findMember = em.find(Member.class, member.getId());
            // 1차캐시에 저장된 member객체를 pk값으로 가져온다
            // 이때 db에 저장된 상태가아님 영속성컨텍스트에서 가져오기때문에 sql문을 날리지 않음

            em.flush();
            // 영속성 컨텍스트에 변경사항이 발생한 경우 데이터베이스에 반영한다.
            // sql 쓰기 지연 저장소에 저장된 sql문이 비워진다
            em.clear();
            // 영속성 컨텍스트에서 관리하는 모든 엔티티들을 초기화 한다
            // 1차 캐시 초기화

            List<Member> members = findMember.getTeam().getMembers();
            int size = members.size();
            System.out.println("members size = " + size);
            // 저장된 team객체의 mebers list를 가져왔으나 아직 db에 반영이 안되었기에
            // 말그대로 그냥 빈 list객체만 있는상태 그래서 size도 0으로 나온다

            // em.flush를 하면 1차 캐시에서 저장되어 있는 엔티티들의 변경사항을
            // sql 쓰기 지연 저장소에 저장되어있는 sql문을 통해 데이터베이스에 반영합니다
            // findMember.getTeam().getMembers()로 찾은 데이터는 1차 캐시에 맨처음 저장한 상태그대로
            // jpa는 조회할때 1차적으로 캐시를 뒤지고 없는 경우에 데이터베이스를 조회함
            // em.clear를 해야 모든 영속성객체의 초기화가 진행되어 관계를 맺고있는 필드의 초기화가 이때 진행됨
            // 1차 캐시도 이때 비워지게 된다 이 후 조회할떄는 새로 조회 해오기에 size가 이때 1이 나옴
            // 결론은 양방향 관계의 경우 양쪽에 값을 세팅할 것.
            // team.getMembers().add(member) 또는 setTeam 메소드에서 연관관계 대상의 객체 값도 수정
            //     public void setTeam(Team team) {
            //     -> 양방향 편의 메소드의 경우 setter의 의미보단 changeTeam같은 명명 권장
            //        this.team = team;
            //        team.getMembers().add(this);  --> 양방향 데이터 설정
            //    }
            // 주의 해야할 내용 서로 객체를 참조하고 있기에 각자의 toString을 무한호출할 수 있음
            // lombok의 @Data 사용 주의  Entity는 반환값으로 부적절 (컨트롤러에서)

            // 객체 설계는 단방향으로 연관관계 설정 추가기능 제공시에 양방향 매핑 단방향으로도 충분할수도?
            // 엔티티는 수정해도 테이블은 변경되지 않음 양방향 : mappedBy
            tx.commit();

        } catch (Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
