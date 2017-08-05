package com.kachkovsky.busyhistory.db.hibernate;

import com.kachkovsky.busyhistory.data.BusyItem;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BusyItemRepository {

    private EntityManager em;

    public BusyItemRepository(EntityManager em) {
        this.em = em;
    }

    public List<BusyItem> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BusyItem> q = cb.createQuery(BusyItem.class);
        Root<BusyItem> root = q.from(BusyItem.class);
        CriteriaQuery<BusyItem> select = q.select(root);
        return em.createQuery(select).getResultList();
    }
}
