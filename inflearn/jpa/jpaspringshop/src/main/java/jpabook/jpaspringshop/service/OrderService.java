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
    }

    /**
     * 주문취소
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
