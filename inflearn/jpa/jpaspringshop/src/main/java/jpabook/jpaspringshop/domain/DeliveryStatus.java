package jpabook.jpaspringshop.domain;

public enum DeliveryStatus {
    READY("배송준비"), COMP("배송완료");

    DeliveryStatus(String value) {
    }
}
