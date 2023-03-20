package com.tontine.app.service;

import com.tontine.app.domain.Hui;
import com.tontine.app.repository.HuiRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hui}.
 */
@Service
@Transactional
public class HuiService {

    private final Logger log = LoggerFactory.getLogger(HuiService.class);

    private final HuiRepository huiRepository;

    public HuiService(HuiRepository huiRepository) {
        this.huiRepository = huiRepository;
    }

    /**
     * Save a hui.
     *
     * @param hui the entity to save.
     * @return the persisted entity.
     */
    public Hui save(Hui hui) {
        log.debug("Request to save Hui : {}", hui);
        return huiRepository.save(hui);
    }

    /**
     * Update a hui.
     *
     * @param hui the entity to save.
     * @return the persisted entity.
     */
    public Hui update(Hui hui) {
        log.debug("Request to update Hui : {}", hui);
        return huiRepository.save(hui);
    }

    /**
     * Partially update a hui.
     *
     * @param hui the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hui> partialUpdate(Hui hui) {
        log.debug("Request to partially update Hui : {}", hui);

        return huiRepository
                .findById(hui.getId())
                .map(existingHui -> {
                    if (hui.getTenHui() != null) {
                        existingHui.setTenHui(hui.getTenHui());
                    }
                    if (hui.getNgayTao() != null) {
                        existingHui.setNgayTao(hui.getNgayTao());
                    }
                    if (hui.getLoaiHui() != null) {
                        existingHui.setLoaiHui(hui.getLoaiHui());
                    }
                    if (hui.getDayHui() != null) {
                        existingHui.setDayHui(hui.getDayHui());
                    }
                    if (hui.getThamKeu() != null) {
                        existingHui.setThamKeu(hui.getThamKeu());
                    }
                    if (hui.getSoPhan() != null) {
                        existingHui.setSoPhan(hui.getSoPhan());
                    }

                    return existingHui;
                })
                .map(huiRepository::save);
    }

    /**
     * Get all the huis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Hui> findAll(Pageable pageable) {
        log.debug("Request to get all Huis");
        return huiRepository.findAll(pageable);
    }

    /**
     * Get one hui by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hui> findOne(Long id) {
        log.debug("Request to get Hui : {}", id);
        return huiRepository.findById(id);
    }

    /**
     * Delete the hui by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hui : {}", id);
        huiRepository.deleteById(id);
    }
}
