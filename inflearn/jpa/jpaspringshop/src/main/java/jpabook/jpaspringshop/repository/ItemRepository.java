package jpabook.jpaspringshop.repository;

import jpabook.jpaspringshop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    public Item findOne(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
