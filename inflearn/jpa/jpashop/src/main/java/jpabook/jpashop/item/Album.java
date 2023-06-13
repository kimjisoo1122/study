package jpabook.jpashop.item;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 기본값은 엔티티명
public class Album extends Item {

    private String artist;
}
