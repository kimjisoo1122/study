package jpabook.jpaspringshop.service;

import jpabook.jpaspringshop.repository.order.simplequery.OrderSimpleQueryDTO;
import jpabook.jpaspringshop.domain.*;
import jpabook.jpaspringshop.domain.item.Item;
import jpabook.jpaspringshop.repository.ItemRepository;
import jpabook.jpaspringshop.repository.MemberRepository;
import jpabook.jpaspringshop.repository.OrderRepository;
import jpabook.jpaspringshop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // jpa는 반드시 트랜잭션안에서 이뤄져야함
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 엔티티 조회
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();

        /**
         * ORM을 사용하게 되면 서비스에서는 별도의 비즈니스 로직이 존재하지 않고
         * 엔티티의 로직들을 호출해서 연결해주는 역할만함 -> 이를 도메인 모델이라고 함
         * 반대로 내가 직접 비즈니스로직을 순서대로 쿼리문을 날리면서 풀어내면
         * 트랜잭션 스크립트 패턴이라고 한다 SQL MAPPER를 사용하는 경우에 해당함
         */

        /**
         * 엔티티 조회 권장 순서
         * 엔티티로 조회 방식으로 우선 접근
         *  - 페치 조인으로 쿼리 수를 최적화
         *  - 컬렉션 최적화 -> 페이징 필요 -> 컬렉션뺴고 페치조인사용후 batchsize를 통해 in절 조회
         *  - 페이징이 필요 없으면 페치조인 사용 후 distinct 적용
         *  대부분의 비즈니스에서 페이징은 거의 대부분 들어가서 페이징처리는 항상 고려해야 함
         *  엔티티 조회 방식으로 해결이 안되면 dto조회 방식 사용
         *  dto로도 안되면 native sql 또는 jdbc template
         */

        /**
         * OpenSessionInView
         * spring.jpa.open-in-view 스프링부트 기본값 true
         * 영속성 컨텍스트 생존 범위를 클라이언트에 제공할때 까지 or 트랜잭션종료시점
         * 영속성 컨텍스트의 생존 범위에서 벗어난 상태에서 프록시를 초기화하면 에러발생
         * 커넥션을 언제까지 물고있냐의 문제
         * 개인적으로는 서비스에서 그냥 끝내는게 맞는거 같은뎅...?
         */
    }

    /**
     * 주문취소
     *
     * @param orderId
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

    public List<Order> findAllWithMemberDelivery() {
        return orderRepository.findAllWithMemberDelivery();
    }

    public List<OrderSimpleQueryDTO> findOrderDTOs() {
        return orderSimpleQueryRepository.findOrderDTOs();
    }
}
