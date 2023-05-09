package jpabook.jpaspringshop.api;

import jpabook.jpaspringshop.domain.*;
import jpabook.jpaspringshop.repository.OrderRepository;
import jpabook.jpaspringshop.repository.order.query.OrderQueryDto;
import jpabook.jpaspringshop.repository.order.query.OrderQueryRepository;
import jpabook.jpaspringshop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().forEach(o -> o.getItem().getName());
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        return orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
    }

    @Data
    static class OrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }
    @Data
    static class OrderItemDto {
        private String itemName;//상품 명
        private int orderPrice; //주문 가격
        private int count; //주문 수량
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
    }


    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orders.stream()
                .map(OrderDTO::new)
                .collect(toList());
    }
    // 결론 -> ManyToOne 또는 OneToOne 관계는 fetch join으로 해도 row수가 변함이 없어
    // 페이징처리대상임(해야한다면) 그래서 fetch join으로 가져오는게 좋고
    // 나머지 OneToMany같은 양방향관계의 컬렉션 조회 같은 경우는 fetch join을 사용할 경우
    // 중복 데이터들이 발생하는데 이를 distinct로 막을순 있다
    // 하지만 페이징처리가 불가능함 -> 애플리케이션단에서 페이징처리를 하기에 out of memory대상임
    // 고로 컬렉션 조회같은 경우는 지연로딩을 사용해야 하는데
    // 그냥 사용하면 n + 1 으로 쿼리가 날라가는 문제가 발생
    // 이를 default_batch_fetch_size 옵션을 설정하여 보통 100~1000개 사이정도 지연로딩을 처리하며
    // 쿼리내에서 100건씩 in을 사용하여 한번에 조회함
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findOrderOptimization();
    }
}
