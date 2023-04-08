package com.tontine.app.repository;

import com.tontine.app.domain.ChiTietHui;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChiTietHui entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChiTietHuiRepository extends JpaRepository<ChiTietHui, Long> {
    //    String CHI_TIET_HUI_BY_ID = "chiTietHuiById";
    //
    //    @Override
    //    @Cacheable(cacheNames = CHI_TIET_HUI_BY_ID)
    //    Optional<ChiTietHui> findById(Long aLong);
}
