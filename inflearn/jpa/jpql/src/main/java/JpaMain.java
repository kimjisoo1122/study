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
