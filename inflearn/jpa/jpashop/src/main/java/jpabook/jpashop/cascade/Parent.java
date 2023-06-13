package jpabook.jpashop.cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    // 글 - 댓글처럼 자식들을 특정 엔티티가 개인 소유하는 경우 all~pesist~remove 사용가능
    // 부모의 컬렉션에 자식 엔티티를 add하고 영속시키면 자식까지 insert된다
    // orphanRemoval 은 연관 관계가 끊길경우 삭제 (컬렉션에서 빠질 경우 삭제)
    // 두 옵션을 모두 활성화 하면 부모가 자식 엔티티의 생명주기 관리
    private List<Child> children = new ArrayList<>();

    public void addChild(Child child) {
        children.add(child);
        child.setParent(this);
    }

    public static void main(String[] args) {

    }
}
