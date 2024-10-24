package com.tontine.app.repository;

import com.tontine.app.domain.ChiTietHui;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ChiTietHui entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChiTietHuiRepository extends JpaRepository<ChiTietHui, Long> {
    List<ChiTietHui> findChiTietHuisByKyGreaterThanAndHuiId(Integer ky, Long id);
    List<ChiTietHui> findAllByNgayKhui( LocalDate ngayKhui);
}
