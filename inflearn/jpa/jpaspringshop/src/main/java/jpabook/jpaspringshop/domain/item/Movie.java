package jpabook.jpaspringshop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") // 기본값은 엔티티명
@Getter @Setter
public class Movie extends Item {
    private String director;
    private String actor;
}
