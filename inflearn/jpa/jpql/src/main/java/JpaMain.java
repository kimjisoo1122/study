import jpql.Member;
import jpql.MemberDTO;
import jpql.Team;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("kimjisoo");
            member.setAge(20);
            member.setTeam(team);
            em.persist(member);

//            List<Member> resultList = em.createQuery("select m from Member m where m.age > 10", Member.class)
//                    .getResultList();
            Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", "kimjisoo")
                    .getSingleResult();
            // single은 무조건 값이 하나여야함 0개나 2개이상은 반드시 exception발생

            System.out.println("singleResult = " + singleResult);

            List<Team> resultList = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();
            // select m.team from Member m 으로도 같은 결과지만 묵시적조인보다는 명시적조인 사용!!
            Team team1 = resultList.get(0);
            System.out.println("team1 = " + team1);

            Query query = em.createQuery("select m.username, m.age from Member m");
            Object[] singleResult1 = (Object[]) query.getResultList().get(0);
            System.out.println("hints = " + singleResult1[0]);
            System.out.println("hints = " + singleResult1[1]);

            List<MemberDTO> resultList1 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
                    .getResultList();
            System.out.println("resultList1 = " + resultList1.get(0));

            em.createQuery("select m FROM Member m join m.team t");
            // 프로젝션이란 jpql로 원하는 쿼리 결과를 엔티티가아닌 다양한 형태로 조회하는것
            // 스칼라 프로젝션 - 특정 컬럼만 지정하여 List<String>형태로 조회 값이 하나여야 한다
            // 튜플 프로젝션 칼럼여러개를 List<Object[]> 컬렉션의 배열형태로 조회된다
            // DTO 프로젝션 dto를 패키지 명을 포함한 전체 클래스명입력하여 생성자형태로만 생성이 가능하다
            // jpa가 내부적으로 리플렉션으로 생성자를 통해 객체를 생성하기 때문


            em.createQuery("select m from Member m left join Team t on m.username = t.name");
            // join on으로 필터링 추가 가능

            // jpa 표준스펙에서는 where와 having에서만 서브쿼리 사용가능
            // hibernate는 select도 가능
            // from절은 jpql에서 불가능 -> 대부분 join으로 푼다 sql에선 인라인뷰로 불림
            // join으로 힘들면 애플리케이션 쿼리를 2번 날리거나

            // select m.team from Member m
            // 해당 jpql은 묵시적 join이 발생함 이는 join인지 실행전에 인지하기 어려우니
            // 명시적으로 select m.team from Member m join Team t 적어줘야 함
            // 로딩이 eager 즉시로딩일 경우 select m from Member m은
            // join해서 가져오는것이 아닌 member엔티티를 조회후 즉시로딩인 필드를 다시 조회함

            // select절의 상태 필드는 경로 탐색의 끝
            // 단일 값 연관 경로 (m.team) 은 묵시적 조인 발생, 계속 탐색 가능 m.team.name
            // 컬렉션값 연관 경로 OneToMany(mapped by team) 묵시적 조인 발생 탐색 끝남
            // 반드시 명시적 조인만 사용할 것
            // 묵시적 조인은 내부조인만 해당

            // select t.members from Team -> 성공
            // select t.members.username from Team -> 실패
            // select musername from Team t join t.members m -> 성공 컬렉션을 조인하여 별칭사용

            // 페치 조인 -> jpql에서 성능에서 가장중요
            // 위의 조인이랑 페치 조인이랑 같은거 같은데??
            // 페치 조인은 엔티티에서 조인할때 사용함
            // 엔티티는 지연로딩이라 엔티티로 조회하면 연관관계 필드는 프록시로 조회됨
            // 프록시를 차후 사용하게 되면 그때 쿼리가 날라가서 db에서 조회후 프록시에 진짜 객체 주입
            // 이때 join fetch로 명시적을

            // select t from Team t join fetch t.members
            // OneToMany에서 이런식으로 조회하면 teamA에 속한 멤버가 2명일 경우
            // 데이터베이스에서는 당연히 TEAM A 멤버1 TEAM A 멤버 2 이렇게 2줄이 나타남
            // 근대 컬렉션으로는 하나에 담기기에 하나만 내보는게 아님
            // 그건 개발자의 선택이라 TEAM 엔티티 2개에 컬렉션모두 동일하게 2개 반환됨
            // 이를 제거하려면 SQL에선 제거안됨 두개의 로우가 달라서
            // JPQL에서 DISTINCT를 하면 식별자가 같은 엔티티는 중복제거 됨
            // 페치조인은 별칭을 사용하면 안된다!
            // 별칭을 사용해 조작을 하면 에러가 발생할수있다 FETCH는 다 조회한다는 생각으로 접근
            // 컬렉션 페치조인은 기본적으로 데이터가 중복이 발생할 수 있다
            // 페이징 처리를 할경우 로그를 남기고 메모리에서 페이징하다 에러 발생위험
            // 페치 조인 정리
            // 모든 것을 페치 조인으로 해결할 수는 없음
            // 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
            // 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른
            // 결과를 내야 하면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 모아서
            // DTO로 반환하는 것이 효과적
            // FETCH조인은 엔티티로 반환할때만 사용하는것


            // 다형성 쿼리
            // 부모 자식 간의 관계에서 사용가능
            // select i from Item i where type(i) in (Book, Movie)
            // -> select i from i where i.dtype in ('B','M')
            // -> treat을 사용하여 다운캐스팅 가능 이는 싱글테이블전략에서 유효

            // 벌크연산은 영속성컨텍스트를 무시하고 데이터베이스에 직접 쿼리를 날린다.
            // 영속성컨텍스트 캐시에 존재하는 엔티티들은 이전 자료일 수 있으니
            // 벌크연산 진행 후 반드시 1차캐시를 비워줘야 다시 반영된 데이터를 조회해온다.


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
