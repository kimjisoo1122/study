package com.hellojpa.inheritance;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Item {

    // 상속해주는 부모클래스는 추상클래스로 선언한다
    // Inheritance의 전략은 joined, 공통적인 칼럼값의 테이블과 각각의 분리 테이블 존재하여 join하여 조회
    // singletable은 모든 칼럼을 하나의 테이블로 설정
    // type per class는 중복된값 포함하여 테이블 여러개 생성

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
