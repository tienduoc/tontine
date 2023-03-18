package com.tontine.app.repository;

import com.tontine.app.domain.ChiTietHui;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChiTietHui entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChiTietHuiRepository extends JpaRepository<ChiTietHui, Long> {}
