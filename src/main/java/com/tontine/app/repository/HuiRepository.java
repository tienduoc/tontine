package com.tontine.app.repository;

import com.tontine.app.domain.Hui;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Hui entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuiRepository extends JpaRepository<Hui, Long> {

    @Query("SELECT h FROM Hui h LEFT JOIN FETCH h.chiTietHuis WHERE h.id = :id")
    Optional<Hui> findByIdWithChiTietHuis(@Param("id") Long id);

    @Query("SELECT DISTINCT h FROM Hui h LEFT JOIN FETCH h.chiTietHuis")
    List<Hui> findAllWithChiTietHuis();
}
