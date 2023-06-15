package jpabook.jpashop.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue
    private Long id;

    private String city;
    private String street;
    private String zipcode;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    private DeliveryStatus status;
}
