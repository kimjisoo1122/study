package jpabook.jpashop.item;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 기본값은 단일테이블
// 관계형데이터베이스의 슈퍼-서브타입을 RDB로 표현하는것
// 단일테이블 또는 조인테이블을 선택할 것 TABLE PER CLASS는 중복 + 조회쿼리복잡(논외)
// 정석은 조인전략 !! 정규화 잘 돼있고 외래키 참조 무결성제약조건 사용가능
// 단일테이블은 조회성능은 좋으나 불필요한 중복과 많은NULL값들 존재
// 단일테이블을 전략으로 사용시 dtype 칼럼이 생성됨 조인테이블은 선택사항
// 단순하면 단일 테이블 복잡하면 조인전략 ~~
@DiscriminatorColumn(name = "dtype")
public class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long itemId;

    private String name;
    private String price;

    public Long getItemId() {
        return itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;


    }
}
