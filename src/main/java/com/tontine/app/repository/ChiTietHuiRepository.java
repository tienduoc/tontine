package com.tontine.app.repository;

import com.tontine.app.domain.ChiTietHui;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChiTietHui entity.
 */
@SuppressWarnings("unused")
@Repository
@Cacheable("com.tontine.app.domain.ChiTietHui")
public interface ChiTietHuiRepository extends JpaRepository<ChiTietHui, Long> {}
