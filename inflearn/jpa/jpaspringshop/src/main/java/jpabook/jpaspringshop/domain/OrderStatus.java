package jpabook.jpaspringshop.domain;

public enum OrderStatus {
    ORDER("주문"), CANCEL("취소");

    OrderStatus(String value) {
    }
}
