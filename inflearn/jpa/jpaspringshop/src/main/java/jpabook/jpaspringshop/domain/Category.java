package jpabook.jpaspringshop.domain;

import jpabook.jpaspringshop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /**
     * 실무에선 다대다 매핑을 사용하지 않는데
     * 다대다에 사용되는 조인테이블에 칼럼을 추가할 수 없어서 추가적인 audit정보를 추가할수가 없다
     * 굳이 써야 한다면 조인테이블을 엔티티로 변경하여 다대일 일대다 관계로 매핑
     */
    @ManyToMany
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
