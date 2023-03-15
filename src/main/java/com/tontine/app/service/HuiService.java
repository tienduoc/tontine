package com.tontine.app.service;

import com.tontine.app.domain.Hui;
import com.tontine.app.repository.HuiRepository;
import com.tontine.app.service.dto.HuiDTO;
import com.tontine.app.service.mapper.HuiMapper;
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

    private final HuiMapper huiMapper;

    public HuiService(HuiRepository huiRepository, HuiMapper huiMapper) {
        this.huiRepository = huiRepository;
        this.huiMapper = huiMapper;
    }

    /**
     * Save a hui.
     *
     * @param huiDTO the entity to save.
     * @return the persisted entity.
     */
    public HuiDTO save(HuiDTO huiDTO) {
        log.debug("Request to save Hui : {}", huiDTO);
        Hui hui = huiMapper.toEntity(huiDTO);
        hui = huiRepository.save(hui);
        return huiMapper.toDto(hui);
    }

    /**
     * Update a hui.
     *
     * @param huiDTO the entity to save.
     * @return the persisted entity.
     */
    public HuiDTO update(HuiDTO huiDTO) {
        log.debug("Request to update Hui : {}", huiDTO);
        Hui hui = huiMapper.toEntity(huiDTO);
        hui = huiRepository.save(hui);
        return huiMapper.toDto(hui);
    }

    /**
     * Partially update a hui.
     *
     * @param huiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HuiDTO> partialUpdate(HuiDTO huiDTO) {
        log.debug("Request to partially update Hui : {}", huiDTO);

        return huiRepository
            .findById(huiDTO.getId())
            .map(existingHui -> {
                huiMapper.partialUpdate(existingHui, huiDTO);

                return existingHui;
            })
            .map(huiRepository::save)
            .map(huiMapper::toDto);
    }

    /**
     * Get all the huis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HuiDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Huis");
        return huiRepository.findAll(pageable).map(huiMapper::toDto);
    }

    /**
     * Get one hui by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HuiDTO> findOne(Long id) {
        log.debug("Request to get Hui : {}", id);
        return huiRepository.findById(id).map(huiMapper::toDto);
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
