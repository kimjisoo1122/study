package jpabook.jpaspringshop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpaspringshop.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static jpabook.jpaspringshop.domain.QMember.*;
import static jpabook.jpaspringshop.domain.QOrder.*;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        return query.select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()), nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();


//        String jpql = "select o from Order o join o.member m";
//        boolean isFirstCondition = true;
//
//        if (orderSearch.getOrderStatus() != null) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " o.status =:status";
//        }
//
//        if (StringUtils.hasText(orderSearch.getMemberName())) {
//            if (isFirstCondition) {
//                jpql += " where";
//                isFirstCondition = false;
//            } else {
//                jpql += " and";
//            }
//            jpql += " m.name like :name";
//        }
//        return em.createQuery(jpql, Order.class)
//                .getResultList();
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return order.status.eq(OrderStatus.ORDER);
    }

    private BooleanExpression nameLike(String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }
        return member.name.eq(name);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                        "select o from Order o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery("" +
                        "select distinct o " +
                        "from Order o " +
                        "join fetch o.delivery d " +
                        "join fetch o.member m " +
                        "join fetch o.orderItems oi " +
                        "join fetch oi.item i", Order.class)
                .getResultList();
        // OneToMany의 컬렉션을 join으로 가져올시 데이터가 뻥튀기 대는데
        // pk가 같은 엔티티는 distinct로 중복을 거를수있다.
        // 기본적으로 쿼리에 distinct를 넣고 추가로 애플리케이션 로직에서 중복을 걸러줌
        // 단!!! 페이징처리불가능
        // 결국 페이징처리를하려면 sql쿼리로 limit offset이 나가야하는데
        // 우리가 원하는 order의 데이터건들은 애플리케이션단에서 정리가 된 내용들임.
        // 그래서 페이징처리를 in memory단에서 진행하게됨 -> 바로 서버장애
        // 그니까 되긴되는데 서버 메모리에서 진행해서 sql쿼리가 수천건이면 바로 에러터지는거임
        // 컬렉션 페치 조인은 단 1개만 사용
        // 해결방법은 many로 설정된 관계는 fetch join을 백날해봐도 row가 안늘어남
        // fetch join할건 하고 컬렉션 one관계는 배치사이즈를 설정하여 lazy조회
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                        "select o from Order o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
