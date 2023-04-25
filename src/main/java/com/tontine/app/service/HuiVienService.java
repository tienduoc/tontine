package com.tontine.app.service;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
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

    private static final String SERVICE_NAME = "huiVien";

    private final HuiVienRepository huiVienRepository;

    public HuiVienService(HuiVienRepository huiVienRepository) {
        this.huiVienRepository = huiVienRepository;
    }

    /**
     * Save a huiVien.
     *
     * @param huiVien the entity to save.
     * @return the persisted entity.
     */
    public HuiVien save(HuiVien huiVien) {
        log.debug("Request to save HuiVien : {}", huiVien);
        Optional<HuiVien> huiViensByHoTen = huiVienRepository.findHuiViensByHoTen(huiVien.getHoTen());
        if (huiViensByHoTen.isPresent()) {
            throw new BadRequestAlertException("Hui vien da ton tai", SERVICE_NAME, "name_exists");
        }
        return huiVienRepository.save(huiVien);
    }

    /**
     * Update a huiVien.
     *
     * @param huiVien the entity to save.
     * @return the persisted entity.
     */
    public HuiVien update(HuiVien huiVien) {
        log.debug("Request to update HuiVien : {}", huiVien);
        return huiVienRepository.save(huiVien);
    }

    /**
     * Partially update a huiVien.
     *
     * @param huiVien the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HuiVien> partialUpdate(HuiVien huiVien) {
        log.debug("Request to partially update HuiVien : {}", huiVien);

        return huiVienRepository
            .findById(huiVien.getId())
            .map(existingHuiVien -> {
                if (huiVien.getHoTen() != null) {
                    existingHuiVien.setHoTen(huiVien.getHoTen());
                }
                if (huiVien.getSdt() != null) {
                    existingHuiVien.setSdt(huiVien.getSdt());
                }

                return existingHuiVien;
            })
            .map(huiVienRepository::save);
    }

    /**
     * Get all the huiViens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HuiVien> findAll(Pageable pageable) {
        log.debug("Request to get all HuiViens");
        return huiVienRepository.findAll(pageable);
    }

    /**
     * Get one huiVien by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HuiVien> findOne(Long id) {
        log.debug("Request to get HuiVien : {}", id);
        return huiVienRepository.findById(id);
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
