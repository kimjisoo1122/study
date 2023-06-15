package jpabook.jpashop.domain;

import javax.persistence.Embeddable;
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 기본 생성자 필수
    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
    // 임베디드 타입은 수정못하게 설계하는게 좋다 -> 불변객체로 설계

}
