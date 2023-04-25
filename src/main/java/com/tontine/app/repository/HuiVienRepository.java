package com.tontine.app.repository;

import com.tontine.app.domain.HuiVien;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HuiVien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuiVienRepository extends JpaRepository<HuiVien, Long> {
    Optional<HuiVien> findHuiViensByHoTen(String fullName);
}
