package com.tontine.app.repository;

import com.tontine.app.domain.HuiVien;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HuiVien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuiVienRepository extends JpaRepository<HuiVien, Long> {
    @Query("SELECT DISTINCT hv FROM HuiVien hv LEFT JOIN FETCH hv.chiTietHuis cth LEFT JOIN FETCH cth.hui h LEFT JOIN FETCH h.chiTietHuis WHERE hv.id = :id")
    Optional<HuiVien> findByIdWithChiTietHuis(@Param("id") Long id);

    @Query("SELECT DISTINCT hv FROM HuiVien hv LEFT JOIN FETCH hv.chiTietHuis")
    List<HuiVien> findAllWithChiTietHuis();

    @Query("SELECT DISTINCT hv FROM HuiVien hv LEFT JOIN FETCH hv.chiTietHuis WHERE hv.hoTen = :fullName")
    Optional<HuiVien> findHuiViensByHoTenWithChiTietHuis(@Param("fullName") String fullName);
}
