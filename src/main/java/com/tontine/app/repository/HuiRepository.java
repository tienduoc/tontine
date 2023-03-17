package com.tontine.app.repository;

import com.tontine.app.domain.Hui;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hui entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HuiRepository extends JpaRepository<Hui, Long> {}
