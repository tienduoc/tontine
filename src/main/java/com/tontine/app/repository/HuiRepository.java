package com.tontine.app.repository;

import com.tontine.app.domain.Hui;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hui entity.
 */
@SuppressWarnings("unused")
@Repository
@Cacheable("com.tontine.app.repository.Hui")
public interface HuiRepository extends JpaRepository<Hui, Long> {}
