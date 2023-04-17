package com.hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            List<Member> findMembers = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();
//            findMembers.stream().map(Member::getName).forEach(System.out::println);
        } catch (Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
