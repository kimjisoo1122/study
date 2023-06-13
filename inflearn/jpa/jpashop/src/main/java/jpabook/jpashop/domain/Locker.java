package jpabook.jpashop.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "locker_id")
    private Long lockerId;
    private String lockerName;

    @OneToOne(mappedBy = "locker")
    private Member member;
}
