package jpabook.jpaspringshop.api;

import jpabook.jpaspringshop.domain.Address;
import jpabook.jpaspringshop.domain.Order;
import jpabook.jpaspringshop.domain.OrderSearch;
import jpabook.jpaspringshop.domain.OrderStatus;
import jpabook.jpaspringshop.repository.order.simplequery.OrderSimpleQueryDTO;
import jpabook.jpaspringshop.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    /*
    쿼리 방식 선택 권장 순서
    1. 우선 엔티티를 DTO로 변환하는 방법 선택 (V3)
    2. 필요하면 fecth join으로 성능을 최적화 -> 대부분의 성능 이슈는 여기서 해결(V2)
    3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.(V4)
    4. 최후의 방법은 JPA가 제공하는 네이티브SQL이나 스프링JDBC Template를 사용하여 SQL직접 사용
    */

    private final OrderService orderService;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // lazy 로 인한 프록시 엔티티들 강제 초기화
            order.getDelivery().getAddress();
        }
        return orders;
        // 결과적으로 엔티티를 그대로 반환하는건 절대금지
        // 양방향관계 일경우 json으로 데이터 변환시 무한루프 발생 @JsonIgnore사용
    }
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDTO> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }
    // dto로 변환해서 반환하는건 맞다 하지만 dto로 변환하는 과정에서
    // 쿼리가 총 오더조회 1 + 멤버조회 N + 배송조회 N 이를 N + 1 문제라 하는데
    // 오더가 총 2개이므로 1 + 2 + 2 2건의 오더를 조회하는데 쿼리가 5개가 나가고 있다...
    // 물론 멤버나 배송이 같은 엔티티의 경우 (영속성컨텍스트의 1차캐시에서 조회하므로 쿼리생략됨)

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDTO> ordersV3() {
        List<Order> orders = orderService.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }
    // fetch join은 기본적으로 jpa는 lazy로 조회하는데
    // lazy를 사용한 경우 연관관계의 객체를 조회할때마다 쿼리가 나가므로
    // 연관관계가 필요한 데이터를 조회할경우 fetch join사용하여 해당 연관관게만 eager로
    // 처음조회할떄 join하여 한방에 조회한다.

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> ordersV4() {
        return orderService.findOrderDTOs();
    }
    // v3와 v4는 select절에 갯수에서 사소한차이발생 v4는 원하는 칼러만 가져옴
    // 단 repository는 entity를 조회해야지 dto를 조회하면 특정 api에 존속 되있는 메소드가 있으면 안좋음
    // 단 이렇게 사용할 경우 repository에 새롭게 querydto전용 repository를 생성
}
