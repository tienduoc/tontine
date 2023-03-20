package com.tontine.app.repository;

import com.tontine.app.domain.Hui;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class HuiRepositoryWithBagRelationshipsImpl implements HuiRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Hui> fetchBagRelationships(Optional<Hui> hui) {
        return hui.map(this::fetchHuiviens);
    }

    @Override
    public Page<Hui> fetchBagRelationships(Page<Hui> huis) {
        return new PageImpl<>(fetchBagRelationships(huis.getContent()), huis.getPageable(), huis.getTotalElements());
    }

    @Override
    public List<Hui> fetchBagRelationships(List<Hui> huis) {
        return Optional.of(huis).map(this::fetchHuiviens).orElse(Collections.emptyList());
    }

    Hui fetchHuiviens(Hui result) {
        return entityManager
            .createQuery("select hui from Hui hui left join fetch hui.huiviens where hui is :hui", Hui.class)
            .setParameter("hui", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Hui> fetchHuiviens(List<Hui> huis) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, huis.size()).forEach(index -> order.put(huis.get(index).getId(), index));
        List<Hui> result = entityManager
            .createQuery("select distinct hui from Hui hui left join fetch hui.huiviens where hui in :huis", Hui.class)
            .setParameter("huis", huis)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
