package com.tontine.app.repository;

import com.tontine.app.domain.ChiTietHui;
import com.tontine.app.domain.Hui;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHuiRepository extends JpaRepository<ChiTietHui, Long> {
    @Query("SELECT cth FROM ChiTietHui cth LEFT JOIN FETCH cth.hui LEFT JOIN FETCH cth.huiVien WHERE cth.id = :id")
    Optional<ChiTietHui> findByIdWithHuiAndHuiVien(@Param("id") Long id);

    @Query("SELECT DISTINCT cth FROM ChiTietHui cth LEFT JOIN FETCH cth.hui LEFT JOIN FETCH cth.huiVien")
    List<ChiTietHui> findAllWithHuiAndHuiVien();

    @Query("SELECT DISTINCT cth FROM ChiTietHui cth LEFT JOIN FETCH cth.hui LEFT JOIN FETCH cth.huiVien WHERE cth.hui.id = :huiId AND cth.huiVien.id = :huiVienId")
    List<ChiTietHui> findAllByHuiAndHuiVien(@Param("huiId") Long huiId, @Param("huiVienId") Long huiVienId);

    @Query("SELECT DISTINCT cth FROM ChiTietHui cth LEFT JOIN FETCH cth.hui LEFT JOIN FETCH cth.huiVien WHERE cth.ky > :ky AND cth.hui.id = :huiId")
    List<ChiTietHui> findChiTietHuisByKyGreaterThanAndHuiIdWithHuiAndHuiVien(@Param("ky") Integer ky, @Param("huiId") Long huiId);

    @Query("SELECT DISTINCT cth FROM ChiTietHui cth LEFT JOIN FETCH cth.hui LEFT JOIN FETCH cth.huiVien WHERE cth.ngayKhui = :ngayKhui")
    List<ChiTietHui> findAllByNgayKhuiWithHuiAndHuiVien(@Param("ngayKhui") LocalDate ngayKhui);

    @Query("SELECT DISTINCT cth FROM ChiTietHui cth WHERE cth.ngayKhui = :ngayKhui")
    List<ChiTietHui> findAllByNgayKhui(@Param("ngayKhui") LocalDate ngayKhui);

    List<ChiTietHui> hui(Hui hui);
}
