package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.item.Movie;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member1 = new Member();
            em.persist(member1);

            em.find(Member.class, member1.getId()); // 데이터베이스를 통해 진짜 엔티티 조회
            em.getReference(Member.class, member1.getId()); // 프록시 객체 조회
            // 프록시객체 내부에 실제 대상 객체가 존재하는데
            // 초기화전에는 빈값이다 클라이언트가 프록시객체를 통해 작업을 진행하면
            // 그때 프록시는 영속성컨텍스트를 통해 db조회하여 실제 엔티티를 생성하여 프록시객체에 주입한다
            // 프록시는 한 번만 초기화 한다.
            // 프록시 객체는 원본 엔티티를 상속받아서 타입체크시 유의해야한다 instance of를 사용해야한다
            // == 비교에 주의 하여야 함 jpa에서 값비교시에 언제 프록시객체가들어올지 모르니 == 를 자제해야한다
            // em.getReference를 통해 프록시객체를 조회하는데 이때 영속성컨텍스트(1차캐시)에
            // 실제 엔티티가 존재하면 프록시가 아닌 실제 엔티티를 반환한다.

            // 중요점은 jpa에서 하나의 트랜잭션에서 조회하는 엔티티는 항상 동일성을 보장해야한다.
            // 프록시로 조회했으면 이후 em.find를해도 프록시를 조회함 동일성을 유지해야함  a == b
            // 엔티티를 조회할때 연관관계의 엔티티들은 lazy 로딩 인경우에 프록시로 조회함
            // 해당 프록시를 사용할떄 비로소 db에서 조회하요 실제 엔티티를 프록시내부에 채워줌
            // 각종유틸
            emf.getPersistenceUnitUtil().isLoaded(Member.class);
            Hibernate.initialize(Member.class); // 명시적인 강제초기화 // jpa표준은 아님 하이버네이트 기술
            em.clear();

            Member member = new Member();
            member.setName("KIMJISOO");
            member.setAddress(new Address("SEOUL", "GRUO", "08189"));
            em.persist(member);

            Item item = new Item();
            item.setPrice(3000);
            item.setName("ItemA");
            item.setStockQuantity(5);
            em.persist(item);

            Order order = new Order();
            order.setMember(member);
            order.setStatus(OrderStatus.ORDER);
            order.setOrderDate(LocalDateTime.now());
            em.persist(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setCount(2);
            orderItem.setOrderPrice(5000);
            orderItem.setItem(item);
            em.persist(orderItem);




            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
