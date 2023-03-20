package com.tontine.app.repository;

import com.tontine.app.domain.Hui;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HuiRepositoryWithBagRelationships {
    Optional<Hui> fetchBagRelationships(Optional<Hui> hui);

    List<Hui> fetchBagRelationships(List<Hui> huis);

    Page<Hui> fetchBagRelationships(Page<Hui> huis);
}
