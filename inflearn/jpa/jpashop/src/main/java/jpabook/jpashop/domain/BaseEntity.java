package jpabook.jpashop.domain;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
// 슈퍼-서브 타입이 아닌 엔티티들의 공통컬럼을 적용할때 사용
public abstract class BaseEntity {

    private LocalDateTime createBy;
    private LocalDateTime updateBy;
    private String createUser;
    private String updateUser;
}
