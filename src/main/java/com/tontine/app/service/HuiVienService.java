package com.tontine.app.service;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.service.dto.HuiVienDTO;
import com.tontine.app.service.mapper.HuiVienMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HuiVien}.
 */
@Service
@Transactional
public class HuiVienService {

    private final Logger log = LoggerFactory.getLogger(HuiVienService.class);

    private final HuiVienRepository huiVienRepository;

    private final HuiVienMapper huiVienMapper;

    public HuiVienService(HuiVienRepository huiVienRepository, HuiVienMapper huiVienMapper) {
        this.huiVienRepository = huiVienRepository;
        this.huiVienMapper = huiVienMapper;
    }

    /**
     * Save a huiVien.
     *
     * @param huiVienDTO the entity to save.
     * @return the persisted entity.
     */
    public HuiVienDTO save(HuiVienDTO huiVienDTO) {
        log.debug("Request to save HuiVien : {}", huiVienDTO);
        HuiVien huiVien = huiVienMapper.toEntity(huiVienDTO);
        huiVien = huiVienRepository.save(huiVien);
        return huiVienMapper.toDto(huiVien);
    }

    /**
     * Update a huiVien.
     *
     * @param huiVienDTO the entity to save.
     * @return the persisted entity.
     */
    public HuiVienDTO update(HuiVienDTO huiVienDTO) {
        log.debug("Request to update HuiVien : {}", huiVienDTO);
        HuiVien huiVien = huiVienMapper.toEntity(huiVienDTO);
        huiVien = huiVienRepository.save(huiVien);
        return huiVienMapper.toDto(huiVien);
    }

    /**
     * Partially update a huiVien.
     *
     * @param huiVienDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HuiVienDTO> partialUpdate(HuiVienDTO huiVienDTO) {
        log.debug("Request to partially update HuiVien : {}", huiVienDTO);

        return huiVienRepository
            .findById(huiVienDTO.getId())
            .map(existingHuiVien -> {
                huiVienMapper.partialUpdate(existingHuiVien, huiVienDTO);

                return existingHuiVien;
            })
            .map(huiVienRepository::save)
            .map(huiVienMapper::toDto);
    }

    /**
     * Get all the huiViens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HuiVienDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HuiViens");
        return huiVienRepository.findAll(pageable).map(huiVienMapper::toDto);
    }

    /**
     * Get one huiVien by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HuiVienDTO> findOne(Long id) {
        log.debug("Request to get HuiVien : {}", id);
        return huiVienRepository.findById(id).map(huiVienMapper::toDto);
    }

    /**
     * Delete the huiVien by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HuiVien : {}", id);
        huiVienRepository.deleteById(id);
    }
}
